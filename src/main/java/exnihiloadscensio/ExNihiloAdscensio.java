package exnihiloadscensio;

import java.io.File;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModIdMappingEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.config.Config;
import exnihiloadscensio.handlers.HandlerCrook;
import exnihiloadscensio.handlers.HandlerHammer;
import exnihiloadscensio.items.ENItems;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.BarrelModeRegistry;
import exnihiloadscensio.registries.CompostRegistry;
import exnihiloadscensio.registries.CrookRegistry;
import exnihiloadscensio.registries.CrucibleRegistry;
import exnihiloadscensio.registries.FluidBlockTransformerRegistry;
import exnihiloadscensio.registries.FluidOnTopRegistry;
import exnihiloadscensio.registries.HammerRegistry;
import exnihiloadscensio.registries.HeatRegistry;
import exnihiloadscensio.registries.OreRegistry;
import exnihiloadscensio.registries.SieveRegistry;

@Mod(modid = ExNihiloAdscensio.MODID, name="Ex Nihilo Adscensio")
public class ExNihiloAdscensio {

	public static final String MODID = "exnihiloadscensio";

	@SidedProxy(serverSide="exnihiloadscensio.CommonProxy",clientSide="exnihiloadscensio.client.ClientProxy")
	public static CommonProxy proxy;

	@Instance(MODID)
	public static ExNihiloAdscensio instance;

	private static File configDirectory;

	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		configDirectory = new File(event.getSuggestedConfigurationFile().getParentFile().getAbsolutePath() + "/" + MODID);
		configDirectory.mkdirs();
		Config.doNormalConfig(new File(configDirectory.getAbsolutePath()+"/ExNihiloAdscensio.cfg"));

		OreRegistry.loadJson(new File(configDirectory.getAbsolutePath() + "/OreRegistry.json"));

		ENItems.init();
		ENBlocks.init();
		proxy.initModels();
		proxy.registerRenderers();

		MinecraftForge.EVENT_BUS.register(new HandlerHammer());

		MinecraftForge.EVENT_BUS.register(new HandlerCrook());

		if (Config.enableBarrels)
		{
			BarrelModeRegistry.registerDefaults();
		}

		PacketHandler.initPackets();
	}

	@EventHandler
	public static void init(FMLInitializationEvent event)
	{
		proxy.registerColorHandlers();
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event)
	{
		Recipes.init();
		
		CompostRegistry.loadJson(new File(configDirectory.getAbsolutePath() + "/CompostRegistry.json"));

		HammerRegistry.loadJson(new File(configDirectory.getAbsolutePath() + "/HammerRegistry.json"));

		FluidBlockTransformerRegistry.loadJson(new File(configDirectory.getAbsolutePath() + "/FluidBlockTransformerRegistry.json"));

		FluidOnTopRegistry.loadJson(new File(configDirectory.getAbsolutePath() + "/FluidOnTopRegistry.json"));

		HeatRegistry.loadJson(new File(configDirectory.getAbsolutePath() + "/HeatRegistry.json"));

		CrucibleRegistry.loadJson(new File(configDirectory.getAbsolutePath() + "/CrucibleRegistry.json"));

		SieveRegistry.loadJson(new File(configDirectory.getAbsolutePath() + "/SieveRegistry.json"));
		
		CrookRegistry.registerDefaults();

		OreRegistry.doRecipes();
	}

	public static CreativeTabs tabExNihilo = new CreativeTabs("exNihilo")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem()
		{
			return Items.STRING;
		}
	};

	@EventHandler
	public static void modMapping(FMLModIdMappingEvent event) {
		
	}


}
