package growthcraft.cellar.common.block;

import java.util.Random;

import growthcraft.cellar.common.tileentity.TileEntityFruitPresser;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.core.common.block.IRotatableBlock;
import growthcraft.core.common.block.IWrenchable;
import growthcraft.api.core.util.BlockFlags;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFruitPresser extends BlockContainer implements IWrenchable, IRotatableBlock
{
	public BlockFruitPresser()
	{
		super(Material.piston);
		this.isBlockContainer = true;
		setHardness(0.5F);
		setStepSound(soundTypePiston);
		setBlockName("grc.fruitPresser");
		setCreativeTab(null);
		setBlockBounds(0.1875F, 0.0F, 0.1875F, 0.8125F, 0.9375F, 0.8125F);
	}

	public String getPressStateName(int meta)
	{
		switch(meta)
		{
			case 0:
			case 1:
				return "unpressed";
			case 2:
			case 3:
				return "pressed";
			default:
				return "invalid";
		}
	}

	/************
	 * TRIGGERS
	 ************/

	/* IRotatableBLock */
	public boolean isRotatable(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		final Block below = world.getBlock(x, y - 1, z);
		if (below instanceof IRotatableBlock)
		{
			return ((IRotatableBlock)below).isRotatable(world, x, y - 1, z, side);
		}
		return false;
	}

	public boolean rotateBlock(World world, BlockPos pos, EnumFacing side)
	{
		if (isRotatable(world, x, y, z, side))
		{
			final Block below = world.getBlock(x, y - 1, z);
			return below.rotateBlock(world, x, y - 1, z, side);
		}
		return false;
	}

	/* IWrenchable */
	@Override
	public boolean wrenchBlock(World world, BlockPos pos, EntityPlayer player, ItemStack wrench)
	{
		final Block below = world.getBlock(x, y - 1, z);
		if (below instanceof BlockFruitPress)
		{
			return ((BlockFruitPress)below).wrenchBlock(world, x, y - 1, z, player, wrench);
		}
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player, int meta, float par7, float par8, float par9)
	{
		if (world.isRemote) return true;
		final Block below = world.getBlock(x, y - 1, z);
		if (below instanceof BlockFruitPress)
		{
			return ((BlockFruitPress)below).tryWrenchItem(player, world, x, y - 1, z);
		}
		return false;
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos)
	{
		super.onBlockAdded(world, x, y, z);
		final int m = world.getBlockMetadata(x,  y - 1, z);
		world.setBlockMetadataWithNotify(x, y, z, m, BlockFlags.UPDATE_AND_SYNC);

		if (!world.isRemote)
		{
			this.updatePressState(world, x, y, z);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, EntityLivingBase entity, ItemStack stack)
	{
		final int m = world.getBlockMetadata(x,  y - 1, z);
		world.setBlockMetadataWithNotify(x, y, z, m, BlockFlags.UPDATE_AND_SYNC);

		if (!world.isRemote)
		{
			this.updatePressState(world, x, y, z);
		}
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, Block block)
	{
		if (!this.canBlockStay(world, x, y, z))
		{
			world.func_147480_a(x, y, z, true);
		}

		if (!world.isRemote)
		{
			this.updatePressState(world, x, y, z);
		}
	}

	private void updatePressState(World world, BlockPos pos)
	{
		final int     meta = world.getBlockMetadata(x, y, z);
		final boolean flag = world.isBlockIndirectlyGettingPowered(x, y, z);

		if (flag && (meta == 0 || meta == 1))
		{
			world.setBlockMetadataWithNotify(x, y, z, meta | 2, BlockFlags.UPDATE_AND_SYNC);
			world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "tile.piston.out", 0.5F, world.rand.nextFloat() * 0.25F + 0.6F);
		}
		else if (!flag && (meta == 2 || meta == 3))
		{
			world.setBlockMetadataWithNotify(x, y, z, meta & 1, BlockFlags.UPDATE_AND_SYNC);
			world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "tile.piston.in", 0.5F, world.rand.nextFloat() * 0.15F + 0.6F);
		}

		world.markBlockForUpdate(x, y, z);
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean canBlockStay(World world, BlockPos pos)
	{
		return GrowthCraftCellar.blocks.fruitPress.getBlock() == world.getBlock(x, y - 1, z);
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		final int meta = world.getBlockMetadata(x, y, z);

		if (meta == 0 || meta == 2)
		{
			return side == EnumFacing.EAST || side == EnumFacing.WEST;
		}
		else if (meta == 1 || meta == 3)
		{
			return side == EnumFacing.NORTH || side == EnumFacing.SOUTH;
		}

		return isNormalCube(world, x, y, z);
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return GrowthCraftCellar.blocks.fruitPress.getItem();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int par2)
	{
		return new TileEntityFruitPresser();
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune)
	{
		return null;
	}

	@Override
	public int quantityDropped(Random rand)
	{
		return 0;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, int side)
	{
		return true;
	}
}
