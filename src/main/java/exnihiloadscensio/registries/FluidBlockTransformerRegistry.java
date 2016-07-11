package exnihiloadscensio.registries;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.json.CustomItemInfoJson;
import exnihiloadscensio.registries.types.FluidBlockTransformer;
import exnihiloadscensio.util.ItemInfo;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidBlockTransformerRegistry {
	
	private static ArrayList<FluidBlockTransformer> registry = new ArrayList<FluidBlockTransformer>();
	
	public static void register(Fluid fluid, ItemInfo inputBlock, ItemInfo outputBlock) {
		registry.add(new FluidBlockTransformer(fluid.getName(), inputBlock, outputBlock));
	}
	
	public static boolean canBlockBeTransformedWithThisFluid(Fluid fluid, ItemStack stack) {
		ItemInfo info = ItemInfo.getItemInfoFromStack(stack);
		for (FluidBlockTransformer transformer : registry) {
			if (fluid.getName().equals(transformer.getFluidName()) && info.equals(transformer.getInput()))
				return true;
		}
		return false;
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
				ArrayList<FluidBlockTransformer> gsonInput = gson.fromJson(fr, new TypeToken<ArrayList<FluidBlockTransformer>>(){}.getType());
				
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
		register(FluidRegistry.WATER, new ItemInfo(new ItemStack(ENBlocks.dust)), new ItemInfo(new ItemStack(Blocks.CLAY)));
	}

}
