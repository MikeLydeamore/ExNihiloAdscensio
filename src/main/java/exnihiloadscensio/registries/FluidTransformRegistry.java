package exnihiloadscensio.registries;

import java.util.HashMap;
import java.util.HashSet;

import exnihiloadscensio.registries.types.FluidTransformer;
import exnihiloadscensio.util.BlockInfo;

public class FluidTransformRegistry {
	
	private static HashSet<FluidTransformer> registry = new HashSet<FluidTransformer>();
	
	private static HashMap<String, FluidTransformer> registryInternal = new HashMap<String, FluidTransformer>();
	
	public static void register(String inputFluid, String outputFluid, BlockInfo[] transformingBlocks, BlockInfo[] blocksToSpawn) {
		FluidTransformer transformer = new FluidTransformer(inputFluid, outputFluid, transformingBlocks, blocksToSpawn);
		registry.add(transformer);
		registryInternal.put(inputFluid, transformer);
	}
	
	public static boolean containsKey(String inputFluid) {
		return registryInternal.containsKey(inputFluid);
	}
	
	public static FluidTransformer getFluidTransformer(String inputFluid) {
		return registryInternal.get(inputFluid);
	}

}
