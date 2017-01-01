package exnihiloadscensio.registries;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.json.CustomItemInfoJson;
import exnihiloadscensio.registries.types.FluidBlockTransformer;
import exnihiloadscensio.util.ItemInfo;
import lombok.Getter;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidBlockTransformerRegistry
{
	@Getter
	private static ArrayList<FluidBlockTransformer> registry = new ArrayList<>();
	
	private static List<FluidBlockTransformer> externalRegistry = new ArrayList<>();
	
	public static void register(Fluid fluid, ItemInfo inputBlock, ItemInfo outputBlock)
	{
	    registerInternal(fluid, inputBlock, outputBlock);
	    
	    externalRegistry.add(new FluidBlockTransformer(fluid.getName(), inputBlock, outputBlock));
	}
	
	private static void registerInternal(Fluid fluid, ItemInfo inputBlock, ItemInfo outputBlock)
	{
	    registry.add(new FluidBlockTransformer(fluid.getName(), inputBlock, outputBlock));
	}
	
    private static void registerInternal(String fluid, ItemInfo inputBlock, ItemInfo outputBlock)
    {
        registry.add(new FluidBlockTransformer(fluid, inputBlock, outputBlock));
    }
	
	public static boolean canBlockBeTransformedWithThisFluid(Fluid fluid, ItemStack stack)
	{
		ItemInfo info = ItemInfo.getItemInfoFromStack(stack);
		
		for (FluidBlockTransformer transformer : registry)
		{
			if (fluid.getName().equals(transformer.getFluidName()) && info.equals(transformer.getInput()))
				return true;
		}
		return false;
	}
	
	public static ItemInfo getBlockForTransformation(Fluid fluid, ItemStack stack)
	{
		ItemInfo info = ItemInfo.getItemInfoFromStack(stack);
		
		for (FluidBlockTransformer transformer : registry)
		{
			if (fluid.getName().equals(transformer.getFluidName()) && info.equals(transformer.getInput()))
			{
				return transformer.getOutput();
			}
		}
		
		return null;
	}
    
    private static Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(ItemInfo.class, new CustomItemInfoJson()).create();
    
	public static void loadJson(File file)
	{
	    registry.clear();
	    
		if (file.exists())
		{
			try 
			{
				FileReader fr = new FileReader(file);
				List<FluidBlockTransformer> gsonInput = gson.fromJson(fr, new TypeToken<List<FluidBlockTransformer>>(){}.getType());
				
				registry.addAll(gsonInput);
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
		
        registry.addAll(externalRegistry);
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
	
	public static void registerDefaults()
	{
		registerInternal(FluidRegistry.WATER, new ItemInfo(new ItemStack(ENBlocks.dust)), new ItemInfo(new ItemStack(Blocks.CLAY)));
		registerInternal(FluidRegistry.LAVA, new ItemInfo(new ItemStack(Items.REDSTONE)), new ItemInfo(new ItemStack(Blocks.NETHERRACK)));
		registerInternal(FluidRegistry.LAVA, new ItemInfo(new ItemStack(Items.GLOWSTONE_DUST)), new ItemInfo(new ItemStack(Blocks.END_STONE)));
		registerInternal("witchwater", new ItemInfo(new ItemStack(Blocks.SAND)), new ItemInfo(new ItemStack(Blocks.SOUL_SAND)));
	}
}
