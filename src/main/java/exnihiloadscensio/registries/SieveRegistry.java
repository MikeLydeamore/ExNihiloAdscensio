package exnihiloadscensio.registries;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import exnihiloadscensio.blocks.BlockSieve.MeshType;
import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.items.ENItems;
import exnihiloadscensio.items.ItemPebble;
import exnihiloadscensio.items.ItemResource;
import exnihiloadscensio.items.ore.ItemOre;
import exnihiloadscensio.items.seeds.ItemSeedBase;
import exnihiloadscensio.json.CustomBlockInfoJson;
import exnihiloadscensio.json.CustomItemInfoJson;
import exnihiloadscensio.registries.types.Siftable;
import exnihiloadscensio.util.BlockInfo;
import exnihiloadscensio.util.ItemInfo;
import lombok.Getter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class SieveRegistry
{
    
    @Getter
    private static HashMap<BlockInfo, ArrayList<Siftable>> registry = new HashMap<>();
    private static Map<BlockInfo, List<Siftable>> externalRegistry = new HashMap<>();
    
    public static void register(BlockInfo block, ItemInfo drop, float chance, int meshLevel)
    {
        register(block, new Siftable(drop, chance, meshLevel));
    }
    
    public static void register(IBlockState state, ItemInfo drop, float chance, int meshLevel)
    {
        register(new BlockInfo(state), new Siftable(drop, chance, meshLevel));
    }
    
    public static void register(IBlockState state, ItemStack drop, float chance, int meshLevel)
    {
        register(new BlockInfo(state), new Siftable(new ItemInfo(drop), chance, meshLevel));
    }
    
    public static void register(BlockInfo block, Siftable drop)
    {
        if (block == null)
        {
            return;
        }
        
        registerInternal(block, drop);
        
        List<Siftable> drops = externalRegistry.get(block);
        
        if (drops == null)
        {
            drops = new ArrayList<>();
        }
        
        drops.add(drop);
        externalRegistry.put(block, drops);
    }
    
    private static void registerInternal(IBlockState state, ItemInfo drop, float chance, int meshLevel)
    {
        register(new BlockInfo(state), new Siftable(drop, chance, meshLevel));
    }
    
    private static void registerInternal(IBlockState state, ItemStack drop, float chance, int meshLevel)
    {
        register(new BlockInfo(state), new Siftable(new ItemInfo(drop), chance, meshLevel));
    }
    
    private static void registerInternal(BlockInfo block, Siftable drop)
    {
        if (block == null)
        {
            return;
        }
        
        ArrayList<Siftable> drops = registry.get(block);
        
        if (drops == null)
        {
            drops = new ArrayList<>();
        }
        
        drops.add(drop);
        registry.put(block, drops);
    }
    
    /**
     * Gets *all* possible drops from the sieve. It is up to the dropper to
     * check whether or not the drops should be dropped!
     * 
     * @param block
     * @return ArrayList of {@linkplain exnihiloadscensio.registries.types.Siftable}
     *         that could *potentially* be dropped.
     */
    public static ArrayList<Siftable> getDrops(BlockInfo block)
    {
        if (!registry.containsKey(block))
            return null;
        
        return registry.get(block);
    }
    
    /**
     * Gets *all* possible drops from the sieve. It is up to the dropper to
     * check whether or not the drops should be dropped!
     * 
     * @param block
     * @return ArrayList of {@linkplain exnihiloadscensio.registries.types.Siftable}
     *         that could *potentially* be dropped.
     */
    public static ArrayList<Siftable> getDrops(ItemStack block)
    {
        return getDrops(new BlockInfo(block));
    }
    
    public static List<ItemStack> getRewardDrops(Random random, IBlockState block, int meshLevel, int fortuneLevel)
    {
        if (block == null)
        {
            return null;
        }
        
        List<Siftable> siftables = getDrops(new BlockInfo(block));
        
        if (siftables == null)
        {
            return null;
        }
        
        List<ItemStack> drops = Lists.newArrayList();
        
        for (Siftable siftable : siftables)
        {
            if (meshLevel == siftable.getMeshLevel())
            {
                int triesWithFortune = Math.max(random.nextInt(fortuneLevel + 2), 1);
                
                for (int i = 0; i < triesWithFortune; i++)
                {
                    if (random.nextDouble() < siftable.getChance())
                    {
                        drops.add(siftable.getDrop().getItemStack());
                    }
                }
            }
        }
        
        return drops;
    }
    
    public static boolean canBeSifted(ItemStack stack)
    {
        if (stack == null)
            return false;
        return registry.containsKey(new BlockInfo(stack));
    }
    
    public static void registerDefaults()
    {
        registerInternal(Blocks.DIRT.getDefaultState(), ItemPebble.getPebbleStack("stone"), 1f, MeshType.STRING.getID());
        registerInternal(Blocks.DIRT.getDefaultState(), ItemPebble.getPebbleStack("stone"), 1f, MeshType.STRING.getID());
        registerInternal(Blocks.DIRT.getDefaultState(), ItemPebble.getPebbleStack("stone"), 0.5f, MeshType.STRING.getID());
        registerInternal(Blocks.DIRT.getDefaultState(), ItemPebble.getPebbleStack("stone"), 0.5f, MeshType.STRING.getID());
        registerInternal(Blocks.DIRT.getDefaultState(), ItemPebble.getPebbleStack("stone"), 0.1f, MeshType.STRING.getID());
        registerInternal(Blocks.DIRT.getDefaultState(), ItemPebble.getPebbleStack("stone"), 0.1f, MeshType.STRING.getID());
        
        registerInternal(Blocks.DIRT.getDefaultState(), ItemPebble.getPebbleStack("granite"), 0.5f, MeshType.STRING.getID());
        registerInternal(Blocks.DIRT.getDefaultState(), ItemPebble.getPebbleStack("granite"), 0.1f, MeshType.STRING.getID());
        
        registerInternal(Blocks.DIRT.getDefaultState(), ItemPebble.getPebbleStack("diorite"), 0.5f, MeshType.STRING.getID());
        registerInternal(Blocks.DIRT.getDefaultState(), ItemPebble.getPebbleStack("diorite"), 0.1f, MeshType.STRING.getID());
        
        registerInternal(Blocks.DIRT.getDefaultState(), ItemPebble.getPebbleStack("andesite"), 0.5f, MeshType.STRING.getID());
        registerInternal(Blocks.DIRT.getDefaultState(), ItemPebble.getPebbleStack("andesite"), 0.1f, MeshType.STRING.getID());
        
        registerInternal(Blocks.DIRT.getDefaultState(), new ItemInfo(Items.WHEAT_SEEDS, 0), 0.7f, MeshType.STRING.getID());
        registerInternal(Blocks.DIRT.getDefaultState(), new ItemInfo(Items.MELON_SEEDS, 0), 0.35f, MeshType.STRING.getID());
        registerInternal(Blocks.DIRT.getDefaultState(), new ItemInfo(Items.PUMPKIN_SEEDS, 0), 0.35f, MeshType.STRING.getID());
        
        registerInternal(Blocks.SAND.getDefaultState(), new ItemInfo(Items.DYE, 3), 0.03f, MeshType.STRING.getID());
        
        registerInternal(Blocks.GRAVEL.getDefaultState(), new ItemInfo(Items.FLINT, 0), 0.25f, MeshType.FLINT.getID());
        registerInternal(Blocks.GRAVEL.getDefaultState(), new ItemInfo(Items.COAL, 0), 0.125f, MeshType.FLINT.getID());
        registerInternal(Blocks.GRAVEL.getDefaultState(), new ItemInfo(Items.DYE, 4), 0.05f, MeshType.FLINT.getID());
        
        registerInternal(Blocks.GRAVEL.getDefaultState(), new ItemInfo(Items.DIAMOND, 0), 0.008f, MeshType.IRON.getID());
        registerInternal(Blocks.GRAVEL.getDefaultState(), new ItemInfo(Items.EMERALD, 0), 0.008f, MeshType.IRON.getID());
        
        registerInternal(Blocks.GRAVEL.getDefaultState(), new ItemInfo(Items.DIAMOND, 0), 0.016f, MeshType.DIAMOND.getID());
        registerInternal(Blocks.GRAVEL.getDefaultState(), new ItemInfo(Items.EMERALD, 0), 0.016f, MeshType.DIAMOND.getID());
        
        registerInternal(Blocks.SOUL_SAND.getDefaultState(), new ItemInfo(Items.QUARTZ, 0), 1f, MeshType.FLINT.getID());
        registerInternal(Blocks.SOUL_SAND.getDefaultState(), new ItemInfo(Items.QUARTZ, 0), 0.33f, MeshType.FLINT.getID());
        
        registerInternal(Blocks.SOUL_SAND.getDefaultState(), new ItemInfo(Items.NETHER_WART, 0), 0.1f, MeshType.STRING.getID());
        
        registerInternal(Blocks.SOUL_SAND.getDefaultState(), new ItemInfo(Items.GHAST_TEAR, 0), 0.02f, MeshType.DIAMOND.getID());
        registerInternal(Blocks.SOUL_SAND.getDefaultState(), new ItemInfo(Items.QUARTZ, 0), 1f, MeshType.DIAMOND.getID());
        registerInternal(Blocks.SOUL_SAND.getDefaultState(), new ItemInfo(Items.QUARTZ, 0), 0.8f, MeshType.DIAMOND.getID());
        
        registerInternal(ENBlocks.dust.getDefaultState(), new ItemInfo(Items.DYE, 15), 0.2f, MeshType.STRING.getID());
        registerInternal(ENBlocks.dust.getDefaultState(), new ItemInfo(Items.GUNPOWDER, 0), 0.07f, MeshType.STRING.getID());
        
        registerInternal(ENBlocks.dust.getDefaultState(), new ItemInfo(Items.REDSTONE, 0), 0.125f, MeshType.IRON.getID());
        registerInternal(ENBlocks.dust.getDefaultState(), new ItemInfo(Items.REDSTONE, 0), 0.25f, MeshType.DIAMOND.getID());
        
        registerInternal(ENBlocks.dust.getDefaultState(), new ItemInfo(Items.GLOWSTONE_DUST, 0), 0.0625f, MeshType.IRON.getID());
        registerInternal(ENBlocks.dust.getDefaultState(), new ItemInfo(Items.BLAZE_POWDER, 0), 0.05f, MeshType.IRON.getID());
        
        // Ores
        for (ItemOre ore : OreRegistry.getItemOreRegistry())
        {
            registerInternal(Blocks.GRAVEL.getDefaultState(), new ItemStack(ore, 1, 0), 0.2f, MeshType.FLINT.getID());
            registerInternal(Blocks.GRAVEL.getDefaultState(), new ItemStack(ore, 1, 0), 0.2f, MeshType.IRON.getID());
            registerInternal(Blocks.GRAVEL.getDefaultState(), new ItemStack(ore, 1, 0), 0.1f, MeshType.DIAMOND.getID());
        }
        
        // Seeds
        for (ItemSeedBase seed : ENItems.itemSeeds)
        {
            registerInternal(Blocks.DIRT.getDefaultState(), new ItemStack(seed), 0.05f, MeshType.STRING.getID());
        }
        
        registerInternal(Blocks.DIRT.getDefaultState(), ItemResource.getResourceStack(ItemResource.ANCIENT_SPORES), 0.05f, MeshType.STRING.getID());
        registerInternal(Blocks.DIRT.getDefaultState(), ItemResource.getResourceStack(ItemResource.GRASS_SEEDS), 0.05f, MeshType.STRING.getID());
    }
    
    private static Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(BlockInfo.class, new CustomBlockInfoJson()).registerTypeAdapter(ItemInfo.class, new CustomItemInfoJson()).create();
    
    public static void loadJson(File file)
    {
        registry.clear();
        
        if (file.exists())
        {
            try
            {
                FileReader fr = new FileReader(file);
                HashMap<String, ArrayList<Siftable>> gsonInput = gson.fromJson(fr, new TypeToken<HashMap<String, ArrayList<Siftable>>>(){}.getType());
                
                for (Map.Entry<String, ArrayList<Siftable>> input : gsonInput.entrySet())
                {
                    BlockInfo block = new BlockInfo(input.getKey());
                    
                    registry.put(block, input.getValue());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            registerDefaults();
            saveJson(file);
        }
        
        for (Entry<BlockInfo, List<Siftable>> entry : externalRegistry.entrySet())
        {
            for (Siftable siftable : entry.getValue())
            {
                registerInternal(entry.getKey(), siftable);
            }
        }
    }
    
    public static void saveJson(File file)
    {
        try
        {
            FileWriter fw = new FileWriter(file);
            gson.toJson(registry, fw);
            
            fw.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
}
