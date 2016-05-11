package growthcraft.rice.common.item;

import growthcraft.core.common.item.GrcItemBase;
import growthcraft.core.GrowthCraftCore;
import growthcraft.rice.GrowthCraftRice;
import growthcraft.rice.util.RiceBlockCheck;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRice extends GrcItemBase
{
	public ItemRice()
	{
		super();
		this.setUnlocalizedName("grc.rice");
		this.setCreativeTab(GrowthCraftCore.creativeTab);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int dir, float par8, float par9, float par10)
	{
		if (dir != 1)
		{
			return false;
		}
		else if (player.canPlayerEdit(x, y, z, dir, stack) && player.canPlayerEdit(x, y + 1, z, dir, stack))
		{
			final Block soil = world.getBlock(x, y, z);

			if (soil != null && RiceBlockCheck.isPaddy(soil) && world.isAirBlock(x, y + 1, z) && world.getBlockMetadata(x, y, z) > 0)
			{
				world.setBlock(x, y + 1, z, GrowthCraftRice.riceBlock.getBlock());
				--stack.stackSize;
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
}
