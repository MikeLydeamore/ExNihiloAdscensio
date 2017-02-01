package exnihiloadscensio.compatibility.jei.sieve;

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
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class SieveRecipe implements IRecipeWrapper
{
    private List<ItemStack> inputs = new ArrayList<ItemStack>();
    private List<ItemStack> outputs = new ArrayList<ItemStack>();
    
    public SieveRecipe(IBlockState block, MeshType mesh)
    {
        List<Siftable> rewards = SieveRegistry.getDrops(new BlockInfo(block));
        // Filter reward list into item stack list, keeping only those of the correct mesh level

        if (rewards == null) {
            return;
        }

        List<ItemStack> allOutputs = Lists.newArrayList(Lists.transform(rewards, reward -> reward.getMeshLevel() == mesh.getID() ? reward.getDrop().getItemStack() : null));
        // Make sure no null rewards, Item or ItemStack
        allOutputs.removeIf(stack -> stack == null || stack.getItem() == Items.AIR);
        
        inputs = Lists.newArrayList(new ItemStack(ENItems.mesh, 1, mesh.getID()), new ItemStack(block.getBlock(), 1, block.getBlock().getMetaFromState(block)));
        outputs = Lists.newArrayList();
        
        for (ItemStack stack : allOutputs)
        {
            boolean alreadyExists = false;
            
            for (ItemStack outputStack : outputs)
            {
                if (stack.getItem().equals(outputStack.getItem()) && stack.getMetadata() == outputStack.getMetadata())
                {
                    outputStack.grow(stack.getCount());
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
    public void getIngredients(@Nonnull IIngredients ingredients)
    {
        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setOutputs(ItemStack.class, outputs);
    }

    public List getInputs()
    {
        return inputs;
    }

    public List getOutputs()
    {
        return outputs;
    }
    
    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }
    
    @Override @Nonnull
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
        return new ArrayList<String>();
    }
    
    @Override
    public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton)
    {
        return false;
    }
}
