package exnihiloadscensio.tiles;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class CrucibleItemHandler extends ItemStackHandler {
	
	public CrucibleItemHandler() {
		super(1);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return null;
	}
	
	@Override
	protected int getStackLimit(int slot, ItemStack stack) {
		return 4;
	}
}
