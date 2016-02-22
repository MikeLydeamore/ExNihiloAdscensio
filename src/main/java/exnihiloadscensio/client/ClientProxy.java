package exnihiloadscensio.client;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import exnihiloadscensio.CommonProxy;
import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.client.renderers.RenderBarrel;
import exnihiloadscensio.items.ENItems;
import exnihiloadscensio.tiles.TileBarrel;

public class ClientProxy extends CommonProxy {
	
	public void initModels()
	{
		ENItems.initModels();
		ENBlocks.initModels();
	}
	
	@Override
	public boolean runningOnServer()
	{
		return false;
	}
	
	@Override
	public void registerRenderers()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileBarrel.class, new RenderBarrel());
	}
	

}
