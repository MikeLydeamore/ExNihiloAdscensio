package exnihiloadscensio.barrel.modes.fluid;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.ItemFluidContainer;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import exnihiloadscensio.barrel.BarrelFluidHandler;
import exnihiloadscensio.barrel.IBarrelMode;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.util.Util;

public class BarrelModeFluid implements IBarrelMode {

	
	
	public BarrelModeFluid() {
		
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isTriggerItemStack(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isTriggerFluidStack(FluidStack stack) {
		if (stack == null)
			return false;
		return stack.getFluid().equals(FluidRegistry.WATER) ||
				stack.getFluid().equals(FluidRegistry.LAVA);
	}

	@Override
	public String getName() {
		return "fluid";
	}

	@Override
	public boolean onBlockActivated(World world, TileBarrel barrel,
			BlockPos pos, IBlockState state, EntityPlayer player,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (FluidContainerRegistry.isContainer(player.getHeldItemMainhand())) {
			FluidStack fluid = barrel.getTank().drain(Fluid.BUCKET_VOLUME, false);
			ItemStack container = player.getHeldItemMainhand();
			ItemStack filledStack = FluidContainerRegistry.fillFluidContainer(fluid, container);
			if (filledStack != null) {
				barrel.getTank().drain(Fluid.BUCKET_VOLUME, true);
				container.stackSize--;
				player.inventory.addItemStackToInventory(filledStack);
			}
			return true;
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getTextureForRender(TileBarrel barrel) {
		if (barrel.getTank().getFluid() != null)
			return Util.getTextureFromBlockState(barrel.getTank().getFluid().getFluid().getBlock().getDefaultState());
		
		return Util.getTextureFromBlockState(Blocks.WATER.getDefaultState());
	}

	@Override
	public Color getColorForRender() {
		return Util.whiteColor;
	}

	@Override
	public float getFilledLevelForRender(TileBarrel barrel) {
		double amount = barrel.getTank().getFluidAmount();
		return (float) (amount/Fluid.BUCKET_VOLUME);
	}

	@Override
	public void update(TileBarrel barrel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addItem(ItemStack stack, TileBarrel barrel) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStackHandler getHandler(TileBarrel barrel) {
		return null;
	}

	@Override
	public FluidTank getFluidHandler(TileBarrel barrel) {
		BarrelFluidHandler handler = barrel.getTank();
		handler.setBarrel(barrel);
		return handler;
	}
	
	@Override
	public boolean canFillWithFluid(TileBarrel barrel) {
		return true;
	}

}
