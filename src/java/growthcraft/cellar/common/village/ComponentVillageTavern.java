package growthcraft.cellar.common.village;

import java.util.List;
import java.util.Random;

import growthcraft.cellar.GrowthCraftCellar;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.World;

public class ComponentVillageTavern extends StructureVillagePieces.Village
{
	public ComponentVillageTavern() {}

	public ComponentVillageTavern(Start startPiece, int type, Random random, StructureBoundingBox boundingBox, EnumFacing facing)
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

			boundingBox.offset(0, field_143015_k - boundingBox.maxY + 9 - 1, 0);
		}

		int x;
		int z;

		fillWithBlocks(world, box, 1, 1, 1, 11, 4, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
		fillWithBlocks(world, box, 0, 0, 0, 12, 0, 6, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
		fillWithBlocks(world, box, 0, 6, 3, 12, 6, 3, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);

		for (z = -1; z <= 1; ++z)
		{
			fillWithBlocks(world, box, 0, 4 - z, 1 - z, 12, 4 - z, 1 - z, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
			fillWithBlocks(world, box, 0, 4 - z, 5 + z, 12, 4 - z, 5 + z, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
		}

		for (z = -1; z <= 2; ++z)
		{
			for (x = 0; x <= 12; ++x)
			{
				setBlockState(world, Blocks.oak_stairs.getDefaultState(), x, 4 + z, z, box);
				setBlockState(world, Blocks.oak_stairs.getDefaultState(), x, 4 + z, 6 - z, box);
			}
		}

		fillWithBlocks(world, box, 0, 1, 0, 0, 1, 6, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
		fillWithBlocks(world, box, 1, 1, 6, 12, 1, 6, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
		fillWithBlocks(world, box, 12, 1, 0, 12, 1, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
		fillWithBlocks(world, box, 1, 1, 0, 11, 1, 0, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
		fillWithBlocks(world, box, 0, 2, 0, 0, 2, 6, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
		fillWithBlocks(world, box, 0, 3, 1, 0, 3, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
		fillWithBlocks(world, box, 0, 4, 2, 0, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
		setBlockState(world, Blocks.planks.getDefaultState(), 0, 0, 5, 3, box);
		fillWithBlocks(world, box, 12, 2, 0, 12, 2, 6, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
		fillWithBlocks(world, box, 12, 3, 1, 12, 3, 5, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
		fillWithBlocks(world, box, 12, 4, 2, 12, 4, 4, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
		setBlockState(world, Blocks.planks.getDefaultState(), 0, 12, 5, 3, box);
		fillWithBlocks(world, box, 1, 2, 0, 11, 2, 0, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
		fillWithBlocks(world, box, 1, 2, 6, 11, 2, 6, Blocks.planks.getDefaultState(), Blocks.planks.getDefaultState(), false);
		setBlockState(world, Blocks.log.getDefaultState(), 0, 0, 2, 1, box);
		setBlockState(world, Blocks.log.getDefaultState(), 0, 0, 2, 5, box);
		setBlockState(world, Blocks.log.getDefaultState(), 0, 8, 2, 6, box);
		setBlockState(world, Blocks.log.getDefaultState(), 0, 11, 2, 6, box);
		setBlockState(world, Blocks.log.getDefaultState(), 0, 12, 4, 2, box);
		setBlockState(world, Blocks.log.getDefaultState(), 0, 12, 4, 4, box);
		setBlockState(world, Blocks.log.getDefaultState(), 0, 1, 2, 0, box);
		setBlockState(world, Blocks.log.getDefaultState(), 0, 3, 2, 0, box);
		setBlockState(world, Blocks.log.getDefaultState(), 0, 7, 2, 0, box);
		setBlockState(world, Blocks.log.getDefaultState(), 0, 11, 2, 0, box);
		setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 0, 2, 2, box);
		setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 0, 2, 3, box);
		setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 0, 2, 4, box);
		setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 9, 2, 6, box);
		setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 10, 2, 6, box);
		setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 12, 4, 3, box);
		setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 2, 2, 0, box);
		setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 8, 2, 0, box);
		setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 9, 2, 0, box);
		setBlockState(world, Blocks.glass_pane.getDefaultState(), 0, 10, 2, 0, box);

		setBlockState(world, Blocks.oak_stairs.getDefaultState(), 2, 1, 1, box);
		setBlockState(world, Blocks.oak_stairs.getDefaultState(), 3, 1, 1, box);
		setBlockState(world, Blocks.oak_stairs.getDefaultState(), 2, 1, 3, box);
		setBlockState(world, Blocks.oak_stairs.getDefaultState(), 3, 1, 3, box);
		setBlockState(world, Blocks.fence.getDefaultState(), 0, 2, 1, 2, box);
		setBlockState(world, Blocks.fence.getDefaultState(), 0, 3, 1, 2, box);
		setBlockState(world, Blocks.wooden_pressure_plate.getDefaultState(), 0, 2, 2, 2, box);
		setBlockState(world, Blocks.wooden_pressure_plate.getDefaultState(), 0, 3, 2, 2, box);

		for (z = 1; z <= 5; ++z)
		{
			if (z < 4)
			{
				setBlockState(world, Blocks.oak_stairs.getDefaultState(), getMetadataWithOffset(Blocks.oak_stairs, 1), 8, 1, z, box);
			}

			if (z != 5)
			{
				setBlockState(world, Blocks.fence.getDefaultState(), 0, 9, 1, z, box);
				setBlockState(world, Blocks.wooden_pressure_plate.getDefaultState(), 0, 9, 2, z, box);
				setBlockState(world, GrowthCraftCellar.blocks.fermentBarrel.getBlock().getDefaultState(), 11, 2, z, box);
			}

			setBlockState(world, Blocks.planks, 0, 11, 1, z, box);
		}

		for (z = -1; z <= 1; ++z)
		{
			fillWithBlocks(world, box, 3, 4 - z, 6 + z, 7, 4 - z, 6 + z, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
		}

		fillWithBlocks(world, box, 3, 2, 6, 7, 3, 6, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
		fillWithBlocks(world, box, 4, 0, 7, 6, 2, 7, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
		fillWithBlocks(world, box, 4, 4, 6, 4, 8, 6, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
		fillWithBlocks(world, box, 5, 4, 7, 5, 8, 7, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
		fillWithBlocks(world, box, 6, 4, 6, 6, 8, 6, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
		fillWithBlocks(world, box, 5, 4, 5, 5, 8, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
		fillWithBlocks(world, box, 4, 1, 5, 4, 2, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
		fillWithBlocks(world, box, 6, 1, 5, 6, 2, 5, Blocks.cobblestone.getDefaultState(), Blocks.cobblestone.getDefaultState(), false);
		setBlockState(world, Blocks.stone_stairs.getDefaultState(), 3, 1, 5, box);
		setBlockState(world, Blocks.stone_stairs.getDefaultState(), 7, 1, 5, box);
		setBlockState(world, Blocks.stone_stairs.getDefaultState(), 4, 3, 5, box);
		setBlockState(world, Blocks.stone_stairs.getDefaultState(), 6, 3, 5, box);
		setBlockState(world, Blocks.stone_slab.getDefaultState(), 3 ^ 8, 5, 3, 5, box);
		fillWithBlocks(world, box, 5, 1, 6, 5, 9, 6, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
		setBlockState(world, Blocks.iron_bars.getDefaultState(), 0, 5, 1, 5, box);
		setBlockState(world, Blocks.netherrack.getDefaultState(), 0, 5, 0, 6, box);
		setBlockState(world, Blocks.fire.getDefaultState(), 0, 5, 1, 6, box);

		placeDoorAtCurrentPosition(world, box, random, 5, 1, 0, getMetadataWithOffset(Blocks.wooden_door, 1));

		if (getBlockAtCurrentPosition(world, 5, 0, -1, box) == Blocks.air && getBlockAtCurrentPosition(world, 5, -1, -1, box) != Blocks.air)
		{
			setBlockState(world, Blocks.stone_stairs.getDefaultState(), 5, 0, -1, box);
		}

		for (z = 0; z < 8; ++z)
		{
			for (x = 0; x < 13; ++x)
			{
				clearCurrentPositionBlocksUpwards(world, x, 9, z, box);
				replaceAirAndLiquidDownwards(world, Blocks.cobblestone.getDefaultState(), x, -1, z, box);
			}
		}

		spawnVillagers(world, box, 10, 1, 2, 1);
		return true;
	}

	@Override
	protected int getVillagerType(int par1)
	{
		return GrowthCraftCellar.getConfig().villagerBrewerID;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static ComponentVillageTavern buildComponent(Start startPiece, List list, Random rand, int par3, int par4, int par5, EnumFacing facing, int par7)
	{
		final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 13, 9, 8, facing);
		if (canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(list, structureboundingbox) == null)
		{
			return new ComponentVillageTavern(startPiece, par7, rand, structureboundingbox, facing);
		}
		return null;
	}
}
