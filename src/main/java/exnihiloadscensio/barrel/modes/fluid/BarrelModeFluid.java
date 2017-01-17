package exnihiloadscensio.barrel.modes.fluid;

import java.util.List;

import exnihiloadscensio.barrel.BarrelFluidHandler;
import exnihiloadscensio.barrel.IBarrelMode;
import exnihiloadscensio.barrel.modes.transform.BarrelModeFluidTransform;
import exnihiloadscensio.networking.MessageBarrelModeUpdate;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.BarrelLiquidBlacklistRegistry;
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
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidBlock;
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
			currenttip.add("Amount: " + String.valueOf(barrel.getTank().getFluidAmount()) + "mb");
		} else {
			currenttip.add("Empty");
		}

		return currenttip;
	}

	@Override
	public boolean onBlockActivated(World world, TileBarrel barrel, BlockPos pos, IBlockState state,
			EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);

		if (stack != null) {
			ItemStack remainder = getHandler(barrel).insertItem(0, stack, false);

			int size = remainder == null ? 0 : remainder.stackSize;

			if (stack.getItem().hasContainerItem(stack)) {
				ItemStack container = stack.getItem().getContainerItem(stack);

				// Should always be 1 but LET'S JUST MAKE SURE
				container.stackSize = stack.stackSize - size;

				if (!player.inventory.addItemStackToInventory(container)) {
					player.worldObj.spawnEntityInWorld(
							new EntityItem(player.worldObj, player.posX, player.posY, player.posZ, container));
				}
			}

			player.setHeldItem(EnumHand.MAIN_HAND, remainder);
		}

		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getTextureForRender(TileBarrel barrel) {
		return Util.getTextureFromFluidStack(barrel.getTank().getFluid());
	}

	@Override
	public Color getColorForRender() {
		return Util.whiteColor;
	}

	@Override
	public float getFilledLevelForRender(TileBarrel barrel) {
		double amount = barrel.getTank().getFluidAmount();
		return (float) (amount / Fluid.BUCKET_VOLUME);
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
			BlockPos pos = new BlockPos(barrelPos.getX(), barrelPos.getY() + 1, barrelPos.getZ());
			Block onTop = barrel.getWorld().getBlockState(pos).getBlock();

			Fluid fluidOnTop = null;
			if (onTop instanceof BlockLiquid) {
				fluidOnTop = onTop.getMaterial(barrel.getWorld().getBlockState(pos)) == Material.WATER
						? FluidRegistry.WATER : FluidRegistry.LAVA;
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

				return;
			}

			// Fluid transforming time!
			if (FluidTransformRegistry.containsKey(barrel.getTank().getFluid().getFluid().getName())) {
				List<FluidTransformer> transformers = FluidTransformRegistry
						.getFluidTransformers(barrel.getTank().getFluid().getFluid().getName());

				boolean found = false;
				for (int radius = 0; radius <= 2; radius++) {
					for (FluidTransformer transformer : transformers) {
						if (!BarrelLiquidBlacklistRegistry.isBlacklisted(barrel.getTier(), transformer.getOutputFluid())
								&& (Util.isSurroundingBlocksAtLeastOneOf(transformer.getTransformingBlocks(),
										barrel.getPos().add(0, -1, 0), barrel.getWorld(), radius)
										|| Util.isSurroundingBlocksAtLeastOneOf(transformer.getTransformingBlocks(),
												barrel.getPos(), barrel.getWorld(), radius))) {
							// Time to start the process.
							FluidStack fstack = tank.getFluid();
							tank.setFluid(null);

							barrel.setMode("fluidTransform");
							BarrelModeFluidTransform mode = (BarrelModeFluidTransform) barrel.getMode();

							mode.setTransformer(transformer);
							mode.setInputStack(fstack);
							mode.setOutputStack(FluidRegistry.getFluidStack(transformer.getOutputFluid(), 1000));

							PacketHandler.sendNBTUpdate(barrel);
							found = true;
						}
					}
					if (found) break;
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
