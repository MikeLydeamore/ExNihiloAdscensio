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
import exnihiloadscensio.json.CustomItemInfoJson;
import exnihiloadscensio.registries.types.Meltable;
import exnihiloadscensio.util.BlockInfo;
import exnihiloadscensio.util.ItemInfo;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class CrucibleRegistry
{
    private static Map<ItemInfo, Meltable> registry = new HashMap<>();
    private static Map<ItemInfo, Meltable> externalRegistry = new HashMap<>();
	
	private static Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(ItemInfo.class, new CustomItemInfoJson())
			.registerTypeAdapter(BlockInfo.class,  new CustomBlockInfoJson()).create();
    
    public static void register(ItemInfo item, Fluid fluid, int amount)
    {
        register(item, new Meltable(fluid.getName(), amount));
    }
    
    public static void register(ItemStack stack, Fluid fluid, int amount)
    {
        register(new ItemInfo(stack), new Meltable(fluid.getName(), amount));
    }
    
    public static void register(ItemInfo item, Meltable meltable)
    {
        registerInternal(item, meltable);
        
        externalRegistry.put(item, meltable);
    }
    
    private static void registerInternal(ItemStack stack, Fluid fluid, int amount)
    {
        registerInternal(new ItemInfo(stack), new Meltable(fluid.getName(), amount));
    }
    
    private static void registerInternal(ItemInfo item, Meltable meltable)
    {
        registry.put(item, meltable);
    }
    
    public static boolean canBeMelted(ItemStack stack)
    {
        return canBeMelted(new ItemInfo(stack));
    }
    
    public static boolean canBeMelted(ItemInfo info)
    {
        return registry.containsKey(info) && FluidRegistry.isFluidRegistered(registry.get(info).getFluid());
    }
    
    public static Meltable getMeltable(ItemStack stack)
    {
        ItemInfo info = new ItemInfo(stack);
        
        return registry.get(info);
    }
    
    public static Meltable getMeltable(ItemInfo info)
    {
        return registry.get(info);
    }
    
    public static void registerDefaults()
    {
        registerInternal(new ItemStack(Blocks.COBBLESTONE), FluidRegistry.LAVA, 250);
    }
    
	public static void loadJson(File file)
	{
	    registry.clear();
	    
		if (file.exists())
		{
			try 
			{
				FileReader fr = new FileReader(file);
				Map<String, Meltable> gsonInput = gson.fromJson(fr, new TypeToken<Map<String, Meltable>>(){}.getType());

                for(Map.Entry<String, Meltable> entry : gsonInput.entrySet())
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

}
