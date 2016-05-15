package growthcraft.cellar.common.block;

import growthcraft.cellar.common.tileentity.TileEntityFermentBarrel;
import growthcraft.cellar.event.EventBarrelDrained;
import growthcraft.cellar.GrowthCraftCellar;
import growthcraft.cellar.util.CellarGuiType;
import growthcraft.api.core.util.BlockFlags;
import growthcraft.core.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFermentBarrel extends BlockCellarContainer
{
	public BlockFermentBarrel()
	{
		super(Material.wood);
		setTileEntityType(TileEntityFermentBarrel.class);
		setHardness(2.5F);
		setStepSound(soundTypeWood);
		setUnlocalizedName("grc.fermentBarrel");
		setCreativeTab(GrowthCraftCellar.tab);
		setGuiType(CellarGuiType.FERMENT_BARREL);
	}

	@Override
	protected boolean shouldRestoreBlockState(World world, BlockPos pos, ItemStack stack)
	{
		return true;
	}

	@Override
	protected boolean shouldDropTileStack(World world, BlockPos pos, IBlockState state, int fortune)
	{
		return true;
	}

	@Override
	public boolean isRotatable(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return true;
	}

	@Override
	protected boolean playerDrainTank(World world, BlockPos pos, IFluidHandler tank, ItemStack held, EntityPlayer player)
	{
		final FluidStack available = Utils.playerDrainTank(world, pos, tank, held, player);
		if (available != null && available.amount > 0)
		{
			GrowthCraftCellar.CELLAR_BUS.post(new EventBarrelDrained(player, world, pos, available));
			return true;
		}
		return false;
	}

	private void setDefaultDirection(World world, BlockPos pos)
	{
		if (!world.isRemote)
		{
			final IBlockState northBlock = world.getBlock(pos.north());
			final IBlockState southBlock = world.getBlock(pos.south());
			final IBlockState westBlock = world.getBlock(pos.west());
			final IBlockState eastBlock = world.getBlock(pos.east());
			int meta = 3;

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
	public void onBlockAdded(World world, BlockPos pos)
	{
		super.onBlockAdded(world, pos);
		setDefaultDirection(world, pos);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, EntityLivingBase entity, ItemStack stack)
	{
		super.onBlockPlacedBy(world, pos, entity, stack);
		final int meta = BlockPistonBase.determineOrientation(world, pos, entity);
		world.setBlockMetadataWithNotify(pos, meta, BlockFlags.UPDATE_AND_SYNC);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing facing)
	{
		return true;
	}

	@Override
	public boolean hasComparatorInputOverride()
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, BlockPos pos, int par5)
	{
		final TileEntityFermentBarrel te = getTileEntity(world, pos);
		if (te != null)
		{
			return te.getDeviceProgressScaled(15);
		}
		return 0;
	}
}
