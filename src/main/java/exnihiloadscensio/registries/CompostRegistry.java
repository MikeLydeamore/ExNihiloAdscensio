package exnihiloadscensio.registries;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import exnihiloadscensio.json.CustomItemInfoJson;
import exnihiloadscensio.registries.types.Compostable;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.util.ItemInfo;
import exnihiloadscensio.util.Util;

public class CompostRegistry {

	private static HashMap<ItemInfo, Compostable> registry = new HashMap<ItemInfo, Compostable>();

	private static Gson gson;

	public static void loadJson(File file)
	{
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
		Compostable compostable = new Compostable(value, color, new ItemInfo(state));

		registry.put(info, compostable);
	}

	public static void register(Block block, int meta, float value, IBlockState state, Color color)
	{
		register(Item.getItemFromBlock(block), meta, value, state, color);
	}

	public static boolean containsItem(Item item, int meta)
	{
		return containsItem(new ItemInfo(item, meta));
	}

	public static Compostable getItem(Item item, int meta)
	{
		return getItem(new ItemInfo(item, meta));
	}

	public static boolean containsItem(ItemStack stack)
	{
		return containsItem(new ItemInfo(stack));
	}

	public static Compostable getItem(ItemStack stack)
	{
		return getItem(new ItemInfo(stack));
	}

	public static boolean containsItem(ItemInfo info)
	{
		return registry.containsKey(info);
	}

	public static Compostable getItem(ItemInfo info)
	{
		return registry.get(info);
	}

