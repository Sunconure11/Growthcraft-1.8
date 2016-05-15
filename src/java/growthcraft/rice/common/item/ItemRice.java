package growthcraft.rice.common.item;

import growthcraft.core.common.item.GrcItemBase;
import growthcraft.core.GrowthCraftCore;
import growthcraft.rice.GrowthCraftRice;
import growthcraft.rice.util.RiceBlockCheck;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing dir, float hitX, float hitY, float hitZ)
	{
		if (dir == EnumFacing.UP)
		{
			if (player.canPlayerEdit(pos, dir, stack) && player.canPlayerEdit(pos.up(), dir, stack))
			{
				final IBlockState soil = world.getBlockState(pos);
				if (soil != null && RiceBlockCheck.isPaddy(soil) && world.isAirBlock(pos.up()) && world.getBlockMetadata(pos) > 0)
				{
					world.setBlockState(pos.up(), GrowthCraftRice.riceBlock.getBlock().getDefaultState());
					--stack.stackSize;
					return true;
				}
			}
		}
		return false;
	}
}
