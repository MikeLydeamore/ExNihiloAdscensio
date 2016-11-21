package exnihiloadscensio;

import java.io.File;

import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.capabilities.ENCapabilities;
import exnihiloadscensio.command.CommandReloadConfig;
import exnihiloadscensio.compatibility.tconstruct.CompatTConstruct;
import exnihiloadscensio.config.Config;
import exnihiloadscensio.enchantments.ENEnchantments;
import exnihiloadscensio.entities.ENEntities;
import exnihiloadscensio.handlers.HandlerCrook;
import exnihiloadscensio.handlers.HandlerHammer;
import exnihiloadscensio.items.ENItems;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.BarrelLiquidBlacklistRegistry;
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
import exnihiloadscensio.util.LogUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLModIdMappingEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = ExNihiloAdscensio.MODID, name="Ex Nihilo Adscensio")
public class ExNihiloAdscensio {

	public static final String MODID = "exnihiloadscensio";

	@SidedProxy(serverSide="exnihiloadscensio.CommonProxy",clientSide="exnihiloadscensio.client.ClientProxy")
	public static CommonProxy proxy;
	
	@Instance(MODID)
	public static ExNihiloAdscensio instance;

	public static File configDirectory;
	
	public static boolean configsLoaded = false;
	
	static
	{
		FluidRegistry.enableUniversalBucket();
	}
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
	    LogUtil.setup();
	    
		configDirectory = new File(event.getModConfigurationDirectory(), "exnihiloadscensio");
		configDirectory.mkdirs();
		
		Config.doNormalConfig(new File(configDirectory, "ExNihiloAdscensio.cfg"));

		OreRegistry.loadJson(new File(configDirectory, "OreRegistry.json"));

        ENCapabilities.init();
		ENItems.init();
		ENBlocks.init();
		ENEntities.init();
		ENEnchantments.init();
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
	    loadConfigs();
	    
		Recipes.init();
        OreRegistry.doRecipes();
        
		if(Loader.isModLoaded("tconstruct"))
		{
			CompatTConstruct.postInit();
		}
	}
	
	public static void loadConfigs()
	{
	    configsLoaded = true;
	    
        CompostRegistry.loadJson(new File(configDirectory, "CompostRegistry.json"));
        CompostRegistry.recommendAllFood(new File(configDirectory, "RecommendedFoodRegistry.json"));
        HammerRegistry.loadJson(new File(configDirectory, "HammerRegistry.json"));
        FluidBlockTransformerRegistry.loadJson(new File(configDirectory, "FluidBlockTransformerRegistry.json"));
        FluidOnTopRegistry.loadJson(new File(configDirectory, "FluidOnTopRegistry.json"));
        HeatRegistry.loadJson(new File(configDirectory, "HeatRegistry.json"));
        CrucibleRegistry.loadJson(new File(configDirectory, "CrucibleRegistry.json"));
        SieveRegistry.loadJson(new File(configDirectory, "SieveRegistry.json"));
        CrookRegistry.loadJson(new File(configDirectory, "CrookRegistry.json"));
        FluidTransformRegistry.loadJson(new File(configDirectory, "FluidTransformRegistry.json"));
        BarrelLiquidBlacklistRegistry.loadJson(new File(configDirectory, "BarrelLiquidBlacklistRegistry.json"));
        
	}
	
	@EventHandler
	public static void serverStart(FMLServerStartingEvent event)
	{
	    event.registerServerCommand(new CommandReloadConfig());
	}

	public static CreativeTabs tabExNihilo = new CreativeTabs("exNihilo")
	{
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem()
		{
			return Item.getItemFromBlock(ENBlocks.sieve);
		}
	};

	@EventHandler
	public static void modMapping(FMLModIdMappingEvent event) {
		
	}


}
