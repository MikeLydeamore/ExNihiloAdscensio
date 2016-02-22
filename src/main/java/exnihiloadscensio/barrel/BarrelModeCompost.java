package exnihiloadscensio.barrel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BarrelModeCompost implements IBarrelMode {

	byte fillAmount = 0;
	
	@Override
	public String getName()
	{
		return "compost";
	}

	@Override
	public boolean isTriggerItemStack(ItemStack stack)
	{
		return stack.getItem() == Items.rotten_flesh;
	}
	
	@Override
	public boolean isTriggerFluidStack(FluidStack stack)
	{
		return false;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag) 
	{
		tag.setByte("fillAmount", fillAmount);

	}

	@Override
	public void readFromNBT(NBTTagCompound tag) 
	{
		fillAmount = tag.getByte("fillAmount");		
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
		return 0.5f;
	}
}
