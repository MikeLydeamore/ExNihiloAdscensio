package exnihiloadscensio.client;

import exnihiloadscensio.CommonProxy;
import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.client.renderers.*;
import exnihiloadscensio.entities.ProjectileStone;
import exnihiloadscensio.items.ENItems;
import exnihiloadscensio.items.ore.ItemOre;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.CompostRegistry;
import exnihiloadscensio.registries.OreRegistry;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.tiles.TileCrucible;
import exnihiloadscensio.tiles.TileInfestedLeaves;
import exnihiloadscensio.tiles.TileSieve;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	public void initModels()
	{
		ENItems.initModels();
		ENBlocks.initModels();
	}
	
	@Override
	public void initOreModels() {
		OreRegistry.initModels();
	}

	@Override
	public void initPackets() {
		PacketHandler.initPackets();
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
		ClientRegistry.bindTileEntitySpecialRenderer(TileInfestedLeaves.class, new RenderInfestedLeaves());
		RenderingRegistry.registerEntityRenderingHandler(ProjectileStone.class, new RenderProjectileStone.Factory());
	}

	@Override
	public void registerColorHandlers()
	{
        Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new RenderOrePiece(), OreRegistry.getItemOreRegistry().toArray(new ItemOre[0]));
	}

	@Override
	public void registerConfigs(File configDirectory) {
		CompostRegistry.recommendAllFood(new File(configDirectory, "RecommendedFoodRegistry.json"));
	}
}
