package exnihiloadscensio.compatibility.jei.hammer;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import exnihiloadscensio.registries.HammerRegistry;
import exnihiloadscensio.registries.HammerReward;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class HammerRecipe implements IRecipeWrapper
{
    private List<ItemStack> inputs;
    private List<ItemStack> outputs;
    
    public HammerRecipe(IBlockState block)
    {
        if(block != null && block.getBlock() != null)
        {
            List<HammerReward> rewards = HammerRegistry.getRewards(block);
            List<ItemStack> allOutputs = Lists.newArrayList(Lists.transform(rewards, reward -> reward.getStack().copy()));

            inputs = Lists.newArrayList(new ItemStack(block.getBlock(), 1, block.getBlock().getMetaFromState(block)));
            outputs = Lists.newArrayList();
            
            for(ItemStack stack : allOutputs)
            {
                boolean alreadyExists = false;
                
                for(ItemStack outputStack : outputs)
                {
                    if(stack.getItem().equals(outputStack.getItem()) && stack.getMetadata() == outputStack.getMetadata())
                    {
                        outputStack.stackSize += stack.stackSize;
                        alreadyExists = true;
                        break;
                    }
                }
                
                if(!alreadyExists)
                {
                    outputs.add(stack);
                }
            }
        }
    }

    @Override
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setOutputs(ItemStack.class, outputs);
    }

    @Override
    public List<ItemStack> getInputs()
    {
        return inputs;
    }

    @Override
    public List<ItemStack> getOutputs()
    {
        return outputs;
    }

    @Override
    public List<FluidStack> getFluidInputs()
    {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<FluidStack> getFluidOutputs()
    {
        return Collections.EMPTY_LIST;
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
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton)
    {
        return false;
    }
}
