package growthcraft.core.common.block;

import net.minecraft.world.IBlockAccess;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public interface IRotatableBlock
{
	/**
	 * Can this block be rotated?
	 *
	 * @param world - world the block is to be rotated in
	 * @param pos - pos of block to rotate
	 * @param axis - axis that block should be rotated on
	 * @return true, if it can be rotated, false otherwise
	 */
	boolean isRotatable(IBlockAccess world, BlockPos pos, EnumFacing axis);
}
