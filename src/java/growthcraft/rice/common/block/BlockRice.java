package growthcraft.rice.common.block;

import java.util.ArrayList;
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
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
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

	private final float growth = GrowthCraftRice.getConfig().riceGrowthRate;

	public BlockRice()
	{
		super(Material.plants);
		this.setHardness(0.0F);
		this.setTickRandomly(true);
		this.setCreativeTab(null);
		this.setBlockName("grc.riceBlock");
		this.setStepSound(soundTypeGrass);
	}

	public boolean isMature(IBlockAccess world, BlockPos pos)
	{
		final int meta = world.getBlockMetadata(pos);
		return meta >= RiceStage.MATURE;
	}

	public float getGrowthProgress(IBlockAccess world, BlockPos pos, int meta)
	{
		return (float)meta / (float)RiceStage.MATURE;
	}

	private void incrementGrowth(World world, BlockPos pos, int meta)
	{
		world.setBlockMetadataWithNotify(pos, meta + 1, BlockFlags.SYNC);
		AppleCore.announceGrowthTick(this, world, pos, meta);
	}

	private void growRice(World world, BlockPos pos, int meta)
	{
		incrementGrowth(world, pos, meta);
		final Block paddyBlock = world.getBlock(pos.down());
		if (RiceBlockCheck.isPaddy(paddyBlock))
		{
			((BlockPaddy)paddyBlock).drainPaddy(world, pos.down());
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		this.checkCropChange(world, pos);

		if (world.getBlockLightValue(pos.up()) >= 9 && world.getBlockMetadata(pos.down()) > 0)
		{
			final Event.Result allowGrowthResult = AppleCore.validateGrowthTick(this, world, pos, random);
			if (allowGrowthResult == Event.Result.DENY)
				return;

			final int meta = world.getBlockMetadata(pos);

			if (meta < RiceStage.MATURE)
			{
				final float f = this.getGrowthRate(world, pos);

				if (allowGrowthResult == Event.Result.ALLOW || (random.nextInt((int)(this.growth / f) + 1) == 0))
				{
					growRice(world, pos, meta);
				}
			}
		}
	}

	/* Both side */
	@Override
	public boolean canGrow(World world, BlockPos pos, boolean isClient)
	{
		return world.getBlockMetadata(pos) < RiceStage.MATURE;
	}

	/* SideOnly(Side.SERVER) Can this apply bonemeal effect? */
	@Override
	public boolean canUseBonemeal(World world, Random random, BlockPos pos)
	{
		return true;
	}

	/* Apply bonemeal effect */
	@Override
	public void grow(World world, Random random, BlockPos pos)
	{
		final int meta = world.getBlockMetadata(pos);
		if (meta < RiceStage.MATURE)
		{
			growRice(world, pos, meta);
		}
	}

	private float getGrowthRate(World world, BlockPos pos)
	{
		float f = 1.0F;
		final Block l = world.getBlock(pos - 1);
		final Block i1 = world.getBlock(pos + 1);
		final Block j1 = world.getBlock(x - 1, y, z);
		final Block k1 = world.getBlock(x + 1, y, z);
		final Block l1 = world.getBlock(x - 1, y, z - 1);
		final Block i2 = world.getBlock(x + 1, y, z - 1);
		final Block j2 = world.getBlock(x + 1, y, z + 1);
		final Block k2 = world.getBlock(x - 1, y, z + 1);
		final boolean flag = j1 == this || k1 == this;
		final boolean flag1 = l == this || i1 == this;
		final boolean flag2 = l1 == this || i2 == this || j2 == this || k2 == this;

		for (int loop_i = x - 1; loop_i <= x + 1; ++loop_i)
		{
			for (int loop_k = z - 1; loop_k <= z + 1; ++loop_k)
			{
				final Block soil = world.getBlock(loop_i, y - 1, loop_k);
				float f1 = 0.0F;

				if (soil != null && RiceBlockCheck.isPaddy(soil))
				{
					f1 = 1.0F;

					if (world.getBlockMetadata(loop_i, y - 1, loop_k) > 0)
					{
						f1 = 3.0F;
					}
				}

				if (loop_i != x || loop_k != z)
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

	protected final void checkCropChange(World world, BlockPos pos)
	{
		if (!this.canBlockStay(world, pos))
		{
			this.dropBlockAsItem(world, pos, world.getBlockMetadata(pos), 0);
			world.setBlockToAir(pos);
		}
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, Block par5)
	{
		this.checkCropChange(world, pos);
	}

	/**
	 * @param block - block to place on
	 * @return can the rice be placed on this block?
	 */
	protected boolean canThisPlantGrowOnThisBlockID(Block block)
	{
		return RiceBlockCheck.isPaddy(block);
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos)
	{
		return (world.getFullBlockLightValue(pos) >= 8 ||
			world.canBlockSeeTheSky(pos)) &&
			this.canThisPlantGrowOnThisBlockID(world.getBlock(x, y - 1, z));
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
	public void dropBlockAsItemWithChance(World world, BlockPos pos, int par5, float par6, int par7)
	{
		super.dropBlockAsItemWithChance(world, pos, par5, par6, 0);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, BlockPos pos, int metadata, int fortune)
	{
		final ArrayList<ItemStack> ret = super.getDrops(world, pos, metadata, fortune);

		if (metadata >= 7)
		{
			for (int n = 0; n < 3 + fortune; n++)
			{
				if (world.rand.nextInt(15) <= metadata)
				{
					ret.add(GrowthCraftRice.rice.asStack(1));
				}
			}
		}

		return ret;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, BlockPos pos)
	{
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, BlockPos pos)
	{
		this.setBlockBoundsBasedOnState(world, pos);
		return super.getSelectedBoundingBoxFromPool(world, pos);
	}
}
