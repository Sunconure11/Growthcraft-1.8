package growthcraft.apples.common.block;

import java.util.Random;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.apples.GrowthCraftApples;
import growthcraft.core.common.block.GrcBlockBase;
import growthcraft.core.common.block.ICropDataProvider;
import growthcraft.core.integration.AppleCore;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockApple extends GrcBlockBase implements IGrowable, ICropDataProvider
{
	public static class AppleStage
	{
		public static final int YOUNG = 0;
		public static final int MID = 1;
		public static final int MATURE = 2;
		public static final int COUNT = 3;

		private AppleStage() {}
	}

	private final int growth = GrowthCraftApples.getConfig().appleGrowthRate;
	private final boolean dropRipeApples = GrowthCraftApples.getConfig().dropRipeApples;
	private final int dropChance = GrowthCraftApples.getConfig().appleDropChance;

	public BlockApple()
	{
		super(Material.plants);
		this.setTickRandomly(true);
		this.setHardness(0.2F);
		this.setResistance(5.0F);
		this.setStepSound(soundTypeWood);
		this.setUnlocalizedName("grc.appleBlock");
		this.setCreativeTab(null);
	}

	public boolean isMature(IBlockAccess world, BlockPos pos)
	{
		final int meta = world.getBlockMetadata(pos);
		return meta >= AppleStage.MATURE;
	}

	public float getGrowthProgress(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return (float)meta / (float)AppleStage.MATURE;
	}

	void incrementGrowth(World world, BlockPos pos, int meta)
	{
		world.setBlockMetadataWithNotify(pos, meta + 1, BlockFlags.SYNC);
		AppleCore.announceGrowthTick(this, world, pos, meta);
	}

	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient)
	{
		return world.getBlockMetadata(pos) < AppleStage.MATURE;
	}

	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state)
	{
		return true;
	}

	/* Apply bonemeal effect */
	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state)
	{
		incrementGrowth(world, pos, world.getBlockState(pos));
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		if (!this.canBlockStay(world, pos))
		{
			fellBlockAsItem(world, pos);
		}
		else
		{
			final Event.Result allowGrowthResult = AppleCore.validateGrowthTick(this, world, pos, random);
			if (allowGrowthResult == Event.Result.DENY)
				return;

			final boolean continueGrowth = random.nextInt(this.growth) == 0;
			if (allowGrowthResult == Event.Result.ALLOW || continueGrowth)
			{
				final int meta = world.getBlockMetadata(pos);
				if (meta < AppleStage.MATURE)
				{
					incrementGrowth(world, pos, meta);
				}
				else if (dropRipeApples && world.rand.nextInt(this.dropChance) == 0)
				{
					fellBlockAsItem(world, pos);
				}
			}
		}
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (world.getBlockMetadata(pos) >= AppleStage.MATURE)
		{
			if (!world.isRemote)
			{
				fellBlockAsItem(world, pos);
			}
			return true;
		}
		return false;
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block block)
	{
		if (!this.canBlockStay(world, pos))
		{
			fellBlockAsItem(world, pos);
		}
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos)
	{
		return GrowthCraftApples.appleLeaves.equals(world.getBlockState(pos.up()).getBlock()) &&
			(world.getBlockMetadata(pos.up()) & 3) == 0;
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return Items.apple;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune)
	{
		return meta >= AppleStage.MATURE ? Items.apple : null;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	@Override
	public void dropBlockAsItemWithChance(World world, BlockPos pos, int par5, float par6, int par7)
	{
		super.dropBlockAsItemWithChance(world, pos, par5, par6, 0);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, BlockPos pos)
	{
		this.setBlockBoundsBasedOnState(world, pos);
		return super.getCollisionBoundingBoxFromPool(world, pos);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, BlockPos pos)
	{
		this.setBlockBoundsBasedOnState(world, pos);
		return super.getSelectedBoundingBoxFromPool(world, pos);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
	{
		final int meta = world.getBlockMetadata(pos);
		final float f = 0.0625F;

		if (meta == AppleStage.YOUNG)
		{
			this.setBlockBounds(6*f, 11*f, 6*f, 10*f, 15*f, 10*f);
		}
		else if (meta == AppleStage.MID)
		{
			this.setBlockBounds((float)(5.5*f), 10*f, (float)(5.5*f), (float)(10.5*f), 15*f, (float)(10.5*f));
		}
		else
		{
			this.setBlockBounds(5*f, 9*f, 5*f, 11*f, 15*f, 11*f);
		}
	}
}
