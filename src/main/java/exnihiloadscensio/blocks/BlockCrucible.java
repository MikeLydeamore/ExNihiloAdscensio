package exnihiloadscensio.blocks;

import java.util.List;

import exnihiloadscensio.items.ItemBlockMeta;
import exnihiloadscensio.registries.CrucibleRegistry;
import exnihiloadscensio.tiles.TileCrucible;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoAccessor;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCrucible extends Block implements IProbeInfoAccessor {

	public static final PropertyBool FIRED = PropertyBool.create("fired");

	public BlockCrucible() {
		super(Material.ROCK);
		String name = "blockCrucible";
		setUnlocalizedName(name);
		setRegistryName(name);
		GameRegistry.<Block>register(this);
		GameRegistry.register(new ItemBlockMeta(this).setRegistryName(name));
		this.setHardness(2.0f);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FIRED, false));
	}

	@Override
	public TileEntity createTileEntity(World worldIn, IBlockState state) {
		if (state.getValue(FIRED))
			return new TileCrucible();

		return null;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return state.getValue(FIRED);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FIRED });
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FIRED, meta == 0 ? false : true);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FIRED) ? 1 : 0;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		list.add(new ItemStack(itemIn, 1, 0));
		list.add(new ItemStack(itemIn, 1, 1));
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(world.getBlockState(pos)));
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;

		TileCrucible te = (TileCrucible) world.getTileEntity(pos);
		if (te != null)
			return te.onBlockActivated(heldItem, player);
		else
			return true;
	}

	@Override
	public boolean isFullyOpaque(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
				new ModelResourceLocation(getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 1,
				new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world,
			IBlockState blockState, IProbeHitData data) {
		TileCrucible crucible = (TileCrucible) world.getTileEntity(data.getPos());

		if (crucible == null)
			return;

		ItemStack solid = crucible.getCurrentItem() == null ? null : crucible.getCurrentItem().getItemStack();
		FluidStack liquid = crucible.getTank().getFluid();

		String solidName = solid == null ? "None" : solid.getDisplayName();
		String liquidName = liquid == null ? "None" : liquid.getLocalizedName();

		int solidAmount = Math.max(0, crucible.getSolidAmount());

		ItemStack toMelt = crucible.getItemHandler().getStackInSlot(0);

		if (toMelt != null) {
			solidAmount += CrucibleRegistry.getMeltable(toMelt).getAmount() * toMelt.stackSize;
		}

		probeInfo.text(String.format("Solid (%s): %d", solidName, solidAmount));
		probeInfo.text(String.format("Liquid (%s): %d", liquidName, crucible.getTank().getFluidAmount()));
		probeInfo.text("Rate: " + crucible.getHeatRate() + "x");
	}

}
