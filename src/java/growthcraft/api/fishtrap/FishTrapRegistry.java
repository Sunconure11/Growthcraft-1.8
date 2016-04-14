package growthcraft.api.fishtrap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

public class FishTrapRegistry
{
	private static final FishTrapRegistry instance = new FishTrapRegistry();
	private final List<FishTrapEntry> fishList = new ArrayList<FishTrapEntry>();
	private final List<FishTrapEntry> treasureList = new ArrayList<FishTrapEntry>();
	private final List<FishTrapEntry> junkList = new ArrayList<FishTrapEntry>();

	public static final FishTrapRegistry instance()
	{
		return instance;
	}

	public void addTrapFish(FishTrapEntry entry)
	{
		fishList.add(entry);
	}

	public void addTrapTreasure(FishTrapEntry entry)
	{
		treasureList.add(entry);
	}

	public void addTrapJunk(FishTrapEntry entry)
	{
		junkList.add(entry);
	}

	private ItemStack getFishableEntry(Random random, List<FishTrapEntry> list)
	{
		final FishTrapEntry entry = WeightedRandom.getRandomItem(random, list);
		if (entry != null)
		{
			return entry.getFishable(random);
		}
		return null;
	}

	public ItemStack getFishList(World world)
	{
		return getFishableEntry(world.rand, fishList);
	}

	public ItemStack getTreasureList(World world)
	{
		return getFishableEntry(world.rand, treasureList);
	}

	public ItemStack getJunkList(World world)
	{
		return getFishableEntry(world.rand, junkList);
	}
}
