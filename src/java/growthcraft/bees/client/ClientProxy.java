package growthcraft.bees.client;

import growthcraft.bees.common.CommonProxy;
import growthcraft.bees.GrowthCraftBees;

import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraft.util.ResourceLocation;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerVillagerSkin()
	{
		VillagerRegistry.instance().registerVillagerSkin(GrowthCraftBees.getConfig().villagerApiaristID, new ResourceLocation("grcbees" , "textures/entity/apiarist.png"));
	}
}
