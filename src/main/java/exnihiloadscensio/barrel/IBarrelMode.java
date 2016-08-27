package exnihiloadscensio.barrel;

import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.tiles.TileBarrel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

public interface IBarrelMode {
	
	public void writeToNBT(NBTTagCompound tag);
	
	public void readFromNBT(NBTTagCompound tag);
	
	public boolean isTriggerItemStack(ItemStack stack);
	
	public boolean isTriggerFluidStack(FluidStack stack);
	
	public String getName();
	
	public boolean onBlockActivated(World world, TileBarrel barrel, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ);
	
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getTextureForRender(TileBarrel barrel);
	
	public Color getColorForRender();
	
	public float getFilledLevelForRender(TileBarrel barrel);
	
	public void update(TileBarrel barrel);
	
	public boolean addItem(ItemStack stack, TileBarrel barrel);
	
	public ItemStackHandler getHandler(TileBarrel barrel);
	
	public FluidTank getFluidHandler(TileBarrel barrel);
	
	public boolean canFillWithFluid(TileBarrel barrel);

}
