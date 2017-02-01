package exnihiloadscensio.registries;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import exnihiloadscensio.json.CustomItemInfoJson;
import exnihiloadscensio.registries.manager.ICompostDefaultRegistryProvider;
import exnihiloadscensio.registries.manager.RegistryManager;
import exnihiloadscensio.registries.types.Compostable;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.util.ItemInfo;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class CompostRegistry {

    @Getter
	private static Map<ItemInfo, Compostable> registry = new HashMap<>();
    
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
        register(new ItemInfo(item, meta), new Compostable(value, color, new ItemInfo(state)));
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
		for (ICompostDefaultRegistryProvider provider : RegistryManager.getDefaultCompostRecipeHandlers()) {
			provider.registerCompostRecipeDefaults();
		}
	}

	@SideOnly(Side.CLIENT)
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
	            
	            NonNullList<ItemStack> stacks = NonNullList.create();
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
