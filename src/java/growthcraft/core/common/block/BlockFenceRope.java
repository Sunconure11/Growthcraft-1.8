package growthcraft.core.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import growthcraft.api.core.util.BlockFlags;
import growthcraft.api.core.util.BlockKey;
import growthcraft.core.GrowthCraftCore;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFenceRope extends GrcBlockBase implements IBlockRope
{
	private BlockKey fenceBlockKey;

	public BlockFenceRope(BlockKey srcKey, String name)
	{
		super(srcKey.getBlock().getMaterial());
		this.fenceBlockKey = srcKey;
		setStepSound(soundTypeWood);
		setUnlocalizedName(name);
		setCreativeTab(null);
	}

	public BlockFenceRope(Block block, String name)
	{
		this(new BlockKey(block), name);
	}

	public Block getFenceBlock()
	{
		return fenceBlockKey.getBlock();
	}

	public int getFenceBlockMetadata()
	{
		return fenceBlockKey.getMetadata();
	}

	@Override
	public float getBlockHardness(World world, BlockPos pos)
	{
		return getFenceBlock().getBlockHardness(world, pos);
	}

	@Override
	public float getExplosionResistance(Entity entity)
	{
		return getFenceBlock().getExplosionResistance(entity);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (player.inventory.getCurrentItem() != null && GrowthCraftCore.items.rope.equals(player.inventory.getCurrentItem().getItem()))
		{
			return false;
		}
		else
		{
			if (!world.isRemote)
			{
				world.setBlock(pos, getFenceBlock(), getFenceBlockMetadata(), BlockFlags.UPDATE_AND_SYNC);
				this.dropBlockAsItem(world, pos, GrowthCraftCore.items.rope.asStack());
			}
		}
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return Item.getItemFromBlock(getFenceBlock());
	}

	@Override
	public boolean canConnectRopeTo(IBlockAccess world, BlockPos pos)
	{
		return world.getBlock(pos) instanceof IBlockRope;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune)
	{
		return null;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		final ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(getFenceBlock()));
		ret.add(GrowthCraftCore.items.rope.asStack());
		return ret;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
	{
		final boolean flag = this.canConnectRopeTo(world, pos.north());
		final boolean flag1 = this.canConnectRopeTo(world, pos.south());
		final boolean flag2 = this.canConnectRopeTo(world, pos.west());
		final boolean flag3 = this.canConnectRopeTo(world, pos.east());
		float f = 0.375F;
		float f1 = 0.625F;
		float f2 = 0.375F;
		float f3 = 0.625F;

		if (flag)
		{
			f2 = 0.0F;
		}

		if (flag1)
		{
			f3 = 1.0F;
		}

		if (flag2)
		{
			f = 0.0F;
		}

		if (flag3)
		{
			f1 = 1.0F;
		}

		this.setBlockBounds(f, 0.0F, f2, f1, 1.0F, f3);
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addCollisionBoxesToList(World world, BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> list, Entity entity)
	{
		final boolean flag = this.canConnectRopeTo(world, pos.north());
		final boolean flag1 = this.canConnectRopeTo(world, pos.south());
		final boolean flag2 = this.canConnectRopeTo(world, pos.west());
		final boolean flag3 = this.canConnectRopeTo(world, pos.east());
		float f = 0.375F;
		float f1 = 0.625F;
		float f2 = 0.375F;
		float f3 = 0.625F;

		if (flag)
		{
			f2 = 0.0F;
		}

		if (flag1)
		{
			f3 = 1.0F;
		}

		if (flag || flag1)
		{
			this.setBlockBounds(f, 0.4375F, f2, f1, 0.5625F, f3);
			super.addCollisionBoxesToList(world, pos, aabb, list, entity);
		}

		f2 = 0.375F;
		f3 = 0.625F;

		if (flag2)
		{
			f = 0.0F;
		}

		if (flag3)
		{
			f1 = 1.0F;
		}

		if (flag2 || flag3 || !flag && !flag1)
		{
			this.setBlockBounds(f, 0.4375F, f2, f1, 0.5625F, f3);
			super.addCollisionBoxesToList(world, pos, aabb, list, entity);
		}

		if (flag)
		{
			f2 = 0.0F;
		}

		if (flag1)
		{
			f3 = 1.0F;
		}

		this.setBlockBoundsBasedOnState(world, pos);
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
