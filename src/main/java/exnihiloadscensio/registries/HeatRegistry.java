package exnihiloadscensio.registries;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import exnihiloadscensio.json.CustomBlockInfoJson;
import exnihiloadscensio.util.BlockInfo;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class HeatRegistry {
	
	private static Map<BlockInfo, Integer> registry = new HashMap<>();
	private static Map<BlockInfo, Integer> externalRegistry = new HashMap<>();
    
    private static Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(BlockInfo.class, new CustomBlockInfoJson()).create();
    
    public static void register(BlockInfo info, int ticksBetween)
    {
        registerInternal(info, ticksBetween);
        externalRegistry.put(info, ticksBetween);
    }
    
    public static void register(ItemStack stack, int ticksBetween)
    {
        register(new BlockInfo(stack), ticksBetween);
    }

    public static void registerInternal(BlockInfo info, int ticksBetween)
    {
        registry.put(info, ticksBetween);
    }
    
    public static void registerInternal(ItemStack stack, int ticksBetween)
    {
        register(new BlockInfo(stack), ticksBetween);
    }
    
    public static void registerDefaults()
    {
        for (int i = 0; i < 16; i++)
            registerInternal(new ItemStack(Blocks.TORCH, 1, i), 1);
        for (int i = 0; i < 16; i++)
            registerInternal(new BlockInfo(Blocks.LAVA, i), 3);
        for (int i = 0; i < 16; i++)
            registerInternal(new BlockInfo(Blocks.FLOWING_LAVA, i), 2);
        for (int i = 0; i < 16; i++)
            registerInternal(new BlockInfo(Blocks.FIRE, i), 4);
    }
    
    public static int getHeatAmount(ItemStack stack)
    {
        return registry.get(new BlockInfo(stack));
    }
    
    public static int getHeatAmount(BlockInfo info)
    {
        if (registry.containsKey(info))
            return registry.get(info);
        
        return 0;
    }
    
    public static void loadJson(File file)
    {
        registry.clear();
        
		
		if (file.exists())
		{
			try 
			{
				FileReader fr = new FileReader(file);
				HashMap<String, Integer> gsonInput = gson.fromJson(fr, new TypeToken<HashMap<String, Integer>>(){}.getType());
				
                for(Map.Entry<String, Integer> entry : gsonInput.entrySet())
                {
                    registry.put(new BlockInfo(entry.getKey()), entry.getValue());
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
	
	public static void saveJson(File file) {
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
