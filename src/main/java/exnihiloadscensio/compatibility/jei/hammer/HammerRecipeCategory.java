package exnihiloadscensio.compatibility.jei.hammer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.registries.HammerRegistry;
import exnihiloadscensio.registries.HammerReward;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

public class HammerRecipeCategory implements IRecipeCategory<HammerRecipe>
{
    public static final String UID = "exnihiloadscensio:hammer";
    private static final ResourceLocation texture = new ResourceLocation(ExNihiloAdscensio.MODID, "textures/gui/jei_hammer.png");
    
    private final IDrawableStatic background;
    private final IDrawableStatic slotHighlight;
    
    private boolean hasHighlight;
    private int highlightX;
    private int highlightY;
    
    public HammerRecipeCategory(IGuiHelper helper)
    {
        this.background = helper.createDrawable(texture, 0, 0, 166, 128);
        this.slotHighlight = helper.createDrawable(texture, 166, 0, 18, 18);
    }
    
    @Override @Nonnull
    public String getUid()
    {
        return UID;
    }
    
    @Override @Nonnull
    public String getTitle()
    {
        return "Hammer";
    }
    
    @Override @Nonnull
    public IDrawable getBackground()
    {
        return background;
    }
    
    @Override
    public void drawExtras(@Nonnull Minecraft minecraft)
    {
        if (hasHighlight)
        {
            slotHighlight.draw(minecraft, highlightX, highlightY);
        }
    }

    private void setRecipe(IRecipeLayout recipeLayout, HammerRecipe recipeWrapper)
    {
        // Block
        recipeLayout.getItemStacks().init(0, true, 74, 9);
        recipeLayout.getItemStacks().set(0, (ItemStack) recipeWrapper.getInputs().get(0));
        
        IFocus<?> focus = recipeLayout.getFocus();

        if (focus != null) {
            hasHighlight = focus.getMode() == IFocus.Mode.OUTPUT;

            int slotIndex = 1;

            for (int i = 0; i < recipeWrapper.getOutputs().size(); i++)
            {
                final int slotX = 2 + (i % 9 * 18);
                final int slotY = 36 + (i / 9 * 18);

                ItemStack outputStack = (ItemStack) recipeWrapper.getOutputs().get(i);

                recipeLayout.getItemStacks().init(slotIndex + i, false, slotX, slotY);
                recipeLayout.getItemStacks().set(slotIndex + i, outputStack);

                ItemStack focusStack = (ItemStack) focus.getValue();

                if (focus.getMode() == IFocus.Mode.OUTPUT && !focusStack.isEmpty() && focusStack.getItem() == outputStack.getItem() && focusStack.getItemDamage() == outputStack.getItemDamage())
                {
                    highlightX = slotX;
                    highlightY = slotY;
                }
            }
        }
        
        recipeLayout.getItemStacks().addTooltipCallback(new HammerTooltipCallback(recipeWrapper));
    }
    
    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull HammerRecipe recipeWrapper, @Nonnull IIngredients ingredients)
    {
        // I learn from the best
        setRecipe(recipeLayout, recipeWrapper);
    }

    @Override
    public IDrawable getIcon()
    {
        return null;
    }

    private class HammerTooltipCallback implements ITooltipCallback<ItemStack>
    {
        private HammerRecipe recipe;
        
        private HammerTooltipCallback(HammerRecipe recipeWrapper)
        {
            this.recipe = recipeWrapper;
        }
        
        @Override @SideOnly(Side.CLIENT)
        public void onTooltip(int slotIndex, boolean input, @Nonnull ItemStack ingredient, @Nonnull List<String> tooltip)
        {
            if (!input)
            {
                ItemStack blockStack = (ItemStack) recipe.getInputs().get(0);
                Block blockBase = Block.getBlockFromItem(blockStack.getItem());
                
                @SuppressWarnings("deprecation")
                IBlockState block = blockBase.getStateFromMeta(blockStack.getMetadata());
                
                List<HammerReward> allRewards = HammerRegistry.getRewards(block);
                
                allRewards.removeIf(reward -> !reward.getStack().getItem().equals(ingredient.getItem()) || reward.getStack().getMetadata() != ingredient.getMetadata());
                
                // Level, Outputs
                Map<Integer, List<HammerReward>> tieredOutputs = Maps.newHashMap();
                
                for(HammerReward reward : allRewards)
                {
                    List<HammerReward> stacks = tieredOutputs.get(reward.getMiningLevel());
                    
                    if(stacks == null)
                    {
                        stacks = Lists.newArrayList(reward);
                        tieredOutputs.put(reward.getMiningLevel(), stacks);
                    }
                    else
                    {
                        stacks.add(reward);
                    }
                }
                
                tieredOutputs.forEach((level, rewards) -> rewards.sort((rewardA, rewardB) -> Float.compare(rewardB.getChance(), rewardA.getChance())));
                
                List<Integer> levelOrder = Lists.newArrayList(tieredOutputs.keySet());
                levelOrder.sort((levelA, levelB) -> Integer.compare(levelB, levelA));
                
                for(int level : levelOrder)
                {
                    tooltip.add(I18n.format("jei.hammer.hammerLevel." + level));
                    
                    List<HammerReward> rewards = tieredOutputs.get(level);
                    
                    for(HammerReward reward : rewards)
                    {
                        float chance = 100.0F * reward.getChance();
                        
                        String format = chance >= 10 ? " - %3.0f%% (x%d)" : "%1.1f%% - (x%d)";
                        
                        tooltip.add(String.format(format, chance, reward.getStack().getCount()));
                    }
                }
            }
        }
    }
}
