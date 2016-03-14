package exnihiloadscensio.barrel;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.tiles.TileBarrel;

public class BarrelModeFluid implements IBarrelMode {

	@Override
	public void writeToNBT(NBTTagCompound tag) 
	{
		// TODO Auto-generated method stub	
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isTriggerItemStack(ItemStack stack) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTriggerFluidStack(FluidStack stack) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getName() 
	{
		return "fluid";
	}

	@Override
	public boolean onBlockActivated(World world, TileBarrel barrel,
			BlockPos pos, IBlockState state, EntityPlayer player,
			EnumFacing side, float hitX, float hitY, float hitZ) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getTextureForRender() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getColorForRender() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getFilledLevelForRender() 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update(TileBarrel barrel) 
	{
		// TODO Auto-generated method stub
	}

}
