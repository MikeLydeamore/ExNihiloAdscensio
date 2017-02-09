package exnihiloadscensio.compatibility.jei.barrel.fluidblocktransform;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class FluidBlockTransformRecipeHandler implements IRecipeHandler<FluidBlockTransformRecipe>
{
    @Override @Nonnull
    public Class<FluidBlockTransformRecipe> getRecipeClass()
    {
        return FluidBlockTransformRecipe.class;
    }

    @Override @Nonnull
    public String getRecipeCategoryUid(@Nonnull FluidBlockTransformRecipe recipe)
    {
        return FluidBlockTransformRecipeCategory.UID;
    }

    @Override @Nonnull
    public IRecipeWrapper getRecipeWrapper(@Nonnull FluidBlockTransformRecipe recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull FluidBlockTransformRecipe recipe)
    {
        return true;
    }
}
