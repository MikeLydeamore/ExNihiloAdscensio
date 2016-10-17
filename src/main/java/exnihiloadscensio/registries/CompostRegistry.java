package exnihiloadscensio.registries;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
import net.minecraft.item.ItemStack;

public class CompostRegistry {

	private static HashMap<ItemInfo, Compostable> registry = new HashMap<ItemInfo, Compostable>();

	private static Gson gson;

	public static void loadJson(File file)
	{
	    registry.clear();
	    
		gson = new GsonBuilder().setPrettyPrinting()
				.registerTypeAdapter(ItemInfo.class, new CustomItemInfoJson()).create();
		if (file.exists())
		{
			try 
			{
				FileReader fr = new FileReader(file);
				HashMap<String, Compostable> gsonInput = gson.fromJson(fr, new TypeToken<HashMap<String, Compostable>>(){}.getType());
				
				Iterator<String> it = gsonInput.keySet().iterator();
				
				while (it.hasNext())
				{
					String s = (String) it.next();
					ItemInfo stack = new ItemInfo(s);
					register(stack, gsonInput.get(s));
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
		registry.put(item, compostable);
	}

	public static void register(Item item, int meta, float value, IBlockState state, Color color)
	{
		ItemInfo info = new ItemInfo(item, meta);
		Compostable compostable = new Compostable(value, color, new ItemInfo(state), false);

		registry.put(info, compostable);
	}

	public static void register(Block block, int meta, float value, IBlockState state, Color color)
	{
		register(Item.getItemFromBlock(block), meta, value, state, color);
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
		
		register(Items.ROTTEN_FLESH, 0, 0.1f, dirtState, new Color("C45631"));

		register(Blocks.SAPLING, 0, 0.125f, dirtState, new Color("35A82A"));
		register(Blocks.SAPLING, 1, 0.125f, dirtState, new Color("2E8042"));
		register(Blocks.SAPLING, 2, 0.125f, dirtState, new Color("6CC449"));
		register(Blocks.SAPLING, 3, 0.125f, dirtState, new Color("22A116"));
		register(Blocks.SAPLING, 4, 0.125f, dirtState, new Color("B8C754"));
		register(Blocks.SAPLING, 5, 0.125f, dirtState, new Color("378030"));

		register(Blocks.LEAVES, 0, 0.125f, dirtState, new Color("35A82A"));
		register(Blocks.LEAVES, 1, 0.125f, dirtState, new Color("2E8042"));
		register(Blocks.LEAVES, 2, 0.125f, dirtState, new Color("6CC449"));
		register(Blocks.LEAVES, 3, 0.125f, dirtState, new Color("22A116"));
		register(Blocks.LEAVES2, 0, 0.125f, dirtState, new Color("B8C754"));
		register(Blocks.LEAVES2, 1, 0.125f, dirtState, new Color("378030"));

		register(Items.SPIDER_EYE, 0, 0.08f, dirtState, new Color("963E44"));

		register(Items.WHEAT, 0, 0.08f, dirtState, new Color("E3E162"));	
		register(Items.WHEAT_SEEDS, 0, 0.08f, dirtState, new Color("35A82A"));
		register(Items.BREAD, 0, 0.16f, dirtState, new Color("D1AF60"));

		register(Blocks.YELLOW_FLOWER, 0, 0.10f, dirtState, new Color("FFF461"));
		register(Blocks.RED_FLOWER, 0, 0.10f, dirtState, new Color("FF1212"));
		register(Blocks.RED_FLOWER, 1, 0.10f, dirtState, new Color("33CFFF"));
		register(Blocks.RED_FLOWER, 2, 0.10f, dirtState, new Color("F59DFA"));
		register(Blocks.RED_FLOWER, 3, 0.10f, dirtState, new Color("E3E3E3"));
		register(Blocks.RED_FLOWER, 4, 0.10f, dirtState, new Color("FF3D12"));
		register(Blocks.RED_FLOWER, 5, 0.10f, dirtState, new Color("FF7E29"));
		register(Blocks.RED_FLOWER, 6, 0.10f, dirtState, new Color("FFFFFF"));
		register(Blocks.RED_FLOWER, 7, 0.10f, dirtState, new Color("F5C4FF"));
		register(Blocks.RED_FLOWER, 8, 0.10f, dirtState, new Color("E9E9E9"));

		register(Blocks.DOUBLE_PLANT, 0, 0.10f, dirtState, new Color("FFDD00"));
		register(Blocks.DOUBLE_PLANT, 1, 0.10f, dirtState, new Color("FCC7F0"));
		register(Blocks.DOUBLE_PLANT, 4, 0.10f, dirtState, new Color("FF1212"));
		register(Blocks.DOUBLE_PLANT, 5, 0.10f, dirtState, new Color("F3D2FC"));

		register(Blocks.BROWN_MUSHROOM, 0, 0.10f, dirtState, new Color("CFBFB6"));
		register(Blocks.RED_MUSHROOM, 0, 0.10f, dirtState, new Color("D6A8A5"));

		register(Items.PUMPKIN_PIE, 0, 0.16f, dirtState, new Color("E39A6D"));

		register(Items.PORKCHOP, 0, 0.2f, dirtState, new Color("FFA091"));
		register(Items.COOKED_PORKCHOP, 0, 0.2f, dirtState, new Color("FFFDBD"));

		register(Items.BEEF, 0, 0.2f, dirtState, new Color("FF4242"));
		register(Items.COOKED_BEEF, 0, 0.2f, dirtState, new Color("80543D"));

		register(Items.CHICKEN, 0, 0.2f, dirtState, new Color("FFE8E8"));
		register(Items.COOKED_CHICKEN, 0, 0.2f, dirtState, new Color("FA955F"));

		register(Items.FISH, 0, 0.15f, dirtState, new Color("6DCFB3"));
		register(Items.COOKED_FISH, 0, 0.15f, dirtState, new Color("D8EBE5"));

		register(Items.FISH, 1, 0.15f, dirtState, new Color("FF2E4A"));
		register(Items.COOKED_FISH, 1, 0.15f, dirtState, new Color("E87A3F"));

		register(Items.FISH, 2, 0.15f, dirtState, new Color("FF771C"));
		register(Items.FISH, 3, 0.15f, dirtState, new Color("DBFAFF"));

		//register(ENItems.Silkworm, 0, 0.04f, ColorRegistry.color("silkworm_raw"));
		//register(ENItems.SilkwormCooked, 0, 0.04f, ColorRegistry.color("silkworm_cooked"));

		register(Items.APPLE, 0, 0.10f, dirtState, new Color("FFF68F"));
		register(Items.MELON, 0, 0.04f, dirtState, new Color("FF443B"));
		register(Blocks.MELON_BLOCK, 0, 1.0f / 6, dirtState, new Color("FF443B"));
		register(Blocks.PUMPKIN, 0, 1.0f / 6, dirtState, new Color("FFDB66"));
		register(Blocks.LIT_PUMPKIN, 0, 1.0f / 6, dirtState, new Color("FFDB66"));

		register(Blocks.CACTUS, 0, 0.10f, dirtState, new Color("DEFFB5"));

		register(Items.CARROT, 0, 0.08f, dirtState, new Color("FF9B0F"));
		register(Items.POTATO, 0, 0.08f, dirtState, new Color("FFF1B5"));
		register(Items.BAKED_POTATO, 0, 0.08f, dirtState, new Color("FFF1B5"));
		register(Items.POISONOUS_POTATO, 0, 0.08f, dirtState, new Color("E0FF8A"));

		register(Blocks.WATERLILY, 0, 0.10f, dirtState, new Color("269900"));
		register(Blocks.VINE, 0, 0.10f, dirtState, new Color("23630E"));
		register(Blocks.TALLGRASS, 1, 0.08f, dirtState, new Color("23630E"));
		register(Items.EGG, 0, 0.08f, dirtState, new Color("FFFA66"));
		register(Items.NETHER_WART, 0, 0.10f, dirtState, new Color("FF2B52"));
		register(Items.REEDS, 0, 0.08f, dirtState, new Color("9BFF8A"));
		register(Items.STRING, 0, 0.04f, dirtState, Util.whiteColor);

	}

}
