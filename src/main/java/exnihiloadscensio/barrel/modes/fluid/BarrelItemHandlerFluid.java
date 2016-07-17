package exnihiloadscensio.barrel.modes.fluid;

import lombok.Setter;
import exnihiloadscensio.networking.MessageBarrelModeUpdate;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.FluidBlockTransformerRegistry;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.util.ItemInfo;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.ItemStackHandler;

public class BarrelItemHandlerFluid extends ItemStackHandler {
	
	@Setter
	private TileBarrel barrel;
	
	public BarrelItemHandlerFluid(TileBarrel barrel) {
		super(1);
		this.barrel = barrel;
	}
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		FluidTank tank = barrel.getTank();
		if (tank.getFluid() == null)
			return stack;
		
		if (FluidBlockTransformerRegistry.canBlockBeTransformedWithThisFluid(tank.getFluid().getFluid(), stack) &&
				tank.getFluidAmount() == tank.getCapacity()) {
			ItemInfo info = FluidBlockTransformerRegistry.getBlockForTransformation(tank.getFluid().getFluid(), stack);
			if (info != null) {
				if (!simulate) {
					tank.drain(tank.getCapacity(), true);
					barrel.setMode("block");
					PacketHandler.sendToAllAround(new MessageBarrelModeUpdate("block", barrel.getPos()), barrel);
				
					barrel.getMode().addItem(info.getItemStack(), barrel);
				}
				ItemStack ret = stack.copy();
				ret.stackSize--;
				
				return ret;
			}
			
		}
		
		return stack;
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return null;
	}

}
