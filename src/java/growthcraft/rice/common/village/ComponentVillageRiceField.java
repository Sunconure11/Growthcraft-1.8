package growthcraft.rice.common.village;

import java.util.List;
import java.util.Random;
import java.util.HashMap;

import growthcraft.core.util.SchemaToVillage.BlockEntry;
import growthcraft.core.util.SchemaToVillage.IBlockEntries;
import growthcraft.core.util.SchemaToVillage;
import growthcraft.rice.GrowthCraftRice;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.World;

public class ComponentVillageRiceField extends StructureVillagePieces.Village implements SchemaToVillage.IVillage
{
	// Design by Ar97x, with some minor modifications by IceDragon (very minor)
	protected static final String[][] riceFieldSchema = {
		{
			"    sss    ",
			"x   x|x   x",
			" x-------x ",
			" |ppppppp| ",
			" |p~~~~~p| ",
			" |p~ppp~p| ",
			" |p~ppp~p| ",
			" |p~ppp~p| ",
			" |p~~~~~p| ",
			" |ppppppp| ",
			" x-------x ",
			"x         x"
		},
		{
			"           ",
			"x---x x---x",
			"|         |",
			"| rrrrrrr |",
			"| r     r |",
			"| r rrr r |",
			"| r rrr r |",
			"| r rrr r |",
			"| r     r |",
			"| rrrrrrr |",
			"|         |",
			"x---------x"
		},
		{
			"           ",
			"f   fgf   f",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"f         f"
		},
		{
			"           ",
			"t   t t   t",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"           ",
			"t         t"
		}
	};

	// DO NOT REMOVE
	public ComponentVillageRiceField() {}

	public ComponentVillageRiceField(Start startPiece, int type, Random random, StructureBoundingBox boundingBox, EnumFacing facing)
	{
		super(startPiece, type);
		this.coordBaseMode = facing;
		this.boundingBox = boundingBox;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public static ComponentVillageRiceField buildComponent(Start startPiece, List list, Random random, int x, int y, int z, EnumFacing facing, int par7)
	{
		final StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 11, 5, 12, facing);
		if (canVillageGoDeeper(structureboundingbox))
		{
			if (StructureComponent.findIntersecting(list, structureboundingbox) == null)
			{
				return new ComponentVillageRiceField(startPiece, par7, random, structureboundingbox, facing);
			}
		}
		return null;
	}

	public void placeBlockAtCurrentPositionPub(World world, IBlockState state, int x, int y, int z, StructureBoundingBox box)
	{
		setBlockState(world, state, x, y, z, box);
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

			this.boundingBox.offset(0, this.field_143015_k - this.boundingBox.maxY + 3, 0);
		}

		// clear entire bounding box
		fillWithBlocks(world, box, 0, 0, 0, 11, 5, 12, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
		fillWithBlocks(world, box, 0, 0, 0, 11, 0, 12, Blocks.grass.getDefaultState(), Blocks.grass.getDefaultState(), false);

		final HashMap<Character, IBlockEntries> map = new HashMap<Character, IBlockEntries>();
		map.put('-', new BlockEntry(Blocks.log.getDefaultState()));
		map.put('f', new BlockEntry(Blocks.oak_fence.getDefaultState()));
		map.put('g', new BlockEntry(Blocks.oak_fence_gate.getDefaultState()));
		map.put('p', new BlockEntry(GrowthCraftRice.paddyField.getBlock().getDefaultState()));
		map.put('r', new BlockEntry(GrowthCraftRice.riceBlock.getBlock().getDefaultState()));
		map.put('s', new BlockEntry(Blocks.oak_stairs.getDefaultState()));
		map.put('t', new BlockEntry(Blocks.torch.getDefaultState()));
		map.put('x', new BlockEntry(Blocks.log.getDefaultState()));
		map.put('|', new BlockEntry(Blocks.log.getDefaultState()));
		map.put('~', new BlockEntry(Blocks.water.getDefaultState()));

		SchemaToVillage.drawSchema(this, world, random, box, riceFieldSchema, map, 0, 1, 0);

		for (int row = 0; row < 12; ++row)
		{
			for (int col = 0; col < 11; ++col)
			{
				clearCurrentPositionBlocksUpwards(world, col, 4, row, box);
				replaceAirAndLiquidDownwards(world, Blocks.dirt.getDefaultState(), col, -1, row, box);
			}
		}

		return true;
	}
}
