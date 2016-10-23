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
import net.minecraftforge.fluids.FluidStack;

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
    public String getUid()
    {
        return UID;
    }
    
    @Override
    public String getTitle()
    {
        return "Fluid Transform";
    }
    
    @Override
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
    
    @Override
    public void drawAnimations(Minecraft minecraft)
    {
        
    }
    
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, FluidTransformRecipe recipeWrapper)
    {
        recipeLayout.getItemStacks().init(0, true, 74, 9);
        recipeLayout.getItemStacks().init(1, true, 47, 9);
        recipeLayout.getItemStacks().init(2, true, 74, 36);
        recipeLayout.getItemStacks().init(3, false, 101, 9);
        
        IFocus<?> focus = recipeLayout.getFocus();
        hasHighlight = focus.getMode() != IFocus.Mode.NONE;
        
        boolean cycleBlocks = true;
        ItemStack focusBlock = null;
        
        if(focus.getMode() == IFocus.Mode.INPUT)
        {
            if(focus.getValue() instanceof FluidStack)
            {
                highlightX = 47;
                highlightY = 9;
            }
            else if(focus.getValue() instanceof ItemStack && recipeWrapper.getInputs().get(0).isItemEqual((ItemStack) focus.getValue()))
            {
                highlightX = 47;
                highlightY = 9;
            }
            else if(recipeWrapper.getInputs().contains(focus.getValue()))
            {
                highlightX = 74;
                highlightY = 36;
                
                cycleBlocks = false;
                focusBlock = (ItemStack) focus.getValue();
            }
        }
        else if(focus.getMode() == IFocus.Mode.OUTPUT)
        {
            highlightX = 101;
            highlightY = 9;
        }
        
        recipeLayout.getItemStacks().set(0, new ItemStack(ENBlocks.barrelStone, 1, 0));
        recipeLayout.getItemStacks().set(1, recipeWrapper.getInputs().get(0));
        recipeLayout.getItemStacks().set(3, recipeWrapper.getOutputs().get(0));
        
        if(cycleBlocks)
        {
            recipeLayout.getItemStacks().set(2, ImmutableList.copyOf(recipeWrapper.getInputs().subList(1, recipeWrapper.getInputs().size())));
        }
        else
        {
            recipeLayout.getItemStacks().set(2, focusBlock);
        }
    }
    
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, FluidTransformRecipe recipeWrapper, IIngredients ingredients)
    {
        // I learn from the best
        setRecipe(recipeLayout, recipeWrapper);
    }
}
