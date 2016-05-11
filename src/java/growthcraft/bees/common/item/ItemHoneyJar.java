package growthcraft.bees.common.item;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.common.item.GrcItemFoodBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemHoneyJar extends GrcItemFoodBase
{
	public ItemHoneyJar()
	{
		super(6, false);
		setUnlocalizedName("grc.honeyJar");
		setCreativeTab(GrowthCraftBees.tab);
		setContainerItem(Items.flower_pot);
		setMaxStackSize(1);
	}

	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
	{
		super.onEaten(stack, world, player);
		return new ItemStack(Items.flower_pot);
	}
}
