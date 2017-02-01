package exnihiloadscensio.compatibility.jei.hammer;

import com.google.common.collect.Lists;
import exnihiloadscensio.registries.HammerRegistry;
import exnihiloadscensio.registries.HammerReward;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class HammerRecipe implements IRecipeWrapper
{
    private List<ItemStack> inputs;
    private List<ItemStack> outputs;
    
    public HammerRecipe(IBlockState block)
    {
        if(block != null && block.getBlock() != Blocks.AIR)
        {
            List<HammerReward> rewards = HammerRegistry.getRewards(block);
            // Make sure no null rewards, Item or ItemStack
            List<ItemStack> allOutputs = Lists.newArrayList(Lists.transform(rewards, reward -> reward == null || reward.getStack().isEmpty() ? ItemStack.EMPTY : reward.getStack().copy()));
            allOutputs.removeIf(stack -> stack == null || stack.getItem() == Items.AIR);
            
            inputs = Lists.newArrayList(new ItemStack(block.getBlock(), 1, block.getBlock().getMetaFromState(block)));
            outputs = Lists.newArrayList();
            
            for(ItemStack stack : allOutputs)
            {
                boolean alreadyExists = false;
                
                for(ItemStack outputStack : outputs)
                {
                    if(stack.getItem().equals(outputStack.getItem()) && stack.getMetadata() == outputStack.getMetadata())
                    {
                        outputStack.grow(stack.getCount());
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
    public void getIngredients(@Nonnull IIngredients ingredients)
    {
        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setOutputs(ItemStack.class, outputs);
    }

    public List<ItemStack> getInputs()
    {
        return inputs;
    }

    public List<ItemStack> getOutputs()
    {
        return outputs;
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
