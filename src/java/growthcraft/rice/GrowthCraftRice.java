package growthcraft.rice;

import growthcraft.api.core.log.GrcLogger;
import growthcraft.api.core.log.ILogger;
import growthcraft.api.core.module.ModuleContainer;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.definition.BlockDefinition;
import growthcraft.core.common.definition.BlockTypeDefinition;
import growthcraft.core.common.definition.ItemDefinition;
import growthcraft.core.eventhandler.PlayerInteractEventPaddy;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.util.MapGenHelper;
import growthcraft.rice.common.block.BlockPaddy;
import growthcraft.rice.common.block.BlockRice;
import growthcraft.rice.common.CommonProxy;
import growthcraft.rice.common.item.ItemRice;
import growthcraft.rice.common.item.ItemRiceBall;
import growthcraft.rice.common.village.ComponentVillageRiceField;
import growthcraft.rice.common.village.VillageHandlerRice;
import growthcraft.rice.init.GrcRiceFluids;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(
	modid = GrowthCraftRice.MOD_ID,
	name = GrowthCraftRice.MOD_NAME,
	version = GrowthCraftRice.MOD_VERSION,
	dependencies = "required-after:Growthcraft@@VERSION@;required-after:Growthcraft|Cellar@@VERSION@"
)
public class GrowthCraftRice
{
	public static final String MOD_ID = "Growthcraft|Rice";
	public static final String MOD_NAME = "Growthcraft Rice";
	public static final String MOD_VERSION = "@VERSION@";

	@Instance(MOD_ID)
	public static GrowthCraftRice instance;

	public static BlockTypeDefinition<BlockRice> riceBlock;
	public static BlockDefinition paddyField;
	public static ItemDefinition rice;
	public static ItemDefinition riceBall;

	public static GrcRiceFluids fluids = new GrcRiceFluids();

	private ILogger logger = new GrcLogger(MOD_ID);
	private GrcRiceConfig config = new GrcRiceConfig();
	private ModuleContainer modules = new ModuleContainer();

	public static GrcRiceConfig getConfig()
	{
		return instance.config;
	}

	@EventHandler
	public void preload(FMLPreInitializationEvent event)
	{
		config.setLogger(logger);
		config.load(event.getModConfigurationDirectory(), "growthcraft/rice.conf");

		modules.add(fluids);
		if (config.enableThaumcraftIntegration) modules.add(new growthcraft.rice.integration.ThaumcraftModule());

		if (config.debugEnabled) modules.setLogger(logger);

		//====================
		// INIT
		//====================
		riceBlock = new BlockTypeDefinition<BlockRice>(new BlockRice());
		paddyField = new BlockDefinition(new BlockPaddy());

		rice     = new ItemDefinition(new ItemRice());
		riceBall = new ItemDefinition(new ItemRiceBall());

		modules.preInit();
		register();
	}

	private void register()
	{
		//====================
		// REGISTRIES
		//====================
		GameRegistry.registerBlock(riceBlock.getBlock(), "grc.riceBlock");
		GameRegistry.registerBlock(paddyField.getBlock(), "grc.paddyField");

		GameRegistry.registerItem(rice.getItem(), "grc.rice");
		GameRegistry.registerItem(riceBall.getItem(), "grc.riceBall");

		MinecraftForge.addGrassSeed(rice.asStack(), config.riceSeedDropRarity);

		MapGenHelper.registerVillageStructure(ComponentVillageRiceField.class, "grc.ricefield");

		//====================
		// ORE DICTIONARY
		//====================
		OreDictionary.registerOre("cropRice", rice.getItem());
		OreDictionary.registerOre("seedRice", rice.getItem());
		// For Pam's HarvestCraft
		// Uses the same OreDict. names as HarvestCraft
		OreDictionary.registerOre("listAllseed", rice.getItem());

		//====================
		// CRAFTING
		//====================
		GameRegistry.addRecipe(new ShapedOreRecipe(riceBall.asStack(1), "###", "###", '#', "cropRice"));

		MinecraftForge.EVENT_BUS.register(this);

		modules.register();
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		PlayerInteractEventPaddy.paddyBlocks.put(Blocks.farmland, paddyField.getBlock());
		final VillageHandlerRice handler = new VillageHandlerRice();
		//VillagerRegistry.instance().registerVillageTradeHandler(GrowthCraftCellar.getConfig().villagerBrewerID, handler);
		VillagerRegistry.instance().registerVillageCreationHandler(handler);

		modules.init();
	}

	@EventHandler
	public void postload(FMLPostInitializationEvent event)
	{
		modules.postInit();
	}
}
