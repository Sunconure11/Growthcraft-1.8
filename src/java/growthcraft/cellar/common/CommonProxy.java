package growthcraft.cellar.common;

import net.minecraftforge.fml.common.SidedProxy;

public class CommonProxy
{
	@SidedProxy(clientSide="growthcraft.cellar.client.ClientProxy", serverSide="growthcraft.cellar.common.CommonProxy")
	public static CommonProxy instance;

	public void init() {}
}
