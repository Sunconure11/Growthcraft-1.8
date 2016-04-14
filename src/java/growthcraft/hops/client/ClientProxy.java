package growthcraft.hops.client;

import growthcraft.hops.client.renderer.RenderHops;
import growthcraft.hops.common.CommonProxy;

import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void initRenders()
	{
		RenderingRegistry.registerBlockHandler(new RenderHops());
	}
}
