package exnihiloadscensio.compatibility.jei.barrel.fluidontop;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.blocks.ENBlocks;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class FluidOnTopRecipeCategory implements IRecipeCategory<FluidOnTopRecipe>
{
    public static final String UID = "exnihiloadscensio:fluid_on_top";
    private static final ResourceLocation texture = new ResourceLocation(ExNihiloAdscensio.MODID, "textures/gui/jei_fluid_on_top.png");
    
    private final IDrawableStatic background;
    
    public FluidOnTopRecipeCategory(IGuiHelper helper)
    {
        this.background = helper.createDrawable(texture, 0, 0, 166, 63);
    }
    
    @Override
    public String getUid()
    {
        return UID;
    }
    
    @Override
    public String getTitle()
    {
        return "Fluid On Top";
    }
    
    @Override
    public IDrawable getBackground()
    {
        return background;
    }
    
    @Override
    public void drawExtras(Minecraft minecraft)
    {
        
    }
    
    @Override
    public void drawAnimations(Minecraft minecraft)
    {
        
    }
    
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, FluidOnTopRecipe recipeWrapper)
    {
        recipeLayout.getItemStacks().init(0, true, 74, 36);
        recipeLayout.getItemStacks().init(1, true, 47, 36);
        recipeLayout.getItemStacks().init(2, true, 74, 9);
        recipeLayout.getItemStacks().init(3, false, 101, 36);
        
        recipeLayout.getItemStacks().set(0, new ItemStack(ENBlocks.barrelStone, 1, 0));
        recipeLayout.getItemStacks().set(1, recipeWrapper.getInputs().get(0));
        recipeLayout.getItemStacks().set(2, recipeWrapper.getInputs().get(1));
        recipeLayout.getItemStacks().set(3, recipeWrapper.getOutputs().get(0));
    }
    
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, FluidOnTopRecipe recipeWrapper, IIngredients ingredients)
    {
        // I learn from the best
        setRecipe(recipeLayout, recipeWrapper);
    }

    @Override
    public IDrawable getIcon()
    {
        return null;
    }
}
