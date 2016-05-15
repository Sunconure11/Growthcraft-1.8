package growthcraft.bamboo.common.block;

import growthcraft.bamboo.GrowthCraftBamboo;

import net.minecraft.block.BlockFenceGate;

public class BlockBambooFenceGate extends BlockFenceGate
{
	public BlockBambooFenceGate()
	{
		super();
		setStepSound(soundTypeWood);
		setHardness(2.0F);
		setResistance(5.0F);
		setUnlocalizedName("grc.bambooFenceGate");
		setCreativeTab(GrowthCraftBamboo.creativeTab);
	}
}
