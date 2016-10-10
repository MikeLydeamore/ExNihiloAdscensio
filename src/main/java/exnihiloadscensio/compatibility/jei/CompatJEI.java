package exnihiloadscensio.compatibility.jei;

import java.util.List;

import com.google.common.collect.Lists;

import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.blocks.BlockSieve.MeshType;
import exnihiloadscensio.registries.SieveRegistry;
import exnihiloadscensio.util.BlockInfo;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class CompatJEI implements IModPlugin {

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registry) {
	}

	@Override
	public void register(IModRegistry registry) {
		registry.addRecipeCategories(
				new SieveRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeHandlers(
				new SieveRecipeHandler());
		
		List<SieveRecipe> sieveRecipes = Lists.newArrayList();
		for (BlockInfo info : SieveRegistry.getRegistry().keySet()) {
			for (MeshType type : MeshType.values())
				if (type.getID() != 0) //The empty mesh strikes back!
					sieveRecipes.add(new SieveRecipe(info.getBlockState(), type));
		}
		
		registry.addRecipes(sieveRecipes);
		
		registry.addRecipeCategoryCraftingItem(new ItemStack(ENBlocks.sieve), SieveRecipeCategory.UID);
		
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
	}

}
