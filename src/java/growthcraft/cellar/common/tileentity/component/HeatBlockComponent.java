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
package growthcraft.cellar.common.tileentity.component;

import growthcraft.api.cellar.CellarRegistry;
import growthcraft.api.cellar.heatsource.IHeatSourceBlock;
import growthcraft.core.util.BlockCheck;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Component for handling heat source blocks for a tile entity
 */
public class HeatBlockComponent
{
	private TileEntity tileEntity;
	private EnumFacing sourceDir = EnumFacing.DOWN;
	// Adjacent heating allows the block to accept heat from blocks on the same y axis and directly
	// adjacent to it
	private float adjacentHeating;

	/**
	 * @param te - parent tile entity
	 * @param adh - adjacent heating rate, set to 0 to disable
	 */
	public HeatBlockComponent(TileEntity te, float adh)
	{
		this.tileEntity = te;
		this.adjacentHeating = adh;
	}

	private World getWorld()
	{
		return tileEntity.getWorld();
	}

	public float getHeatMultiplierFromDir(EnumFacing dir)
	{
		final BlockPos pos = tileEntity.getPos().offset(dir);
		final IBlockState state = getWorld().getBlockState(pos);
		final IHeatSourceBlock heatSource = CellarRegistry.instance().heatSource().getHeatSource(state.getBlock(), 0);

		if (heatSource != null) return heatSource.getHeat(getWorld(), pos);
		return 0.0f;
	}

	public float getHeatMultiplierForAdjacent()
	{
		if (adjacentHeating > 0)
		{
			float heat = 0.0f;
			for (EnumFacing dir : BlockCheck.DIR4)
			{
				heat += getHeatMultiplierFromDir(dir);
			}
			return heat * adjacentHeating;
		}
		return 0.0f;
	}

	public float getHeatMultiplier()
	{
		return getHeatMultiplierFromDir(sourceDir) + getHeatMultiplierForAdjacent();
	}
}
