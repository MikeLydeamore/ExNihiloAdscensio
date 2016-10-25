package exnihiloadscensio.compatibility.jei.barrel.fluidontop;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class FluidOnTopRecipeHandler implements IRecipeHandler<FluidOnTopRecipe>
{
    @Override
    public Class<FluidOnTopRecipe> getRecipeClass()
    {
        return FluidOnTopRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid()
    {
        return FluidOnTopRecipeCategory.UID;
    }

    @Override
    public String getRecipeCategoryUid(FluidOnTopRecipe recipe)
    {
        return FluidOnTopRecipeCategory.UID;
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(FluidOnTopRecipe recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(FluidOnTopRecipe recipe)
    {
        return true;
    }
}
