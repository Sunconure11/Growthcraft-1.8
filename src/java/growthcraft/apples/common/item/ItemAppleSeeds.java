package growthcraft.apples.common.item;

import growthcraft.apples.GrowthCraftApples;
import growthcraft.core.common.item.GrcItemBase;
import growthcraft.core.GrowthCraftCore;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAppleSeeds extends GrcItemBase implements IPlantable
{
	private Block cropBlock;

	public ItemAppleSeeds()
	{
		super();
		this.cropBlock = GrowthCraftApples.appleSapling.getBlock();
		setUnlocalizedName("grc.appleSeeds");
		setCreativeTab(GrowthCraftCore.creativeTab);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing dir, float hitX, float hitY, float hitZ)
	{
		final Block block = world.getBlock(pos);
		if (block == Blocks.snow_layer && (world.getBlockMetadata(pos) & 7) < 1)
		{
			dir = 1;
		}
		else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush)
		{
			if (dir == 0)
			{
				--y;
			}

			if (dir == 1)
			{
				++y;
			}

			if (dir == 2)
			{
				--z;
			}

			if (dir == 3)
			{
				++z;
			}

			if (dir == 4)
			{
				--x;
			}

			if (dir == 5)
			{
				++x;
			}
		}


		if (!player.canPlayerEdit(pos, dir, stack))
		{
			return false;
		}
		else if (stack.stackSize == 0)
		{
			return false;
		}
		else
		{
			if (world.canPlaceEntityOnSide(cropBlock, pos, false, dir, (Entity)null, stack))
			{
				final int meta = cropBlock.onBlockPlaced(world, pos, dir, par8, par9, par10, 0);

				if (world.setBlock(pos, cropBlock, meta, 3))
				{
					if (world.getBlock(pos) == cropBlock)
					{
						cropBlock.onBlockPlacedBy(world, pos, player, stack);
						cropBlock.onPostBlockPlaced(world, pos, meta);
					}

					world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), cropBlock.stepSound.func_150496_b(), (cropBlock.stepSound.getVolume() + 1.0F) / 2.0F, cropBlock.stepSound.getPitch() * 0.8F);
					--stack.stackSize;
				}
			}

			return true;
		}
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Plains;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos)
	{
		return cropBlock.getDefaultState();
	}
}
