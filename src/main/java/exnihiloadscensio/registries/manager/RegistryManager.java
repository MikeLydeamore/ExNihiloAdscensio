package exnihiloadscensio.registries.manager;

import java.util.ArrayList;

import lombok.Getter;

public class RegistryManager {
	
	@Getter
	private static ArrayList<ISieveDefaultRegistryProvider> defaultSieveRecipeHandlers = new ArrayList<ISieveDefaultRegistryProvider>();
	@Getter
	private static ArrayList<IHammerDefaultRegistryProvider> defaultHammerRecipeHandlers = new ArrayList<IHammerDefaultRegistryProvider>();
	@Getter
	private static ArrayList<ICompostDefaultRegistryProvider> defaultCompostRecipeHandlers = new ArrayList<ICompostDefaultRegistryProvider>();
		
	
	
	public static void registerSieveDefaultRecipeHandler(ISieveDefaultRegistryProvider provider) {
		defaultSieveRecipeHandlers.add(provider);
	}
	
	public static void registerHammerDefaultRecipeHandler(IHammerDefaultRegistryProvider provider) {
		defaultHammerRecipeHandlers.add(provider);
	}
	
	public static void registerCompostDefaultRecipeHandler(ICompostDefaultRegistryProvider provider) {
		defaultCompostRecipeHandlers.add(provider);
	}

}
