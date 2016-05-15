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
package growthcraft.rice.util;

import growthcraft.rice.GrowthCraftRice;
import growthcraft.core.common.block.BlockPaddyBase;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class RiceBlockCheck
{
	private RiceBlockCheck() {}

	/**
	 * Determines if block is a normal dirt paddy block
	 *
	 * @param block - the block to check
	 * @return true if the block is a Paddy, false otherwise
	 */
	public static boolean isPaddy(IBlockState state)
	{
		if (state != null)
		{
			return GrowthCraftRice.paddyField.equals(state.getBlock());
		}
		return false;
	}

	/**
	 * Determines if the block at the location is a paddy
	 *
	 * @param world - world to check the block
	 * @param pos - block position
	 * @return true, the block is a paddy, false otherwise
	 */
	public static boolean isPaddy(IBlockAccess world, BlockPos pos)
	{
		final IBlockState state = world.getBlockState(pos);
		return isPaddy(state);
	}

	/**
	 * Determines if the block at the location is a paddy (with water)
	 *
	 * @param world - world to check the block
	 * @param pos - block position
	 * @param amount - metadata, how much water should be present
	 * @return true, the block is a paddy, false otherwise
	 */
	public static boolean isPaddyWithWater(IBlockAccess world, BlockPos pos, int amount)
	{
		final IBlockState state = world.getBlockState(pos);
		if (isPaddy(state))
		{
			return state.getValue(BlockPaddyBase.FLUID_LEVEL) >= amount;
		}
		return false;
	}
}
