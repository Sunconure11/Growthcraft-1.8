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
package growthcraft.cellar.common.block;

import javax.annotation.Nonnull;

import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.CellarGuiType;
import growthcraft.core.common.block.GrcBlockContainer;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Base class for Cellar machines and the like
 */
public abstract class BlockCellarContainer extends GrcBlockContainer
{
	protected CellarGuiType guiType = CellarGuiType.NONE;

	public BlockCellarContainer(Material material)
	{
		super(material);
	}


	protected BlockCellarContainer setGuiType(@Nonnull CellarGuiType type)
	{
		this.guiType = type;
		return this;
	}

	protected boolean openGui(EntityPlayer player, World world, BlockPos pos)
	{
		if (guiType != CellarGuiType.NONE)
		{
			player.openGui(GrowthCraftCellar.instance, guiType.ordinal(), world, pos);
			return true;
		}
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (super.onBlockActivated(world, pos, player, facing, hitX, hitY, hitZ)) return true;
		return !player.isSneaking() && openGui(player, world, pos);
	}
}
