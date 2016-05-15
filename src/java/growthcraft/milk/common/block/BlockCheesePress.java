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

import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.common.block.GrcBlockContainer;
import growthcraft.milk.common.tileentity.TileEntityCheesePress;
import growthcraft.milk.GrowthCraftMilk;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCheesePress extends GrcBlockContainer
{
	public BlockCheesePress()
	{
		super(Material.wood);
		setResistance(5.0F);
		setHardness(2.0F);
		setStepSound(soundTypeWood);
		setUnlocalizedName("grcmilk.CheesePress");
		setCreativeTab(GrowthCraftMilk.creativeTab);
		setTileEntityType(TileEntityCheesePress.class);
	}

	@Override
	public boolean isRotatable(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return true;
	}

	@Override
	public void doRotateBlock(World world, BlockPos pos, EnumFacing side)
	{
		//world.setBlockMetadataWithNotify(pos, world.getBlockMetadata(pos) ^ 1, BlockFlags.SYNC);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos)
	{
		super.onBlockAdded(world, pos);
		this.setDefaultDirection(world, pos);
	}

	private void setDefaultDirection(World world, BlockPos pos)
	{
		if (!world.isRemote)
		{
			final Block block = world.getBlockState(pos.north()).getBlock();
			final Block block1 = world.getBlockState(pos.south()).getBlock();
			final Block block2 = world.getBlockState(pos.west()).getBlock();
			final Block block3 = world.getBlockState(pos.east()).getBlock();
			byte meta = 3;

			if (block.func_149730_j() && !block1.func_149730_j())
			{
				meta = 3;
			}

			if (block1.func_149730_j() && !block.func_149730_j())
			{
				meta = 2;
			}

			if (block2.func_149730_j() && !block3.func_149730_j())
			{
				meta = 5;
			}

			if (block3.func_149730_j() && !block2.func_149730_j())
			{
				meta = 4;
			}

			//world.setBlockMetadataWithNotify(pos, meta, BlockFlags.UPDATE_AND_SYNC);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, EntityLivingBase entity, ItemStack stack)
	{
		super.onBlockPlacedBy(world, pos, entity, stack);
		//final int a = MathHelper.floor_double((entity.rotationYaw * 4.0D / 360.0D) + 0.5D) & 3;
		//if (a == 0 || a == 2)
		//{
		//	world.setBlockMetadataWithNotify(pos, 0, BlockFlags.SYNC);
		//}
		//else if (a == 1 || a == 3)
		//{
		//	world.setBlockMetadataWithNotify(pos, 1, BlockFlags.SYNC);
		//}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (super.onBlockActivated(world, pos, state, player, hitX, hitY, hitZ)) return true;
		if (GrowthCraftMilk.getConfig().cheesePressHandOperated)
		{
			if (!player.isSneaking())
			{
				final TileEntityCheesePress cheesePress = getTileEntity(world, pos);
				if (cheesePress != null)
				{
					if (cheesePress.toggle())
					{
						world.playSoundEffect((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), "random.wood_click", 0.3f, 0.5f);
					}
					return true;
				}
			}
		}
		return false;
	}

	private void updatePressState(World world, BlockPos pos)
	{
		final int isPowered = world.isBlockIndirectlyGettingPowered(pos);
		final TileEntityCheesePress cheesePress = getTileEntity(world, pos);
		if (cheesePress != null)
		{
			if (cheesePress.toggle(isPowered > 0))
			{
				world.playSoundEffect((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), "random.wood_click", 0.3f, 0.5f);
			}
		}
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block block)
	{
		super.onNeighborBlockChange(world, pos, state, block);
		if (!world.isRemote)
		{
			if (GrowthCraftMilk.getConfig().cheesePressRedstoneOperated)
			{
				updatePressState(world, pos);
			}
		}
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

	@Override
	public boolean hasComparatorInputOverride()
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, BlockPos pos)
	{
		final TileEntityCheesePress te = getTileEntity(world, pos);
		if (te != null)
		{
			return Container.calcRedstoneFromInventory(te);
		}
		return 0;
	}
}
