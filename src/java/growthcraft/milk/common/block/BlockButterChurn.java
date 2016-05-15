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
package growthcraft.milk.common.block;

import growthcraft.core.common.block.GrcBlockContainer;
import growthcraft.api.core.util.BBox;
import growthcraft.milk.common.tileentity.TileEntityButterChurn;
import growthcraft.milk.GrowthCraftMilk;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockButterChurn extends GrcBlockContainer
{
	public BlockButterChurn()
	{
		super(Material.wood);
		setResistance(5.0F);
		setHardness(2.0F);
		setUnlocalizedName("grcmilk.ButterChurn");
		setStepSound(soundTypeWood);
		setCreativeTab(GrowthCraftMilk.creativeTab);
		setTileEntityType(TileEntityButterChurn.class);
		final BBox bb = BBox.newCube(4f, 0f, 4f, 8f, 16f, 8f).scale(1f / 16f);
		setBlockBounds(bb.x0(), bb.y0(), bb.z0(), bb.x1(), bb.y1(), bb.z1());
	}

	private boolean tryChurning(World world, BlockPos pos, EntityPlayer player)
	{
		final TileEntityButterChurn butterChurn = getTileEntity(world, pos);
		if (butterChurn != null)
		{
			switch (butterChurn.doWork())
			{
				case CHURN:
				case PRODUCE:
					return true;
				case NONE:
				default:
					break;
			}
		}
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (super.onBlockActivated(world, pos, player, meta, hitX, hitY, hitZ)) return true;
		if (!player.isSneaking())
		{
			if (tryChurning(world, pos, player)) return true;
		}
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing facing)
	{
		return true;
	}
}
