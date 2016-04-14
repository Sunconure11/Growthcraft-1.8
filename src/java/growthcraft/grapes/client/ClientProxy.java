package growthcraft.grapes.client;

import growthcraft.grapes.client.renderer.RenderGrape;
import growthcraft.grapes.client.renderer.RenderGrapeLeaves;
import growthcraft.grapes.client.renderer.RenderGrapeVine1;
import growthcraft.grapes.common.CommonProxy;

import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void initRenders()
	{
		RenderingRegistry.registerBlockHandler(new RenderGrape());
		RenderingRegistry.registerBlockHandler(new RenderGrapeLeaves());
		RenderingRegistry.registerBlockHandler(new RenderGrapeVine1());
	}
}
