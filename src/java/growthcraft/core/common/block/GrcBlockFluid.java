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

import java.util.Random;

import growthcraft.api.core.util.FXHelper;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GrcBlockFluid extends BlockFluidClassic
{
	private int color = 0xFFFFFF;

	public GrcBlockFluid(Fluid fluid, Material material)
	{
		super(fluid, material);
		setUnlocalizedName(fluid.getUnlocalizedName());
	}

	public GrcBlockFluid refreshSettings()
	{
		setDensity(definedFluid.getDensity());
		return this;
	}

	public GrcBlockFluid refreshLight()
	{
		// http://stackoverflow.com/questions/596216/formula-to-determine-brightness-of-rgb-color
		final float lum = 0.2126f * (((color >> 16) & 0xFF) / 255.0f) +
			0.7152f * (((color >> 8) & 0xFF) / 255.0f) +
			0.0722f * ((color & 0xFF) / 255.0f);
		setLightOpacity((int)((1f - lum) * 15));
		return this;
	}

	public GrcBlockFluid setColor(int kolor)
	{
		this.color = kolor;
		return this;
	}

	public int getColor()
	{
		return this.color;
	}

	@Override
	public boolean canDisplace(IBlockAccess world, BlockPos pos)
	{
		final IBlockState state = world.getBlockState(pos);
		if (state.getBlock().getMaterial().isLiquid()) return false;
		return super.canDisplace(world, pos);
	}

	@Override
	public boolean displaceIfPossible(World world, BlockPos pos)
	{
		final IBlockState state = world.getBlockState(pos);
		if (state.getBlock().getMaterial().isLiquid()) return false;
		return super.displaceIfPossible(world, pos);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess world, BlockPos pos, int renderPass)
	{
		return color;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand)
	{
		super.randomDisplayTick(world, pos, rand);

		final IBlockState belowState = world.getBlockState(pos.down(2));
		if (rand.nextInt(10) == 0 &&
			World.doesBlockHaveSolidTopSurface(world, pos.down()) &&
			!belowState.getBlock().getMaterial().blocksMovement())
		{
			final double px = pos.getX() + rand.nextFloat();
			final double py = pos.getY() - 1.05D;
			final double pz = pos.getZ() + rand.nextFloat();
			FXHelper.dropParticle(world, px, py, pz, color);
		}
	}

	@Override
	public boolean canDropFromExplosion(Explosion explosion)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
}
