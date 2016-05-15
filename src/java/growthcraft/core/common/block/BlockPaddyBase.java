package growthcraft.core.common.block;

import java.util.List;
import java.util.Random;

import growthcraft.core.util.ItemUtils;
import growthcraft.api.core.util.BlockFlags;

import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockPaddyBase extends GrcBlockBase implements IPaddy
{
	public static final PropertyInteger FLUID_LEVEL = PropertyInteger.create("fluid_level", 0, 15);

	public BlockPaddyBase(Material material)
	{
		super(material);
		setTickRandomly(true);
		setDefaultState(blockState.getBaseState().withProperty(FLUID_LEVEL, 0));
	}

	@Override
	public int getPaddyMaxFluidLevel(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return 15;
	}

	@Override
	public boolean isFilledWithFluid(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return state.getValue(FLUID_LEVEL) >= getPaddyMaxFluidLevel(world, pos, state);
	}

	public void drainPaddy(World world, BlockPos pos, IBlockState state)
	{
		final int level = state.getValue(FLUID_LEVEL);
		if (level > 0)
		{
			world.setBlockState(pos, state.withProperty(FLUID_LEVEL, level - 1), BlockFlags.UPDATE_AND_SYNC);
		}
	}

	public void fillPaddy(World world, BlockPos pos, IBlockState state)
	{
		final int level = state.getValue(FLUID_LEVEL);
		if (level < getPaddyMaxFluidLevel(world. pos, state) - 1)
		{
			world.setBlockState(pos, state.withProperty(FLUID_LEVEL,level + 1), BlockFlags.UPDATE_AND_SYNC);
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		if (isBelowFillingFluid(world, pos, state) && world.canLightningStrikeAt(x, y + 1, z))
		{
			fillPaddy(world, pos, state);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			return true;
		}
		else
		{
			final ItemStack itemstack = player.inventory.getCurrentItem();
			if (itemstack != null)
			{
				if (FluidContainerRegistry.isFilledContainer(itemstack))
				{
					final FluidStack fillerFluidStack = FluidContainerRegistry.getFluidForFilledItem(itemstack);
					if (fillerFluidStack != null)
					{
						int radius = fillerFluidStack.amount * 2 / FluidContainerRegistry.BUCKET_VOLUME;
						if (radius % 2 == 0)
						{
							radius -= 1;
						}

						if (fillerFluidStack.getFluid() == getFillingFluid() && radius > 0)
						{
							for (int lx = pos.getX() - radius; lx <= pos.getX() + radius; ++lx)
							{
								for (int lz = pos.getZ() - radius; lz <= pos.getZ() + radius; ++lz)
								{
									final BlockPos fillPos = new BlockPos(lx, pos.getY(), lz);
									final IBlockState fillState = world.getBlockState(fillPos);
									if (fillState.getBlock() == this)
									{
										fillPaddy(world, fillPos, state);
									}
								}
							}

							if (!player.capabilities.isCreativeMode)
							{
								player.inventory.setInventorySlotContents(player.inventory.currentItem, ItemUtils.consumeStack(itemstack));
							}

							return true;
						}
					}
				}
			}

			return false;
		}
	}

	@Override
	public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance)
	{
		if (!world.isRemote && world.rand.nextFloat() < fallDistance - 0.5F)
		{
			if (!(entity instanceof EntityPlayer) && !world.getGameRules().getGameRuleBooleanValue("mobGriefing"))
			{
				return;
			}

			final Block plant = world.getBlock(pos.up());
			if (plant instanceof IPaddyCrop)
			{
				plant.dropBlockAsItem(world, pos.up(), world.getBlockMetadata(pos.up()), 0);
				world.setBlockToAir(pos.up());
			}
		}
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block block)
	{
		super.onNeighborBlockChange(world, pos, state, block);
		if (isBelowFillingFluid(world, pos, state))
		{
			fillPaddy(world, pos, state);
		}
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return EnumFacing.UP != side;
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass()
	{
		return 1;
	}

	/************
	 * BOXES
	 ************/
	@Override
	public void setBlockBoundsForItemRender()
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public boolean canConnectPaddyTo(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		final IBlockState newState = world.getBlockState(pos);
		if (this.equals(newState.getBlock()))
		{
			final int meta = newState.getValue(FLUID_LEVEL);
			final int m = state.getValue(FLUID_LEVEL);
			if ((m == 0 && meta == 0) || (m > 0 && meta > 0))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity entity)
	{
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.875F, 1.0F);
		super.addCollisionBoxesToList(world, pos, state, mask, list, entity);

		final float thick = 0.125F;
		final float j1 = 0.875F;
		final float j2 = 1.0F;
		float i1 = 0.0F;
		float i2 = 1.0F;
		float k1 = 0.0F;
		float k2 = 1.0F;

		final boolean boolXPos = canConnectPaddyTo(world, pos.east(), state);
		final boolean boolXNeg = canConnectPaddyTo(world, pos.west(), state);
		final boolean boolYPos = canConnectPaddyTo(world, pos.south(), state);
		final boolean boolYNeg = canConnectPaddyTo(world, pos.north(), state);

		if (!boolXPos)
		{
			i1 = 1.0F - thick;
			i2 = 1.0F;
			k1 = 0.0F + thick;
			k2 = 1.0F - thick;

			this.setBlockBounds(i1, j1, k1, i2, j2, k2);
			super.addCollisionBoxesToList(world, pos, state, mask, list, entity);
		}

		if (!boolXNeg)
		{
			i1 = 0.0F;
			i2 = 0.0F + thick;
			k1 = 0.0F + thick;
			k2 = 1.0F - thick;

			this.setBlockBounds(i1, j1, k1, i2, j2, k2);
			super.addCollisionBoxesToList(world, pos, state, mask, list, entity);
		}

		if (!boolYPos)
		{
			i1 = 0.0F + thick;
			i2 = 1.0F - thick;
			k1 = 1.0F - thick;
			k2 = 1.0F;

			this.setBlockBounds(i1, j1, k1, i2, j2, k2);
			super.addCollisionBoxesToList(world, pos, state, mask, list, entity);
		}

		if (!boolYNeg)
		{
			i1 = 0.0F + thick;
			i2 = 1.0F - thick;
			k1 = 0.0F;
			k2 = 0.0F + thick;

			this.setBlockBounds(i1, j1, k1, i2, j2, k2);
			super.addCollisionBoxesToList(world, pos, state, mask, list, entity);
		}

		//corners
		if ((!canConnectPaddyTo(world, pos.north().west(), state)) || (!boolXNeg) || (!boolYNeg))
		{
			this.setBlockBounds(0.0F, j1, 0.0F, 0.0F + thick, j2, 0.0F + thick);
			super.addCollisionBoxesToList(world, pos, state, mask, list, entity);
		}

		if ((!canConnectPaddyTo(world, pos.north().east(), state)) || (!boolXPos) || (!boolYNeg))
		{
			this.setBlockBounds(1.0F - thick, j1, 0.0F, 1.0F, j2, 0.0F + thick);
			super.addCollisionBoxesToList(world, pos, state, mask, list, entity);
		}

		if ((!canConnectPaddyTo(world, pos.south().west(), state)) || (!boolXNeg) || (!boolYPos))
		{
			this.setBlockBounds(0.0F, j1, 1.0F - thick, 0.0F + thick, j2, 1.0F);
			super.addCollisionBoxesToList(world, pos, state, mask, list, entity);
		}

		if ((!canConnectPaddyTo(world, pos.south().east(), state)) || (!boolXPos) || (!boolYPos))
		{
			this.setBlockBounds(1.0F - thick, j1, 1.0F - thick, 1.0F, j2, 1.0F);
			super.addCollisionBoxesToList(world, pos, state, mask, list, entity);
		}

		this.setBlockBoundsForItemRender();
	}
}
