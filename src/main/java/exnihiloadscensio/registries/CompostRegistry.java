package exnihiloadscensio.registries;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import exnihiloadscensio.json.CustomItemInfoJson;
import exnihiloadscensio.registries.types.Compostable;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.util.ItemInfo;
import exnihiloadscensio.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class CompostRegistry {

	private static Map<ItemInfo, Compostable> registry = new HashMap<>();
    private static Map<ItemInfo, Compostable> externalRegistry = new HashMap<>();
    
    private static Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(ItemInfo.class, new CustomItemInfoJson()).create();
    
	public static void loadJson(File file)
	{
	    registry.clear();
	    
		if (file.exists())
		{
			try 
			{
				FileReader fr = new FileReader(file);
				Map<String, Compostable> gsonInput = gson.fromJson(fr, new TypeToken<Map<String, Compostable>>(){}.getType());
				
				for(Map.Entry<String, Compostable> entry : gsonInput.entrySet())
				{
				    registry.put(new ItemInfo(entry.getKey()), entry.getValue());
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
        
        registry.putAll(externalRegistry);
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

    public static void register(ItemInfo item, Compostable compostable)
    {
        registerInternal(item, compostable);
        externalRegistry.put(item, compostable);
    }

    public static void register(Item item, int meta, float value, IBlockState state, Color color)
    {
        register(new ItemInfo(item, meta), new Compostable(value, color, new ItemInfo(state)));
    }

    public static void register(Block block, int meta, float value, IBlockState state, Color color)
    {
        register(Item.getItemFromBlock(block), meta, value, state, color);
    }

    private static void registerInternal(ItemInfo item, Compostable compostable)
    {
        registry.put(item, compostable);
    }

    private static void registerInternal(Item item, int meta, float value, IBlockState state, Color color)
    {
        registerInternal(new ItemInfo(item, meta), new Compostable(value, color, new ItemInfo(state)));
    }

    private static void registerInternal(Block block, int meta, float value, IBlockState state, Color color)
    {
        registerInternal(Item.getItemFromBlock(block), meta, value, state, color);
    }

	public static Compostable getItem(Item item, int meta)
	{
		return getItem(new ItemInfo(item, meta));
	}

	public static Compostable getItem(ItemStack stack)
	{
		return getItem(new ItemInfo(stack));
	}

    public static Compostable getItem(ItemInfo info)
    {
        return registry.get(info);
    }

    public static boolean containsItem(Item item, int meta)
    {
        return containsItem(new ItemInfo(item, meta));
    }

    public static boolean containsItem(ItemStack stack)
    {
        return containsItem(new ItemInfo(stack));
    }

	public static boolean containsItem(ItemInfo info)
	{
	    return registry.containsKey(info);
	}

	public static void registerDefaults()
	{
		IBlockState dirtState = Blocks.DIRT.getDefaultState();
		
		registerInternal(Items.ROTTEN_FLESH, 0, 0.1f, dirtState, new Color("C45631"));

		registerInternal(Blocks.SAPLING, 0, 0.125f, dirtState, new Color("35A82A"));
		registerInternal(Blocks.SAPLING, 1, 0.125f, dirtState, new Color("2E8042"));
		registerInternal(Blocks.SAPLING, 2, 0.125f, dirtState, new Color("6CC449"));
		registerInternal(Blocks.SAPLING, 3, 0.125f, dirtState, new Color("22A116"));
		registerInternal(Blocks.SAPLING, 4, 0.125f, dirtState, new Color("B8C754"));
		registerInternal(Blocks.SAPLING, 5, 0.125f, dirtState, new Color("378030"));

		registerInternal(Blocks.LEAVES, 0, 0.125f, dirtState, new Color("35A82A"));
		registerInternal(Blocks.LEAVES, 1, 0.125f, dirtState, new Color("2E8042"));
		registerInternal(Blocks.LEAVES, 2, 0.125f, dirtState, new Color("6CC449"));
		registerInternal(Blocks.LEAVES, 3, 0.125f, dirtState, new Color("22A116"));
		registerInternal(Blocks.LEAVES2, 0, 0.125f, dirtState, new Color("B8C754"));
		registerInternal(Blocks.LEAVES2, 1, 0.125f, dirtState, new Color("378030"));

		registerInternal(Items.SPIDER_EYE, 0, 0.08f, dirtState, new Color("963E44"));

		registerInternal(Items.WHEAT, 0, 0.08f, dirtState, new Color("E3E162"));	
		registerInternal(Items.WHEAT_SEEDS, 0, 0.08f, dirtState, new Color("35A82A"));
		registerInternal(Items.BREAD, 0, 0.16f, dirtState, new Color("D1AF60"));

		registerInternal(Blocks.YELLOW_FLOWER, 0, 0.10f, dirtState, new Color("FFF461"));
		registerInternal(Blocks.RED_FLOWER, 0, 0.10f, dirtState, new Color("FF1212"));
		registerInternal(Blocks.RED_FLOWER, 1, 0.10f, dirtState, new Color("33CFFF"));
		registerInternal(Blocks.RED_FLOWER, 2, 0.10f, dirtState, new Color("F59DFA"));
		registerInternal(Blocks.RED_FLOWER, 3, 0.10f, dirtState, new Color("E3E3E3"));
		registerInternal(Blocks.RED_FLOWER, 4, 0.10f, dirtState, new Color("FF3D12"));
		registerInternal(Blocks.RED_FLOWER, 5, 0.10f, dirtState, new Color("FF7E29"));
		registerInternal(Blocks.RED_FLOWER, 6, 0.10f, dirtState, new Color("FFFFFF"));
		registerInternal(Blocks.RED_FLOWER, 7, 0.10f, dirtState, new Color("F5C4FF"));
		registerInternal(Blocks.RED_FLOWER, 8, 0.10f, dirtState, new Color("E9E9E9"));

		registerInternal(Blocks.DOUBLE_PLANT, 0, 0.10f, dirtState, new Color("FFDD00"));
		registerInternal(Blocks.DOUBLE_PLANT, 1, 0.10f, dirtState, new Color("FCC7F0"));
		registerInternal(Blocks.DOUBLE_PLANT, 4, 0.10f, dirtState, new Color("FF1212"));
		registerInternal(Blocks.DOUBLE_PLANT, 5, 0.10f, dirtState, new Color("F3D2FC"));

		registerInternal(Blocks.BROWN_MUSHROOM, 0, 0.10f, dirtState, new Color("CFBFB6"));
		registerInternal(Blocks.RED_MUSHROOM, 0, 0.10f, dirtState, new Color("D6A8A5"));

		registerInternal(Items.PUMPKIN_PIE, 0, 0.16f, dirtState, new Color("E39A6D"));

		registerInternal(Items.PORKCHOP, 0, 0.2f, dirtState, new Color("FFA091"));
		registerInternal(Items.COOKED_PORKCHOP, 0, 0.2f, dirtState, new Color("FFFDBD"));

		registerInternal(Items.BEEF, 0, 0.2f, dirtState, new Color("FF4242"));
		registerInternal(Items.COOKED_BEEF, 0, 0.2f, dirtState, new Color("80543D"));

		registerInternal(Items.CHICKEN, 0, 0.2f, dirtState, new Color("FFE8E8"));
		registerInternal(Items.COOKED_CHICKEN, 0, 0.2f, dirtState, new Color("FA955F"));

		registerInternal(Items.FISH, 0, 0.15f, dirtState, new Color("6DCFB3"));
		registerInternal(Items.COOKED_FISH, 0, 0.15f, dirtState, new Color("D8EBE5"));

		registerInternal(Items.FISH, 1, 0.15f, dirtState, new Color("FF2E4A"));
		registerInternal(Items.COOKED_FISH, 1, 0.15f, dirtState, new Color("E87A3F"));

		registerInternal(Items.FISH, 2, 0.15f, dirtState, new Color("FF771C"));
		registerInternal(Items.FISH, 3, 0.15f, dirtState, new Color("DBFAFF"));

		//registerInternal(ENItems.Silkworm, 0, 0.04f, ColorRegistry.color("silkworm_raw"));
		//registerInternal(ENItems.SilkwormCooked, 0, 0.04f, ColorRegistry.color("silkworm_cooked"));

		registerInternal(Items.APPLE, 0, 0.10f, dirtState, new Color("FFF68F"));
		registerInternal(Items.MELON, 0, 0.04f, dirtState, new Color("FF443B"));
		registerInternal(Blocks.MELON_BLOCK, 0, 1.0f / 6, dirtState, new Color("FF443B"));
		registerInternal(Blocks.PUMPKIN, 0, 1.0f / 6, dirtState, new Color("FFDB66"));
		registerInternal(Blocks.LIT_PUMPKIN, 0, 1.0f / 6, dirtState, new Color("FFDB66"));

		registerInternal(Blocks.CACTUS, 0, 0.10f, dirtState, new Color("DEFFB5"));

		registerInternal(Items.CARROT, 0, 0.08f, dirtState, new Color("FF9B0F"));
		registerInternal(Items.POTATO, 0, 0.08f, dirtState, new Color("FFF1B5"));
		registerInternal(Items.BAKED_POTATO, 0, 0.08f, dirtState, new Color("FFF1B5"));
		registerInternal(Items.POISONOUS_POTATO, 0, 0.08f, dirtState, new Color("E0FF8A"));

		registerInternal(Blocks.WATERLILY, 0, 0.10f, dirtState, new Color("269900"));
		registerInternal(Blocks.VINE, 0, 0.10f, dirtState, new Color("23630E"));
		registerInternal(Blocks.TALLGRASS, 1, 0.08f, dirtState, new Color("23630E"));
		registerInternal(Items.EGG, 0, 0.08f, dirtState, new Color("FFFA66"));
		registerInternal(Items.NETHER_WART, 0, 0.10f, dirtState, new Color("FF2B52"));
		registerInternal(Items.REEDS, 0, 0.08f, dirtState, new Color("9BFF8A"));
		registerInternal(Items.STRING, 0, 0.04f, dirtState, Util.whiteColor);
	}
	
	public static void recommendAllFood(File file)
	{
	    if(FMLCommonHandler.instance().getSide().isServer())
	    {
	        return;
	    }
	    
	    IBlockState dirt = Blocks.DIRT.getDefaultState();
	    Color brown = new Color("7F3F0F");
	    
	    Map<String, Compostable> recommended = Maps.newHashMap();
	    
	    for(Item item : Item.REGISTRY)
	    {
	        if(item instanceof ItemFood)
	        {
	            ItemFood food = (ItemFood) item;
	            
	            List<ItemStack> stacks = Lists.newArrayList();
	            food.getSubItems(food, null, stacks);
	            
	            for(ItemStack foodStack : stacks)
	            {
	                ItemInfo foodItemInfo = new ItemInfo(foodStack);
	                
	                if(!containsItem(foodItemInfo))
	                {
    	                int hungerRestored = food.getHealAmount(foodStack);
    	                
    	                recommended.put(foodItemInfo.toString(), new Compostable(hungerRestored * 0.025F, brown, new ItemInfo(dirt)));
    	            }
	            }
	        }
	    }
	    
	    String json = gson.toJson(recommended, new TypeToken<Map<String, Compostable>>(){}.getType());
	    
	    try
        {
            Files.write(file.toPath(), json.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
	}
}
