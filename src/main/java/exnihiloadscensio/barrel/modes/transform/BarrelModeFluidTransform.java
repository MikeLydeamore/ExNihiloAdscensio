package exnihiloadscensio.barrel.modes.transform;

import exnihiloadscensio.barrel.IBarrelMode;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.FluidTransformRegistry;
import exnihiloadscensio.registries.types.FluidTransformer;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.util.BlockInfo;
import exnihiloadscensio.util.Util;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BarrelModeFluidTransform implements IBarrelMode {

	@Setter
	@Getter
	private FluidStack inputStack, outputStack;
	@Getter
	private float progress = 0;
	@Setter
	@Getter
	private FluidTransformer transformer;

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		NBTTagCompound inputTag = new NBTTagCompound(), outputTag = new NBTTagCompound();
		if (inputStack != null)
			inputTag = inputStack.writeToNBT(new NBTTagCompound());
		if (outputStack != null)
			outputTag = outputStack.writeToNBT(new NBTTagCompound());

		tag.setTag("inputTag", inputTag);
		tag.setTag("outputTag", outputTag);
		tag.setFloat("progress", progress);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		if (tag.hasKey("inputTag")) {
			NBTTagCompound inputTag = (NBTTagCompound) tag.getTag("inputTag");
			inputStack = FluidStack.loadFluidStackFromNBT(inputTag);
		}
		if (tag.hasKey("outputTag")) {
			NBTTagCompound outputTag = (NBTTagCompound) tag.getTag("outputTag");
			outputStack = FluidStack.loadFluidStackFromNBT(outputTag);
		}
		if (tag.hasKey("progress")) {
			progress = tag.getFloat("progress");
		}

	}

	@Override
	public boolean isTriggerItemStack(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isTriggerFluidStack(FluidStack stack) {
		return false;
	}

	@Override
	public String getName() {
		return "fluidTransform";
	}

	@Override
	public boolean onBlockActivated(World world, TileBarrel barrel,
			BlockPos pos, IBlockState state, EntityPlayer player,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		return false;
	}

	@Override
	public TextureAtlasSprite getTextureForRender(TileBarrel barrel) {
		if (progress < 0.5) {
			return Util.getTextureFromFluidStack(inputStack);
		} else {
			return Util.getTextureFromFluidStack(outputStack);
		}
	}

	@Override
	public Color getColorForRender() {
		return Color.average(Util.blackColor, Util.whiteColor,
				2 * Math.abs(progress - 0.5F));
	}

	@Override
	public float getFilledLevelForRender(TileBarrel barrel) {
		return 1;
	}

	@Override
	public void update(TileBarrel barrel) {
		if (transformer == null) {
			transformer = FluidTransformRegistry.getFluidTransformer(inputStack
					.getFluid().getName(), outputStack.getFluid().getName());
		}
		if (transformer == null)
			return;
		if (progress < 1) {
			int numberOfBlocks = Util.getNumSurroundingBlocksAtLeastOneOf(
					transformer.getTransformingBlocks(),
					barrel.getPos().add(0, -1, 0), barrel.getWorld())
					+ Util.getNumSurroundingBlocksAtLeastOneOf(
							transformer.getTransformingBlocks(),
							barrel.getPos(), barrel.getWorld());
			if (numberOfBlocks > 0) {
				progress += numberOfBlocks * 1.0 / transformer.getDuration();

				if (barrel.getWorld().rand.nextDouble() < 0.005) {
					boolean spawned = false;
					ArrayList<BlockInfo> blockList = new ArrayList<BlockInfo>(
							Arrays.asList(transformer.getTransformingBlocks()));
					for (int xShift = -1; xShift <= 1; xShift++) {
						for (int zShift = -1; zShift <= 1; zShift++) {
							if (!spawned) {
								BlockPos testPos = barrel.getPos().add(xShift,
										-1, zShift);
								if (blockList.contains(new BlockInfo(barrel
										.getWorld().getBlockState(testPos)))
										&& barrel.getWorld().isAirBlock(
												testPos.add(0, 1, 0))) {
									BlockInfo[] toSpawn = transformer
											.getBlocksToSpawn();
									if (toSpawn != null && toSpawn.length > 0) {
										barrel.getWorld()
												.setBlockState(
														testPos.add(0, 1, 0),
														toSpawn[barrel
																.getWorld().rand
																.nextInt(toSpawn.length)]
																.getBlockState());
										spawned = true;
									}
								}
							}
						}
					}
				}
			}
			PacketHandler.sendNBTUpdate(barrel);
		}

		if (progress >= 1) {
			barrel.setMode("fluid");
			FluidTank tank = barrel.getMode().getFluidHandler(barrel);
			Fluid fluid = FluidRegistry.getFluid(transformer.getOutputFluid());
			tank.setFluid(new FluidStack(fluid, 1000));
			PacketHandler.sendNBTUpdate(barrel);
		}
	}

	@Override
	public boolean addItem(ItemStack stack, TileBarrel barrel) {
		return false;
	}

	@Override
	public ItemStackHandler getHandler(TileBarrel barrel) {
		return null;
	}

	@Override
	public FluidTank getFluidHandler(TileBarrel barrel) {
		return null;
	}

	@Override
	public boolean canFillWithFluid(TileBarrel barrel) {
		return false;
	}

	@Override
	public List<String> getWailaTooltip(TileBarrel barrel,
			List<String> currenttip) {
		currenttip.add("Transforming: " + Math.round(progress * 100) + "%");
		return currenttip;
	}

}
