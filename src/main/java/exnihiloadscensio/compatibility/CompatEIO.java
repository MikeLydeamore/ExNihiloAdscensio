package exnihiloadscensio.compatibility;

import crazypants.enderio.machine.sagmill.SagMillRecipeManager;
import exnihiloadscensio.items.ore.ItemOre;
import exnihiloadscensio.registries.OreRegistry;
import net.minecraft.item.ItemStack;

public class CompatEIO {
	
	public static void postInit() {
		registerCrushing();		
	}
	
	public static void registerCrushing() {
		for (ItemOre ore : OreRegistry.getItemOreRegistry()) {
    			SagMillRecipeManager.getInstance().addRecipe(new ItemStack(ore, 1, 1), SagMillRecipeManager.ORE_ENERGY_COST, new ItemStack(ore, 1, 2));
    	}
	}

}
