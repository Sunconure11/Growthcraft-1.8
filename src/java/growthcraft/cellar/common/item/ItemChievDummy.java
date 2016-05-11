package growthcraft.cellar.common.item;

import growthcraft.core.common.item.GrcItemBase;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemChievDummy extends GrcItemBase
{
	public ItemChievDummy()
	{
		super();
		setCreativeTab(null);
		setMaxStackSize(1);
		setHasSubtypes(true);
		setMaxDamage(0);
		setUnlocalizedName("grc.chievItemDummy");
	}
}
