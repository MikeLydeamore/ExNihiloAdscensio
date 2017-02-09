package exnihiloadscensio.compatibility.jei.barrel.fluidtransform;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class FluidTransformRecipeHandler implements IRecipeHandler<FluidTransformRecipe>
{
    @Override @Nonnull
    public Class<FluidTransformRecipe> getRecipeClass()
    {
        return FluidTransformRecipe.class;
    }

    @Override @Nonnull
    public String getRecipeCategoryUid(@Nonnull FluidTransformRecipe recipe)
    {
        return FluidTransformRecipeCategory.UID;
    }

    @Override @Nonnull
    public IRecipeWrapper getRecipeWrapper(@Nonnull FluidTransformRecipe recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull FluidTransformRecipe recipe)
    {
        return true;
    }
}
