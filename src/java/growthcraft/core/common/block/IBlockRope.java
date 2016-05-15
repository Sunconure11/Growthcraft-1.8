package growthcraft.core.common.block;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IBlockRope
{
	/**
	 * @param world - world to interact with
	 * @param pos - position to connect to
	 * @return can the rope be connected at this point?
	 */
	public boolean canConnectRopeTo(IBlockAccess world, BlockPos pos);
}
