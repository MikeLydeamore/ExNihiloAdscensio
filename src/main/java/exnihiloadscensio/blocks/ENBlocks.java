package exnihiloadscensio.blocks;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.fluid.FluidWitchWater;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.tiles.TileCrucible;
import exnihiloadscensio.tiles.TileInfestedLeaves;
import exnihiloadscensio.tiles.TileSieve;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ENBlocks {

	public static BlockDust dust;
	public static BlockBarrel barrelWood;
	public static BlockInfestedLeaves infestedLeaves;
	public static BlockCrucible crucible;
	public static BlockSieve sieve;
	
	public static FluidWitchWater fluidWitchwater;
	public static BlockFluidWitchwater blockWitchwater;

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
		
		crucible = new BlockCrucible();
		crucible.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		GameRegistry.registerTileEntity(TileCrucible.class, "blockCrucible");
		
		sieve = new BlockSieve();
		sieve.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		GameRegistry.registerTileEntity(TileSieve.class, "blockSieve");
		
		fluidWitchwater = new FluidWitchWater();
		blockWitchwater = new BlockFluidWitchwater();
		boolean result = FluidRegistry.addBucketForFluid(fluidWitchwater);
		
	}

	@SideOnly(Side.CLIENT)
	public static void initModels()
	{
		dust.initModel();
		barrelWood.initModel();
		sieve.initModel();
		crucible.initModel();
		
		fluidWitchwater.initModel();
	}

}
