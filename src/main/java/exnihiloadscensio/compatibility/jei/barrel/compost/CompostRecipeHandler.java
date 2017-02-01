package exnihiloadscensio.compatibility.jei.barrel.compost;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class CompostRecipeHandler implements IRecipeHandler<CompostRecipe>
{
    @Override @Nonnull
    public Class<CompostRecipe> getRecipeClass()
    {
        return CompostRecipe.class;
    }

    @Override @Nonnull
    public String getRecipeCategoryUid(@Nonnull CompostRecipe recipe)
    {
        return CompostRecipeCategory.UID;
    }

    @Override @Nonnull
    public IRecipeWrapper getRecipeWrapper(@Nonnull CompostRecipe recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull CompostRecipe recipe)
    {
        return true;
    }
}
