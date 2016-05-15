package growthcraft.bamboo.common.block;

import java.util.Random;

import growthcraft.bamboo.GrowthCraftBamboo;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBambooDoor extends BlockDoor
{
	private static final String[] doorIconNames = new String[] {"grcbamboo:door_lower", "grcbamboo:door_upper"};

	public BlockBambooDoor()
	{
		super(Material.wood);
		setStepSound(soundTypeWood);
		setHardness(3.0F);
		disableStats();
		setCreativeTab(null);
		setUnlocalizedName("grc.bambooDoor");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World world, BlockPos pos)
	{
		return GrowthCraftBamboo.items.bambooDoorItem.getItem();
	}

	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune)
	{
		return (meta & 8) != 0 ? null : GrowthCraftBamboo.items.bambooDoorItem.getItem();
	}
}
