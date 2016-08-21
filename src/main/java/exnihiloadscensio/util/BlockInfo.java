package exnihiloadscensio.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode @AllArgsConstructor
public class BlockInfo {
	
	@Getter
	private Block block;
	
	@Getter
	private int meta;
	
	public BlockInfo(IBlockState state) {
		block = state.getBlock();
		meta = state.getBlock().getMetaFromState(state);
	}

	public BlockInfo(ItemStack stack) {
		block = Block.getBlockFromItem(stack.getItem());
		meta = stack.getItemDamage();
	}

	public BlockInfo(String string) {
		String[] arr = string.split(":");
		block = Block.REGISTRY.getObject(new ResourceLocation(arr[0]+":"+arr[1]));
		meta = Integer.parseInt(arr[2]);
	}
	
	public String toString() {
		return Block.REGISTRY.getNameForObject(block)+":"+meta;
	}

}
