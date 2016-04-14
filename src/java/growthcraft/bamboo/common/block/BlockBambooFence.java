package growthcraft.bamboo.common.block;

import growthcraft.bamboo.GrowthCraftBamboo;
import growthcraft.bamboo.client.renderer.RenderBambooFence;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockBambooFence extends BlockFence
{
	public BlockBambooFence()
	{
		super(null, Material.wood);
		this.useNeighborBrightness = true;
		setStepSound(soundTypeWood);
		setResistance(5.0F);
		setHardness(2.0F);
		setCreativeTab(GrowthCraftBamboo.creativeTab);
		setBlockName("grc.bambooFence");
	}

	/************
	 * STUFF
	 ************/
	@Override
	public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z)
	{
		return false;
	}

	@Override
	public boolean canPlaceTorchOnTop(World world, int x, int y, int z)
	{
		return true;
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side)
	{
		return side == ForgeDirection.UP;
	}

	@Override
	public boolean canConnectFenceTo(IBlockAccess world, int x, int y, int z)
	{
		final Block block = world.getBlock(x, y, z);

		if (this == block ||
			(block instanceof BlockFence) ||
			(block instanceof BlockFenceGate) ||
			GrowthCraftBamboo.blocks.bambooWall.isSameAs(block) ||
			GrowthCraftBamboo.blocks.bambooStalk.isSameAs(block))
		{
			return true;
		}
		else
		{
			if (block != null && block.getMaterial().isOpaque() && block.renderAsNormalBlock())
			{
				return block.getMaterial() != Material.gourd;
			}
		}
		return false;
	}
}
