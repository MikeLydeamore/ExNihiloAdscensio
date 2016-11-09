package exnihiloadscensio.compatibility.jei;

import java.util.List;

import com.google.common.collect.Lists;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.blocks.BlockSieve.MeshType;
import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.compatibility.jei.barrel.fluidblocktransform.FluidBlockTransformRecipe;
import exnihiloadscensio.compatibility.jei.barrel.fluidblocktransform.FluidBlockTransformRecipeCategory;
import exnihiloadscensio.compatibility.jei.barrel.fluidblocktransform.FluidBlockTransformRecipeHandler;
import exnihiloadscensio.compatibility.jei.barrel.fluidontop.FluidOnTopRecipe;
import exnihiloadscensio.compatibility.jei.barrel.fluidontop.FluidOnTopRecipeCategory;
import exnihiloadscensio.compatibility.jei.barrel.fluidontop.FluidOnTopRecipeHandler;
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
import exnihiloadscensio.registries.FluidBlockTransformerRegistry;
import exnihiloadscensio.registries.FluidOnTopRegistry;
import exnihiloadscensio.registries.FluidTransformRegistry;
import exnihiloadscensio.registries.HammerRegistry;
import exnihiloadscensio.registries.SieveRegistry;
import exnihiloadscensio.registries.types.FluidBlockTransformer;
import exnihiloadscensio.registries.types.FluidFluidBlock;
import exnihiloadscensio.registries.types.FluidTransformer;
import exnihiloadscensio.util.BlockInfo;
import exnihiloadscensio.util.ItemInfo;
import exnihiloadscensio.util.LogUtil;
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
        LogUtil.info("Config Loaded: " + ExNihiloAdscensio.configsLoaded);
        
        if(ExNihiloAdscensio.configsLoaded)
        {
            ExNihiloAdscensio.loadConfigs();
        }
        
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

        registry.addRecipeCategories(new FluidOnTopRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeHandlers(new FluidOnTopRecipeHandler());

        List<FluidOnTopRecipe> fluidOnTopRecipes = Lists.newArrayList();
        
        for (FluidFluidBlock transformer : FluidOnTopRegistry.getRegistry())
        {
            // Make sure both fluids are registered
            if (FluidRegistry.isFluidRegistered(transformer.getFluidInBarrel()) && FluidRegistry.isFluidRegistered(transformer.getFluidOnTop()) && transformer.getResult().getItem() != null)
            {
                FluidOnTopRecipe recipe = new FluidOnTopRecipe(transformer);
                
                if(recipe.getInputs().size() == 2 && recipe.getOutputs().size() == 1)
                {
                    fluidOnTopRecipes.add(new FluidOnTopRecipe(transformer));
                }
            }
        }
        
        registry.addRecipes(fluidOnTopRecipes);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENBlocks.barrelWood), FluidOnTopRecipeCategory.UID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENBlocks.barrelStone), FluidOnTopRecipeCategory.UID);

        registry.addRecipeCategories(new FluidBlockTransformRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeHandlers(new FluidBlockTransformRecipeHandler());

        List<FluidBlockTransformRecipe> fluidBlockTransformRecipes = Lists.newArrayList();
        
        for (FluidBlockTransformer transformer : FluidBlockTransformerRegistry.getRegistry())
        {
            // Make sure everything's registered
            if (FluidRegistry.isFluidRegistered(transformer.getFluidName()) && transformer.getInput().getItem() != null && transformer.getOutput().getItem() != null)
            {
                FluidBlockTransformRecipe recipe = new FluidBlockTransformRecipe(transformer);
                
                if(recipe.getInputs().size() == 2 && recipe.getOutputs().size() == 1)
                {
                    fluidBlockTransformRecipes.add(new FluidBlockTransformRecipe(transformer));
                }
            }
        }
        
        registry.addRecipes(fluidBlockTransformRecipes);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENBlocks.barrelWood), FluidBlockTransformRecipeCategory.UID);
        registry.addRecipeCategoryCraftingItem(new ItemStack(ENBlocks.barrelStone), FluidBlockTransformRecipeCategory.UID);

        LogUtil.info("Hammer Recipes Loaded:             " + hammerRecipes.size());
        LogUtil.info("Sieve Recipes Loaded:              " + sieveRecipes.size());
        LogUtil.info("Fluid Transform Recipes Loaded:    " + fluidTransformRecipes.size());
        LogUtil.info("Fluid On Top Recipes Loaded:       " + fluidOnTopRecipes.size());
        LogUtil.info("");
        LogUtil.info("Hammer Registries Loaded:          " + HammerRegistry.getRegistry().size());
        LogUtil.info("Sieve Registries Loaded:           " + SieveRegistry.getRegistry().size());
        LogUtil.info("Fluid Transform Registries Loaded: " + FluidTransformRegistry.getRegistry().size());
        LogUtil.info("Fluid On Top Registries Loaded:    " + FluidOnTopRegistry.getRegistry().size());
    }
    
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
    {
    }
}
