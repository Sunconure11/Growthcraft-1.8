package growthcraft.cellar.client;

import growthcraft.cellar.client.resource.GrcCellarResources;
import growthcraft.cellar.common.CommonProxy;
import growthcraft.cellar.GrowthCraftCellar;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class ClientProxy extends CommonProxy
{
	public void registerVillagerSkin()
	{
		VillagerRegistry.instance().registerVillagerSkin(GrowthCraftCellar.getConfig().villagerBrewerID,
			new ResourceLocation("grccellar" , "textures/entity/brewer.png"));
	}

	@Override
	public void init()
	{
		new GrcCellarResources();

		initRenders();
		registerVillagerSkin();
	}
}
