package exnihiloadscensio.blocks;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.fluid.FluidWitchWater;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.tiles.TileCrucible;
import exnihiloadscensio.tiles.TileInfestedLeaves;
import exnihiloadscensio.tiles.TileSieve;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ENBlocks {

    public static BlockBaseFalling dust;
    public static BlockBaseFalling netherrackCrushed;
    public static BlockBaseFalling endstoneCrushed;
    public static BlockBarrel barrelWood;
    public static BlockBarrel barrelStone;
	public static BlockInfestedLeaves infestedLeaves;
	public static BlockCrucible crucible;
	public static BlockSieve sieve;
	
	public static FluidWitchWater fluidWitchwater;
	public static BlockFluidWitchwater blockWitchwater;

	public static void init()
	{
        dust = new BlockBaseFalling(SoundType.CLOTH, "blockDust");
        dust.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
        dust.setHardness(0.7F);
        
        netherrackCrushed = new BlockBaseFalling(SoundType.GROUND, "blockNetherrackCrushed");
        netherrackCrushed.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
        netherrackCrushed.setHardness(0.7F);

        endstoneCrushed = new BlockBaseFalling(SoundType.GROUND, "blockEndstoneCrushed");
        endstoneCrushed.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
        endstoneCrushed.setHardness(0.7F);
        
        barrelWood = new BlockBarrel(0, Material.WOOD);
        barrelWood.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
        GameRegistry.registerTileEntity(TileBarrel.class, "blockBarrel0");
        
        barrelStone = new BlockBarrel(1, Material.ROCK);
        barrelStone.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
        GameRegistry.registerTileEntity(TileBarrel.class, "blockBarrel1");
		
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
		FluidRegistry.addBucketForFluid(fluidWitchwater);
	}

	@SideOnly(Side.CLIENT)
	public static void initModels()
	{
		dust.initModel();
		netherrackCrushed.initModel();
		endstoneCrushed.initModel();
		
		barrelWood.initModel();
		barrelStone.initModel();
		
		sieve.initModel();
		crucible.initModel();
		
		fluidWitchwater.initModel();
	}

}
