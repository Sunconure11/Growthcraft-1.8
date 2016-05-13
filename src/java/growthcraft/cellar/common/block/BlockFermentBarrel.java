package growthcraft.cellar.common.block;

import growthcraft.cellar.common.tileentity.TileEntityFermentBarrel;
import growthcraft.cellar.event.EventBarrelDrained;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.CellarGuiType;
import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.Utils;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class BlockFermentBarrel extends BlockCellarContainer
{
	public BlockFermentBarrel()
	{
		super(Material.wood);
		setTileEntityType(TileEntityFermentBarrel.class);
		setHardness(2.5F);
		setStepSound(soundTypeWood);
		setBlockName("grc.fermentBarrel");
		setCreativeTab(GrowthCraftCellar.tab);
		setGuiType(CellarGuiType.FERMENT_BARREL);
	}

	@Override
	protected boolean shouldRestoreBlockState(World world, int x, int y, int z, ItemStack stack)
	{
		return true;
	}

	@Override
	protected boolean shouldDropTileStack(World world, int x, int y, int z, int metadata, int fortune)
	{
		return true;
	}

	@Override
	public boolean isRotatable(IBlockAccess world, int x, int y, int z, EnumFacing side)
	{
		return true;
	}

	@Override
	protected boolean playerDrainTank(World world, int x, int y, int z, IFluidHandler tank, ItemStack held, EntityPlayer player)
	{
		final FluidStack available = Utils.playerDrainTank(world, x, y, z, tank, held, player);
		if (available != null && available.amount > 0)
		{
			GrowthCraftCellar.CELLAR_BUS.post(new EventBarrelDrained(player, world, x, y, z, available));
			return true;
		}
		return false;
	}

	private void setDefaultDirection(World world, int x, int y, int z)
	{
		if (!world.isRemote)
		{
			final Block southBlock = world.getBlock(x, y, z - 1);
			final Block northBlock = world.getBlock(x, y, z + 1);
			final Block westBlock = world.getBlock(x - 1, y, z);
			final Block eastBlock = world.getBlock(x + 1, y, z);
			byte meta = 3;

			if (southBlock.func_149730_j() && !northBlock.func_149730_j())
			{
				meta = 3;
			}

			if (northBlock.func_149730_j() && !southBlock.func_149730_j())
			{
				meta = 2;
			}

			if (westBlock.func_149730_j() && !eastBlock.func_149730_j())
			{
				meta = 5;
			}

			if (eastBlock.func_149730_j() && !westBlock.func_149730_j())
			{
				meta = 4;
			}

			world.setBlockMetadataWithNotify(x, y, z, meta, BlockFlags.UPDATE_AND_SYNC);
		}
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		super.onBlockAdded(world, x, y, z);
		setDefaultDirection(world, x, y, z);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	{
		super.onBlockPlacedBy(world, x, y, z, entity, stack);
		final int meta = BlockPistonBase.determineOrientation(world, x, y, z, entity);
		world.setBlockMetadataWithNotify(x, y, z, meta, BlockFlags.UPDATE_AND_SYNC);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
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
	public int getComparatorInputOverride(World world, int x, int y, int z, int par5)
	{
		final TileEntityFermentBarrel te = getTileEntity(world, x, y, z);
		if (te != null)
		{
			return te.getDeviceProgressScaled(15);
		}
		return 0;
	}
}
