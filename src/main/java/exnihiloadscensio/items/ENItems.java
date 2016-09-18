package exnihiloadscensio.items;

import java.util.ArrayList;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.items.ore.ItemOre;
import exnihiloadscensio.items.seeds.ItemSeedBase;
import exnihiloadscensio.items.tools.CrookBase;
import exnihiloadscensio.items.tools.HammerBase;
import exnihiloadscensio.registries.OreRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ENItems {
	
	public static HammerBase hammerWood;
	public static HammerBase hammerStone;
	public static HammerBase hammerIron;
	public static HammerBase hammerDiamond;
	public static HammerBase hammerGold;
	
	public static CrookBase crookWood;
	public static CrookBase crookBone;
	
	public static ItemMesh mesh;
	
	public static ItemResource resources;
	
	public static ArrayList<ItemSeedBase> itemSeeds = new ArrayList<ItemSeedBase>();
	
	public static void init()
	{
		hammerWood = new HammerBase("hammerWood", 64, ToolMaterial.WOOD);
		hammerWood.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		
		hammerStone = new HammerBase("hammerStone", 128, ToolMaterial.STONE);
		hammerStone.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		
		hammerIron = new HammerBase("hammerIron", 512, ToolMaterial.IRON);
		hammerIron.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		
		hammerDiamond = new HammerBase("hammerDiamond", 4096, ToolMaterial.DIAMOND);
		hammerDiamond.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		
		hammerGold = new HammerBase("hammerGold", 64, ToolMaterial.GOLD);
		hammerGold.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		
		crookWood = new CrookBase("crookWood", 64);
		crookWood.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		
		crookBone = new CrookBase("crookBone", 256);
		crookBone.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		
		mesh = new ItemMesh();
		mesh.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		
		resources = new ItemResource();
		
		itemSeeds.add(new ItemSeedBase("oak", Blocks.SAPLING.getStateFromMeta(0), Blocks.DIRT.getDefaultState()));
		itemSeeds.add(new ItemSeedBase("spruce", Blocks.SAPLING.getStateFromMeta(1), Blocks.DIRT.getDefaultState()));
		itemSeeds.add(new ItemSeedBase("birch", Blocks.SAPLING.getStateFromMeta(2), Blocks.DIRT.getDefaultState()));
		itemSeeds.add(new ItemSeedBase("jungle", Blocks.SAPLING.getStateFromMeta(3), Blocks.DIRT.getDefaultState()));
		itemSeeds.add(new ItemSeedBase("acacia", Blocks.SAPLING.getStateFromMeta(4), Blocks.DIRT.getDefaultState()));
		itemSeeds.add(new ItemSeedBase("cactus", Blocks.CACTUS.getDefaultState(), Blocks.SAND.getDefaultState()).setPlantType(EnumPlantType.Desert));
		itemSeeds.add(new ItemSeedBase("sugarcane", Blocks.REEDS.getDefaultState(), Blocks.SAND.getDefaultState()).setPlantType(EnumPlantType.Beach));
		itemSeeds.add(new ItemSeedBase("carrot", Blocks.CARROTS.getDefaultState(), Blocks.FARMLAND.getDefaultState()).setPlantType(EnumPlantType.Crop));
		itemSeeds.add(new ItemSeedBase("potato", Blocks.POTATOES.getDefaultState(), Blocks.FARMLAND.getDefaultState()).setPlantType(EnumPlantType.Crop));
		
		
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModels()
	{
		hammerWood.initModel();
		hammerStone.initModel();
		hammerIron.initModel();
		hammerDiamond.initModel();
		hammerGold.initModel();
		
		crookWood.initModel();
		crookBone.initModel();
		
		mesh.initModel();
		
		resources.initModel();
		
		OreRegistry.initModels();
		
		for (ItemSeedBase seed : itemSeeds)
			seed.initModel();
	}
	
	
}
