package exnihiloadscensio.compatibility.jei.sieve;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.registries.SieveRegistry;
import exnihiloadscensio.registries.types.Siftable;
import exnihiloadscensio.util.ItemInfo;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SieveRecipeCategory implements IRecipeCategory<SieveRecipe> {

	public static final String UID = "exnihiloadscensio:sieve";
	private static final ResourceLocation texture = new ResourceLocation(ExNihiloAdscensio.MODID, "textures/gui/jei_sieve.png");

	private final IDrawableStatic background;
	private final IDrawableStatic slotHighlight;
	private boolean hasHighlight;
	private int highlightX;
    private int highlightY;
    
    public SieveRecipeCategory(IGuiHelper helper)
    {
        this.background = helper.createDrawable(texture, 0, 0, 166, 128);
        this.slotHighlight = helper.createDrawable(texture, 166, 0, 18, 18);
    }
    
	@Override
	public String getUid() {
		return UID;
	}

	@Override
	public String getTitle() {
		return "Sieve";
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
		if(hasHighlight) {
			slotHighlight.draw(minecraft, highlightX, highlightY);
		}		
	}

	@Override
	public void drawAnimations(Minecraft minecraft) {
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, final SieveRecipe recipeWrapper) {
		//Mesh
		recipeLayout.getItemStacks().init(0, true, 61, 9);
		recipeLayout.getItemStacks().set(0, (ItemStack) recipeWrapper.getInputs().get(0));
		//Block
		recipeLayout.getItemStacks().init(1, true, 87, 9);
		recipeLayout.getItemStacks().set(1, (ItemStack) recipeWrapper.getInputs().get(1));

		IFocus<?> focus = recipeLayout.getFocus();
		hasHighlight = focus.getMode() == IFocus.Mode.OUTPUT;

		int slotIndex = 2;
		for (int i = 0 ; i < recipeWrapper.getOutputs().size(); i++) {
			final int slotX = 2 + (i % 9 * 18);
			final int slotY = 36 + (i / 9 * 18);
			ItemStack outputStack = (ItemStack) recipeWrapper.getOutputs().get(i);
			recipeLayout.getItemStacks().init(slotIndex+i, false, slotX, slotY);
			recipeLayout.getItemStacks().set(slotIndex+i, outputStack);

			ItemStack focusStack = (ItemStack) focus.getValue();
			if(focus.getMode() == IFocus.Mode.OUTPUT && focusStack != null
					&& focusStack.getItem() == outputStack.getItem()
					&& focusStack.getItemDamage() == outputStack.getItemDamage()) {
				highlightX = slotX;
				highlightY = slotY;
			}
		}
		
		recipeLayout.getItemStacks().addTooltipCallback(new ITooltipCallback<ItemStack>() {
			@Override
			public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
				if(!input) {
					ItemStack mesh = (ItemStack) recipeWrapper.getInputs().get(0);
					Multiset<String> condensedTooltips = HashMultiset.create();
					for(Siftable siftable : SieveRegistry.getDrops((ItemStack) recipeWrapper.getInputs().get(1))) {
						if (siftable.getMeshLevel() != mesh.getItemDamage())
							continue;
						ItemInfo info = siftable.getDrop();
						if (info.getItem() != ingredient.getItem() || info.getMeta() != ingredient.getItemDamage())
							continue;
						
						String s;
						int iChance = (int) (siftable.getChance() * 100f);
						if(iChance > 0) {
							s = String.format("%3d%%", (int) (siftable.getChance() * 100f));
						} else {
							s = String.format("%1.1f%%", siftable.getChance() * 100f);
						}
						condensedTooltips.add(s);
					}
					tooltip.add(I18n.format("jei.sieve.dropChance"));
					for(String line : condensedTooltips.elementSet()) {
						tooltip.add(" * " + condensedTooltips.count(line) + "x " + line);
					}
				}
			}
		});
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, SieveRecipe recipeWrapper, IIngredients ingredients) {
		setRecipe(recipeLayout, recipeWrapper); //I'm sure this is bad.

	}

    @Override
    public IDrawable getIcon()
    {
        return null;
    }

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return Collections.emptyList();
	}
}
