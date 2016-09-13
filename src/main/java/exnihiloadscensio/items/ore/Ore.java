package exnihiloadscensio.items.ore;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.util.BlockInfo;
import exnihiloadscensio.util.ItemInfo;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

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
