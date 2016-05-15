/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015, 2016 IceDragon200
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
package growthcraft.core.common.tileentity;

import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.inventory.IInventoryFlagging;
import growthcraft.core.common.inventory.IInventoryWatcher;
import growthcraft.core.common.inventory.InventoryProcessor;
import growthcraft.core.util.ItemUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

/**
 * Extend this base class if you want a Tile with an `Inventory`
 */
public abstract class GrcTileEntityInventoryBase extends GrcTileEntityCommonBase implements ISidedInventory, ICustomDisplayName, IInventoryWatcher, IInventoryFlagging, ILockableContainer
{
	protected static final int[] NO_SLOTS = new int[]{};

	protected String inventoryName;
	protected GrcInternalInventory inventory;
    private LockCode code = LockCode.EMPTY_CODE;

	public GrcTileEntityInventoryBase()
	{
		super();
		this.inventory = createInventory();
	}

	protected GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 0);
	}

	public String getDefaultInventoryName()
	{
		return "grc.inventory.name";
	}

	@Override
	public void onInventoryChanged(IInventory inv, int index)
	{
		markForInventoryUpdate();
	}

	@Override
	public void onItemDiscarded(IInventory inv, ItemStack stack, int index, int discardedAmount)
	{
		final ItemStack discarded = stack.copy();
		discarded.stackSize = discardedAmount;
		ItemUtils.spawnItemStack(worldObj, pos.getX(), pos.getY(), pos.getZ(), discarded, worldObj.rand);
	}

    @Override
    public boolean isLocked()
    {
        return this.code != null && !this.code.isEmpty();
    }

    @Override
    public LockCode getLockCode()
    {
        return this.code;
    }

    @Override
    public void setLockCode(LockCode code)
    {
        this.code = code;
    }

	@Override
    public IChatComponent getDisplayName()
    {
        return (IChatComponent)(this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]));
    }

	@Override
	public boolean hasCustomName()
	{
		return inventoryName != null && inventoryName.length() > 0;
	}

	@Override
	public void setGuiDisplayName(String string)
	{
		this.inventoryName = string;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inventory.getStackInSlot(index);
	}

	public ItemStack tryMergeItemIntoSlot(ItemStack itemstack, int index)
	{
		final ItemStack result = ItemUtils.mergeStacksBang(getStackInSlot(index), itemstack);
		if (result != null)
		{
			inventory.setInventorySlotContents(index, result);
		}
		return result;
	}

	// Attempts to merge the given itemstack into the main slot
	public ItemStack tryMergeItemIntoMainSlot(ItemStack itemstack)
	{
		return tryMergeItemIntoSlot(itemstack, 0);
	}

	@Override
	public ItemStack decrStackSize(int index, int par2)
	{
		return inventory.decrStackSize(index, par2);
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return inventory.removeStackFromSlot(index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack itemstack)
	{
		inventory.setInventorySlotContents(index, itemstack);
	}

	@Override
	public int getInventoryStackLimit()
	{
		return inventory.getInventoryStackLimit();
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.getSizeInventory();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		if (worldObj.getTileEntity(pos) != this)
		{
			return false;
		}
		return player.getDistanceSq((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		return inventory.isItemValidForSlot(slot, itemstack);
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing side)
	{
		return InventoryProcessor.instance().canInsertItem(this, stack, slot);
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side)
	{
		return InventoryProcessor.instance().canExtractItem(this, stack, slot);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return NO_SLOTS;
	}

	@Override
	public void clear()
	{
		inventory.clear();
	}

	@Override
	public int getField(int id)
	{
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
	}

	@Override
	public int getFieldCount()
	{
		return 0;
	}

	private void readInventoryFromNBT(NBTTagCompound compound)
	{
		if (compound.hasKey("items"))
		{
			inventory.readFromNBT(compound, "items");
		}
		else if (compound.hasKey("inventory"))
		{
			inventory.readFromNBT(compound, "inventory");
		}
	}

	private void readInventoryNameFromNBT(NBTTagCompound compound)
	{
		if (compound.hasKey("name"))
		{
			this.inventoryName = compound.getString("name");
		}
		else if (compound.hasKey("inventory_name"))
		{
			this.inventoryName = compound.getString("inventory_name");
		}
	}

	private void readLockCodeFromNBT(NBTTagCompound compound)
	{
        this.code = LockCode.fromNBT(compound);
	}

	@Override
	public void readFromNBTForItem(NBTTagCompound compound)
	{
		super.readFromNBTForItem(compound);
		readInventoryFromNBT(compound);
		// Do not reload the inventory name from NBT, allow the ItemStack to do that
		//readInventoryNameFromNBT(compound);
		readLockCodeFromNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		readInventoryFromNBT(compound);
		readInventoryNameFromNBT(compound);
		readLockCodeFromNBT(compound);
	}

	private void writeInventoryToNBT(NBTTagCompound compound)
	{
		inventory.writeToNBT(compound, "inventory");
		// NAME
		if (hasCustomName())
		{
			compound.setString("inventory_name", inventoryName);
		}
		compound.setInteger("inventory_tile_version", 3);
	}

	private void writeLockCodeToNBT(NBTTagCompound compound)
	{
		if (code != null)
        {
            code.toNBT(compound);
        }
	}

	@Override
	public void writeToNBTForItem(NBTTagCompound compound)
	{
		super.writeToNBTForItem(compound);
		writeInventoryToNBT(compound);
		writeLockCodeToNBT(compound);
	}

	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		writeInventoryToNBT(compound);
		writeLockCodeToNBT(compound);
	}
}
