package exnihiloadscensio.compatibility.jei;

import java.util.List;

import com.google.common.collect.Lists;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.blocks.BlockSieve.MeshType;
import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.compatibility.jei.barrel.fluidtransform.FluidTransformRecipe;
import exnihiloadscensio.compatibility.jei.barrel.fluidtransform.FluidTransformRecipeCategory;
import exnihiloadscensio.compatibility.jei.barrel.fluidtransform.FluidTransformRecipeHandler;
import exnihiloadscensio.compatibility.jei.hammer.HammerRecipe;
import exnihiloadscensio.compatibility.jei.hammer.HammerRecipeCategory;
import exnihiloadscensio.compatibility.jei.hammer.HammerRecipeHandler;
import exnihiloadscensio.compatibility.jei.sieve.SieveRecipe;
import exnihiloadscensio.compatibility.jei.sieve.SieveRecipeCategory;
import exnihiloadscensio.compatibility.jei.sieve.SieveRecipeHandler;
import exnihiloadscensio.items.ENItems;
import exnihiloadscensio.registries.FluidTransformRegistry;
import exnihiloadscensio.registries.HammerRegistry;
import exnihiloadscensio.registries.SieveRegistry;
import exnihiloadscensio.registries.types.FluidTransformer;
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
import net.minecraftforge.fluids.FluidRegistry;

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
                if (type.getID() != 0 && info.getBlockState() != null) // Bad configs strike back!
                {
                    SieveRecipe recipe = new SieveRecipe(info.getBlockState(), type);
                    
                    // If there's an input block, mesh, and at least one output
                    if(recipe.getInputs().size() == 2 && recipe.getOutputs().size() > 0)
                    {
                        sieveRecipes.add(recipe);
                    }
                }
            }
        }
        
        registry.addRecipes(sieveRecipes);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENBlocks.sieve), SieveRecipeCategory.UID);
        
        registry.addRecipeCategories(new HammerRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeHandlers(new HammerRecipeHandler());
        
        List<HammerRecipe> hammerRecipes = Lists.newArrayList();
        
        for (ItemInfo info : HammerRegistry.getRegistry().keySet())
        {
            if (info.getItem() != null)
            {
                @SuppressWarnings("deprecation")
                IBlockState block = Block.getBlockFromItem(info.getItem()).getStateFromMeta(info.getMeta());
                
                HammerRecipe recipe = new HammerRecipe(block);
                
                // If there's an input block, and at least one output
                if(recipe.getInputs().size() == 1 && recipe.getOutputs().size() > 0)
                {
                    hammerRecipes.add(recipe);
                }
            }
        }
        
        registry.addRecipes(hammerRecipes);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENItems.hammerWood), HammerRecipeCategory.UID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENItems.hammerGold), HammerRecipeCategory.UID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENItems.hammerStone), HammerRecipeCategory.UID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENItems.hammerIron), HammerRecipeCategory.UID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENItems.hammerDiamond), HammerRecipeCategory.UID);
        
        registry.addRecipeCategories(new FluidTransformRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeHandlers(new FluidTransformRecipeHandler());
        
        List<FluidTransformRecipe> fluidTransformRecipes = Lists.newArrayList();
        
        for (FluidTransformer transformer : FluidTransformRegistry.getRegistry())
        {
            // Make sure both fluids are registered
            if (FluidRegistry.isFluidRegistered(transformer.getInputFluid()) && FluidRegistry.isFluidRegistered(transformer.getOutputFluid()))
            {
                FluidTransformRecipe recipe = new FluidTransformRecipe(transformer);
                
                // If theres a bucket and at least one block (and an output, for consistency)
                if(recipe.getInputs().size() >= 2 && recipe.getOutputs().size() == 1)
                {
                    fluidTransformRecipes.add(new FluidTransformRecipe(transformer));
                }
            }
        }
        
        registry.addRecipes(fluidTransformRecipes);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENBlocks.barrelWood), FluidTransformRecipeCategory.UID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENBlocks.barrelStone), FluidTransformRecipeCategory.UID);

        ExNihiloAdscensio.instance.logger.info("Hammer Recipes Loaded:          " + hammerRecipes.size());
        ExNihiloAdscensio.instance.logger.info("Sieve Recipes Loaded:           " + sieveRecipes.size());
        ExNihiloAdscensio.instance.logger.info("Fluid Transform Recipes Loaded: " + fluidTransformRecipes.size());
    }
    
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
    {
    }
}
