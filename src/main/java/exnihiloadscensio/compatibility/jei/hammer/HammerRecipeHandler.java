package exnihiloadscensio.compatibility.jei.hammer;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class HammerRecipeHandler implements IRecipeHandler<HammerRecipe>
{
    @Override
    public Class<HammerRecipe> getRecipeClass()
    {
        return HammerRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid()
    {
        return HammerRecipeCategory.UID;
    }

    @Override
    public String getRecipeCategoryUid(HammerRecipe recipe)
    {
        return HammerRecipeCategory.UID;
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(HammerRecipe recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(HammerRecipe recipe)
    {
        return true;
    }
}
