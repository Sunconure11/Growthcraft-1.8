package growthcraft.core.integration;

import java.util.Random;

import growthcraft.core.GrowthCraftCore;
import squeek.applecore.api.AppleCoreAPI;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class AppleCore extends ModIntegrationBase
{
	public static final String MOD_ID = "AppleCore";
	private static boolean appleCoreLoaded;

	public AppleCore()
	{
		super(GrowthCraftCore.MOD_ID, MOD_ID);
	}

	@Override
	public void doInit()
	{
		appleCoreLoaded = Loader.isModLoaded(modID);
	}

	// abstract the AppleCoreAPI reference into an Optional.Method so that AppleCore is not a hard dependency
	@Optional.Method(modid=MOD_ID)
	private static Event.Result validateGrowthTick_AC(Block block, World world, BlockPos pos, Random random)
	{
		return AppleCoreAPI.dispatcher.validatePlantGrowth(block, world, pos, random);
	}

	public static Event.Result validateGrowthTick(Block block, World world, BlockPos pos, Random random)
	{
		if (appleCoreLoaded)
			return validateGrowthTick_AC(block, world, pos, random);

		return Event.Result.DEFAULT;
	}

	// abstract the AppleCoreAPI reference into an Optional.Method so that AppleCore is not a hard dependency
	@Optional.Method(modid=MOD_ID)
	private static void announceGrowthTick_AC(Block block, World world, BlockPos pos, int previousMetadata)
	{
		AppleCoreAPI.dispatcher.announcePlantGrowth(block, world, pos, previousMetadata);
	}

	public static void announceGrowthTick(Block block, World world, BlockPos pos, int previousMetadata)
	{
		if (appleCoreLoaded)
			announceGrowthTick_AC(block, world, pos, previousMetadata);
	}
}
