package growthcraft.rice.common.item;

import growthcraft.core.common.item.GrcItemFoodBase;
import growthcraft.core.GrowthCraftCore;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;

public class ItemRiceBall extends GrcItemFoodBase
{
	public ItemRiceBall()
	{
		super(5, 0.6F, false);
		this.setUnlocalizedName("grc.riceBall");
		this.setCreativeTab(GrowthCraftCore.creativeTab);
	}

	/************
	 * TEXTURES
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg)
	{
		this.itemIcon = reg.registerIcon("grcrice:rice_ball");
	}
}
