package growthcraft.grapes.common.block;

import java.util.Random;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.util.BlockCheck;
import growthcraft.grapes.GrowthCraftGrapes;

import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * This is the Grape Vine sapling block
 */
public class BlockGrapeVine0 extends BlockGrapeVineBase implements IGrowable
{
	public BlockGrapeVine0()
	{
		super();
		setGrowthRateMultiplier(GrowthCraftGrapes.getConfig().grapeVineSeedlingGrowthRate);
		setTickRandomly(true);
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		setUnlocalizedName("grc.grapeVine0");
		setCreativeTab(null);
	}

	@Override
	protected boolean canUpdateGrowth(World world, BlockPos pos, IBlockState state)
	{
		return getLightValue(world, pos.up()) >= 9;
	}

	@Override
	protected void doGrowth(World world, BlockPos pos, IBlockState state)
	{
		final int meta = state.getValue(GROWTH);
		if (meta == 0)
		{
			incrementGrowth(world, pos, state);
		}
		else
		{
			world.setBlock(pos, GrowthCraftGrapes.blocks.grapeVine1.getBlock(), 0, BlockFlags.UPDATE_AND_SYNC);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return GrowthCraftGrapes.items.grapeSeeds.getItem();
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
	{
		final IBlockState state = world.getBlockState(pos);
		final int meta = state.getValue(GROWTH);
		final float f = 0.0625F;

		if (meta == 0)
		{
			this.setBlockBounds(6*f, 0.0F, 6*f, 10*f, 5*f, 10*f);
		}
		else
		{
			this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state)
	{
		final int meta = state.getValue(GROWTH);

		if (!world.isRemote)
		{
			final int i = MathHelper.getRandomIntegerInRange(world.rand, 1, 2);
			final IBlockState vine1State = GrowthCraftGrapes.blocks.grapeVine1.getBlock().getDefaultState();
			if (meta == 0)
			{
				if (i == 1)
				{
					incrementGrowth(world, pos, state);
				}
				else if (i == 2)
				{
					world.setBlockState(pos, vine1State, BlockFlags.ALL);
				}
			}
			else if (meta == 1)
			{
				if (i == 1)
				{
					world.setBlockState(pos, vine1State, BlockFlags.ALL);
				}
				else if (i == 2)
				{
					if (BlockCheck.isRope(world.getBlockState(pos.up())))
					{
						world.setBlockState(pos, vine1State.withProperty(GROWTH, 1), BlockFlags.ALL);
						world.setBlockState(pos.up(), GrowthCraftGrapes.blocks.grapeLeaves.getBlock().getDefaultState(), BlockFlags.ALL);
					}
					else
					{
						world.setBlockState(pos, vine1State, BlockFlags.ALL);
					}
				}
			}
		}
	}
}
