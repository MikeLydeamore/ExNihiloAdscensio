package exnihiloadscensio.registries;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import exnihiloadscensio.json.CustomItemInfoJson;
import exnihiloadscensio.registries.types.FluidFluidBlock;
import exnihiloadscensio.util.ItemInfo;
import net.minecraft.init.Blocks;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidOnTopRegistry {

	private static ArrayList<FluidFluidBlock> registry = new ArrayList<FluidFluidBlock>();

	public static void register(Fluid fluidInBarrel, Fluid fluidOnTop, ItemInfo result) {
		FluidFluidBlock reg = new FluidFluidBlock(fluidInBarrel.getName(), fluidOnTop.getName(), result);
		registry.add(reg);
	}
	
	public static boolean isValidRecipe(Fluid fluidInBarrel, Fluid fluidOnTop) {
		if (fluidInBarrel == null || fluidOnTop == null)
			return false;
		for (FluidFluidBlock fBlock : registry) {
			if (fBlock.getFluidInBarrel().equals(fluidInBarrel.getName()) &&
					fBlock.getFluidOnTop().equals(fluidOnTop.getName()))
				return true;
		}
		
		return false;
	}
	
	public static ItemInfo getTransformedBlock(Fluid fluidInBarrel, Fluid fluidOnTop) {
		for (FluidFluidBlock fBlock : registry) {
			if (fBlock.getFluidInBarrel().equals(fluidInBarrel.getName()) &&
					fBlock.getFluidOnTop().equals(fluidOnTop.getName()))
				return fBlock.getResult();
		}
		
		return null;
	}
	
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
				ArrayList<FluidFluidBlock> gsonInput = gson.fromJson(fr, new TypeToken<ArrayList<FluidFluidBlock>>(){}.getType());
				
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
	
	public static void registerDefaults() {
		register(FluidRegistry.LAVA, FluidRegistry.WATER, new ItemInfo(Blocks.OBSIDIAN.getDefaultState()));
		register(FluidRegistry.WATER, FluidRegistry.LAVA, new ItemInfo(Blocks.COBBLESTONE.getDefaultState()));
	}
	
}
