package exnihiloadscensio.enchantments;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ENEnchantments {
	public static EnchantmentEfficiency efficiency = new EnchantmentEfficiency();
	public static EnchantmentFortune fortune = new EnchantmentFortune();
	public static EnchantmentLuckOfTheSea luckOfTheSea = new EnchantmentLuckOfTheSea();
	
    public static void init() {
        GameRegistry.register(efficiency);
        GameRegistry.register(fortune);
        GameRegistry.register(luckOfTheSea);
    }
    
}
