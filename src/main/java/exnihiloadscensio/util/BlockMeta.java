package exnihiloadscensio.util;

import net.minecraft.block.Block;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor @EqualsAndHashCode
public class BlockMeta {
	
	@Getter
	private Block block;
	@Getter
	private int meta;

}
