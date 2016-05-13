package growthcraft.grapes.common.block;

import java.util.ArrayList;
import java.util.Random;

import growthcraft.cellar.common.item.EnumYeast;
import growthcraft.core.common.block.GrcBlockBase;
import growthcraft.grapes.GrowthCraftGrapes;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
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
		setBlockName("grc.grapeBlock");
		setBlockBounds(0.1875F, 0.5F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
		setCreativeTab(null);
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		super.updateTick(world, pos, random);
		if (!this.canBlockStay(world, pos))
		{
			fellBlockAsItem(world, pos);
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player, int dir, float par7, float par8, float par9)
	{
		if (!world.isRemote)
		{
			fellBlockAsItem(world, pos);
		}
		return true;
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, Block par5)
	{
		if (!this.canBlockStay(world, pos))
		{
			fellBlockAsItem(world, pos);
		}
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos)
	{
		final IBlockState state = world.getBlockState(pos.up());
		return GrowthCraftGrapes.blocks.grapeLeaves.equals(state.getBlock());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return GrowthCraftGrapes.items.grapes.getItem();
	}

	@Override
	public boolean canSilkHarvest(World world, EntityPlayer player, BlockPos pos, int metadata)
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
	public ArrayList<ItemStack> getDrops(World world, BlockPos pos, int metadata, int fortune)
	{
		final ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		final int count = quantityDropped(metadata, fortune, world.rand);
		for(int i = 0; i < count; ++i)
		{
			final Item item = getItemDropped(metadata, world.rand, fortune);
			if (item != null)
			{
				ret.add(new ItemStack(item, 1, damageDropped(metadata)));
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

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, BlockPos pos)
	{
		return null;
	}
}
