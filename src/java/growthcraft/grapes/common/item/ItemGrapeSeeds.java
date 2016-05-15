package growthcraft.grapes.common.item;

import growthcraft.core.common.item.GrcItemBase;
import growthcraft.core.util.BlockCheck;
import growthcraft.grapes.common.block.BlockGrapeVine0;
import growthcraft.grapes.GrowthCraftGrapes;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGrapeSeeds extends GrcItemBase implements IPlantable
{
	public ItemGrapeSeeds()
	{
		super();
		this.setUnlocalizedName("grc.grapeSeeds");
		this.setCreativeTab(GrowthCraftGrapes.creativeTab);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing dir, float hitX, float hitY, float hitZ)
	{
		if (EnumFacing.UP == dir)
		{
			if (player.canPlayerEdit(pos, dir, stack) && player.canPlayerEdit(pos.up(), dir, stack))
			{
				final BlockGrapeVine0 block = GrowthCraftGrapes.blocks.grapeVine0.getBlock();
				if (BlockCheck.canSustainPlant(world, pos, EnumFacing.UP, block) && world.isAirBlock(pos.up()))
				{
					world.setBlock(pos.up(), block);
					--stack.stackSize;
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Crop;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return GrowthCraftGrapes.blocks.grapeVine0.getBlock().getDefaultState();
	}
}
