package exnihiloadscensio.blocks;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.config.Config;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.tiles.TileInfestedLeaves;

public class ENBlocks {

	public static BlockDust dust;
	public static BlockBarrel barrelWood;
	public static BlockInfestedLeaves infestedLeaves;

	public static void init()
	{
		dust = new BlockDust();
		dust.setCreativeTab(ExNihiloAdscensio.tabExNihilo);

		barrelWood = new BlockBarrel();
		barrelWood.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		GameRegistry.registerTileEntity(TileBarrel.class, "blockBarrel");
		
		infestedLeaves = new BlockInfestedLeaves();
		GameRegistry.registerTileEntity(TileInfestedLeaves.class, "blockInfestedLeaves");
		infestedLeaves.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
	}

	@SideOnly(Side.CLIENT)
	public static void initModels()
	{
		dust.initModel();
		barrelWood.initModel();
	}

}
