package exnihiloadscensio.registries;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

import exnihiloadscensio.barrel.IBarrelMode;
import exnihiloadscensio.barrel.modes.compost.BarrelModeCompost;

public class BarrelModeRegistry {
	
	public enum TriggerType
	{
		ITEM, FLUID, TICK
	}
	
	private static EnumMap<TriggerType, ArrayList<IBarrelMode>> barrelModes = new EnumMap<TriggerType, ArrayList<IBarrelMode>>(TriggerType.class);
	
	private static HashMap<String, IBarrelMode> barrelModeNames = new HashMap<String, IBarrelMode>();
	
	public static void registerBarrelMode(IBarrelMode mode, TriggerType type)
	{
		ArrayList<IBarrelMode> list = barrelModes.get(type);
		if (list == null)
			list = new ArrayList<IBarrelMode>();
		
		list.add(mode);
		barrelModes.put(type, list);
		
		barrelModeNames.put(mode.getName(), mode);
	}
	
	public static ArrayList<IBarrelMode> getModes(TriggerType type)
	{
		return barrelModes.get(type);
	}
	
	public static void registerDefaults()
	{
		registerBarrelMode(new BarrelModeCompost(), TriggerType.ITEM);
	}

}
