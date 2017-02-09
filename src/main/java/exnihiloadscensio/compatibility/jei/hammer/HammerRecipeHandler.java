package exnihiloadscensio.compatibility.jei.hammer;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class HammerRecipeHandler implements IRecipeHandler<HammerRecipe>
{
    @Override @Nonnull
    public Class<HammerRecipe> getRecipeClass()
    {
        return HammerRecipe.class;
    }

    @Override @Nonnull
    public String getRecipeCategoryUid(@Nonnull HammerRecipe recipe)
    {
        return HammerRecipeCategory.UID;
    }

    @Override @Nonnull
    public IRecipeWrapper getRecipeWrapper(@Nonnull HammerRecipe recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull HammerRecipe recipe)
    {
        return true;
    }
}
