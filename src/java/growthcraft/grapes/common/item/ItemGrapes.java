package growthcraft.grapes.common.item;

import java.util.List;

import growthcraft.core.common.item.GrcItemFoodBase;
import growthcraft.grapes.GrowthCraftGrapes;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class ItemGrapes extends GrcItemFoodBase
{
	public ItemGrapes()
	{
		super(2, 0.3F, false);
		setHasSubtypes(true);
		setMaxDamage(0);
		setUnlocalizedName("grc.grapes");
		setCreativeTab(GrowthCraftGrapes.creativeTab);
	}

	public EnumGrapes getEnumGrapes(ItemStack stack)
	{
		return EnumGrapes.get(stack.getItemDamage());
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		final EnumGrapes en = getEnumGrapes(stack);
		return super.getUnlocalizedName(stack) + "." + en.name;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getSubItems(Item item, CreativeTabs ct, List list)
	{
		for (EnumGrapes en : EnumGrapes.VALUES)
		{
			list.add(en.asStack());
		}
	}
}
