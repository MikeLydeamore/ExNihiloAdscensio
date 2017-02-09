package exnihiloadscensio.compatibility.jei.barrel.fluidontop;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class FluidOnTopRecipeHandler implements IRecipeHandler<FluidOnTopRecipe>
{
    @Override @Nonnull
    public Class<FluidOnTopRecipe> getRecipeClass()
    {
        return FluidOnTopRecipe.class;
    }

    @Override @Nonnull
    public String getRecipeCategoryUid(@Nonnull FluidOnTopRecipe recipe)
    {
        return FluidOnTopRecipeCategory.UID;
    }

    @Override @Nonnull
    public IRecipeWrapper getRecipeWrapper(@Nonnull FluidOnTopRecipe recipe)
    {
        return recipe;
    }

    @Override
    public boolean isRecipeValid(@Nonnull FluidOnTopRecipe recipe)
    {
        return true;
    }
}
