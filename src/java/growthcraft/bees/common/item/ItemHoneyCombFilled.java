package growthcraft.bees.common.item;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.common.item.GrcItemBase;

public class ItemHoneyCombFilled extends GrcItemBase
{
	public ItemHoneyCombFilled()
	{
		super();
		setUnlocalizedName("grc.honeyComb.full");
		setCreativeTab(GrowthCraftBees.tab);
		setContainerItem(GrowthCraftBees.items.honeyCombEmpty.getItem());
	}
}
