package exnihiloadscensio.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;


public class Config {
	
	public static boolean enableBarrels;
	public static boolean enableCrucible;
	public static boolean shouldBarrelsFillWithRain;
    public static boolean fakePlayersCanSieve;
	
	public static int compostingTicks;
	public static int infestedLeavesTicks;
	
	public static double stringChance;
	public static double stringFortuneChance;
	
	public static int leavesUpdateFrequency;
	public static double leavesSpreadChance;
	public static boolean doLeavesUpdateClient;
	
	public static boolean enableBarrelTransformLighting;
	
	public static int enchantmentIDEfficiency;
	public static int enchantmentIDFortune;
	public static int enchantmentIDLuckOfTheSea;
	
	public static void doNormalConfig(File file)
	{
		Configuration config = new Configuration(file);
		
		config.load();
		
		enableBarrels = config.get("Mechanics", "barrels", true).getBoolean();
		enableCrucible = config.get("Mechanics", "crucible", true).getBoolean();
		shouldBarrelsFillWithRain = config.get("Mechanics", "barrelsFillWithRain", true).getBoolean();
		fakePlayersCanSieve = config.get("Mechanics", "fakePlayersCanSieve", false).getBoolean();
		
		compostingTicks = config.get("Composting", "ticksToFormDirt", 600).getInt();
		
		infestedLeavesTicks = config.get("Infested Leaves","ticksToTransform",600).getInt();
        leavesUpdateFrequency = config.get("Infested Leaves", "leavesUpdateFrequency", 40).getInt();
        leavesSpreadChance = config.get("Infested Leaves", "leavesSpreadChance", 0.0015).getDouble();
        doLeavesUpdateClient = config.get("Infested Leaves", "doLeavesUpdateClient", true).getBoolean();
		
        enableBarrelTransformLighting = config.get("Misc", "enableBarrelTransformLighting", true).getBoolean();
        
		stringChance = config.get("Crooking", "stringChance", 1).getDouble();
		stringFortuneChance = config.get("Crooking", "stringFortuneChance", 1).getDouble();
		
		enchantmentIDEfficiency = config.get("Enchantments","efficiency", 105).getInt();
		enchantmentIDFortune = config.get("Enchantments","fortune", 106).getInt();
		enchantmentIDLuckOfTheSea = config.get("Enchantments", "luckofthesea", 107).getInt();
		
		if (config.hasChanged())
			config.save();
				
	}
}
