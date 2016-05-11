package growthcraft.rice.common.item;

import growthcraft.core.common.item.GrcItemFoodBase;
import growthcraft.core.GrowthCraftCore;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRiceBall extends GrcItemFoodBase
{
	public ItemRiceBall()
	{
		super(5, 0.6F, false);
		this.setUnlocalizedName("grc.riceBall");
		this.setCreativeTab(GrowthCraftCore.creativeTab);
	}
}
