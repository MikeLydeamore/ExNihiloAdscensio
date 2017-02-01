package exnihiloadscensio.compatibility.jei.sieve;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class SieveRecipeHandler implements IRecipeHandler<SieveRecipe> {

	@Override @Nonnull
	public Class<SieveRecipe> getRecipeClass() {
		return SieveRecipe.class;
	}

	@Override @Nonnull
	public String getRecipeCategoryUid(@Nonnull SieveRecipe recipe) {
		return SieveRecipeCategory.UID;
	}

	@Override @Nonnull
	public IRecipeWrapper getRecipeWrapper(@Nonnull SieveRecipe recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull SieveRecipe recipe) {
		return true;
	}

}
