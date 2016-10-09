package exnihiloadscensio.registries.types;

import exnihiloadscensio.util.ItemInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Siftable {

	@Getter
	private ItemInfo drop;
	@Getter
	private float chance;
	@Getter
	private int meshLevel;
}
