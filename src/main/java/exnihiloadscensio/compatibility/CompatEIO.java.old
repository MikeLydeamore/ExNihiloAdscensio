package exnihiloadscensio.compatibility;

import crazypants.enderio.machine.recipe.Recipe;
import crazypants.enderio.machine.recipe.RecipeBonusType;
import crazypants.enderio.machine.recipe.RecipeInput;
import crazypants.enderio.machine.recipe.RecipeOutput;
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
    			SagMillRecipeManager.getInstance().addRecipe(new Recipe(new RecipeInput(new ItemStack(ore, 1, 1), true), SagMillRecipeManager.ORE_ENERGY_COST, RecipeBonusType.NONE, new RecipeOutput(new ItemStack(ore, 2, 2))));
    	}
	}
}
