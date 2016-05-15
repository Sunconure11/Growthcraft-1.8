package growthcraft.core.common.block;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;

public interface IPaddy
{
	@Nonnull public Block getFluidBlock();
	@Nonnull public Fluid getFillingFluid();
	public int getPaddyMaxFluidLevel(IBlockAccess world, BlockPos pos, IBlockState state);
	public boolean isFilledWithFluid(IBlockAccess world, BlockPos pos, IBlockState state);
	public boolean canConnectPaddyTo(IBlockAccess world, BlockPos pos, IBlockState state);
	public boolean isBelowFillingFluid(IBlockAccess world, BlockPos pos, IBlockState state);
}
