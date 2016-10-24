package exnihiloadscensio.compatibility.jei.sieve;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import exnihiloadscensio.blocks.BlockSieve.MeshType;
import exnihiloadscensio.items.ENItems;
import exnihiloadscensio.registries.SieveRegistry;
import exnihiloadscensio.registries.types.Siftable;
import exnihiloadscensio.util.BlockInfo;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class SieveRecipe implements IRecipeWrapper
{
    private List<ItemStack> inputs = new ArrayList<ItemStack>();
    private List<ItemStack> outputs = new ArrayList<ItemStack>();
    
    public SieveRecipe(IBlockState block, MeshType mesh)
    {
        List<Siftable> rewards = SieveRegistry.getDrops(new BlockInfo(block));
        // Make sure no null rewards, Item or ItemStack
        List<ItemStack> allOutputs = Lists.newArrayList(Lists.transform(rewards, reward -> reward.getDrop().getItemStack()));
        allOutputs.removeIf(stack -> stack == null || stack.getItem() == null);
        
        inputs = Lists.newArrayList(new ItemStack(ENItems.mesh, 1, mesh.getID()), new ItemStack(block.getBlock(), 1, block.getBlock().getMetaFromState(block)));
        outputs = Lists.newArrayList();
        
        for (ItemStack stack : allOutputs)
        {
            boolean alreadyExists = false;
            
            for (ItemStack outputStack : outputs)
            {
                if (stack.getItem().equals(outputStack.getItem()) && stack.getMetadata() == outputStack.getMetadata())
                {
                    outputStack.stackSize += stack.stackSize;
                    alreadyExists = true;
                    break;
                }
            }
            
            if (!alreadyExists)
            {
                outputs.add(stack);
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
    public List getInputs()
    {
        return inputs;
    }
    
    @Override
    public List getOutputs()
    {
        return outputs;
    }
    
    @Override
    public List<FluidStack> getFluidInputs()
    {
        return ImmutableList.of();
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
