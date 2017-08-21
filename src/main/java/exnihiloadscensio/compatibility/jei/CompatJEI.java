package exnihiloadscensio.compatibility.jei;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.blocks.BlockSieve.MeshType;
import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.compatibility.jei.barrel.compost.CompostRecipe;
import exnihiloadscensio.compatibility.jei.barrel.compost.CompostRecipeCategory;
import exnihiloadscensio.compatibility.jei.barrel.fluidblocktransform.FluidBlockTransformRecipe;
import exnihiloadscensio.compatibility.jei.barrel.fluidblocktransform.FluidBlockTransformRecipeCategory;
import exnihiloadscensio.compatibility.jei.barrel.fluidontop.FluidOnTopRecipe;
import exnihiloadscensio.compatibility.jei.barrel.fluidontop.FluidOnTopRecipeCategory;
import exnihiloadscensio.compatibility.jei.barrel.fluidtransform.FluidTransformRecipe;
import exnihiloadscensio.compatibility.jei.barrel.fluidtransform.FluidTransformRecipeCategory;
import exnihiloadscensio.compatibility.jei.hammer.HammerRecipe;
import exnihiloadscensio.compatibility.jei.hammer.HammerRecipeCategory;
import exnihiloadscensio.compatibility.jei.sieve.SieveRecipe;
import exnihiloadscensio.compatibility.jei.sieve.SieveRecipeCategory;
import exnihiloadscensio.items.ENItems;
import exnihiloadscensio.registries.*;
import exnihiloadscensio.registries.types.Compostable;
import exnihiloadscensio.registries.types.FluidBlockTransformer;
import exnihiloadscensio.registries.types.FluidFluidBlock;
import exnihiloadscensio.registries.types.FluidTransformer;
import exnihiloadscensio.util.BlockInfo;
import exnihiloadscensio.util.ItemInfo;
import exnihiloadscensio.util.LogUtil;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.List;
import java.util.Map;

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
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new SieveRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new HammerRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new FluidTransformRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new FluidOnTopRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new FluidBlockTransformRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
        registry.addRecipeCategories(new CompostRecipeCategory(registry.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void register(IModRegistry registry)
    {
        LogUtil.info("Config Loaded: " + ExNihiloAdscensio.configsLoaded);
        
        if(!ExNihiloAdscensio.configsLoaded)
        {
            ExNihiloAdscensio.loadConfigs();
        }

        registry.handleRecipes(SieveRecipe.class, recipe -> recipe, SieveRecipeCategory.UID);
        
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
        
        registry.addRecipes(sieveRecipes, SieveRecipeCategory.UID);
        registry.addRecipeCatalyst(Ingredient.fromItem(Item.getItemFromBlock(ENBlocks.sieve)), SieveRecipeCategory.UID);
        registry.handleRecipes(HammerRecipe.class, recipe -> recipe, HammerRecipeCategory.UID);
        
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
        
        registry.addRecipes(hammerRecipes, HammerRecipeCategory.UID);
        registry.addRecipeCatalyst(Ingredient.fromItem(ENItems.hammerWood), HammerRecipeCategory.UID);
        registry.addRecipeCatalyst(Ingredient.fromItem(ENItems.hammerGold), HammerRecipeCategory.UID);
        registry.addRecipeCatalyst(Ingredient.fromItem(ENItems.hammerStone), HammerRecipeCategory.UID);
        registry.addRecipeCatalyst(Ingredient.fromItem(ENItems.hammerIron), HammerRecipeCategory.UID);
        registry.addRecipeCatalyst(Ingredient.fromItem(ENItems.hammerDiamond), HammerRecipeCategory.UID);

        registry.handleRecipes(FluidTransformRecipe.class, recipe -> recipe, FluidTransformRecipeCategory.UID);

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
        
        registry.addRecipes(fluidTransformRecipes, FluidTransformRecipeCategory.UID);
        registry.addRecipeCatalyst(Ingredient.fromItem(Item.getItemFromBlock(ENBlocks.barrelWood)), FluidTransformRecipeCategory.UID);
        registry.addRecipeCatalyst(Ingredient.fromItem(Item.getItemFromBlock(ENBlocks.barrelStone)), FluidTransformRecipeCategory.UID);

        registry.handleRecipes(FluidOnTopRecipe.class, recipe -> recipe, FluidOnTopRecipeCategory.UID);

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
        
        registry.addRecipes(fluidOnTopRecipes, FluidOnTopRecipeCategory.UID);
        registry.addRecipeCatalyst(Ingredient.fromItem(Item.getItemFromBlock(ENBlocks.barrelWood)), FluidOnTopRecipeCategory.UID);
        registry.addRecipeCatalyst(Ingredient.fromItem(Item.getItemFromBlock(ENBlocks.barrelStone)), FluidOnTopRecipeCategory.UID);

        registry.handleRecipes(FluidBlockTransformRecipe.class, recipe -> recipe, FluidBlockTransformRecipeCategory.UID);

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
        
        registry.addRecipes(fluidBlockTransformRecipes, FluidBlockTransformRecipeCategory.UID);
        registry.addRecipeCatalyst(Ingredient.fromItem(Item.getItemFromBlock(ENBlocks.barrelWood)), FluidBlockTransformRecipeCategory.UID);
        registry.addRecipeCatalyst(Ingredient.fromItem(Item.getItemFromBlock(ENBlocks.barrelStone)), FluidBlockTransformRecipeCategory.UID);

        registry.handleRecipes(CompostRecipe.class, recipe -> recipe, CompostRecipeCategory.UID);
        
        List<CompostRecipe> compostRecipes = Lists.newArrayList();
        
        Map<ItemInfo, Compostable> compostRegistry = CompostRegistry.getRegistry();
        Map<ItemInfo, List<ItemStack>> compostEntries = Maps.newHashMap();
        
        for(Map.Entry<ItemInfo, Compostable> compostEntry : compostRegistry.entrySet())
        {
            ItemInfo compostBlock = compostEntry.getValue().getCompostBlock();
            
            List<ItemStack> compostables = compostEntries.get(compostBlock);
            
            if(compostables == null)
            {
                compostEntries.put(compostBlock, compostables = Lists.newArrayList());
            }
            
            Item compostItem = compostEntry.getKey().getItem();
            int compostCount = (int) Math.ceil(1.0F / compostEntry.getValue().getValue());
            int compostMeta = compostEntry.getKey().getMeta();
            
            if(compostMeta == -1)
            {
                NonNullList<ItemStack> subItems = NonNullList.create();
                compostItem.getSubItems(null, subItems);
                
                for(ItemStack subItem : subItems)
                {
                    subItem.setCount(compostCount);
                    compostables.add(subItem);
                }
            }
            else
            {
                compostables.add(new ItemStack(compostItem, compostCount, compostMeta));
            }
        }
        
        for(Map.Entry<ItemInfo, List<ItemStack>> compostEntry : compostEntries.entrySet())
        {
            // I heard you like lists, you I put some lists in your lists, so you can list while you list
            List<List<ItemStack>> splitList = Lists.newArrayList(ImmutableList.of(Lists.newArrayList()));
            
            for(ItemStack stack : compostEntry.getValue())
            {
                if(splitList.get(0).size() >= 45)
                {
                    splitList.add(0, Lists.newArrayList());
                }
                
                splitList.get(0).add(stack);
            }
            
            for(List<ItemStack> compostInputs : Lists.reverse(splitList))
            {
                compostRecipes.add(new CompostRecipe(compostEntry.getKey().getItemStack(), compostInputs));
            }
        }

        registry.addRecipes(compostRecipes, CompostRecipeCategory.UID);
        registry.addRecipeCatalyst(Ingredient.fromItem(Item.getItemFromBlock(ENBlocks.barrelWood)), CompostRecipeCategory.UID);
        registry.addRecipeCatalyst(Ingredient.fromItem(Item.getItemFromBlock(ENBlocks.barrelStone)), CompostRecipeCategory.UID);
        
        LogUtil.info("Hammer Recipes Loaded:             " + hammerRecipes.size());
        LogUtil.info("Sieve Recipes Loaded:              " + sieveRecipes.size());
        LogUtil.info("Fluid Transform Recipes Loaded:    " + fluidTransformRecipes.size());
        LogUtil.info("Fluid On Top Recipes Loaded:       " + fluidOnTopRecipes.size());
        LogUtil.info("Compost Recipes Loaded:            " + compostRecipes.size());
    }
    
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
    {
    }
}
