package exnihiloadscensio.items;

import exnihiloadscensio.ExNihiloAdscensio;
import exnihiloadscensio.items.tools.CrookBase;
import exnihiloadscensio.items.tools.HammerBase;
import net.minecraft.item.Item.ToolMaterial;
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
	}
	
	
}
