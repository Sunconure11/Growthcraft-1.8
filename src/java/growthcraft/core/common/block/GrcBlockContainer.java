/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 IceDragon200
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package growthcraft.core.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;

import growthcraft.api.core.nbt.INBTItemSerializable;
import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.common.item.IItemTileBlock;
import growthcraft.core.common.tileentity.ICustomDisplayName;
import growthcraft.core.common.tileentity.IItemHandler;
import growthcraft.core.GrowthCraftCore;
import growthcraft.core.util.ItemUtils;
import growthcraft.core.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * Base class for machines and the like
 */
public abstract class GrcBlockContainer extends GrcBlockBase implements IDroppableBlock, IRotatableBlock, IWrenchable, ITileEntityProvider
{
	public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 15);

	protected Random rand = new Random();
	protected Class<? extends TileEntity> tileEntityType;

	public GrcBlockContainer(@Nonnull Material material)
	{
		super(material);
		this.isBlockContainer = true;
	}

	@Override
	public boolean onBlockEventReceived(World world, BlockPos pos, IBlockState state, int eventID, int eventParam)
	{
		super.onBlockEventReceived(world, pos, state, eventID, eventParam);
		final TileEntity te = getTileEntity(world, pos);
		return te != null ? te.receiveClientEvent(eventID, eventParam) : false;
	}

	protected void setTileEntityType(Class<? extends TileEntity> klass)
	{
		this.tileEntityType = klass;
	}

	@SuppressWarnings("unchecked")
	public <T extends TileEntity> T getTileEntity(IBlockAccess world, BlockPos pos)
	{
		final TileEntity te = world.getTileEntity(pos);
		if (te != null)
		{
			if (tileEntityType.isInstance(te))
			{
				return (T)te;
			}
			else
			{
				// warn
			}
		}
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int unused)
	{
		if (tileEntityType != null)
		{
			try
			{
				return tileEntityType.newInstance();
			}
			catch (InstantiationException e)
			{
				throw new IllegalStateException("Failed to create a new instance of an illegal class " + this.tileEntityType, e);
			}
			catch (IllegalAccessException e)
			{
				throw new IllegalStateException("Failed to create a new instance of " + this.tileEntityType + ", because lack of permissions", e);
			}
		}
		return null;
	}

	/* IRotatableBlock */
	@Override
	public boolean isRotatable(IBlockAccess world, BlockPos pos, EnumFacing axis)
	{
		return false;
	}

	public void doRotateBlock(World world, BlockPos pos, EnumFacing axis)
	{
		final IBlockState state = world.getBlockState(pos);
		final int rotation = state.getValue(ROTATION);
		final EnumFacing current = EnumFacing.getFront(rotation);
		EnumFacing newDirection = current;
		if (current == axis)
		{
			newDirection = current.rotateY();
		}
		else
		{
			newDirection = current.rotateAround(axis.getAxis());
		}
		if (newDirection != current)
		{
			world.setBlockState(pos, state.withProperty(ROTATION, newDirection.ordinal()), BlockFlags.UPDATE_AND_SYNC);
		}
	}

	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
	{
		if (isRotatable(world, pos, axis))
		{
			doRotateBlock(world, pos, axis);
			world.markBlockForUpdate(pos);
			return true;
		}
		return false;
	}

	public boolean wrenchBlock(World world, BlockPos pos, EntityPlayer player, ItemStack wrench)
	{
		if (player != null)
		{
			if (ItemUtils.canWrench(wrench, player, pos))
			{
				if (player.isSneaking())
				{
					if (!world.isRemote)
					{
						fellBlockAsItem(world, pos);
						ItemUtils.wrenchUsed(wrench, player, pos);
					}
					return true;
				}
			}
		}
		return false;
	}

	public boolean tryWrenchItem(EntityPlayer player, World world, BlockPos pos)
	{
		if (player == null) return false;
		final ItemStack is = player.inventory.getCurrentItem();
		return wrenchBlock(world, pos, player, is);
	}

	protected void placeBlockByEntityDirection(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack)
	{
		final int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		EnumFacing facing = EnumFacing.NORTH;

		if (l == 0) facing = EnumFacing.SOUTH;
		else if (l == 1) facing = EnumFacing.WEST;
		else if (l == 2) facing = EnumFacing.NORTH;
		else if (l == 3) facing = EnumFacing.EAST;

		if (isRotatable(world, pos, EnumFacing.UP))
		{
			world.setBlockState(pos, state.withProperty(ROTATION, facing.ordinal()), BlockFlags.SYNC);
		}
	}

	protected void setupCustomDisplayName(World world, BlockPos pos, ItemStack stack)
	{
		if (stack.hasDisplayName())
		{
			final TileEntity te = getTileEntity(world, pos);
			if (te instanceof ICustomDisplayName)
			{
				((ICustomDisplayName)te).setGuiDisplayName(stack.getDisplayName());
			}
		}
	}

	protected NBTTagCompound getTileTagCompound(World world, BlockPos pos, ItemStack stack)
	{
		final Item item = stack.getItem();
		if (item instanceof IItemTileBlock)
		{
			final IItemTileBlock itb = (IItemTileBlock)item;
			return itb.getTileTagCompound(stack);
		}
		else
		{
			GrowthCraftCore.getLogger().error("Cannot get tile tag compound for a non IItemTileBlock: stack=%s block=%s", stack, this);
		}
		return null;
	}

	protected void setTileTagCompound(World world, BlockPos pos, ItemStack stack, NBTTagCompound tag)
	{
		final Item item = stack.getItem();
		if (item instanceof IItemTileBlock)
		{
			final IItemTileBlock itb = (IItemTileBlock)item;
			itb.setTileTagCompound(stack, tag);
		}
		else
		{
			GrowthCraftCore.getLogger().error("Cannot set tile tag compound for a non IItemTileBlock: stack=%s block=%s", stack, this);
		}
	}

	protected boolean shouldRestoreBlockState(World world, BlockPos pos, IBlockState state, ItemStack stack)
	{
		return false;
	}

	protected void restoreBlockStateFromStack(World world, BlockPos pos, IBlockState state, ItemStack stack)
	{
		if (shouldRestoreBlockState(world, pos, state, stack))
		{
			final TileEntity te = getTileEntity(world, pos);
			if (te instanceof INBTItemSerializable)
			{
				final NBTTagCompound tag = getTileTagCompound(world, pos, stack);
				if (tag != null)
				{
					((INBTItemSerializable)te).readFromNBTForItem(tag);
				}
			}
			else
			{
				GrowthCraftCore.getLogger().error("Cannot restore tile from stack, the TileEntity does not support INBTItemSerializable: stack=%s block=%s tile=%s", stack, this, te);
			}
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		super.onBlockPlacedBy(world, pos, state, placer, stack);
		restoreBlockStateFromStack(world, pos, state, stack);
		setupCustomDisplayName(world, pos, stack);
	}

	protected void scatterInventory(World world, BlockPos pos, Block block)
	{
		final TileEntity te = getTileEntity(world, pos);
		if (te instanceof IInventory)
		{
			final IInventory inventory = (IInventory)te;
			if (inventory != null)
			{
				for (int index = 0; index < inventory.getSizeInventory(); ++index)
				{
					final ItemStack stack = inventory.getStackInSlot(index);
					ItemUtils.spawnItemStack(world, pos, stack, rand);
				}
				world.updateComparatorOutputLevel(pos, block);
			}
		}
	}

	protected boolean shouldScatterInventoryOnBreak(World world, BlockPos pos)
	{
		return true;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, Block block, int meta)
	{
		if (shouldScatterInventoryOnBreak(world, pos))
			scatterInventory(world, pos, block);
		world.removeTileEntity(pos);
	}

	protected ItemStack createHarvestedBlockItemStack(World world, EntityPlayer player, BlockPos pos, IBlockState state)
	{
		return createStackedBlock(state);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te)
	{
		player.addStat(StatList.mineBlockStatArray[getIdFromBlock(this)], 1);
		player.addExhaustion(0.025F);

		if (this.canSilkHarvest(world, pos, state, player) && EnchantmentHelper.getSilkTouchModifier(player))
		{
			final ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			final ItemStack itemstack = createHarvestedBlockItemStack(world, player, pos, state);

			if (itemstack != null)
			{
				items.add(itemstack);
			}

			ForgeEventFactory.fireBlockHarvesting(items, world, this, pos, world.getBlockState(pos), 0, 1.0f, true, player);
			for (ItemStack is : items)
			{
				GrowthCraftCore.getLogger().error("Unimplemented harvestBlock dropBlockAsItem(World, BlockPos, IBlockState, ItemStack)");
				//dropBlockAsItem(world, pos, state, fortune);
			}
		}
		else
		{
			harvesters.set(player);
			final int fortune = EnchantmentHelper.getFortuneModifier(player);
			dropBlockAsItem(world, pos, state, fortune);
			harvesters.set(null);
		}
	}

	protected boolean shouldDropTileStack(World world, BlockPos pos, IBlockState state, int fortune)
	{
		return false;
	}

	private void getDefaultDrops(List<ItemStack> ret, World world, BlockPos pos, IBlockState state, int fortune)
	{
		final int count = quantityDropped(state, fortune, world.rand);
		for (int i = 0; i < count; ++i)
		{
			final Item item = getItemDropped(state, world.rand, fortune);
			if (item != null)
			{
				ret.add(new ItemStack(item, 1, damageDropped(state)));
			}
		}
	}

	protected void getTileItemStackDrops(List<ItemStack> ret, World world, BlockPos pos, IBlockState state, int fortune)
	{
		final TileEntity te = getTileEntity(world, pos);
		if (te instanceof INBTItemSerializable)
		{
			final NBTTagCompound tag = new NBTTagCompound();
			((INBTItemSerializable)te).writeToNBTForItem(tag);
			final ItemStack stack = new ItemStack(this, 1, 0);
			setTileTagCompound(world, pos, stack, tag);
			ret.add(stack);
		}
		else
		{
			getDefaultDrops(ret, world, pos, state, fortune);
		}
	}

	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		final List<ItemStack> ret = new ArrayList<ItemStack>();
		if (shouldDropTileStack(world, pos, state, fortune))
		{
			getTileItemStackDrops(ret, world, pos, state, fortune);
		}
		else
		{
			getDefaultDrops(ret, world, pos, state, fortune);
		}
		return ret;
	}

	protected boolean playerFillTank(World world, BlockPos pos, IFluidHandler fh, ItemStack is, EntityPlayer player)
	{
		return Utils.playerFillTank(world, pos, fh, is, player);
	}

	protected boolean playerDrainTank(World world, BlockPos pos, IFluidHandler fh, ItemStack is, EntityPlayer player)
	{
		final FluidStack fs = Utils.playerDrainTank(world, pos, fh, is, player);
		return fs != null && fs.amount > 0;
	}

	private boolean handleIFluidHandler(World world, BlockPos pos, EntityPlayer player, int meta)
	{
		final TileEntity te = world.getTileEntity(pos);
		if (te instanceof IFluidHandler)
		{
			if (world.isRemote)
			{
				return true;
			}
			else
			{
				final IFluidHandler fh = (IFluidHandler)te;
				final ItemStack is = player.inventory.getCurrentItem();

				boolean needUpdate = false;

				if (!player.isSneaking())
				{
					// While not sneaking, draining is given priority
					if (playerDrainTank(world, pos, fh, is, player) ||
						playerFillTank(world, pos, fh, is, player)) needUpdate = true;
				}
				else
				{
					// Otherwise filling is given priority
					if (playerFillTank(world, pos, fh, is, player) ||
						playerDrainTank(world, pos, fh, is, player)) needUpdate = true;
				}
				if (needUpdate)
				{
					world.markBlockForUpdate(pos);
					return true;
				}
			}
		}
		return false;
	}

	private boolean handleOnUseItem(World world, BlockPos pos, EntityPlayer player, int meta)
	{
		final TileEntity te = world.getTileEntity(pos);
		if (te instanceof IItemHandler)
		{
			if (world.isRemote)
			{
				return true;
			}
			else
			{
				final IItemHandler ih = (IItemHandler)te;
				final ItemStack is = player.inventory.getCurrentItem();

				boolean needUpdate = false;
				if (ih.tryPlaceItem(player, is))
				{
					needUpdate = true;
				}
				else if (ih.tryTakeItem(player, is))
				{
					needUpdate = true;
				}

				if (needUpdate)
				{
					world.markBlockForUpdate(pos);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (tryWrenchItem(player, world, pos)) return true;
		if (handleIFluidHandler(world, pos, player, meta)) return true;
		if (handleOnUseItem(world, pos, player, meta)) return true;
		return false;
	}
}
