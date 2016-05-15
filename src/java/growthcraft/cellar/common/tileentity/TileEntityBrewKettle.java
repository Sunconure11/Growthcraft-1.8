package growthcraft.cellar.common.tileentity;

import java.util.Arrays;
import java.util.List;
import java.io.IOException;

import io.netty.buffer.ByteBuf;

import growthcraft.api.core.fluids.FluidUtils;
import growthcraft.cellar.common.fluids.CellarTank;
import growthcraft.cellar.common.tileentity.device.BrewKettle;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.inventory.GrcInternalInventory;
import growthcraft.core.common.tileentity.event.EventHandler;
import growthcraft.core.common.tileentity.ITileHeatedDevice;
import growthcraft.core.common.tileentity.ITileProgressiveDevice;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileEntityBrewKettle extends TileEntityCellarDevice implements ITickable, ITileHeatedDevice, ITileProgressiveDevice
{
	public static enum BrewKettleDataID
	{
		TIME,
		TIME_MAX,
		TANK1_FLUID_ID,
		TANK1_FLUID_AMOUNT,
		TANK2_FLUID_ID,
		TANK2_FLUID_AMOUNT,
		HEAT_AMOUNT;

		public static final List<BrewKettleDataID> VALUES = Arrays.asList(values());
	}

	private static final int[] rawSlotIDs = new int[] {0, 1};
	private static final int[] residueSlotIDs = new int[] {0};

	private BrewKettle brewKettle = new BrewKettle(this, 0, 1, 0, 1);

	@Override
	protected FluidTank[] createTanks()
	{
		final int maxCap = GrowthCraftCellar.getConfig().brewKettleMaxCap;
		return new FluidTank[] {
			new CellarTank(maxCap, this),
			new CellarTank(maxCap, this)
		};
	}

	@Override
	protected GrcInternalInventory createInventory()
	{
		return new GrcInternalInventory(this, 2);
	}

	protected void markForFluidUpdate()
	{
		// Brew Kettles need to update their rendering state when a fluid
		// changes, most of the other cellar blocks are unaffected by this
		markForBlockUpdate();
	}

	@Override
	public String getDefaultInventoryName()
	{
		return "container.grc.brewKettle";
	}

	@Override
	public float getDeviceProgress()
	{
		return brewKettle.getProgress();
	}

	@Override
	public int getDeviceProgressScaled(int scale)
	{
		return brewKettle.getProgressScaled(scale);
	}

	@Override
	public void update()
	{
		if (!worldObj.isRemote)
		{
			brewKettle.update();
		}
	}

	@Override
	public int getHeatScaled(int range)
	{
		return (int)(MathHelper.clamp_float(brewKettle.getHeatMultiplier(), 0.0f, 1.0f) * range);
	}

	@Override
	public boolean isHeated()
	{
		return brewKettle.isHeated();
	}

	@Override
	public float getHeatMultiplier()
	{
		return brewKettle.getHeatMultiplier();
	}

	public boolean canBrew()
	{
		return brewKettle.canBrew();
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		// 0 = raw
		// 1 = residue
		return EnumFacing.DOWN == side ? rawSlotIDs : residueSlotIDs;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing side)
	{
		return EnumFacing.DOWN != side || index == 1;
	}

	/************
	 * NBT
	 ************/
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		if (nbt.hasKey("time"))
		{
			// Pre 2.5
			brewKettle.setTime(nbt.getShort("time"));
			brewKettle.setGrain(nbt.getFloat("grain"));
		}
		else
		{
			brewKettle.readFromNBT(nbt, "brew_kettle");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		brewKettle.writeToNBT(nbt, "brew_kettle");
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_READ)
	public boolean readFromStream_BrewKettle(ByteBuf stream) throws IOException
	{
		brewKettle.readFromStream(stream);
		return false;
	}

	@EventHandler(type=EventHandler.EventType.NETWORK_WRITE)
	public boolean writeToStream_BrewKettle(ByteBuf stream) throws IOException
	{
		brewKettle.writeToStream(stream);
		return false;
	}

	/************
	 * PACKETS
	 ************/

	/**
	 * @param id - data id
	 * @param v - value
	 */
	@Override
	public void receiveGUINetworkData(int id, int v)
	{
		super.receiveGUINetworkData(id, v);
		final BrewKettleDataID dataId = BrewKettleDataID.VALUES.get(id);
		switch (dataId)
		{
			case TIME:
				brewKettle.setTime(v);
				break;
			case TIME_MAX:
				brewKettle.setTimeMax(v);
				break;
			case TANK1_FLUID_ID:
			{
				final FluidStack result = FluidUtils.replaceFluidStack(v, getFluidStack(0));
				if (result != null) getFluidTank(0).setFluid(result);
			}	break;
			case TANK1_FLUID_AMOUNT:
				getFluidTank(0).setFluid(FluidUtils.updateFluidStackAmount(getFluidStack(0), v));
				break;
			case TANK2_FLUID_ID:
			{
				final FluidStack result = FluidUtils.replaceFluidStack(v, getFluidStack(1));
				if (result != null) getFluidTank(1).setFluid(result);
			}	break;
			case TANK2_FLUID_AMOUNT:
				getFluidTank(1).setFluid(FluidUtils.updateFluidStackAmount(getFluidStack(1), v));
				break;
			case HEAT_AMOUNT:
				brewKettle.setHeatMultiplier((float)v / (float)0x7FFF);
				break;
			default:
				break;
		}
	}

	@Override
	public void sendGUINetworkData(Container container, ICrafting iCrafting)
	{
		super.sendGUINetworkData(container, iCrafting);
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TIME.ordinal(), (int)brewKettle.getTime());
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TIME_MAX.ordinal(), (int)brewKettle.getTimeMax());
		FluidStack fluid = getFluidStack(0);
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TANK1_FLUID_ID.ordinal(), fluid != null ? fluid.getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TANK1_FLUID_AMOUNT.ordinal(), fluid != null ? fluid.amount : 0);
		fluid = getFluidStack(1);
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TANK2_FLUID_ID.ordinal(), fluid != null ? fluid.getFluidID() : 0);
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.TANK2_FLUID_AMOUNT.ordinal(), fluid != null ? fluid.amount : 0);
		iCrafting.sendProgressBarUpdate(container, BrewKettleDataID.HEAT_AMOUNT.ordinal(), (int)(brewKettle.getHeatMultiplier() * 0x7FFF));
	}

	@Override
	protected int doFill(EnumFacing from, FluidStack resource, boolean shouldFill)
	{
		return fillFluidTank(0, resource, shouldFill);
	}

	@Override
	protected FluidStack doDrain(EnumFacing from, int maxDrain, boolean shouldDrain)
	{
		return drainFluidTank(1, maxDrain, shouldDrain);
	}

	@Override
	protected FluidStack doDrain(EnumFacing from, FluidStack stack, boolean shouldDrain)
	{
		if (stack == null || !stack.isFluidEqual(getFluidStack(1)))
		{
			return null;
		}
		return doDrain(from, stack.amount, shouldDrain);
	}

	public void switchTanks()
	{
		FluidStack f0 = null;
		FluidStack f1 = null;
		if (this.getFluidStack(0) != null)
		{
			f0 = this.getFluidStack(0).copy();
		}
		if (this.getFluidStack(1) != null)
		{
			f1 = this.getFluidStack(1).copy();
		}
		this.clearTank(0);
		this.clearTank(1);
		this.getFluidTank(0).fill(f1, true);
		this.getFluidTank(1).fill(f0, true);

		markForBlockUpdate();
	}
}
