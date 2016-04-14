package growthcraft.bamboo.common.block;

import growthcraft.bamboo.GrowthCraftBamboo;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockBamboo extends Block
{
	public BlockBamboo()
	{
		super(Material.wood);
		setStepSound(soundTypeWood);
		setResistance(5.0F);
		setHardness(2.0F);
		setBlockName("grc.bambooBlock");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}
}
