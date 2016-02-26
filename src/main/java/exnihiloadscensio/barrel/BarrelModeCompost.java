package exnihiloadscensio.barrel;

import lombok.Setter;
import exnihiloadscensio.config.Config;
import exnihiloadscensio.networking.MessageCompostUpdate;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.CompostRegistry;
import exnihiloadscensio.registries.types.Compostable;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.util.ItemInfo;
import exnihiloadscensio.util.Util;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BarrelModeCompost implements IBarrelMode {

	@Setter
	private float fillAmount = 0;
	@Setter
	private Color color = new Color("EEA96D");
	private Color whiteColor = new Color(1f, 1f, 1f, 1f);
	private Color originalColor;
	@Setter
	private float progress = 0;
	
	@Override
	public boolean onBlockActivated(World world, TileBarrel barrel, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (fillAmount < 1)
		{
			if (player.getHeldItem() != null)
			{
				ItemInfo info = ItemInfo.getItemInfoFromStack(player.getHeldItem());
				if (CompostRegistry.containsItem(info))
				{
					Compostable compost = CompostRegistry.getItem(info);
					
					if (fillAmount == 0)
						color = compost.getColor();
					else
						color = Color.average(color, compost.getColor(), compost.getValue());
					
					fillAmount += compost.getValue();
					if (fillAmount > 1)
						fillAmount = 1;
					if (!player.capabilities.isCreativeMode)
						player.getHeldItem().stackSize--;
					PacketHandler.sendToAllAround(new MessageCompostUpdate(this.fillAmount, this.color, this.progress, barrel.getPos()), barrel);
					barrel.markDirty();
					return true;
				}
			}
		}
		else if (progress >= 1)
		{
			Util.dropItemInWorld(barrel, player, new ItemStack(Blocks.dirt), 0.02f);
			progress = 0;
			fillAmount = 0;
			color = new Color("EEA96D");
			PacketHandler.sendToAllAround(new MessageCompostUpdate(this.fillAmount, this.color, this.progress, barrel.getPos()), barrel);
			return true;
		}
		
		return false;
	}
	
	@Override
	public void update(TileBarrel barrel)
	{
		if (fillAmount >= 1 && progress < 1)
		{
			if (progress == 0)
				originalColor = color;
			progress += 1.0/Config.compostingTicks;
			color = Color.average(originalColor, whiteColor, progress);
			PacketHandler.sendToAllAround(new MessageCompostUpdate(this.fillAmount, this.color, this.progress, barrel.getPos()), barrel);
			barrel.markDirty();
		}
	}
	
	@Override
	public String getName()
	{
		return "compost";
	}

	@Override
	public boolean isTriggerItemStack(ItemStack stack)
	{
		return CompostRegistry.containsItem(stack);
	}
	
	@Override
	public boolean isTriggerFluidStack(FluidStack stack)
	{
		return false;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) 
	{
		tag.setFloat("fillAmount", fillAmount);
		tag.setInteger("color", color.toInt());
		if (originalColor != null)
			tag.setInteger("originalColor", originalColor.toInt());
		tag.setFloat("progress", progress);

	}

	@Override
	public void readFromNBT(NBTTagCompound tag) 
	{
		fillAmount = tag.getFloat("fillAmount");
		this.color = new Color(tag.getInteger("color"));
		if (tag.hasKey("originalColor"))
			this.originalColor = new Color(tag.getInteger("originalColor"));
		this.progress = tag.getFloat("progress");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getTextureForRender()
	{
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes()
		.getTexture(Blocks.dirt.getDefaultState());
	}
	
	@Override
	public float getFilledLevelForRender()
	{
		return fillAmount;
	}
	
	@Override
	public Color getColorForRender()
	{
		return color;
	}
}
