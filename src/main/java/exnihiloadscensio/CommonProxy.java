package exnihiloadscensio;

import exnihiloadscensio.items.ENItems;
import exnihiloadscensio.items.ore.ItemFMLRegistryWrapper;
import exnihiloadscensio.items.ore.Ore;
import exnihiloadscensio.registries.OreRegistry;

public class CommonProxy {

	private ItemFMLRegistryWrapper<Ore> oreRegistryWrapper;

	public void initModels() {}

	public boolean runningOnServer()
	{
		return true;
	}

	public void registerRenderers()
	{

	}

	public void registerColorHandlers() {}

	public final ItemFMLRegistryWrapper<Ore> getFoodRegistryWrapper() {
		if (oreRegistryWrapper == null)
			oreRegistryWrapper = new ItemFMLRegistryWrapper<Ore>(OreRegistry.ORES, ENItems.ores).setDefaultPrefix(ExNihiloAdscensio.MODID);
		return oreRegistryWrapper;
	}

}
