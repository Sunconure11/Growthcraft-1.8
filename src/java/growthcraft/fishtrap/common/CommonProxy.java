package growthcraft.fishtrap.common;

import net.minecraftforge.fml.common.SidedProxy;

public class CommonProxy
{
	@SidedProxy(clientSide="growthcraft.fishtrap.client.ClientProxy", serverSide="growthcraft.fishtrap.common.CommonProxy")
	public static CommonProxy instance;

	public void initRenders(){}
}
