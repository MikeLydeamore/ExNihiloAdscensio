package exnihiloadscensio.items.ore;

import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.util.ItemInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class Ore {
	
	@Getter
	private String name;
	@Getter
	private Color color;
	@Getter
	private ItemInfo result;
	
	public Ore(String name, Color color, ItemInfo result) {
		this.name = name;
		this.color = color;
		this.result = result;
	}

}
