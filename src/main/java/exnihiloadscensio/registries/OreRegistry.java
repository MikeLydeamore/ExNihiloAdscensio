package exnihiloadscensio.registries;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.items.ore.Ore;
import exnihiloadscensio.texturing.Color;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;

public class OreRegistry {
	
	public static final int INVALID_ID = Short.MAX_VALUE - 1;
    public static FMLControlledNamespacedRegistry<Ore> ORES;
    
    public static void registerDefaults() {
    	ORES = (FMLControlledNamespacedRegistry<Ore>) new RegistryBuilder<Ore>()
                .setName(new ResourceLocation(ExNihiloAdscensio.MODID, "ores"))
                .setIDRange(0, INVALID_ID - 1)
                .setType(Ore.class)
                .create(); 
    	
    	Ore iron = new Ore("iron", new Color(194, 129, 45, 1));
    }

}
