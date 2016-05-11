package growthcraft.hops.common.item;

import growthcraft.core.common.item.GrcItemBase;
import growthcraft.core.GrowthCraftCore;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHops extends GrcItemBase
{
	public ItemHops()
	{
		super();
		this.setUnlocalizedName("grc.hops");
		this.setCreativeTab(GrowthCraftCore.creativeTab);
	}
}
