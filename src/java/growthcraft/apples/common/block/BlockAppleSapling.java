package growthcraft.apples.common.block;

import java.util.Random;
import growthcraft.apples.common.world.WorldGenAppleTree;
import growthcraft.apples.GrowthCraftApples;
import growthcraft.core.GrowthCraftCore;
import growthcraft.api.core.util.BlockFlags;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.TerrainGen;

public class BlockAppleSapling extends BlockBush implements IGrowable
{
	private final int growthRate = GrowthCraftApples.getConfig().appleSaplingGrowthRate;

	public BlockAppleSapling()
	{
		super(Material.plants);
		setHardness(0.0F);
		setStepSound(soundTypeGrass);
		setUnlocalizedName("grc.appleSapling");
		setTickRandomly(true);
		setCreativeTab(GrowthCraftCore.creativeTab);
		final float f = 0.4F;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
	}

	public void markOrGrowMarked(World world, BlockPos pos, Random random)
	{
		final int meta = world.getBlockMetadata(pos);

		if ((meta & 8) == 0)
		{
			world.setBlockMetadataWithNotify(pos, meta | 8, BlockFlags.SUPRESS_RENDER);
		}
		else
		{
			this.growTree(world, pos, random);
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		if (!world.isRemote)
		{
			super.updateTick(world, pos, random);

			if (getLightValue(world, pos.up()) >= 9 && random.nextInt(growthRate) == 0)
			{
				this.markOrGrowMarked(world, pos, random);
			}
		}
	}

	public void growTree(World world, BlockPos pos, Random random)
	{
		if (!TerrainGen.saplingGrowTree(world, random, pos)) return;

		final int meta = world.getBlockMetadata(pos) & 3;
		final WorldGenerator generator = new WorldGenAppleTree(true);

		world.setBlockToAir(pos);

		if (!generator.generate(world, random, pos))
		{
			world.setBlock(pos, this, meta, BlockFlags.ALL);
		}
	}

	@Override
	public boolean canGrow(World world, BlockPos pos, boolean isClient)
	{
		return (world.getBlockMetadata(pos) & 8) == 0;
	}

	@Override
	public boolean canUseBonemeal(World world, Random random, BlockPos pos)
	{
		return true;
	}

	@Override
	public void grow(World world, Random random, BlockPos pos)
	{
		if (random.nextFloat() < 0.45D)
		{
			growTree(world, pos, random);
		}
	}
}
