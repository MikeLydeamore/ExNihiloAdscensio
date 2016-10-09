package exnihiloadscensio.barrel.modes.fluid;

import java.util.List;

import exnihiloadscensio.barrel.BarrelFluidHandler;
import exnihiloadscensio.barrel.IBarrelMode;
import exnihiloadscensio.barrel.modes.transform.BarrelModeFluidTransform;
import exnihiloadscensio.networking.MessageBarrelModeUpdate;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.FluidOnTopRegistry;
import exnihiloadscensio.registries.FluidTransformRegistry;
import exnihiloadscensio.registries.types.FluidTransformer;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.util.ItemInfo;
import exnihiloadscensio.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

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
		return true;
	}

	@Override
	public String getName() {
		return "fluid";
	}
	
	@Override
	public List<String> getWailaTooltip(TileBarrel barrel, List<String> currenttip) {
		if (barrel.getTank().getFluid() != null) {
			currenttip.add(barrel.getTank().getFluid().getLocalizedName());
			currenttip.add("Amount: "+String.valueOf(barrel.getTank().getFluidAmount())+"mb");
		}
		
		return currenttip;
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

	@SuppressWarnings("deprecation")
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
				fluidOnTop = onTop.getMaterial(barrel.getWorld().getBlockState(pos)) == Material.WATER ? FluidRegistry.WATER : FluidRegistry.LAVA;
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
			
			//Fluid transforming time!
			if (FluidTransformRegistry.containsKey(barrel.getTank().getFluid().getFluid().getName())) {
				FluidTransformer transformer = FluidTransformRegistry.getFluidTransformer(barrel.getTank().getFluid().getFluid().getName());
				
				if (Util.isSurroundingBlocksAtLeastOneOf(transformer.getTransformingBlocks(), barrel.getPos().add(0, -1, 0), barrel.getWorld())) {
					//Time to start the process.
					FluidStack fstack = tank.getFluid();
					tank.setFluid(null);
					barrel.setMode("fluidTransform");
					((BarrelModeFluidTransform) barrel.getMode()).setTransformer(transformer);
					((BarrelModeFluidTransform) barrel.getMode()).setInputStack(fstack);
					((BarrelModeFluidTransform) barrel.getMode()).setOutputStack(FluidRegistry.getFluidStack(transformer.getOutputFluid(), 1000));
					PacketHandler.sendNBTUpdate(barrel);
					
				}
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
