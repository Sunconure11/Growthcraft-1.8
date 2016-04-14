package growthcraft.bees.common.item;

import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.common.item.GrcItemFoodBase;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHoneyJar extends GrcItemFoodBase
{
	public ItemHoneyJar()
	{
		super(6, false);
		this.setUnlocalizedName("grc.honeyJar");
		this.setCreativeTab(GrowthCraftBees.tab);
		this.setContainerItem(Items.flower_pot);
		this.setMaxStackSize(1);
	}

	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
	{
		super.onEaten(stack, world, player);
		return new ItemStack(Items.flower_pot);
	}
}
