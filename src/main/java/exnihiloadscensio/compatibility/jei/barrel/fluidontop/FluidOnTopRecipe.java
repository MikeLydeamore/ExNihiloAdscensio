package exnihiloadscensio.compatibility.jei.barrel.fluidontop;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import exnihiloadscensio.registries.types.FluidFluidBlock;
import exnihiloadscensio.util.Util;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class FluidOnTopRecipe implements IRecipeWrapper
{
    private FluidStack inputFluidInBarrel;
    private FluidStack inputFluidOnTop;
    
    private ItemStack inputBucketInBarrel;
    private ItemStack inputBucketOnTop;
    
    private ItemStack outputStack;
    
    public FluidOnTopRecipe(FluidFluidBlock recipe)
    {
        inputFluidInBarrel = new FluidStack(FluidRegistry.getFluid(recipe.getFluidInBarrel()), 1000);
        inputFluidOnTop = new FluidStack(FluidRegistry.getFluid(recipe.getFluidOnTop()), 1000);

        inputBucketInBarrel = Util.getBucketStack(inputFluidInBarrel.getFluid());
        inputBucketOnTop = Util.getBucketStack(inputFluidOnTop.getFluid());
        
        outputStack = recipe.getResult().getItemStack();
    }
    
    @Override
    public void getIngredients(@Nonnull IIngredients ingredients)
    {
        ingredients.setInputs(ItemStack.class, getInputs());
        ingredients.setInputs(FluidStack.class, getFluidInputs());
        
        ingredients.setOutput(ItemStack.class, outputStack);
    }

    public List<ItemStack> getInputs()
    {
        return ImmutableList.of(inputBucketInBarrel, inputBucketOnTop);
    }

    public List<ItemStack> getOutputs()
    {
        return ImmutableList.of(outputStack);
    }

    public List<FluidStack> getFluidInputs()
    {
        return ImmutableList.of(inputFluidInBarrel, inputFluidOnTop);
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        
    }

    @Override @Nonnull
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
        return Lists.newArrayList();
    }

    @Override
    public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton)
    {
        return false;
    }
}
