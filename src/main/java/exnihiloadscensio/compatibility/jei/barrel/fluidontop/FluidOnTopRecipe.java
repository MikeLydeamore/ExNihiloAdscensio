package exnihiloadscensio.compatibility.jei.barrel.fluidontop;

import java.util.List;

import com.google.common.collect.ImmutableList;

import exnihiloadscensio.registries.types.FluidFluidBlock;
import exnihiloadscensio.util.Util;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

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
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInputs(ItemStack.class, getInputs());
        ingredients.setInputs(FluidStack.class, getFluidInputs());
        
        ingredients.setOutput(ItemStack.class, outputStack);
    }

    @Override
    public List<ItemStack> getInputs()
    {
        return ImmutableList.of(inputBucketInBarrel, inputBucketOnTop);
    }

    @Override
    public List<ItemStack> getOutputs()
    {
        return ImmutableList.of(outputStack);
    }

    @Override
    public List<FluidStack> getFluidInputs()
    {
        return ImmutableList.of(inputFluidInBarrel, inputFluidOnTop);
    }

    @Override
    public List<FluidStack> getFluidOutputs()
    {
        return ImmutableList.of();
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        
    }

    @Override
    public void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight)
    {
        
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
        return null;
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton)
    {
        return false;
    }
}
