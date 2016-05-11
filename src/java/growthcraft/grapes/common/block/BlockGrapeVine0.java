package growthcraft.grapes.common.block;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.grapes.GrowthCraftGrapes;

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
public class BlockGrapeVine0 extends BlockGrapeVineBase
{
	public BlockGrapeVine0()
	{
		super();
		setGrowthRateMultiplier(GrowthCraftGrapes.getConfig().grapeVineSeedlingGrowthRate);
		setTickRandomly(true);
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		setBlockName("grc.grapeVine0");
		setCreativeTab(null);
	}

	/************
	 * TICK
	 ************/
	@Override
	protected boolean canUpdateGrowth(World world, BlockPos pos)
	{
		return world.getBlockLightValue(x, y + 1, z) >= 9;
	}

	@Override
	protected void doGrowth(World world, BlockPos pos, int meta)
	{
		if (meta == 0)
		{
			incrementGrowth(world, pos, meta);
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
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, BlockPos pos)
	{
		return null;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
	{
		final int meta = world.getBlockMetadata(pos);
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
}
