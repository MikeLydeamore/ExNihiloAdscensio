package exnihiloadscensio;

import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.config.Config;
import exnihiloadscensio.items.ENItems;
import exnihiloadscensio.items.ItemPebble;
import exnihiloadscensio.items.ItemResource;
import exnihiloadscensio.util.LegacyCraftingHelper;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class Recipes {
	
	public static void init()
	{
		LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(ENItems.hammerWood), " x ", " yx", "y  ", 'x', "plankWood", 'y', "stickWood");
		LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(ENItems.hammerStone), " x ", " yx", "y  ", 'x', "cobblestone", 'y', "stickWood");
		LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(ENItems.hammerIron), " x ", " yx", "y  ", 'x', "ingotIron", 'y', "stickWood");
		LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(ENItems.hammerGold), " x ", " yx", "y  ", 'x', "ingotGold", 'y', "stickWood");
		LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(ENItems.hammerDiamond), " x ", " yx", "y  ", 'x', "gemDiamond", 'y', "stickWood");

		LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(ENItems.crookWood), "xx"," x"," x", 'x', "stickWood");
		LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(ENItems.crookBone), "xx"," x"," x", 'x', Items.BONE);

		if (Config.enableBarrels) {
			LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(ENBlocks.barrelWood), "x x","x x", "xyx", 'x', "plankWood", 'y', "slabWood");
			LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(ENBlocks.barrelStone), "x x","x x", "xyx", 'x', new ItemStack(Blocks.STONE), 'y', new ItemStack(Blocks.STONE_SLAB));
		}

		if (Config.enableCrucible) {
			LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(ENBlocks.crucible), "x x","x x","xxx", 'x', "clayPorcelain");

			FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(ENBlocks.crucible), new ItemStack(ENBlocks.crucible, 1, 1), 0.7f);
		}

		LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.COBBLESTONE), "xx","xx", 'x', ItemPebble.getPebbleStack("stone"));
		LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.GRANITE.ordinal()), "xx","xx", 'x', ItemPebble.getPebbleStack("granite"));
		LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.DIORITE.ordinal()), "xx","xx", 'x', ItemPebble.getPebbleStack("diorite"));
		LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(Blocks.STONE, 1, BlockStone.EnumType.ANDESITE.ordinal()), "xx","xx", 'x', ItemPebble.getPebbleStack("andesite"));
		LegacyCraftingHelper.addShapelessRecipe(ItemResource.getResourceStack("porcelain_clay"), new ItemStack(Items.CLAY_BALL), new ItemStack(Items.DYE, 1, 15));

		LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(ENBlocks.sieve), "x x","xyx","z z", 'x', "plankWood", 'y', "slabWood", 'z', "stickWood");
		LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(ENItems.mesh, 1, 1), "xxx","xxx","xxx", 'x', Items.STRING);
		LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(ENItems.mesh, 1, 2), "x x","xyx","x x", 'x', Items.FLINT, 'y', new ItemStack(ENItems.mesh, 1, 1));
		LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(ENItems.mesh, 1, 3), "x x","xyx","x x", 'x', Items.IRON_INGOT, 'y', new ItemStack(ENItems.mesh, 1, 2));
		LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(ENItems.mesh, 1, 4), "x x","xyx","x x", 'x', Items.DIAMOND, 'y', new ItemStack(ENItems.mesh, 1, 3));

		FurnaceRecipes.instance().addSmeltingRecipe(ItemResource.getResourceStack("silkworm"), new ItemStack(ENItems.cookedSilkworm), 0.7f);

		LegacyCraftingHelper.addShapedOreRecipe(ItemResource.getResourceStack("doll", 4), "xyx"," x ", "x x", 'x', "clayPorcelain", 'y', "gemDiamond");
		LegacyCraftingHelper.addShapedOreRecipe(ItemResource.getResourceStack("doll", 6), "xyx"," x ", "x x", 'x', "clayPorcelain", 'y', "gemEmerald");
		LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(ENItems.dolls), "xyx", "zwz", "xvx", 'x', Items.BLAZE_POWDER, 'v', Items.NETHER_WART, 'w', ItemResource.getResourceStack("doll"), 'y', "dustRedstone", 'z', "dustGlowstone");
		LegacyCraftingHelper.addShapedOreRecipe(new ItemStack(ENItems.dolls, 1, 1), "xyx", "zwz", "xvx", 'v', Items.NETHER_WART, 'x', "dyeBlack", 'w', ItemResource.getResourceStack("doll"), 'y', "dustRedstone", 'z', "dustGlowstone");
	}
}
