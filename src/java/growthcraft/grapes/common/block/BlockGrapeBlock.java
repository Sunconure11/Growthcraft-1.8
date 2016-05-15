package growthcraft.grapes.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import growthcraft.cellar.common.item.EnumYeast;
import growthcraft.core.common.block.GrcBlockBase;
import growthcraft.grapes.GrowthCraftGrapes;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGrapeBlock extends GrcBlockBase
{
	protected int bayanusDropRarity = GrowthCraftGrapes.getConfig().bayanusDropRarity;
	protected int grapesDropMin = GrowthCraftGrapes.getConfig().grapesDropMin;
	protected int grapesDropMax = GrowthCraftGrapes.getConfig().grapesDropMax;

	public BlockGrapeBlock()
	{
		super(Material.plants);
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		setUnlocalizedName("grc.grapeBlock");
		setBlockBounds(0.1875F, 0.5F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
		setCreativeTab(null);
	}

	public boolean canBlockStay(World world, BlockPos pos)
	{
		final IBlockState state = world.getBlockState(pos.up());
		return GrowthCraftGrapes.blocks.grapeLeaves.equals(state.getBlock());
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		super.updateTick(world, pos, state, random);
		if (!canBlockStay(world, pos))
		{
			fellBlockAsItem(world, pos, state);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player, EnumFacing dir, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote)
		{
			fellBlockAsItem(world, pos);
		}
		return true;
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block block)
	{
		if (!this.canBlockStay(world, pos))
		{
			fellBlockAsItem(world, pos);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return GrowthCraftGrapes.items.grapes.getItem();
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return false;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune)
	{
		return GrowthCraftGrapes.items.grapes.getItem();
	}

	@Override
	public int quantityDropped(Random random)
	{
		return grapesDropMin + random.nextInt(grapesDropMax - grapesDropMin);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		final List<ItemStack> ret = new ArrayList<ItemStack>();
		final int count = quantityDropped(state, fortune, world.rand);
		for(int i = 0; i < count; ++i)
		{
			final Item item = getItemDropped(state, world.rand, fortune);
			if (item != null)
			{
				ret.add(new ItemStack(item, 1, damageDropped(state)));
			}
			if (world.rand.nextInt(bayanusDropRarity) == 0)
			{
				ret.add(EnumYeast.BAYANUS.asStack(1));
			}
		}
		return ret;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
}
