package exnihiloadscensio.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import exnihiloadscensio.CommonProxy;
import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.client.renderers.RenderBarrel;
import exnihiloadscensio.client.renderers.RenderCrucible;
import exnihiloadscensio.client.renderers.RenderInfestedLeaves;
import exnihiloadscensio.client.renderers.RenderOrePiece;
import exnihiloadscensio.client.renderers.RenderSieve;
import exnihiloadscensio.items.ENItems;
import exnihiloadscensio.items.ore.ItemOre;
import exnihiloadscensio.registries.OreRegistry;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.tiles.TileCrucible;
import exnihiloadscensio.tiles.TileSieve;

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
		ClientRegistry.bindTileEntitySpecialRenderer(TileSieve.class, new RenderSieve());
	}

	@Override
	public void registerColorHandlers()
	{
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new RenderInfestedLeaves(), ENBlocks.infestedLeaves);
		int size = OreRegistry.getItemOreRegistry().size();
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new RenderOrePiece(), OreRegistry.getItemOreRegistry().toArray(new ItemOre[size]));
	}


}
