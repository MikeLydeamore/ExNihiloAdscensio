package exnihiloadscensio.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemBlockMeta extends ItemBlock {

	public ItemBlockMeta(Block block) {
		super(block);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}

	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	@Nonnull
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack) + "." + stack.getItemDamage();
	}

}
