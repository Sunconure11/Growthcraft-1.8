package growthcraft.grapes.common.block;

import java.util.List;
import java.util.Random;

import growthcraft.core.util.BlockCheck;
import growthcraft.api.core.util.BlockFlags;
import growthcraft.grapes.GrowthCraftGrapes;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGrapeVine1 extends BlockGrapeVineBase
{
	public BlockGrapeVine1()
	{
		super();
		setGrowthRateMultiplier(GrowthCraftGrapes.getConfig().grapeVineTrunkGrowthRate);
		setTickRandomly(true);
		setHardness(2.0F);
		setResistance(5.0F);
		setStepSound(soundTypeWood);
		setUnlocalizedName("grc.grapeVine1");
		setCreativeTab(null);
	}

	@Override
	protected boolean canUpdateGrowth(World world, BlockPos pos)
	{
		return world.getBlockMetadata(pos) == 0 || world.isAirBlock(pos.up());
	}

	@Override
	protected void doGrowth(World world, BlockPos pos, int meta)
	{
		final BlockPos upPos = pos.up();
		final Block above = world.getBlock(upPos);
		/* Is there a rope block above this? */
		if (BlockCheck.isRope(above))
		{
			incrementGrowth(world, pos, meta);
			world.setBlock(upPos, GrowthCraftGrapes.blocks.grapeLeaves.getBlock(), 0, BlockFlags.UPDATE_AND_SYNC);
		}
		else if (world.isAirBlock(upPos))
		{
			incrementGrowth(world, pos, meta);
			world.setBlock(upPos, this, 0, BlockFlags.UPDATE_AND_SYNC);
		}
		else if (GrowthCraftGrapes.blocks.grapeLeaves.getBlock() == above)
		{
			incrementGrowth(world, pos, meta);
		}
	}

	@Override
	protected float getGrowthRate(World world, BlockPos pos)
	{
		int j = pos.getY();
		final BlockState belowState = world.getBlockState(pos.down());
		// for lack of a better name
		final BlockState belowBelowState = world.getBlock(pos.down(2));
		if (this == belowState.getBlock() && Blocks.farmland == belowBelowState.getBlock())
		{
			j--;
		}
		return super.getGrowthRate(world, new BlockPos(pos.getX(), j, pos.getZ()));
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos)
	{
		return BlockCheck.canSustainPlant(world, pos.down(), EnumFacing.UP, this) ||
			this == world.getBlock(pos.down());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return GrowthCraftGrapes.items.grapeSeeds.getItem();
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB aabb, List<AxisAlignedBB> list, Entity entity)
	{
		final int meta = world.getBlockMetadata(pos);
		final float f = 0.0625F;

		if (meta == 0)
		{
			setBlockBounds(6*f, 0.0F, 6*f, 10*f, 0.5F, 10*f);
			super.addCollisionBoxesToList(world, pos, state, aabb, list, entity);
			setBlockBounds(4*f, 0.5F, 4*f, 12*f, 1.0F, 12*f);
			super.addCollisionBoxesToList(world, pos, state, aabb, list, entity);
		}
		else if (meta == 1)
		{
			setBlockBounds(6*f, 0.0F, 6*f, 10*f, 1.0F, 10*f);
			super.addCollisionBoxesToList(world, pos, state, aabb, list, entity);
		}

		setBlockBoundsBasedOnState(world, pos);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
	{
		final int meta = world.getBlockMetadata(pos);
		final float f = 0.0625F;

		if (meta == 0)
		{
			setBlockBounds(4*f, 0.0F, 4*f, 12*f, 1.0F, 12*f);
		}
		else
		{
			setBlockBounds(6*f, 0.0F, 6*f, 10*f, 1.0F, 10*f);
		}
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state)
	{
		final int meta = world.getBlockMetadata(pos);
		if (meta == 0 && BlockCheck.isRope(world.getBlock(pos.up())))
		{
			if (!world.isRemote)
			{
				incrementGrowth(world, pos, meta);
				world.setBlockState(pos.up(), GrowthCraftGrapes.blocks.grapeLeaves.getBlock().getDefaultState(), 0, BlockFlags.ALL);
			}
		}
		if (meta == 0 && world.isAirBlock(pos.up()))
		{
			if (!world.isRemote)
			{
				incrementGrowth(world, pos, meta);
				world.setBlockState(pos.up(), GrowthCraftGrapes.blocks.grapeVine1.getBlock().getDefaultState(), 0, BlockFlags.ALL);
			}
		}
		else if (meta == 0 && world.getBlock(pos.up()) ==  GrowthCraftGrapes.blocks.grapeLeaves.getBlock())
		{
			if (!world.isRemote)
			{
				incrementGrowth(world, pos, meta);
			}
		}
	}
}
