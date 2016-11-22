package exnihiloadscensio.enchantments;

import exnihiloadscensio.config.Config;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;

public class ENEnchantments
{
	public static EnchantmentEfficiency efficiency = new EnchantmentEfficiency();
	public static EnchantmentFortune fortune = new EnchantmentFortune();
	public static EnchantmentLuckOfTheSea luckOfTheSea = new EnchantmentLuckOfTheSea();
	
    public static void init()
    {
        Enchantment.REGISTRY.register(Config.enchantmentIDEfficiency, new ResourceLocation("sieveefficiency"), efficiency);
        Enchantment.REGISTRY.register(Config.enchantmentIDFortune, new ResourceLocation("sievefortune"), fortune);
        Enchantment.REGISTRY.register(Config.enchantmentIDLuckOfTheSea, new ResourceLocation("sieveluckofthesea"), luckOfTheSea);
    }
    
}
