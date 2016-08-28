package exnihiloadscensio.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import exnihiloadscensio.CommonProxy;
import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.client.renderers.RenderBarrel;
import exnihiloadscensio.client.renderers.RenderCrucible;
import exnihiloadscensio.client.renderers.RenderInfestedLeaves;
import exnihiloadscensio.client.renderers.RenderOrePiece;
import exnihiloadscensio.items.ENItems;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.tiles.TileCrucible;

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
		ClientRegistry.bindTileEntitySpecialRenderer(TileCrucible.class, new RenderCrucible());
		//ClientRegistry.bindTileEntitySpecialRenderer(TileSieve.class, new RenderSieve());
	}

	@Override
	public void registerColorHandlers()
	{
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new RenderInfestedLeaves(), ENBlocks.infestedLeaves);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new RenderOrePiece(), ENItems.ores);
	}


}
