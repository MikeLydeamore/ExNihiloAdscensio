package exnihiloadscensio.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;


public class Config {
	
	public static boolean enableBarrels;
	public static boolean enableCrucible;
	public static boolean shouldBarrelsFillWithRain;
	
	public static int compostingTicks;
	public static int infestedLeavesTicks;
	
	public static double silkwormChance;
	public static double silkwormFortuneChance;
	
	public static double stringChance;
	public static double stringFortuneChance;
	
	public static void doNormalConfig(File file)
	{
		Configuration config = new Configuration(file);
		
		config.load();
		
		enableBarrels = config.get("Mechanics", "barrels", true).getBoolean();
		enableCrucible = config.get("Mechanics", "crucible", true).getBoolean();
		shouldBarrelsFillWithRain = config.get("Mechanics", "barrelsFillWithRain", true).getBoolean();
		
		compostingTicks = config.get("Composting", "ticksToFormDirt", 600).getInt();
		
		infestedLeavesTicks = config.get("Infested Leaves","ticksToTransform",600).getInt();
		
		stringChance = config.get("Crooking", "stringChance", 1).getDouble();
		stringFortuneChance = config.get("Crooking", "stringFortuneChance", 1).getDouble();
		
		if (config.hasChanged())
			config.save();
				
	}
}
