package exnihiloadscensio.tiles;

import exnihiloadscensio.registries.CrucibleRegistry;
import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class CrucibleItemHandler extends ItemStackHandler {

	@Setter
	private TileCrucible te;
	
	public CrucibleItemHandler() {
		super(1);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (CrucibleRegistry.canBeMelted(stack)) {
			return super.insertItem(slot, stack, simulate);
		}
		
		return stack;
	}
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return null;
	}

	@Override
	protected int getStackLimit(int slot, ItemStack stack) {
		return te.getCurrentItem() == null ? 4 : 3;
	}
}
