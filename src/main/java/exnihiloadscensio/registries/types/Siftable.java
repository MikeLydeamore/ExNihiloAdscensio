package exnihiloadscensio.registries.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import exnihiloadscensio.util.ItemInfo;

@AllArgsConstructor
public class Siftable {

	@Getter
	private ItemInfo drop;
	@Getter
	private float chance;
	@Getter
	private int meshLevel;
}
