package exnihiloadscensio.compatibility.jei.barrel.fluidblocktransform;

import java.util.List;

import com.google.common.collect.ImmutableList;

import exnihiloadscensio.registries.types.FluidBlockTransformer;
import exnihiloadscensio.util.Util;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidBlockTransformRecipe implements IRecipeWrapper
{
    private FluidStack inputFluid;
    
    private ItemStack inputBucket;
    private ItemStack inputStack;
    
    private ItemStack outputStack;
    
    public FluidBlockTransformRecipe(FluidBlockTransformer recipe)
    {
        inputFluid = new FluidStack(FluidRegistry.getFluid(recipe.getFluidName()), 1000);
        
        inputBucket = Util.getBucketStack(inputFluid.getFluid());
        inputStack = recipe.getInput().getItemStack();
        
        outputStack = recipe.getOutput().getItemStack();
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
        return ImmutableList.of(inputBucket, inputStack);
    }

    @Override
    public List<ItemStack> getOutputs()
    {
        return ImmutableList.of(outputStack);
    }

    @Override
    public List<FluidStack> getFluidInputs()
    {
        return ImmutableList.of(inputFluid);
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
