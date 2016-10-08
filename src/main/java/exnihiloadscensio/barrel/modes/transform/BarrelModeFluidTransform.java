package exnihiloadscensio.barrel.modes.transform;

import java.util.List;

import exnihiloadscensio.barrel.IBarrelMode;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.FluidTransformRegistry;
import exnihiloadscensio.registries.types.FluidTransformer;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.util.BlockInfo;
import exnihiloadscensio.util.Util;
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

public class BarrelModeFluidTransform implements IBarrelMode {

	private FluidStack inputStack, outputStack;
	private BlockInfo transformingBlock;
	private byte progress = 0;
	private FluidTransformer transformer;

	@Override
	public void writeToNBT(NBTTagCompound tag) {
		NBTTagCompound inputTag = inputStack.writeToNBT(new NBTTagCompound());
		NBTTagCompound outputTag = outputStack.writeToNBT(new NBTTagCompound());
		NBTTagCompound blockTag = transformingBlock.writeToNBT(new NBTTagCompound());

		tag.setTag("inputTag", inputTag);
		tag.setTag("outputTag", outputTag);
		tag.setTag("blockTag", blockTag);
		tag.setByte("progress", progress);
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
		if (tag.hasKey("blockTag")) {
			NBTTagCompound blockTag = (NBTTagCompound) tag.getTag("blockTag");
			transformingBlock = BlockInfo.readFromNBT(blockTag);
		}
		if (tag.hasKey("progress")) {
			progress = tag.getByte("progress");
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
	public boolean onBlockActivated(World world, TileBarrel barrel, BlockPos pos, IBlockState state,
			EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		return false;
	}

	@Override
	public TextureAtlasSprite getTextureForRender(TileBarrel barrel) {
		if (transformer == null)
			return null;
		
		if (progress < 50) {
			return Util.getTextureFromBlockState(inputStack.getFluid().getBlock().getDefaultState());
		}
		else
			return Util.getTextureFromBlockState(FluidRegistry.getFluid(transformer.getOutputFluid()).getBlock().getDefaultState());
	}

	@Override
	public Color getColorForRender() {
		return Util.whiteColor;
	}

	@Override
	public float getFilledLevelForRender(TileBarrel barrel) {
		return 1;
	}

	@Override
	public void update(TileBarrel barrel) {
		if (transformer == null) {
			transformer = FluidTransformRegistry.getFluidTransformer(inputStack.getFluid().getName());
		}
		if (Util.isSurroundingBlocksAtLeastOneOf(transformer.getTransformingBlocks(), barrel.getPos(), barrel.getWorld())) {
			if (progress < 100) {
				progress++;
				if (barrel.getWorld().rand.nextDouble() < 0.05) {
					//Spawn a block!
				}
			}
		}
		
		if (progress == 100) {
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
	public List<String> getWailaTooltip(TileBarrel barrel, List<String> currenttip) {
		return currenttip;
	}

}