	public static void registerDefaults()
	{
		IBlockState dirtState = Blocks.dirt.getDefaultState();
		
		register(Items.rotten_flesh, 0, 0.1f, dirtState, new Color("C45631"));

		register(Blocks.sapling, 0, 0.125f, dirtState, new Color("35A82A"));
		register(Blocks.sapling, 1, 0.125f, dirtState, new Color("2E8042"));
		register(Blocks.sapling, 2, 0.125f, dirtState, new Color("6CC449"));
		register(Blocks.sapling, 3, 0.125f, dirtState, new Color("22A116"));
		register(Blocks.sapling, 4, 0.125f, dirtState, new Color("B8C754"));
		register(Blocks.sapling, 5, 0.125f, dirtState, new Color("378030"));

		register(Blocks.leaves, 0, 0.125f, dirtState, new Color("35A82A"));
		register(Blocks.leaves, 1, 0.125f, dirtState, new Color("2E8042"));
		register(Blocks.leaves, 2, 0.125f, dirtState, new Color("6CC449"));
		register(Blocks.leaves, 3, 0.125f, dirtState, new Color("22A116"));
		register(Blocks.leaves2, 0, 0.125f, dirtState, new Color("B8C754"));
		register(Blocks.leaves2, 1, 0.125f, dirtState, new Color("378030"));

		register(Items.spider_eye, 0, 0.08f, dirtState, new Color("963E44"));

		register(Items.wheat, 0, 0.08f, dirtState, new Color("E3E162"));	
		register(Items.wheat_seeds, 0, 0.08f, dirtState, new Color("35A82A"));
		register(Items.bread, 0, 0.16f, dirtState, new Color("D1AF60"));

		register(Blocks.yellow_flower, 0, 0.10f, dirtState, new Color("FFF461"));
		register(Blocks.red_flower, 0, 0.10f, dirtState, new Color("FF1212"));
		register(Blocks.red_flower, 1, 0.10f, dirtState, new Color("33CFFF"));
		register(Blocks.red_flower, 2, 0.10f, dirtState, new Color("F59DFA"));
		register(Blocks.red_flower, 3, 0.10f, dirtState, new Color("E3E3E3"));
		register(Blocks.red_flower, 4, 0.10f, dirtState, new Color("FF3D12"));
		register(Blocks.red_flower, 5, 0.10f, dirtState, new Color("FF7E29"));
		register(Blocks.red_flower, 6, 0.10f, dirtState, new Color("FFFFFF"));
		register(Blocks.red_flower, 7, 0.10f, dirtState, new Color("F5C4FF"));
		register(Blocks.red_flower, 8, 0.10f, dirtState, new Color("E9E9E9"));

		register(Blocks.double_plant, 0, 0.10f, dirtState, new Color("FFDD00"));
		register(Blocks.double_plant, 1, 0.10f, dirtState, new Color("FCC7F0"));
		register(Blocks.double_plant, 4, 0.10f, dirtState, new Color("FF1212"));
		register(Blocks.double_plant, 5, 0.10f, dirtState, new Color("F3D2FC"));

		register(Blocks.brown_mushroom, 0, 0.10f, dirtState, new Color("CFBFB6"));
		register(Blocks.red_mushroom, 0, 0.10f, dirtState, new Color("D6A8A5"));

		register(Items.pumpkin_pie, 0, 0.16f, dirtState, new Color("E39A6D"));

		register(Items.porkchop, 0, 0.2f, dirtState, new Color("FFA091"));
		register(Items.cooked_porkchop, 0, 0.2f, dirtState, new Color("FFFDBD"));

		register(Items.beef, 0, 0.2f, dirtState, new Color("FF4242"));
		register(Items.cooked_beef, 0, 0.2f, dirtState, new Color("80543D"));

		register(Items.chicken, 0, 0.2f, dirtState, new Color("FFE8E8"));
		register(Items.cooked_chicken, 0, 0.2f, dirtState, new Color("FA955F"));

		register(Items.fish, 0, 0.15f, dirtState, new Color("6DCFB3"));
		register(Items.cooked_fish, 0, 0.15f, dirtState, new Color("D8EBE5"));

		register(Items.fish, 1, 0.15f, dirtState, new Color("FF2E4A"));
		register(Items.cooked_fish, 1, 0.15f, dirtState, new Color("E87A3F"));

		register(Items.fish, 2, 0.15f, dirtState, new Color("FF771C"));
		register(Items.fish, 3, 0.15f, dirtState, new Color("DBFAFF"));

		//register(ENItems.Silkworm, 0, 0.04f, ColorRegistry.color("silkworm_raw"));
		//register(ENItems.SilkwormCooked, 0, 0.04f, ColorRegistry.color("silkworm_cooked"));

		register(Items.apple, 0, 0.10f, dirtState, new Color("FFF68F"));
		register(Items.melon, 0, 0.04f, dirtState, new Color("FF443B"));
		register(Blocks.melon_block, 0, 1.0f / 6, dirtState, new Color("FF443B"));
		register(Blocks.pumpkin, 0, 1.0f / 6, dirtState, new Color("FFDB66"));
		register(Blocks.lit_pumpkin, 0, 1.0f / 6, dirtState, new Color("FFDB66"));

		register(Blocks.cactus, 0, 0.10f, dirtState, new Color("DEFFB5"));

		register(Items.carrot, 0, 0.08f, dirtState, new Color("FF9B0F"));
		register(Items.potato, 0, 0.08f, dirtState, new Color("FFF1B5"));
		register(Items.baked_potato, 0, 0.08f, dirtState, new Color("FFF1B5"));
		register(Items.poisonous_potato, 0, 0.08f, dirtState, new Color("E0FF8A"));

		register(Blocks.waterlily, 0, 0.10f, dirtState, new Color("269900"));
		register(Blocks.vine, 0, 0.10f, dirtState, new Color("23630E"));
		register(Blocks.tallgrass, 1, 0.08f, dirtState, new Color("23630E"));
		register(Items.egg, 0, 0.08f, dirtState, new Color("FFFA66"));
		register(Items.nether_wart, 0, 0.10f, dirtState, new Color("FF2B52"));
		register(Items.reeds, 0, 0.08f, dirtState, new Color("9BFF8A"));
		register(Items.string, 0, 0.04f, dirtState, Util.whiteColor);

	}

}
