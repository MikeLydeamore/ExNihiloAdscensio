package exnihiloadscensio.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;


public class Config {
	
	public static boolean enableBarrels;
	
	public static int compostingTicks;
	
	public static void doNormalConfig(File file)
	{
		Configuration config = new Configuration(file);
		
		config.load();
		
		enableBarrels = config.get("Mechanics", "barrels", true).getBoolean();
		
		compostingTicks = config.get("Composting", "ticksToFormDirt", 600).getInt();
		
		if (config.hasChanged())
			config.save();
				
	}
}
