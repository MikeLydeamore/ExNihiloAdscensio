package exnihiloadscensio;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.handlers.HandlerHammer;
import exnihiloadscensio.items.ENItems;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.BarrelModeRegistry;
import exnihiloadscensio.registries.HammerRegistry;

@Mod(modid = ExNihiloAdscensio.MODID, name="Ex Nihilo Adscensio")
public class ExNihiloAdscensio {
	
	public static final String MODID = "exnihiloadscensio";
	
	@SidedProxy(serverSide="exnihiloadscensio.CommonProxy",clientSide="exnihiloadscensio.client.ClientProxy")
	public static CommonProxy proxy;
	
	@Instance(MODID)
	public static ExNihiloAdscensio instance;
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		ENItems.init();
		ENBlocks.init();
		proxy.initModels();
		proxy.registerRenderers();
		
		HammerRegistry.addDefaultRecipes();
		MinecraftForge.EVENT_BUS.register(new HandlerHammer());
		
		BarrelModeRegistry.registerDefaults();
		
		PacketHandler.initPackets();
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event)
	{
		
	}
	
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event)
	{
		
	}
	
	public static CreativeTabs tabExNihilo = new CreativeTabs("exNihilo")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem()
		{
			return Items.string;
		}
	};

}
