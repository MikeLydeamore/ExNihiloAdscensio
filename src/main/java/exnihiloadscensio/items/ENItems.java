package exnihiloadscensio.items;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.items.seeds.ItemSeedBase;
import exnihiloadscensio.items.tools.CrookBase;
import exnihiloadscensio.items.tools.HammerBase;
import exnihiloadscensio.registries.OreRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

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
	public static ItemCookedSilkworm cookedSilkworm;

    public static ItemPebble pebbles;
    
    public static ItemDoll dolls;
	
	public static ArrayList<ItemSeedBase> itemSeeds = new ArrayList<ItemSeedBase>();
	
	@SuppressWarnings("deprecation")
    public static void init()
	{
		hammerWood = new HammerBase("hammerwood", 64, ToolMaterial.WOOD);
		hammerWood.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		
		hammerStone = new HammerBase("hammerstone", 128, ToolMaterial.STONE);
		hammerStone.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		
		hammerIron = new HammerBase("hammeriron", 512, ToolMaterial.IRON);
		hammerIron.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		
		hammerDiamond = new HammerBase("hammerdiamond", 4096, ToolMaterial.DIAMOND);
		hammerDiamond.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		
		hammerGold = new HammerBase("hammergold", 64, ToolMaterial.GOLD);
		hammerGold.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		
		crookWood = new CrookBase("crookwood", 64);
		crookWood.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		
		crookBone = new CrookBase("crookbone", 256);
		crookBone.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		
		mesh = new ItemMesh();
		mesh.setCreativeTab(ExNihiloAdscensio.tabExNihilo);
		
		resources = new ItemResource();
		OreDictionary.registerOre("clayporcelain", ItemResource.getResourceStack("porcelain_clay"));
		
		cookedSilkworm = new ItemCookedSilkworm();

        pebbles = new ItemPebble();
		
		itemSeeds.add(new ItemSeedBase("oak", Blocks.SAPLING.getStateFromMeta(0)));
		itemSeeds.add(new ItemSeedBase("spruce", Blocks.SAPLING.getStateFromMeta(1)));
		itemSeeds.add(new ItemSeedBase("birch", Blocks.SAPLING.getStateFromMeta(2)));
		itemSeeds.add(new ItemSeedBase("jungle", Blocks.SAPLING.getStateFromMeta(3)));
        itemSeeds.add(new ItemSeedBase("acacia", Blocks.SAPLING.getStateFromMeta(4)));
        itemSeeds.add(new ItemSeedBase("darkoak", Blocks.SAPLING.getStateFromMeta(5)));
		itemSeeds.add(new ItemSeedBase("cactus", Blocks.CACTUS.getDefaultState()).setPlantType(EnumPlantType.Desert));
		itemSeeds.add(new ItemSeedBase("sugarcane", Blocks.REEDS.getDefaultState()).setPlantType(EnumPlantType.Beach));
		itemSeeds.add(new ItemSeedBase("carrot", Blocks.CARROTS.getDefaultState()).setPlantType(EnumPlantType.Crop));
		itemSeeds.add(new ItemSeedBase("potato", Blocks.POTATOES.getDefaultState()).setPlantType(EnumPlantType.Crop));
		
		dolls = new ItemDoll();
		
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
		pebbles.initModel();
		
		cookedSilkworm.initModel();
		
		OreRegistry.initModels();
		
		for (ItemSeedBase seed : itemSeeds)
			seed.initModel();
		
		dolls.initModel();
	}
}
