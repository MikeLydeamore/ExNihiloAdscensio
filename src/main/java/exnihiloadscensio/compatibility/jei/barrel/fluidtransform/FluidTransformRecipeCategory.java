package exnihiloadscensio.compatibility.jei.barrel.fluidtransform;

import com.google.common.collect.ImmutableList;
import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.blocks.ENBlocks;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class FluidTransformRecipeCategory implements IRecipeCategory<FluidTransformRecipe>
{
    public static final String UID = "exnihiloadscensio:fluid_transform";
    private static final ResourceLocation texture = new ResourceLocation(ExNihiloAdscensio.MODID, "textures/gui/jei_fluid_transform.png");
    
    private final IDrawableStatic background;
    private final IDrawableStatic slotHighlight;
    
    private boolean hasHighlight;
    private int highlightX;
    private int highlightY;
    
    public FluidTransformRecipeCategory(IGuiHelper helper)
    {
        this.background = helper.createDrawable(texture, 0, 0, 166, 63);
        this.slotHighlight = helper.createDrawable(texture, 166, 0, 18, 18);
    }

    @Override
    @Nonnull
    public String getModName() {
        return "ExNihiloAdscensio";
    }

    @Override
    @Nonnull
    public String getUid()
    {
        return UID;
    }
    
    @Override
    @Nonnull
    public String getTitle()
    {
        return "Fluid Transform";
    }
    
    @Override
    @Nonnull
    public IDrawable getBackground()
    {
        return background;
    }
    
    @Override
    public void drawExtras(Minecraft minecraft)
    {
        if (hasHighlight)
        {
            slotHighlight.draw(minecraft, highlightX, highlightY);
        }
    }


    public void setRecipe(IRecipeLayout recipeLayout, FluidTransformRecipe recipeWrapper)
    {
        recipeLayout.getItemStacks().init(0, true, 74, 9);
        recipeLayout.getItemStacks().init(1, true, 47, 9);
        recipeLayout.getItemStacks().init(2, true, 74, 36);
        recipeLayout.getItemStacks().init(3, false, 101, 9);
        
        boolean noCycle = false;
        List<ItemStack> focusStack = null;
        
        IFocus<?> focus = recipeLayout.getFocus();
        
        if(focus.getMode() == IFocus.Mode.INPUT && focus.getValue() instanceof ItemStack)
        {
            ItemStack stack = (ItemStack) focus.getValue();
            
            for(ItemStack inputStack : recipeWrapper.getInputs().subList(1, recipeWrapper.getInputs().size()))
            {
                if(stack.isItemEqual(inputStack))
                {
                    noCycle = true;
                    focusStack = ImmutableList.of(inputStack);
                }
            }
        }
        
        recipeLayout.getItemStacks().set(0, new ItemStack(ENBlocks.barrelStone, 1, 0));
        recipeLayout.getItemStacks().set(1, recipeWrapper.getInputs().get(0));
        recipeLayout.getItemStacks().set(2, noCycle ? focusStack : ImmutableList.copyOf(recipeWrapper.getInputs().subList(1, recipeWrapper.getInputs().size())));
        recipeLayout.getItemStacks().set(3, recipeWrapper.getOutputs().get(0));
    }
    
    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull FluidTransformRecipe recipeWrapper, @Nonnull IIngredients ingredients)
    {
        // I learn from the best
        setRecipe(recipeLayout, recipeWrapper);
    }

    @Override
    public IDrawable getIcon()
    {
        return null;
    }

	@Override
    @Nonnull
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return Collections.emptyList();
	}
}
