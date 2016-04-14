package growthcraft.rice.client;

import growthcraft.rice.client.renderer.RenderRice;
import growthcraft.rice.common.CommonProxy;

import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void initRenders()
	{
		RenderingRegistry.registerBlockHandler(new RenderRice());
	}
}
