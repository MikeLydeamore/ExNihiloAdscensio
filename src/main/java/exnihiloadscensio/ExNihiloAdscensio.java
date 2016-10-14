package exnihiloadscensio;

import java.io.File;

import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.config.Config;
import exnihiloadscensio.entities.ENEntities;
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
import exnihiloadscensio.registries.FluidTransformRegistry;
import exnihiloadscensio.registries.HammerRegistry;
import exnihiloadscensio.registries.HeatRegistry;
import exnihiloadscensio.registries.OreRegistry;
import exnihiloadscensio.registries.SieveRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLModIdMappingEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = ExNihiloAdscensio.MODID, name="Ex Nihilo Adscensio")
public class ExNihiloAdscensio {

	public static final String MODID = "exnihiloadscensio";

	@SidedProxy(serverSide="exnihiloadscensio.CommonProxy",clientSide="exnihiloadscensio.client.ClientProxy")
	public static CommonProxy proxy;

	@Instance(MODID)
	public static ExNihiloAdscensio instance;

	private static File configDirectory;
	
	static {
		FluidRegistry.enableUniversalBucket();
	}
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		configDirectory = new File(event.getSuggestedConfigurationFile().getParentFile().getAbsolutePath() + "/" + MODID);
		configDirectory.mkdirs();
		Config.doNormalConfig(new File(configDirectory.getAbsolutePath()+"/ExNihiloAdscensio.cfg"));

		OreRegistry.loadJson(new File(configDirectory.getAbsolutePath() + "/OreRegistry.json"));

		FluidRegistry.enableUniversalBucket();
		
		ENItems.init();
		ENBlocks.init();
		ENEntities.init();
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
		
		FMLInterModComms.sendMessage("Waila", "register", "exnihiloadscensio.compatibility.CompatWaila.callbackRegister");
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
		
		CrookRegistry.loadJson(new File(configDirectory.getAbsolutePath() + "/CrookRegistry.json"));

		OreRegistry.doRecipes();
		
		FluidTransformRegistry.loadJson(new File(configDirectory.getAbsolutePath()+"/FluidTransformRegistry.json"));
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
