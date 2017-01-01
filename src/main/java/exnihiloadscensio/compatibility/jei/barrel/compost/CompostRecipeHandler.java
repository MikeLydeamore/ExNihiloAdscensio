package exnihiloadscensio.compatibility.jei.barrel.compost;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class CompostRecipeHandler implements IRecipeHandler<CompostRecipe>
{
    @Override
    public Class<CompostRecipe> getRecipeClass()
    {
        return CompostRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid()
    {
        return CompostRecipeCategory.UID;
    }

    @Override
    public String getRecipeCategoryUid(CompostRecipe recipe)
    {
        return CompostRecipeCategory.UID;
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(CompostRecipe recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(CompostRecipe recipe)
    {
        return true;
    }
}
