package exnihiloadscensio.enchantments;

import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ENEnchantments {
	public static EnchantmentEfficiency efficiency = new EnchantmentEfficiency();
	public static EnchantmentFortune fortune = new EnchantmentFortune();
	public static EnchantmentLuckOfTheSea luckOfTheSea = new EnchantmentLuckOfTheSea();
	
    public static void init() {
        ForgeRegistries.ENCHANTMENTS.register(efficiency);
        ForgeRegistries.ENCHANTMENTS.register(fortune);
        ForgeRegistries.ENCHANTMENTS.register(luckOfTheSea);
    }
    
}
