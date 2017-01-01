package exnihiloadscensio.compatibility.jei.barrel.compost;

import java.util.List;

import com.google.common.collect.ImmutableList;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

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
    public void getIngredients(IIngredients ingredients)
    {
        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setOutput(ItemStack.class, output);
    }
    
    @Override
    public List<ItemStack> getInputs()
    {
        return inputs;
    }

    @Override
    public List<ItemStack> getOutputs()
    {
        return ImmutableList.of(output);
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
