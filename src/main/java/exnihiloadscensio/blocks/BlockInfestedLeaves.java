package exnihiloadscensio.blocks;

import exnihiloadscensio.config.Config;
import exnihiloadscensio.items.tools.ICrook;
import exnihiloadscensio.tiles.TileInfestedLeaves;
import exnihiloadscensio.util.Util;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoAccessor;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockInfestedLeaves extends BlockLeaves implements ITileEntityProvider, IProbeInfoAccessor {

	public BlockInfestedLeaves() {
		super();
		this.setUnlocalizedName("blockInfestedLeaves");
		this.setRegistryName("blockInfestedLeaves");
		GameRegistry.<Block>register(this);
		GameRegistry.register(new ItemBlock(this).setRegistryName("blockInfestedLeaves"));
		this.setDefaultState(
				this.blockState.getBaseState().withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, false));
		this.leavesFancy = true;
	}

	@Override @Nonnull @Deprecated
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	public static void infestLeafBlock(World world, BlockPos pos) {
		IBlockState block = world.getBlockState(pos);

		if (block.getBlock().isLeaves(block, world, pos) && !block.getBlock().equals(ENBlocks.infestedLeaves)) {
			world.setBlockState(pos, ENBlocks.infestedLeaves.getDefaultState());

			TileInfestedLeaves tile = (TileInfestedLeaves) world.getTileEntity(pos);

			if (tile != null) {
				tile.setLeafBlock(block);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override @Nonnull
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
	}

	@Override @Nonnull @Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(CHECK_DECAY, meta == 0 || meta == 1).withProperty(DECAYABLE, meta == 0 || meta == 2);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		boolean checkDecay = state.getValue(CHECK_DECAY);
		boolean decayable = state.getValue(DECAYABLE);
		if (checkDecay && decayable)
			return 0;
		if (checkDecay && !decayable)
			return 1;
		if (!checkDecay && decayable)
			return 2;
		return 3;
	}

	@Override @Nonnull
	public List<ItemStack> getDrops(IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, int fortune) {
		return new ArrayList<ItemStack>();
	}

	@Override
	public void updateTick(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, Random rand) {

	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (!world.isRemote && !player.isCreative()) {
			TileEntity tile = world.getTileEntity(pos);

			if (tile != null) {
				if (tile instanceof TileInfestedLeaves) {
					TileInfestedLeaves leaves = (TileInfestedLeaves) tile;

					if (!player.getHeldItemMainhand().isEmpty()
							&& player.getHeldItemMainhand().getItem() instanceof ICrook) {
						if (world.rand.nextFloat() < leaves.getProgress() * Config.stringChance) {
							Util.dropItemInWorld(leaves, player, new ItemStack(Items.STRING, 1, 0), 0.02f);
						}

						if (world.rand.nextFloat() < leaves.getProgress() * Config.stringChance / 4.0d) {
							Util.dropItemInWorld(leaves, player, new ItemStack(Items.STRING, 1, 0), 0.02f);
						}
					}
				}

				world.removeTileEntity(pos);
			}
		}
	}

	@Override
	public List<ItemStack> onSheared(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
		ret.add(new ItemStack(this));
		return ret;
	}

	@Override @Nonnull
	public EnumType getWoodType(int meta) {
		return EnumType.OAK;
	}

	@Override
	public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
		return new TileInfestedLeaves();
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world,
			IBlockState blockState, IProbeHitData data) {

		TileInfestedLeaves tile = (TileInfestedLeaves) world.getTileEntity(data.getPos());

		if (tile != null) {
			if (tile.getProgress() >= 1.0F) {
				probeInfo.text("Progress: Done");
			} else {
				probeInfo.progress((int) (tile.getProgress() * 100), 100);
			}
		}
	}
}
