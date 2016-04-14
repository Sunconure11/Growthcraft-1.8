package growthcraft.bamboo.common;

import net.minecraftforge.fml.common.SidedProxy;

public class CommonProxy
{
	@SidedProxy(clientSide="growthcraft.bamboo.client.ClientProxy", serverSide="growthcraft.bamboo.common.CommonProxy")
	public static CommonProxy instance;
}
