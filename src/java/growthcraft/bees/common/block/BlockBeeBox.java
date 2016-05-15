package growthcraft.bees.common.block;

import java.util.List;
import java.util.Random;

import growthcraft.bees.common.tileentity.TileEntityBeeBox;
import growthcraft.bees.GrowthCraftBees;
import growthcraft.core.common.block.GrcBlockContainer;
import growthcraft.core.integration.minecraft.EnumMinecraftWoodType;
import growthcraft.core.util.ItemUtils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBeeBox extends GrcBlockContainer
{
	private int flammability;
	private int fireSpreadSpeed;

	public BlockBeeBox(Material material)
	{
		super(material);
		setBlockTextureName("grcbees:beebox");
		setTickRandomly(true);
		setHardness(2.5F);
		setStepSound(soundTypeWood);
		setUnlocalizedName("grc.BeeBox.Minecraft");
		setCreativeTab(GrowthCraftBees.tab);
		setTileEntityType(TileEntityBeeBox.class);
	}

	public BlockBeeBox()
	{
		this(Material.wood);
	}

	public String getMetaname(int meta)
	{
		if (meta >= 0 && meta < EnumMinecraftWoodType.VALUES.length)
		{
			return EnumMinecraftWoodType.VALUES[meta].name;
		}
		return "" + meta;
	}

	public BlockBeeBox setFlammability(int flam)
	{
		this.flammability = flam;
		return this;
	}

	public BlockBeeBox setFireSpreadSpeed(int speed)
	{
		this.fireSpreadSpeed = speed;
		return this;
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return flammability;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return fireSpreadSpeed;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getSubBlocks(Item block, CreativeTabs tab, List list)
	{
		for (EnumMinecraftWoodType woodType : EnumMinecraftWoodType.VALUES)
		{
			list.add(new ItemStack(block, 1, woodType.meta));
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		super.updateTick(world, pos, rand);
		final TileEntityBeeBox te = getTileEntity(world, pos);
		if (te != null) te.updateBlockTick();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, Random random)
	{
		if (random.nextInt(24) == 0)
		{
			final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(pos);
			if (te != null)
			{
				if (te.hasBees())
				{
					world.playSound((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F),
						"grcbees:buzz", 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
				}
			}
		}
	}

	/************
	 * TRIGGERS
	 ************/
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, EntityPlayer player, EnumFacing dir, float par7, float par8, float par9)
	{
		if (super.onBlockActivated(world, pos, player, dir, par7, par8, par9)) return true;
		if (world.isRemote)
		{
			return true;
		}
		else
		{
			final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(pos);
			if (te != null)
			{
				player.openGui(GrowthCraftBees.instance, 0, world, pos);
				return true;
			}
			return false;
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, Block par5, int par6)
	{
		final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(pos);

		if (te != null)
		{
			for (int index = 0; index < te.getSizeInventory(); ++index)
			{
				final ItemStack stack = te.getStackInSlot(index);

				ItemUtils.spawnItemStack(world, pos, stack, world.rand);
			}

			world.func_147453_f(pos, par5);
		}

		super.breakBlock(world, pos, par5, par6);
	}

	/************
	 * CONDITIONS
	 ************/
	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return EnumFacing.UP == side;
	}

	/************
	 * DROPS
	 ************/
	@Override
	public int damageDropped(int damage)
	{
		return damage;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune)
	{
		return GrowthCraftBees.beeBox.getItem();
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 1;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, int side)
	{
		return true;
	}

	/************
	 * BOXES
	 ************/
	@Override
	public void setBlockBoundsForItemRender()
	{
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState state, AxisAlignedBB axis, List<AxisAlignedBB> list, Entity entity)
	{
		final float f = 0.0625F;
		// LEGS
		setBlockBounds(3*f, 0.0F, 3*f, 5*f, 3*f, 5*f);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		setBlockBounds(11*f, 0.0F, 3*f, 13*f, 3*f, 5*f);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		setBlockBounds(3*f, 0.0F, 11*f, 5*f, 3*f, 13*f);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		setBlockBounds(11*f, 0.0F, 11*f, 13*f, 3*f, 13*f);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		// BODY
		setBlockBounds(1*f, 3*f, 1*f, 15*f, 10*f, 15*f);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		// ROOF
		setBlockBounds(0.0F, 10*f, 0.0F, 1.0F, 13*f, 1.0F);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		setBlockBounds(2*f, 13*f, 2*f, 14*f, 1.0F, 14*f);
		super.addCollisionBoxesToList(world, pos, state, axis, list, entity);
		setBlockBoundsForItemRender();
	}

	/************
	 * COMPARATOR
	 ************/
	@Override
	public boolean hasComparatorInputOverride()
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, BlockPos pos, int par5)
	{
		final TileEntityBeeBox te = (TileEntityBeeBox)world.getTileEntity(pos);
		return te.countHoney() * 15 / 27;
	}
}
