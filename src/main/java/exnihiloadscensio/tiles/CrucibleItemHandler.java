package exnihiloadscensio.tiles;

import exnihiloadscensio.registries.CrucibleRegistry;
import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class CrucibleItemHandler extends ItemStackHandler {

	@Setter
	private TileCrucible te;
	
	public CrucibleItemHandler() {
		super(1);
	}

	@Override @Nonnull
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (CrucibleRegistry.canBeMelted(stack)) {
			return super.insertItem(slot, stack, simulate);
		}
		
		return stack;
	}
	@Override @Nonnull
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return ItemStack.EMPTY;
	}

	@Override
	protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
		return te.getCurrentItem() == null ? 4 : 3;
	}
}
