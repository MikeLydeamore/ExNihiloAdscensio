package exnihiloadscensio.registries.manager;

import lombok.Getter;

import java.util.ArrayList;

public class RegistryManager {
	
	@Getter
	private static ArrayList<ISieveDefaultRegistryProvider> defaultSieveRecipeHandlers = new ArrayList<ISieveDefaultRegistryProvider>();
	@Getter
	private static ArrayList<IHammerDefaultRegistryProvider> defaultHammerRecipeHandlers = new ArrayList<IHammerDefaultRegistryProvider>();
	@Getter
	private static ArrayList<ICompostDefaultRegistryProvider> defaultCompostRecipeHandlers = new ArrayList<ICompostDefaultRegistryProvider>();
	@Getter
	private static ArrayList<ICrookDefaultRegistryProvider> defaultCrookRecipeHandlers = new ArrayList<ICrookDefaultRegistryProvider>();
	@Getter
	private static ArrayList<ICrucibleDefaultRegistryProvider> defaultCrucibleRecipeHandlers = new ArrayList<ICrucibleDefaultRegistryProvider>();
	@Getter
	private static ArrayList<IFluidBlockDefaultRegistryProvider> defaultFluidBlockRecipeHandlers = new ArrayList<IFluidBlockDefaultRegistryProvider>();
	@Getter
	private static ArrayList<IFluidTransformDefaultRegistryProvider> defaultFluidTransformRecipeHandlers = new ArrayList<IFluidTransformDefaultRegistryProvider>();
	@Getter
	private static ArrayList<IFluidOnTopDefaultRegistryProvider> defaultFluidOnTopRecipeHandlers = new ArrayList<IFluidOnTopDefaultRegistryProvider>();
	@Getter
	private static ArrayList<IHeatDefaultRegistryProvider> defaultHeatRecipeHandlers = new ArrayList<IHeatDefaultRegistryProvider>();
	@Getter
	private static ArrayList<IOreDefaultRegistryProvider> defaultOreRecipeHandlers = new ArrayList<IOreDefaultRegistryProvider>();
		
	
	
	public static void registerSieveDefaultRecipeHandler(ISieveDefaultRegistryProvider provider) {
		defaultSieveRecipeHandlers.add(provider);
	}
	
	public static void registerHammerDefaultRecipeHandler(IHammerDefaultRegistryProvider provider) {
		defaultHammerRecipeHandlers.add(provider);
	}
	
	public static void registerCompostDefaultRecipeHandler(ICompostDefaultRegistryProvider provider) {
		defaultCompostRecipeHandlers.add(provider);
	}
	
	public static void registerCrookDefaultRecipeHandler(ICrookDefaultRegistryProvider provider) {
		defaultCrookRecipeHandlers.add(provider);
	}
	
	public static void registerCrucibleDefaultRecipeHandler(ICrucibleDefaultRegistryProvider provider) {
		defaultCrucibleRecipeHandlers.add(provider);
	}
	
	public static void registerFluidBlockDefaultRecipeHandler(IFluidBlockDefaultRegistryProvider provider) {
		defaultFluidBlockRecipeHandlers.add(provider);
	}
	
	public static void registerFluidTransformDefaultRecipeHandler(IFluidTransformDefaultRegistryProvider provider) {
		defaultFluidTransformRecipeHandlers.add(provider);
	}

	public static void registerFluidOnTopDefaultRecipeHandler(IFluidOnTopDefaultRegistryProvider provider) {
		defaultFluidOnTopRecipeHandlers.add(provider);
	}
	
	public static void registerHeatDefaultRecipeHandler(IHeatDefaultRegistryProvider provider) {
		defaultHeatRecipeHandlers.add(provider);
	}
	
	public static void registerOreDefaultRecipeHandler(IOreDefaultRegistryProvider provider) {
		defaultOreRecipeHandlers.add(provider);
	}
}
