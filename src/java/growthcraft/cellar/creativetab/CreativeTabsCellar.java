package growthcraft.cellar.creativetab;

import growthcraft.cellar.GrowthCraftCellar;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabsCellar extends CreativeTabs
{
	public CreativeTabsCellar(String name)
	{
		super(name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getTabIconItem()
	{
		return GrowthCraftCellar.blocks.fermentBarrel.getItem();
	}
}
