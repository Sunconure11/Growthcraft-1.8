package growthcraft.bamboo.common.block;

import java.util.List;
import java.util.Random;

import growthcraft.bamboo.GrowthCraftBamboo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBambooSlab extends BlockSlab
{
	public BlockBambooSlab(boolean par2)
	{
		super(par2, Material.wood);
		this.useNeighborBrightness = true;
		setStepSound(soundTypeWood);
		setResistance(5.0F);
		setHardness(2.0F);
		setUnlocalizedName("grc.bambooSlab");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}

	/************
	 * STUFF
	 ************/
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		if (item != Item.getItemFromBlock(GrowthCraftBamboo.blocks.bambooDoubleSlab.getBlock()))
		{
			list.add(new ItemStack(item, 1, 0));
		}
	}

	@Override
	public String func_150002_b(int par1)
	{
		return super.getUnlocalizedName();
	}

	@SideOnly(Side.CLIENT)
	private static boolean isBlockSingleSlab(Block block)
	{
		return block == GrowthCraftBamboo.blocks.bambooSingleSlab.getBlock();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World par1World, int par2, int par3, int par4)
	{
		return Item.getItemFromBlock(GrowthCraftBamboo.blocks.bambooSingleSlab.getBlock());
	}

	/************
	 * DROPS
	 ************/
	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune)
	{
		return Item.getItemFromBlock(GrowthCraftBamboo.blocks.bambooSingleSlab.getBlock());
	}

	protected ItemStack createStackedBlock(int par1)
	{
		return new ItemStack(GrowthCraftBamboo.blocks.bambooSingleSlab.getBlock(), 2, 0);
	}
}
