package exnihiloadscensio.registries;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.items.ore.Ore;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.util.ItemInfo;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;

public class OreRegistry {
	
	public static final int INVALID_ID = Short.MAX_VALUE - 1;
    public static FMLControlledNamespacedRegistry<Ore> ORES;
    
    private static ArrayList<Ore> registry = new ArrayList<Ore>();
    
    public static void registerDefaults() {
    	ORES = (FMLControlledNamespacedRegistry<Ore>) new RegistryBuilder<Ore>()
                .setName(new ResourceLocation(ExNihiloAdscensio.MODID, "ores"))
                .setIDRange(0, INVALID_ID - 1)
                .setType(Ore.class)
                .create(); 
    	
    	registerOre("gold", new Color("E6B800"), new ItemInfo(Items.GOLD_INGOT, 0));
    }
    
    public static Ore[] registerOre(String name, Color color, ItemInfo info) {
    	Ore[] ret = new Ore[3];
    	
    	ret[0] = new Ore(name, color, info); registry.add(ret[1]);
    	ret[1] = new Ore("hunk"+StringUtils.capitalize(name), color, info);
    	if (info == null)
    		ret[2] = new Ore("ingot"+StringUtils.capitalize(name), color, info);
    		
    	return ret;
    }
    
    public static void doRecipes() {
    	for (Ore ore : ORES.getValues()) {
    		if (ore.getRegistryName().toString().contains("hunk") ||
    				ore.getRegistryName().toString().contains("ingot"))
    			continue;
    		
    		String name = ore.getRegistryName().toString();
    		ItemStack in = ExNihiloAdscensio.proxy.getFoodRegistryWrapper().getStack(name);
    		ItemStack result = ExNihiloAdscensio.proxy.getFoodRegistryWrapper().getStack("hunk"+StringUtils.capitalize(name));
    		
    		GameRegistry.addShapedRecipe(result, new Object[] {"xx","xx", 'x', in});
    	}
    }

}
