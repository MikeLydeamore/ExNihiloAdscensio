package exnihiloadscensio.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;


public class Config {
	
	public static int compostingTicks;
	
	public static void doNormalConfig(File file)
	{
		Configuration config = new Configuration(file);
		
		compostingTicks = config.get("Composting", "ticksToFormDirt", 600).getInt();
	}
}
