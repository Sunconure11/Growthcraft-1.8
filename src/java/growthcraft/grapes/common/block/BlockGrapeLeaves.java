package growthcraft.grapes.common.block;

import java.util.Random;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.common.block.IBlockRope;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.util.BlockCheck;
import growthcraft.grapes.GrowthCraftGrapes;
import growthcraft.grapes.util.GrapeBlockCheck;

import net.minecraft.block.BlockLeavesBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGrapeLeaves extends BlockLeavesBase implements IBlockRope
{
	private final int grapeLeavesGrowthRate = GrowthCraftGrapes.getConfig().grapeLeavesGrowthRate;
	private final int grapeSpawnRate = GrowthCraftGrapes.getConfig().grapeSpawnRate;
	// how far can a grape leaf grow before it requires support from a trunk
	private final int grapeVineSupportedLength = GrowthCraftGrapes.getConfig().grapeVineSupportedLength;

	public BlockGrapeLeaves()
	{
		super(Material.leaves, false);
		setTickRandomly(true);
		setHardness(0.2F);
		setLightOpacity(1);
		setStepSound(soundTypeGrass);
		setBlockName("grc.grapeLeaves");
		setCreativeTab(null);
	}

	private boolean isTrunk(World world, BlockPos pos)
	{
		return GrapeBlockCheck.isGrapeVineTrunk(world.getBlockState(pos));
	}

	public boolean isSupportedByTrunk(World world, BlockPos pos)
	{
		return isTrunk(world, pos.down());
	}

	/**
	 * Use this method to check if the block can grow outwards on a rope
	 *
	 * @param world - the world
	 * @param x - x coord
	 * @param y - y coord
	 * @param z - z coord
	 * @return true if the block can grow here, false otherwise
	 */
	public boolean canGrowOutwardsOnRope(World world, BlockPos pos)
	{
		if (BlockCheck.isRope(world.getBlock(pos.west()))) return true;
		if (BlockCheck.isRope(world.getBlock(pos.east()))) return true;
		if (BlockCheck.isRope(world.getBlock(pos.north()))) return true;
		if (BlockCheck.isRope(world.getBlock(pos.south()))) return true;
		return false;
	}

	public boolean canGrowOutwards(World world, BlockPos pos)
	{
		final boolean leavesTotheSouth = world.getBlock(pos.south()) == this;
		final boolean leavesToTheNorth = world.getBlock(pos.north()) == this;
		final boolean leavesToTheEast = world.getBlock(pos.east()) == this;
		final boolean leavesToTheWest = world.getBlock(pos.west()) == this;

		if (!leavesTotheSouth && !leavesToTheNorth && !leavesToTheEast && !leavesToTheWest) return false;

		for (int i = 1; i <= grapeVineSupportedLength; ++i)
		{
			final BlockPos down = pos.down();
			if (leavesTotheSouth && isTrunk(world, down.south(i))) return true;
			if (leavesToTheNorth && isTrunk(world, down.north(i))) return true;
			if (leavesToTheEast && isTrunk(world, down.east(i))) return true;
			if (leavesToTheWest && isTrunk(world, down.west(i))) return true;
		}
		return false;
	}

	/**
	 * Variation of canGrowOutwards, use this method to check rope blocks
	 *
	 * @param world - the world
	 * @param x - x coord
	 * @param y - y coord
	 * @param z - z coord
	 * @return true if the block can grow here, false otherwise
	 */
	public boolean canGrowHere(World world, BlockPos pos)
	{
		if (BlockCheck.isRope(world.getBlock(pos)))
		{
			return canGrowOutwards(world, pos);
		}
		return false;
	}

	private void setGrapeBlock(World world, BlockPos pos)
	{
		world.setBlock(pos, GrowthCraftGrapes.blocks.grapeBlock.getBlock(), 0, BlockFlags.UPDATE_AND_SYNC);
	}

	public boolean growGrapeBlock(World world, BlockPos pos)
	{
		final BlockPos dpos = pos.down();
		if (world.isAirBlock(dpos))
		{
			if (!world.isRemote)
			{
				setGrapeBlock(world, dpos);
			}
			return true;
		}
		return false;
	}

	private void grow(World world, BlockPos pos, Random random)
	{
		final BlockPos dpos = pos.down();
		if (world.isAirBlock(dpos) && (random.nextInt(this.grapeSpawnRate) == 0))
		{
			setGrapeBlock(world, dpos);
		}

		if (world.rand.nextInt(this.grapeLeavesGrowthRate) == 0)
		{
			if (canGrowOutwards(world, pos))
			{
				final EnumFacing dir = BlockCheck.DIR4[random.nextInt(4)];

				if (canGrowHere(world, x + dir.offsetX, y, z + dir.offsetZ))
				{
					world.setBlock(x + dir.offsetX, y, z + dir.offsetZ, this, 0, BlockFlags.UPDATE_AND_SYNC);
				}
			}
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		super.updateTick(world, pos, state, random);
		if (!this.canBlockStay(world, pos))
		{
			world.setBlock(pos, GrowthCraftCore.blocks.ropeBlock.getBlock());
		}
		else
		{
			grow(world, pos, random);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, Random random)
	{
		if (world.canLightningStrikeAt(pos.up()) && !World.doesBlockHaveSolidTopSurface(world, pos.down()) && random.nextInt(15) == 1)
		{
			final double d0 = (double)((float)x + random.nextFloat());
			final double d1 = (double)y - 0.05D;
			final double d2 = (double)((float)z + random.nextFloat());
			world.spawnParticle("dripWater", d0, d1, d2, 0.0D, 0.0D, 0.0D);
		}
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean canBlockStay(World world, BlockPos pos)
	{
		if (this.isSupportedByTrunk(world, pos))
		{
			return true;
		}
		else
		{
			for (EnumFacing dir : BlockCheck.DIR4)
			{
				for (int i = 1; i <= grapeVineSupportedLength; ++i)
				{
					final int bx = x + dir.offsetX * i;
					final int bz = z + dir.offsetZ * i;
					final IBlockState state = world.getBlock(new BlockPos(bx, y, bz));
					if (state == null || state.getBlock() != this)
					{
						break;
					}
					else if (isSupportedByTrunk(world, bx, y, bz))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return GrowthCraftGrapes.items.grapeSeeds.getItem();
	}

	@Override
	public boolean isLeaves(IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, BlockPos pos, int metadata)
	{
		return false;
	}

	@Override
	public boolean canConnectRopeTo(IBlockAccess world, BlockPos pos)
	{
		if (world.getBlock(pos) instanceof IBlockRope)
		{
			return true;
		}
		return false;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune)
	{
		return GrowthCraftCore.items.rope.getItem();
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return Blocks.leaves.isOpaqueCube();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, int side)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBlockColor()
	{
		final double d0 = 0.5D;
		final double d1 = 1.0D;
		return ColorizerFoliage.getFoliageColor(d0, d1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderColor(int meta)
	{
		return ColorizerFoliage.getFoliageColorBasic();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, BlockPos pos)
	{
		final int meta = world.getBlockMetadata(pos);

		int r = 0;
		int g = 0;
		int b = 0;

		for (int l1 = -1; l1 <= 1; ++l1)
		{
			for (int i2 = -1; i2 <= 1; ++i2)
			{
				final BlockPos leafPos = new BlockPos(x + i2, y, z + l1);
				final int j2 = world.getBiomeGenForCoords(x + i2, z + l1).getBiomeFoliageColor(leafPos);
				r += (j2 & 16711680) >> 16;
				g += (j2 & 65280) >> 8;
				b += j2 & 255;
			}
		}

		return (r / 9 & 255) << 16 | (g / 9 & 255) << 8 | b / 9 & 255;
	}
}
