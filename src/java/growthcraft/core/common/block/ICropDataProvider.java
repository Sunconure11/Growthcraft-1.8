package growthcraft.core.common.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Waila data provider for crop blocks
 */
public interface ICropDataProvider
{
	public float getGrowthProgress(IBlockAccess world, BlockPos pos, IBlockState state);
}
