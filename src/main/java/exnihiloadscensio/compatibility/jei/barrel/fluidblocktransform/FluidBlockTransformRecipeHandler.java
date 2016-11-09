package exnihiloadscensio.compatibility.jei.barrel.fluidblocktransform;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class FluidBlockTransformRecipeHandler implements IRecipeHandler<FluidBlockTransformRecipe>
{
    @Override
    public Class<FluidBlockTransformRecipe> getRecipeClass()
    {
        return FluidBlockTransformRecipe.class;
    }

    @Override
    public String getRecipeCategoryUid()
    {
        return FluidBlockTransformRecipeCategory.UID;
    }

    @Override
    public String getRecipeCategoryUid(FluidBlockTransformRecipe recipe)
    {
        return FluidBlockTransformRecipeCategory.UID;
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(FluidBlockTransformRecipe recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(FluidBlockTransformRecipe recipe)
    {
        return true;
    }
}
