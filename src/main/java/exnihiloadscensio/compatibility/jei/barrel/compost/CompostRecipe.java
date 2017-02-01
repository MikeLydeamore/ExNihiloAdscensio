package exnihiloadscensio.compatibility.jei.barrel.compost;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class CompostRecipe implements IRecipeWrapper
{
    private List<ItemStack> inputs;
    private ItemStack output;
    
    public CompostRecipe(ItemStack output, List<ItemStack> inputs)
    {
        this.inputs = ImmutableList.copyOf(inputs);
        this.output = output.copy();
    }
    
    @Override
    public void getIngredients(@Nonnull IIngredients ingredients)
    {
        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, output);
    }

    public List<ItemStack> getInputs()
    {
        return inputs;
    }

    public List<ItemStack> getOutputs()
    {
        return ImmutableList.of(output);
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
