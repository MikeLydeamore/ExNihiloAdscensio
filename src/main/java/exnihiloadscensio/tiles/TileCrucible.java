package exnihiloadscensio.tiles;

import exnihiloadscensio.registries.CrucibleRegistry;
import exnihiloadscensio.registries.HeatRegistry;
import exnihiloadscensio.registries.types.Meltable;
import exnihiloadscensio.util.ItemInfo;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileCrucible extends TileEntity implements ITickable {
	
	private FluidTank tank;
	private int solidAmount;
	private ItemInfo currentItem;
	
	private int ticksSinceLast = 0;
	
	private CrucibleItemHandler itemHandler;
	
	public TileCrucible() {
		tank = new FluidTank(4*Fluid.BUCKET_VOLUME);
		itemHandler = new CrucibleItemHandler();
	}

	@Override
	public void update() {
		if (worldObj.isRemote)
			return;
		
		ticksSinceLast++;
		if (ticksSinceLast >= 10) {
			ticksSinceLast = 0;
			if (getHeatRate() < 0)
				return;
			
			Fluid fluidToFill;
			if (solidAmount > 0) {
				Meltable meltable = CrucibleRegistry.getMeltable(currentItem);
				fluidToFill = FluidRegistry.getFluid(meltable.getFluid());
			}
			else {
				ItemStack toMelt = itemHandler.getStackInSlot(0);
				if (toMelt == null) {
					return; //If we get to here, there is nothing left to melt.
				}
				Meltable meltable = CrucibleRegistry.getMeltable(toMelt);
				solidAmount = meltable.getAmount();
				currentItem = new ItemInfo(toMelt);
				
				toMelt.stackSize--;
				itemHandler.setStackInSlot(0, toMelt);
				
				fluidToFill = FluidRegistry.getFluid(meltable.getFluid());
			}
			//Check if the tank can take the fluid.
			FluidStack fStack = new FluidStack(fluidToFill, getHeatRate());
			int fillAmount = tank.fill(fStack, false);
			if (fillAmount == getHeatRate()) {
				tank.fill(fStack, true);
				solidAmount--; //Moving items into the "meltable" slot is handled earlier.
				System.out.println(tank.getFluidAmount());
			}
		}
		
	}
	
	private int getHeatRate() {
		BlockPos posBelow = new BlockPos(pos.getX(), pos.getY()-1, pos.getZ());
		IBlockState blockBelow = worldObj.getBlockState(posBelow);
		
		if (blockBelow == null)
			return 0;
		
		return HeatRegistry.getHeatAmount(new ItemInfo(blockBelow));
	}

}
