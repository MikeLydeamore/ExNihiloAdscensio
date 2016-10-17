package exnihiloadscensio.compatibility.jei;

import java.util.List;

import com.google.common.collect.Lists;

import exnihiloadscensio.blocks.BlockSieve.MeshType;
import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.compatibility.jei.hammer.HammerRecipe;
import exnihiloadscensio.compatibility.jei.hammer.HammerRecipeCategory;
import exnihiloadscensio.compatibility.jei.hammer.HammerRecipeHandler;
import exnihiloadscensio.compatibility.jei.sieve.SieveRecipe;
import exnihiloadscensio.compatibility.jei.sieve.SieveRecipeCategory;
import exnihiloadscensio.compatibility.jei.sieve.SieveRecipeHandler;
import exnihiloadscensio.items.ENItems;
import exnihiloadscensio.registries.HammerRegistry;
import exnihiloadscensio.registries.SieveRegistry;
import exnihiloadscensio.util.BlockInfo;
import exnihiloadscensio.util.ItemInfo;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class CompatJEI implements IModPlugin
{
    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry)
    {
    }
    
    @Override
    public void registerIngredients(IModIngredientRegistration registry)
    {
    }
    
    @Override
    public void register(IModRegistry registry)
    {
        registry.addRecipeCategories(new SieveRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeHandlers(new SieveRecipeHandler());
        
        List<SieveRecipe> sieveRecipes = Lists.newArrayList();
        
        for (BlockInfo info : SieveRegistry.getRegistry().keySet())
        {
            for (MeshType type : MeshType.values())
            {
                if (type.getID() != 0) // The empty mesh strikes back!
                {
                    sieveRecipes.add(new SieveRecipe(info.getBlockState(), type));
                }
            }
        }
        
        registry.addRecipes(sieveRecipes);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENBlocks.sieve), SieveRecipeCategory.UID);
        
        
        registry.addRecipeCategories(new HammerRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeHandlers(new HammerRecipeHandler());
        
        List<HammerRecipe> hammerRecipes = Lists.newArrayList();
        
        for(ItemInfo info : HammerRegistry.getRegistry().keySet())
        {
            @SuppressWarnings("deprecation")
            IBlockState block = Block.getBlockFromItem(info.getItem()).getStateFromMeta(info.getMeta());
            
            hammerRecipes.add(new HammerRecipe(block));
        }
        
        registry.addRecipes(hammerRecipes);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENItems.hammerWood), HammerRecipeCategory.UID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENItems.hammerGold), HammerRecipeCategory.UID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENItems.hammerStone), HammerRecipeCategory.UID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENItems.hammerIron), HammerRecipeCategory.UID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENItems.hammerDiamond), HammerRecipeCategory.UID);
    }
    
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
    {
    }
}
