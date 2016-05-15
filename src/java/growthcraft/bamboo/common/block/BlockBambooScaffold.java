package growthcraft.bamboo.common.block;

import java.util.Random;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.core.common.block.GrcBlockBase;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBambooScaffold extends GrcBlockBase
{
	public BlockBambooScaffold()
	{
		super(Material.wood);
		setStepSound(soundTypeWood);
		setResistance(0.2F);
		setHardness(0.5F);
		setUnlocalizedName("grc.bambooScaffold");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		super.updateTick(world, pos, state, random);
		onNeighborBlockChange(world, pos, state, null);
	}

	public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player, int meta, float float7, float float8, float float9)
	{
		final ItemStack itemstack = player.inventory.getCurrentItem();
		if (itemstack != null)
		{
			if (itemstack.getItem() == Item.getItemFromBlock(this))
			{
				final int loop = world.getActualHeight();
				for (int j = y + 1; j < loop; j++)
				{
					final Block block = world.getBlock(x, j, z);
					if ((block == null) || (world.isAirBlock(x, j, z)) || (block.isReplaceable(world, x, j, z)))
					{
						if (!world.isRemote)
						{
							if (world.setBlock(x, j, z, this, 0, 3) && !player.capabilities.isCreativeMode)
							{
								itemstack.stackSize -= 1;
							}
							return true;
						}
					}
					if (block != this)
					{
						return false;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, Block par5)
	{
		if (!this.canBlockStay(world, pos))
		{
			this.dropBlockAsItem(world, pos, world.getBlockMetadata(pos), 0);
			world.setBlock(pos, Blocks.air, 0, 3);
		}
	}

	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, Entity entity)
	{
		entity.fallDistance = 0.0F;
		if (entity.isCollidedHorizontally)
		{
			entity.motionY = 0.2D;
		}
		else if (entity.isSneaking())
		{
			final double d = entity.prevPosY - entity.posY;
			entity.boundingBox.minY += d;
			entity.boundingBox.maxY += d;
			entity.posY = entity.prevPosY;
		}
		else
		{
			entity.motionY = -0.1D;
		}
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos)
	{
		return canBlockStay(world, pos);
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos)
	{
		if (world.getBlock(x, y -1 , z).isSideSolid(world, x, y - 1, z, EnumFacing.UP)) return true;
		if (checkSides(world, pos)) return true;

		return false;
	}

	private boolean checkSides(World world, BlockPos pos)
	{
		final boolean flag = world.getBlock(x + 1, y, z) == this;
		final boolean flag1 = world.getBlock(x - 1, y, z) == this;
		final boolean flag2 = world.getBlock(pos + 1) == this;
		final boolean flag3 = world.getBlock(pos - 1) == this;

		if (!flag && !flag1 && !flag2 && !flag3) return false;

		if (flag && world.getBlock(x + 1, y - 1, z).isSideSolid(world, x + 1, y - 1, z, EnumFacing.UP)) return true;
		if (flag1 && world.getBlock(x - 1, y - 1, z).isSideSolid(world, x - 1, y - 1, z, EnumFacing.UP)) return true;
		if (flag2 && world.getBlock(x, y - 1, z + 1).isSideSolid(world, x, y - 1, z + 1, EnumFacing.UP)) return true;
		if (flag3 && world.getBlock(x, y - 1, z - 1).isSideSolid(world, x, y - 1, z - 1, EnumFacing.UP)) return true;

		if (flag && world.getBlock(x + 2, y - 1, z).isSideSolid(world, x + 2, y - 1, z, EnumFacing.UP)) return true;
		if (flag1 && world.getBlock(x - 2, y - 1, z).isSideSolid(world, x - 2, y - 1, z, EnumFacing.UP)) return true;
		if (flag2 && world.getBlock(x, y - 1, z + 2).isSideSolid(world, x, y - 1, z + 2, EnumFacing.UP)) return true;
		if (flag3 && world.getBlock(x, y - 1, z - 2).isSideSolid(world, x, y - 1, z - 2, EnumFacing.UP)) return true;

		return false;
	}

	/************
	 * STUFF
	 ************/

	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return EnumFacing.UP == side;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, int side)
	{
		return true;
	}

	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, BlockPos pos)
	{
		final float f = 0.125F;
		return AxisAlignedBB.getBoundingBox(x + this.minX + f, y + this.minY, z + this.minZ + f, x + this.maxX - f, y + this.maxY, z + this.maxZ - f);
	}
}
