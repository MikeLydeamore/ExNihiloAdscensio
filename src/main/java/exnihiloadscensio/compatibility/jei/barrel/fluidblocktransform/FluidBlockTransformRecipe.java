package exnihiloadscensio.compatibility.jei.barrel.fluidblocktransform;

import com.google.common.collect.ImmutableList;
import exnihiloadscensio.registries.types.FluidBlockTransformer;
import exnihiloadscensio.util.Util;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

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
    public void getIngredients(@Nonnull IIngredients ingredients)
    {
        ingredients.setInputs(ItemStack.class, getInputs());
        ingredients.setInputs(FluidStack.class, getFluidInputs());
        
        ingredients.setOutput(ItemStack.class, outputStack);
    }

    public List<ItemStack> getInputs()
    {
        return ImmutableList.of(inputBucket, inputStack);
    }

    public List<ItemStack> getOutputs()
    {
        return ImmutableList.of(outputStack);
    }

    public List<FluidStack> getFluidInputs()
    {
        return ImmutableList.of(inputFluid);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        
    }

    @Override
    @Nonnull
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
