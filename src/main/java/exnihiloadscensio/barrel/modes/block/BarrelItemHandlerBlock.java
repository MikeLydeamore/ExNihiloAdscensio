package exnihiloadscensio.barrel.modes.block;

import lombok.Setter;
import exnihiloadscensio.networking.MessageBarrelModeUpdate;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.tiles.TileBarrel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class BarrelItemHandlerBlock extends ItemStackHandler {
	
	@Setter
	private TileBarrel barrel;
	
	public BarrelItemHandlerBlock(TileBarrel barrel) {
		super(1);
		this.barrel = barrel;
	}
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (slot != 0)
			return stack;
		
		if (getStackInSlot(0) == null) {
			ItemStack ret = super.insertItem(slot, stack, simulate);
			PacketHandler.sendNBTUpdate(barrel);
			return ret;
		}
		
		return stack;
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		ItemStack ret = super.extractItem(slot, amount, simulate);
		if (ret != null && !simulate) {
			barrel.setMode("null");
			PacketHandler.sendToAllAround(new MessageBarrelModeUpdate("null", barrel.getPos()), barrel);
		}
		
		return ret;
	}

}
