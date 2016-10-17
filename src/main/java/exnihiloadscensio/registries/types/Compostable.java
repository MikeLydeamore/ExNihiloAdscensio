package exnihiloadscensio.registries.types;

import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.util.ItemInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Compostable {
	
	@Getter
	private float value;
	@Getter
	private Color color;
	@Getter
	private ItemInfo compostBlock;
	@Getter
	private boolean ignoreMeta;
}
