package exnihiloadscensio.compatibility.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class SieveRecipeHandler implements IRecipeHandler<SieveRecipe> {

	@Override
	public Class<SieveRecipe> getRecipeClass() {
		return SieveRecipe.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return SieveRecipeCategory.UID;
	}

	@Override
	public String getRecipeCategoryUid(SieveRecipe recipe) {
		return SieveRecipeCategory.UID;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(SieveRecipe recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(SieveRecipe recipe) {
		return true;
	}

}
