package exnihiloadscensio.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.GameData;

import javax.annotation.Nonnull;

public final class LegacyCraftingHelper {

    public static void addShapedOreRecipe(@Nonnull ItemStack output, @Nonnull Object... params) {
        ResourceLocation location = getNameForRecipe(output);

        if (location != null) {
            ShapedOreRecipe recipe = new ShapedOreRecipe(location, output, params);

            recipe.setRegistryName(location);
            GameData.register_impl(recipe);
        }
    }

    public static void addShapedRecipe(@Nonnull ItemStack output, @Nonnull Object... params) {
        ResourceLocation location = getNameForRecipe(output);
        ResourceLocation outputRegistryName = output.getItem().getRegistryName();

        if (location != null && outputRegistryName != null) {
            CraftingHelper.ShapedPrimer primer = CraftingHelper.parseShaped(params);
            ShapedRecipes recipe = new ShapedRecipes(outputRegistryName.toString(), primer.width, primer.height, primer.input, output);

            recipe.setRegistryName(location);
            GameData.register_impl(recipe);
        }
    }

    public static void addShapelessOreRecipe(@Nonnull ItemStack output, @Nonnull Object... params) {
        ResourceLocation location = getNameForRecipe(output);

        if (location != null) {
            ShapelessOreRecipe recipe = new ShapelessOreRecipe(location, output, params);

            recipe.setRegistryName(location);
            GameData.register_impl(recipe);
        }
    }

    public static void addShapelessRecipe(@Nonnull ItemStack output, @Nonnull Object... params) {
        ResourceLocation location = getNameForRecipe(output);

        if (location != null) {
            ShapelessRecipes recipe = new ShapelessRecipes(location.getResourceDomain(), output, buildInput(params));

            recipe.setRegistryName(location);
            GameData.register_impl(recipe);
        }
    }

    public static ResourceLocation getNameForRecipe(@Nonnull ItemStack output) {
        ModContainer activeContainer = Loader.instance().activeModContainer();
        ResourceLocation outputRegistryName = output.getItem().getRegistryName();

        if (activeContainer != null && outputRegistryName != null) {
            ResourceLocation baseLocation = new ResourceLocation(activeContainer.getModId(), outputRegistryName.getResourcePath());
            ResourceLocation recipeLocation = baseLocation;

            for (int i = 0; CraftingManager.REGISTRY.containsKey(recipeLocation); i ++, recipeLocation = new ResourceLocation(activeContainer.getModId(), baseLocation.getResourcePath() + "_" + i));

            return recipeLocation;
        }

        return null;
    }

    private static NonNullList<Ingredient> buildInput(Object[] input) {
        NonNullList<Ingredient> list = NonNullList.create();

        for (Object obj : input) {
            if (obj instanceof Ingredient) {
                list.add((Ingredient) obj);
            } else {
                Ingredient ingredient = CraftingHelper.getIngredient(obj);

                if (ingredient == null) {
                    ingredient = Ingredient.EMPTY;
                }

                list.add(ingredient);
            }
        }

        return list;
    }

}
