package growthcraft.grapes.common.village;

import java.util.List;
import java.util.Random;

import growthcraft.core.GrowthCraftCore;
import growthcraft.grapes.GrowthCraftGrapes;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;

public class ComponentVillageGrapeVineyard extends StructureVillagePieces.Village
{
	public ComponentVillageGrapeVineyard() {}

	public ComponentVillageGrapeVineyard(Start startPiece, int type, Random random, StructureBoundingBox boundingBox, EnumFacing facing)
	{
		super(startPiece, type);
		this.coordBaseMode = facing;
		this.boundingBox = boundingBox;
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox box)
	{
		if (field_143015_k < 0)
		{
			field_143015_k = getAverageGroundLevel(world, box);

			if (field_143015_k < 0)
			{
				return true;
			}

			boundingBox.offset(0, field_143015_k - boundingBox.maxY + 6 - 1, 0);
		}

		fillWithBlocks(world, box, 0, 1, 0, 12, 6, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
		fillWithBlocks(world, box, 0, 0, 0, 0, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
		fillWithBlocks(world, box, 12, 0, 0, 12, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
		fillWithBlocks(world, box, 1, 0, 0, 11, 0, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
		fillWithBlocks(world, box, 1, 0, 8, 11, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
		fillWithBlocks(world, box, 1, 0, 1, 11, 0, 7, Blocks.grass.getDefaultState(), Blocks.grass.getDefaultState(), false);

		for (int loop = 1; loop < 12; loop = loop + 2)
		{
			fillWithBlocks(world, box, loop, 0, 2, loop, 0, 6, Blocks.water.getDefaultState(), Blocks.water.getDefaultState(), false);
			fillWithBlocks(world, box, loop, 0, 4, loop, 0, 4, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
			setBlockState(world, Blocks.oak_fence.getDefaultState(), loop, 1, 1, box);
			setBlockState(world, Blocks.oak_fence.getDefaultState(), loop, 1, 7, box);
			setBlockState(world, Blocks.oak_fence.getDefaultState(), loop, 2, 1, box);
			setBlockState(world, Blocks.oak_fence.getDefaultState(), loop, 2, 7, box);
			setBlockState(world, GrowthCraftCore.blocks.fenceRope.getBlock().getDefaultState(), loop, 3, 1, box);
			setBlockState(world, GrowthCraftCore.blocks.fenceRope.getBlock().getDefaultState(), loop, 3, 7, box);
			setBlockState(world, GrowthCraftGrapes.blocks.grapeVine1.getBlock().getDefaultState(), loop, 1, 4, box);
			setBlockState(world, GrowthCraftGrapes.blocks.grapeVine1.getBlock().getDefaultState(), loop, 2, 4, box);
			for (loop2 = 2; loop2 <= 6; ++loop2)
			{
				setBlockState(world, GrowthCraftGrapes.blocks.grapeLeaves.getBlock().getDefaultState(), loop, 3, loop2, box);
				if (MathHelper.getRandomIntegerInRange(random, 0, 2) != 0 && loop2 != 4)
				{
					setBlockState(world, GrowthCraftGrapes.blocks.grapeBlock.getBlock().getDefaultState(), loop, 2, loop2, box);
				}
			}
		}

		for (int zl = 0; zl < 9; ++zl)
		{
			for (int xl = 0; xl < 13; ++xl)
			{
				clearCurrentPositionBlocksUpwards(world, xl, 6, zl, box);
				replaceAirAndLiquidDownwards(world, Blocks.dirt.getDefaultState(), xl, -1, zl, box);
			}
		}

		return true;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static ComponentVillageGrapeVineyard buildComponent(Start startPiece, List list, Random random, int par3, int par4, int par5, EnumFacing dir, int par7)
	{
		final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 13, 6, 9, dir);
		if (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null)
		{
			return new ComponentVillageGrapeVineyard(startPiece, par7, random, structureboundingbox, dir);
		}
		return null;
	}
}
