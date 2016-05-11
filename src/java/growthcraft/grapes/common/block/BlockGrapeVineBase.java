package growthcraft.grapes.common.block;

import java.util.Random;

import growthcraft.core.common.block.ICropDataProvider;
import growthcraft.core.integration.AppleCore;
import growthcraft.core.util.BlockCheck;
import growthcraft.api.core.util.BlockFlags;
import growthcraft.grapes.util.GrapeBlockCheck;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.eventhandler.Event;

public abstract class BlockGrapeVineBase extends Block implements IPlantable, ICropDataProvider, IGrowable
{
	private ItemStack itemDrop;
	private float growthRateMultiplier;

	public BlockGrapeVineBase()
	{
		super(Material.plants);
		this.itemDrop = new ItemStack((Item)null, 0);
		this.growthRateMultiplier = 1.0f;
	}

	public void setItemDrop(ItemStack itemstack)
	{
		this.itemDrop = itemstack;
	}

	public ItemStack getItemDrop()
	{
		return this.itemDrop;
	}

	@Override
	public Item getItemDropped(int meta, Random par2Random, int par3)
	{
		return itemDrop.getItem();
	}

	@Override
	public int quantityDropped(Random random)
	{
		return itemDrop.stackSize;
	}

	public void setGrowthRateMultiplier(float rate)
	{
		this.growthRateMultiplier = rate;
	}

	public float getGrowthRateMultiplier()
	{
		return this.growthRateMultiplier;
	}

	public int getGrowthMax()
	{
		return 1;
	}

	public float getGrowthProgress(IBlockAccess world, BlockPos pos, int meta)
	{
		return (float)meta / (float)getGrowthMax();
	}

	protected boolean isGrapeVine(Block block)
	{
		return GrapeBlockCheck.isGrapeVine(block);
	}

	public void incrementGrowth(World world, BlockPos pos, int meta)
	{
		world.setBlockMetadataWithNotify(pos, meta + 1, BlockFlags.SYNC);
		AppleCore.announceGrowthTick(this, world, pos, meta);
	}

	protected float getGrowthRate(World world, BlockPos pos)
	{
		final Block l = world.getBlockState(pos.north());
		final Block i1 = world.getBlockState(pos.south());
		final Block j1 = world.getBlockState(pos.west());
		final Block k1 = world.getBlockState(pos.east());
		final Block l1 = world.getBlockState(pos.west().north());
		final Block i2 = world.getBlockState(pos.east().north());
		final Block j2 = world.getBlockState(pos.east().south());
		final Block k2 = world.getBlockState(pos.west().south());
		final boolean flag = this.isGrapeVine(j1) || this.isGrapeVine(k1);
		final boolean flag1 = this.isGrapeVine(l) || this.isGrapeVine(i1);
		final boolean flag2 = this.isGrapeVine(l1) || this.isGrapeVine(i2) || this.isGrapeVine(j2) || this.isGrapeVine(k2);
		float f = 1.0F;

		for (int l2 = x - 1; l2 <= x + 1; ++l2)
		{
			for (int i3 = z - 1; i3 <= z + 1; ++i3)
			{
				final Block block = world.getBlock(l2, y - 1, i3);
				float f1 = 0.0F;

				if (block != null && block == Blocks.farmland)
				{
					f1 = 1.0F;

					if (block.isFertile(world, l2, y - 1, i3))
					{
						f1 = 3.0F;
					}
				}

				if (l2 != x || i3 != z)
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

	@Override
	public boolean canBlockStay(World world, BlockPos pos)
	{
		return BlockCheck.canSustainPlant(world, pos.down(), EnumFacing.UP, this);
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, Block par5)
	{
		if (!this.canBlockStay(world, pos))
		{
			this.dropBlockAsItem(world, pos, world.getBlockMetadata(pos), 0);
			world.setBlock(pos, Blocks.air, 0, BlockFlags.UPDATE_AND_SYNC);
		}
	}

	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, BlockPos pos, int metadata)
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	/************
	 * IPLANTABLE
	 ************/
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
	{
		return EnumPlantType.Crop;
	}

	@Override
	public Block getPlant(IBlockAccess world, BlockPos pos)
	{
		return this;
	}

	@Override
	public int getPlantMetadata(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockMetadata(pos);
	}

	/**
	 * If all conditions have passed, do plant growth
	 *
	 * @param world - world with block
	 * @param x - x coord
	 * @param y - y coord
	 * @param z - z coord
	 * @param meta - block metadata
	 */
	protected abstract void doGrowth(World world, BlockPos pos, int meta);

	/**
	 * Are the conditions right for this plant to grow?
	 *
	 * @param world - world with block
	 * @param x - x coord
	 * @param y - y coord
	 * @param z - z coord
	 * @return true, it can grow, false otherwise
	 */
	protected abstract boolean canUpdateGrowth(World world, BlockPos pos);

	@Override
	public void updateTick(World world, BlockPos pos, Random random)
	{
		super.updateTick(world, pos, random);
		if (canUpdateGrowth(world, pos))
		{
			final Event.Result allowGrowthResult = AppleCore.validateGrowthTick(this, world, pos, random);
			if (Event.Result.DENY == allowGrowthResult)
				return;

			final int meta = world.getBlockMetadata(pos);
			final float f = this.getGrowthRate(world, pos);

			final boolean continueGrowth = random.nextInt((int)(getGrowthRateMultiplier() / f) + 1) == 0;
			if (Event.Result.ALLOW == allowGrowthResult || continueGrowth)
			{
				doGrowth(world, pos, meta);
			}
		}
	}

	/* Can this accept bonemeal? */
	@Override
	public boolean func_149851_a(World world, BlockPos pos, boolean isClient)
	{
		return canUpdateGrowth(world, pos);
	}

	/* SideOnly(Side.SERVER) Can this apply bonemeal effect? */
	@Override
	public boolean func_149852_a(World world, Random random, BlockPos pos)
	{
		return true;
	}

	/* Apply bonemeal effect */
	@Override
	public void func_149853_b(World world, Random random, BlockPos pos)
	{
		if (random.nextFloat() < 0.5D)
		{
			doGrowth(world, pos, world.getBlockMetadata(pos));
		}
	}
}
