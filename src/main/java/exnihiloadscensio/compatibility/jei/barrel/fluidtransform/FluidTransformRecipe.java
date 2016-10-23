package exnihiloadscensio.compatibility.jei.barrel.fluidtransform;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import exnihiloadscensio.registries.types.FluidTransformer;
import exnihiloadscensio.util.BlockInfo;
import exnihiloadscensio.util.Util;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidTransformRecipe implements IRecipeWrapper
{
    private FluidStack inputFluid;
    private FluidStack outputFluid;
    
    private ItemStack inputBucket;
    private ItemStack outputBucket;
    
    private List<IBlockState> transformBlocks;
    
    private List<ItemStack> inputStacks;
    
    public FluidTransformRecipe(FluidTransformer recipe)
    {
        inputFluid = new FluidStack(FluidRegistry.getFluid(recipe.getInputFluid()), 1000);
        outputFluid = new FluidStack(FluidRegistry.getFluid(recipe.getOutputFluid()), 1000);

        inputBucket = Util.getBucketStack(inputFluid.getFluid());
        outputBucket = Util.getBucketStack(outputFluid.getFluid());
        
        transformBlocks = Lists.newArrayList();
        inputStacks = Lists.newArrayList(inputBucket);
        
        for(BlockInfo block : recipe.getTransformingBlocks())
        {
            transformBlocks.add(block.getBlockState());
            inputStacks.add(new ItemStack(block.getBlock(), 1, block.getMeta()));
        }
    }
    
    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInputs(ItemStack.class, inputStacks);
        ingredients.setInput(FluidStack.class, inputFluid);
        
        ingredients.setOutput(ItemStack.class, outputBucket);
        ingredients.setOutput(FluidStack.class, outputFluid);
    }

    @Override
    public List<ItemStack> getInputs()
    {
        return inputStacks;
    }

    @Override
    public List<ItemStack> getOutputs()
    {
        return ImmutableList.of(outputBucket);
    }

    @Override
    public List<FluidStack> getFluidInputs()
    {
        return ImmutableList.of(inputFluid);
    }

    @Override
    public List<FluidStack> getFluidOutputs()
    {
        return ImmutableList.of(new FluidStack(outputFluid, 1000));
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
