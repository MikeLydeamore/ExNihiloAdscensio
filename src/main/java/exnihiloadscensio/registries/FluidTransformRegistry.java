package exnihiloadscensio.registries;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import exnihiloadscensio.json.CustomBlockInfoJson;
import exnihiloadscensio.registries.types.FluidTransformer;
import exnihiloadscensio.util.BlockInfo;
import net.minecraft.init.Blocks;

public class FluidTransformRegistry {
	
	private static ArrayList<FluidTransformer> registry = new ArrayList<FluidTransformer>();
	
	private static HashMap<String, FluidTransformer> registryInternal = new HashMap<String, FluidTransformer>();
	
	public static void register(String inputFluid, String outputFluid, int duration, BlockInfo[] transformingBlocks, BlockInfo[] blocksToSpawn) {
		FluidTransformer transformer = new FluidTransformer(inputFluid, outputFluid, duration, transformingBlocks, blocksToSpawn);
		registry.add(transformer);
		registryInternal.put(inputFluid, transformer);
	}
	
	public static boolean containsKey(String inputFluid) {
		return registryInternal.containsKey(inputFluid);
	}
	
	public static FluidTransformer getFluidTransformer(String inputFluid) {
		return registryInternal.get(inputFluid);
	}
	
	public static void registerDefaults() {
		register("water","witchwater", 12000, new BlockInfo[] {new BlockInfo(Blocks.MYCELIUM.getDefaultState())}, 
				new BlockInfo[] {new BlockInfo(Blocks.BROWN_MUSHROOM.getDefaultState()), new BlockInfo(Blocks.RED_MUSHROOM.getDefaultState())});
	}
	
	private static Gson gson;

	public static void loadJson(File file)
	{
		gson = new GsonBuilder().setPrettyPrinting()
				.registerTypeAdapter(BlockInfo.class, new CustomBlockInfoJson()).create();
		if (file.exists())
		{
			try 
			{
				FileReader fr = new FileReader(file);
				ArrayList<FluidTransformer> gsonInput = gson.fromJson(fr, new TypeToken<ArrayList<FluidTransformer>>(){}.getType());
				
				registry = gsonInput;
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

}
