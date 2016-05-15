package growthcraft.rice.common.block;

import java.util.List;
import java.util.Random;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.common.block.GrcBlockBase;
import growthcraft.core.common.block.ICropDataProvider;
import growthcraft.core.common.block.IPaddyCrop;
import growthcraft.core.integration.AppleCore;
import growthcraft.rice.GrowthCraftRice;
import growthcraft.rice.util.RiceBlockCheck;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRice extends GrcBlockBase implements IPaddyCrop, ICropDataProvider, IGrowable
{
	public static class RiceStage
	{
		public static final int MATURE = 7;

		private RiceStage() {}
	}

	public static final PropertyInteger GROWTH = PropertyInteger.create("growth", 0, RiceStage.MATURE);
	private final float growthRate = GrowthCraftRice.getConfig().riceGrowthRate;

	public BlockRice()
	{
		super(Material.plants);
		setHardness(0.0F);
		setTickRandomly(true);
		setCreativeTab(null);
		setUnlocalizedName("grc.riceBlock");
		setStepSound(soundTypeGrass);
		setDefaultState(blockState.getBaseState().withProperty(GROWTH, 0));
	}

	public float getGrowthProgress(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return (float)state.getValue(GROWTH) / (float)RiceStage.MATURE;
	}

	private void incrementGrowth(World world, BlockPos pos, IBlockState state)
	{
		final int meta = state.getValue(GROWTH);
		world.setBlockState(pos, state.withProperty(GROWTH, meta + 1));
		AppleCore.announceGrowthTick(this, world, pos, meta);
	}

	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient)
	{
		return (int)state.getValue(GROWTH) < RiceStage.MATURE;
	}

	@Override
	public boolean canUseBonemeal(World world, Random random, BlockPos pos, IBlockState state)
	{
		return true;
	}

	@Override
	public void grow(World world, Random random, BlockPos pos, IBlockState state)
	{
		if ((int)state.getValue(GROWTH) < RiceStage.MATURE)
		{
			incrementGrowth(world, pos, state);
			final IBlockState paddyBlock = world.getBlockState(pos.down());
			if (RiceBlockCheck.isPaddy(paddyBlock))
			{
				((BlockPaddy)paddyBlock.getBlock()).drainPaddy(world, pos.down(), paddyBlock);
			}
		}
	}

	private float getGrowthRate(World world, BlockPos pos, IBlockState state)
	{
		float f = 1.0F;
		final IBlockState l = world.getBlockState(pos.north());
		final IBlockState i1 = world.getBlockState(pos.south());
		final IBlockState j1 = world.getBlockState(pos.west());
		final IBlockState k1 = world.getBlockState(pos.east());
		final IBlockState l1 = world.getBlockState(pos.north().west());
		final IBlockState i2 = world.getBlockState(pos.south().west());
		final IBlockState j2 = world.getBlockState(pos.south().east());
		final IBlockState k2 = world.getBlockState(pos.north().east());
		final boolean flag = j1.getBlock() == this || k1.getBlock() == this;
		final boolean flag1 = l.getBlock() == this || i1.getBlock() == this;
		final boolean flag2 = l1.getBlock() == this || i2.getBlock() == this || j2.getBlock() == this || k2.getBlock() == this;

		for (int loop_i = pos.getX() - 1; loop_i <= pos.getX() + 1; ++loop_i)
		{
			for (int loop_k = pos.getZ() - 1; loop_k <= pos.getZ() + 1; ++loop_k)
			{
				final BlockPos soilPos = new BlockPos(loop_i, pos.getY(), loop_k);
				final IBlockState soil = world.getBlockState(soilPos);
				float f1 = 0.0F;

				if (soil != null && RiceBlockCheck.isPaddy(soil))
				{
					f1 = 1.0F;

					if ((int)state.getValue(GROWTH) > 0)
					{
						f1 = 3.0F;
					}
				}

				if (loop_i != pos.getX() || loop_k != pos.getZ())
				{
					f1 /= 4.0F;
				}

				f += f1;
			}
		}

		if (flag2 || flag && flag1)
		{
			f /= 2.0F;
		}

		return f;
	}

	public boolean canBlockStay(World world, BlockPos pos, IBlockState state)
	{
		final IBlockState paddyState = world.getBlockState(pos.down());
		return (getLightValue(world, pos) >= 8 ||
			world.canBlockSeeSky(pos)) &&
			RiceBlockCheck.isPaddy(paddyState);
	}

	protected final boolean checkCropChange(World world, BlockPos pos, IBlockState state)
	{
		if (!canBlockStay(world, pos, state))
		{
			fellBlockAsItem(world, pos, state);
			return true;
		}
		return false;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		super.updateTick(world, pos, state, random);
		if (!checkCropChange(world, pos, state))
		{
			final IBlockState paddyState = world.getBlockState(pos.down());
			final boolean paddyHasWater = false;

			if (getLightValue(world, pos.up()) >= 9 && paddyHasWater)
			{
				final Event.Result allowGrowthResult = AppleCore.validateGrowthTick(this, world, pos, random);
				if (allowGrowthResult == Event.Result.DENY)
					return;

				if ((int)state.getValue(GROWTH) < RiceStage.MATURE)
				{
					final float f = getGrowthRate(world, pos, state);

					if (allowGrowthResult == Event.Result.ALLOW || (random.nextInt((int)(growthRate / f) + 1) == 0))
					{
						grow(world, random, pos, state);
					}
				}
			}
		}
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block block)
	{
		super.onNeighborBlockChange(world, pos, state, block);
		checkCropChange(world, pos, state);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return GrowthCraftRice.rice.getItem();
	}

	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune)
	{
		return GrowthCraftRice.rice.getItem();
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 1;
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		final List<ItemStack> ret = super.getDrops(world, pos, state, fortune);

		final int meta = state.getValue(GROWTH);
		if (meta >= RiceStage.MATURE)
		{
			for (int n = 0; n < 3 + fortune; n++)
			{
				ret.add(GrowthCraftRice.rice.asStack(1));
			}
		}

		return ret;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
}
