package exnihiloadscensio.client;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import exnihiloadscensio.CommonProxy;
import exnihiloadscensio.blocks.BlockInfestedLeaves;
import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.client.renderers.RenderBarrel;
import exnihiloadscensio.client.renderers.RenderCrucible;
import exnihiloadscensio.client.renderers.RenderInfestedLeaves;
import exnihiloadscensio.client.renderers.RenderSieve;
import exnihiloadscensio.items.ENItems;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.tiles.TileCrucible;
import exnihiloadscensio.tiles.TileInfestedLeaves;
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
	}


}
