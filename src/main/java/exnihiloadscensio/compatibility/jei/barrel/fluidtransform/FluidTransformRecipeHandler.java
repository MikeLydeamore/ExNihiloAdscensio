package exnihiloadscensio.compatibility.jei.barrel.fluidtransform;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class FluidTransformRecipeHandler implements IRecipeHandler<FluidTransformRecipe>
{
    @Override
    public Class<FluidTransformRecipe> getRecipeClass()
    {
        return FluidTransformRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid()
    {
        return FluidTransformRecipeCategory.UID;
    }

    @Override
    public String getRecipeCategoryUid(FluidTransformRecipe recipe)
    {
        return FluidTransformRecipeCategory.UID;
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(FluidTransformRecipe recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(FluidTransformRecipe recipe)
    {
        return true;
    }
}
