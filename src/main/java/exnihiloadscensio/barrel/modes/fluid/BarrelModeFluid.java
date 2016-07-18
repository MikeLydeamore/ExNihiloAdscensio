package exnihiloadscensio.barrel.modes.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.BlockLiquidWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import exnihiloadscensio.barrel.BarrelFluidHandler;
import exnihiloadscensio.barrel.IBarrelMode;
import exnihiloadscensio.networking.MessageBarrelModeUpdate;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.FluidOnTopRegistry;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.util.ItemInfo;
import exnihiloadscensio.util.Util;

public class BarrelModeFluid implements IBarrelMode {

	private BarrelItemHandlerFluid handler;

	public BarrelModeFluid() {
		handler = new BarrelItemHandlerFluid(null);
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
		ItemStack stack = player.getHeldItemMainhand();
		if (stack != null && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
			IFluidHandler handler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
			FluidStack fluid = barrel.getTank().drain(Fluid.BUCKET_VOLUME, false);
			int amount = handler.fill(fluid, true);
			if (amount != 0) {
				barrel.getTank().drain(Fluid.BUCKET_VOLUME, true);
			}
			return true;
		}

		ItemStack stack2 = getHandler(barrel).insertItem(0, stack, true);
		if (!ItemStack.areItemStacksEqual(stack, stack2)) {
			getHandler(barrel).insertItem(0, stack, false);
			stack.stackSize--;
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
		// Fluids on top.
		if (barrel.getTank().getFluid() != null) {
			FluidTank tank = barrel.getTank();
			if (tank.getFluid().amount != tank.getCapacity())
				return;
			
			Fluid fluidInBarrel = tank.getFluid().getFluid();

			BlockPos barrelPos = barrel.getPos();
			BlockPos pos = new BlockPos(barrelPos.getX(), barrelPos.getY()+1, barrelPos.getZ());
			Block onTop = barrel.getWorld().getBlockState(pos).getBlock();

			Fluid fluidOnTop = null;
			if (onTop instanceof BlockLiquid) {
				BlockLiquidWrapper block = new BlockLiquidWrapper((BlockLiquid) onTop, barrel.getWorld(), pos);
				fluidOnTop = block.drain(Fluid.BUCKET_VOLUME, false).getFluid();
			}

			if (onTop != null && onTop instanceof IFluidBlock) {
				fluidOnTop = ((BlockFluidBase) onTop).getFluid();
			}

			if (FluidOnTopRegistry.isValidRecipe(fluidInBarrel, fluidOnTop)) {
				ItemInfo info = FluidOnTopRegistry.getTransformedBlock(fluidInBarrel, fluidOnTop);
				tank.drain(tank.getCapacity(), true);
				barrel.setMode("block");
				PacketHandler.sendToAllAround(new MessageBarrelModeUpdate("block", barrel.getPos()), barrel);

				barrel.getMode().addItem(info.getItemStack(), barrel);
			}
		}
	}

	@Override
	public boolean addItem(ItemStack stack, TileBarrel barrel) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ItemStackHandler getHandler(TileBarrel barrel) {
		handler.setBarrel(barrel);
		return handler;
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
