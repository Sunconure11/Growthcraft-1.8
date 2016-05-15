package growthcraft.hops.common.village;

import java.util.List;
import java.util.Random;

import growthcraft.core.GrowthCraftCore;
import growthcraft.hops.GrowthCraftHops;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.World;

public class ComponentVillageHopVineyard extends StructureVillagePieces.Village
{
	public ComponentVillageHopVineyard(){}

	public ComponentVillageHopVineyard(Start startPiece, int par2, Random random, StructureBoundingBox boundingBox, int par5)
	{
		super(startPiece, par2);
		this.coordBaseMode = par5;
		this.boundingBox = boundingBox;
	}

	public boolean addComponentParts(World world, Random random, StructureBoundingBox box)
	{
		if (this.field_143015_k < 0)
		{
			this.field_143015_k = this.getAverageGroundLevel(world, box);

			if (this.field_143015_k < 0)
			{
				return true;
			}

			this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 9 - 1, 0);
		}

		this.fillWithBlocks(world, box, 0, 1, 0, 12, 5, 8, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
		this.fillWithBlocks(world, box, 0, 0, 0, 0, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
		this.fillWithBlocks(world, box, 12, 0, 0, 12, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
		this.fillWithBlocks(world, box, 1, 0, 0, 11, 0, 0, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
		this.fillWithBlocks(world, box, 1, 0, 8, 11, 0, 8, Blocks.log.getDefaultState(), Blocks.log.getDefaultState(), false);
		this.fillWithBlocks(world, box, 1, 0, 1, 11, 0, 7, Blocks.grass.getDefaultState(), Blocks.grass.getDefaultState(), false);
		int loop;
		int loop2;

		for (loop = 1; loop < 12; loop = loop + 2)
		{
			this.fillWithBlocks(world, box, loop, 0, 2, loop, 0, 6, Blocks.water.getDefaultState(), Blocks.water.getDefaultState(), false);
			this.fillWithBlocks(world, box, loop, 0, 2, loop, 0, 2, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
			this.fillWithBlocks(world, box, loop, 0, 4, loop, 0, 4, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
			this.fillWithBlocks(world, box, loop, 0, 6, loop, 0, 6, Blocks.farmland.getDefaultState(), Blocks.farmland.getDefaultState(), false);
			this.setBlockState(world, GrowthCraftCore.blocks.fenceRope.getBlock().getDefaultState(), 0, loop, 6, 1, box);
			this.setBlockState(world, GrowthCraftCore.blocks.fenceRope.getBlock().getDefaultState(), 0, loop, 6, 7, box);
			for (loop2 = 2; loop2 <= 6; ++loop2)
			{
				this.setBlockState(world, GrowthCraftCore.blocks.ropeBlock.getBlock(), 0, loop, 6, loop2, box);
			}
			for (loop2 = 1; loop2 <= 5; ++loop2)
			{
				this.setBlockState(world, Blocks.fence.getDefaultState(), 0, loop, loop2, 1, box);
				this.setBlockState(world, Blocks.fence.getDefaultState(), 0, loop, loop2, 7, box);
				this.setBlockState(world, GrowthCraftHops.hopVine.getBlock().getDefaultState(), 3, loop, loop2, 2, box);
				this.setBlockState(world, GrowthCraftHops.hopVine.getBlock().getDefaultState(), 3, loop, loop2, 4, box);
				this.setBlockState(world, GrowthCraftHops.hopVine.getBlock().getDefaultState(), 3, loop, loop2, 6, box);
			}
		}

		for (loop = 0; loop < 9; ++loop)
		{
			for (loop2 = 0; loop2 < 13; ++loop2)
			{
				clearCurrentPositionBlocksUpwards(world, loop2, 9, loop, box);
				replaceAirAndLiquidDownwards(world, Blocks.dirt.getDefaultState(), 0, loop2, -1, loop, box);
			}
		}

		return true;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static ComponentVillageHopVineyard buildComponent(Start startPiece, List<StructureComponent> structures, Random random, int par3, int par4, int par5, EnumFacing facing, int par7)
	{
		final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 13, 9, 9, facing);
		if (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(structures, structureboundingbox) == null)
		{
			return new ComponentVillageHopVineyard(startPiece, par7, random, structureboundingbox, facing);
		}
		return null;
	}
}
