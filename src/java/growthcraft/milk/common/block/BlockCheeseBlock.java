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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import growthcraft.api.core.util.BBox;
import growthcraft.core.common.block.GrcBlockContainer;
import growthcraft.milk.common.item.EnumCheeseType;
import growthcraft.milk.common.item.EnumCheeseStage;
import growthcraft.milk.common.item.ItemBlockCheeseBlock;
import growthcraft.milk.common.tileentity.TileEntityCheeseBlock;
import growthcraft.milk.GrowthCraftMilk;
import growthcraft.core.util.ItemUtils;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCheeseBlock extends GrcBlockContainer
{
	public BlockCheeseBlock()
	{
		super(Material.cake);
		setHardness(0.5F);
		setStepSound(soundTypeCloth);
		setBlockName("grcmilk.CheeseBlock");
		setCreativeTab(GrowthCraftMilk.creativeTab);
		setTileEntityType(TileEntityCheeseBlock.class);
		final BBox bb = BBox.newCube(4f, 0f, 4f, 8f, 8f, 8f).scale(1f / 16f);
		setBlockBounds(bb.x0(), bb.y0(), bb.z0(), bb.x1(), bb.y1(), bb.z1());
	}

	@Override
	protected boolean shouldRestoreBlockState(World world, BlockPos pos, ItemStack stack)
	{
		return true;
	}

	@Override
	protected boolean shouldDropTileStack(World world, BlockPos pos, int metadata, int fortune)
	{
		return false;
	}

	@Override
	protected ItemStack createHarvestedBlockItemStack(World world, EntityPlayer player, BlockPos pos, int meta)
	{
		final TileEntityCheeseBlock te = getTileEntity(world, x, y, z);
		if (te != null)
		{
			return te.asItemStack();
		}
		return new ItemStack(this, 1, meta);
	}

	@Override
	protected void getTileItemStackDrops(List<ItemStack> ret, World world, BlockPos pos, int metadata, int fortune)
	{
		final TileEntityCheeseBlock te = getTileEntity(world, x, y, z);
		if (te != null)
		{
			ret.add(te.asItemStack());
		}
		else
		{
			super.getTileItemStackDrops(ret, world, x, y, z, metadata, fortune);
		}
	}

	@Override
	protected boolean shouldScatterInventoryOnBreak(World world, BlockPos pos)
	{
		return true;
	}

	@Override
	protected void scatterInventory(World world, BlockPos pos, Block block)
	{
		final TileEntityCheeseBlock te = getTileEntity(world, x, y, z);
		if (te != null)
		{
			final List<ItemStack> drops = new ArrayList<ItemStack>();
			te.populateDrops(drops);
			for (ItemStack stack : drops)
			{
				ItemUtils.spawnItemStack(world, x, y, z, stack, rand);
			}
		}
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
	{
		final TileEntityCheeseBlock teCheeseBlock = getTileEntity(world, x, y, z);
		if (teCheeseBlock != null)
		{
			return teCheeseBlock.asItemStack();
		}
		return super.getPickBlock(target, world, x, y, z, player);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, BlockPos pos, int metadata, int fortune)
	{
		final ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		final TileEntityCheeseBlock te = getTileEntity(world, x, y, z);
		if (te != null)
		{
			te.populateDrops(ret);
		}
		return ret;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		if (item instanceof ItemBlockCheeseBlock)
		{
			final ItemBlockCheeseBlock ib = (ItemBlockCheeseBlock)item;
			for (EnumCheeseType cheese : EnumCheeseType.VALUES)
			{
				if (cheese.hasBlock())
				{
					final ItemStack stack = new ItemStack(item, 1, cheese.meta);
					// This causes the NBT data to refresh
					ib.getTileTagCompound(stack);
					list.add(stack);
				}
			}
		}
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, int side)
	{
		return true;
	}

	@Override
	public int damageDropped(int metadata)
	{
		return metadata;
	}
}
