package growthcraft.rice.common.block;

import java.util.Random;

import javax.annotation.Nonnull;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.common.block.BlockPaddyBase;
import growthcraft.rice.GrowthCraftRice;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPaddy extends BlockPaddyBase
{
	public BlockPaddy()
	{
		super(Material.ground);
		this.setHardness(0.5F);
		this.setStepSound(soundTypeGravel);
		this.setUnlocalizedName("grc.paddyField");
		this.setCreativeTab(null);
	}

	@Override
	public void fillWithRain(World world, BlockPos pos)
	{
		if (world.rand.nextInt(20) == 0)
		{
			final IBlockState state = world.getBlockState(pos);
			final int level = state.getValue(FLUID_LEVEL);
			if (level < getPaddyMaxFluidLevel(world, pos, state))
			{
				world.setBlockState(pos, state.withProperty(FLUID_LEVEL, level + 1), BlockFlags.UPDATE_AND_SYNC);
			}
		}
	}

	/**
	 * Returns the fluid block used to fill this paddy
	 *
	 * @return fluid block
	 */
	@Override
	@Nonnull public Block getFluidBlock()
	{
		return Blocks.water;
	}

	@Override
	@Nonnull public Fluid getFillingFluid()
	{
		return FluidRegistry.WATER;
	}

	@Override
	public boolean isBelowFillingFluid(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return world.getBlockState(pos.up()).getBlock().getMaterial() == Material.water;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return Item.getItemFromBlock(Blocks.dirt);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune)
	{
		return Item.getItemFromBlock(Blocks.dirt);
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}
}
