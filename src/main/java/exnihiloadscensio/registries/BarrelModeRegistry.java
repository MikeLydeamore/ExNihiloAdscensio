package exnihiloadscensio.registries;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;

import exnihiloadscensio.barrel.IBarrelMode;
import exnihiloadscensio.barrel.modes.block.BarrelModeBlock;
import exnihiloadscensio.barrel.modes.compost.BarrelModeCompost;
import exnihiloadscensio.barrel.modes.fluid.BarrelModeFluid;
import exnihiloadscensio.barrel.modes.transform.BarrelModeFluidTransform;

public class BarrelModeRegistry {
	
	public enum TriggerType
	{
		ITEM, FLUID, TICK, NONE
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
		registerBarrelMode(new BarrelModeFluid(), TriggerType.FLUID);
		registerBarrelMode(new BarrelModeBlock(), TriggerType.NONE);
		registerBarrelMode(new BarrelModeFluidTransform(), TriggerType.NONE);
	}
	
	public static IBarrelMode getModeByName(String name)
	{
		return barrelModeNames.get(name);
	}

}
