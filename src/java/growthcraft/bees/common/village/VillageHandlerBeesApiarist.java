package growthcraft.bees.common.village;

import java.util.Random;
import java.util.List;

import growthcraft.bees.GrowthCraftBees;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.world.gen.structure.StructureVillagePieces.Village;
import net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageCreationHandler;

public class VillageHandlerBeesApiarist implements IVillageCreationHandler
{
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random)
	{
		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 1 + random.nextInt(2)), GrowthCraftBees.items.honeyJar.asStack(1, 2)));
		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 1 + random.nextInt(2)), GrowthCraftBees.items.honeyCombFilled.asStack(7)));
		recipeList.add(new MerchantRecipe(new ItemStack(Items.emerald, 1 + random.nextInt(2)), GrowthCraftBees.items.bee.asStack(3, 5)));
	}

	@Override
	public PieceWeight getVillagePieceWeight(Random random, int i)
	{
		int num = MathHelper.getRandomIntegerInRange(random, 0 + i, 1 + i);
		if (!GrowthCraftBees.getConfig().generateApiaristStructure)
			num = 0;

		return new PieceWeight(ComponentVillageApiarist.class, 21, num);
	}

	@Override
	public Class<?> getComponentClass()
	{
		return ComponentVillageApiarist.class;
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public Village buildComponent(PieceWeight villagePiece, Start startPiece, List pieces, Random random, int p1, int p2, int p3, EnumFacing facing, int p5)
	{
		return ComponentVillageApiarist.buildComponent(startPiece, pieces, random, p1, p2, p3, facing, p5);
	}
}
