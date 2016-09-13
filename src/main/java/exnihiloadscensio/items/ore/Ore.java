package exnihiloadscensio.items.ore;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.util.BlockInfo;
import exnihiloadscensio.util.ItemInfo;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public class Ore extends IForgeRegistryEntry.Impl<Ore> {
	
	@Getter
	private String name;
	@Getter
	private Color color;
	@Getter
	private ItemInfo result;
	@Getter
	private HashMap<BlockInfo, ArrayList<OreSiftable>> drop;
	
	public Ore(String name, Color color, ItemInfo result, HashMap<BlockInfo, ArrayList<OreSiftable>> drop) {
		this.name = name;
		this.color = color;
		this.result = result;
		this.drop = drop;
		this.setRegistryName(name);
		GameRegistry.<Ore>register(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Ore ore = (Ore) o;

		return getRegistryName().equals(ore.getRegistryName());
	}

	@Override
	public int hashCode() {
		return getRegistryName().hashCode();
	}


}
