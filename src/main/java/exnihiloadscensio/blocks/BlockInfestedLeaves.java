package exnihiloadscensio.blocks;

import java.util.ArrayList;
import java.util.List;

import exnihiloadscensio.config.Config;
import exnihiloadscensio.tiles.TileInfestedLeaves;
import exnihiloadscensio.util.Util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockInfestedLeaves extends BlockLeaves implements ITileEntityProvider {

	public BlockInfestedLeaves()
	{
		super();
		this.setUnlocalizedName("blockInfestedLeaves");
		this.setRegistryName("blockInfestedLeaves");
		GameRegistry.<Block>register(this);
		GameRegistry.register(new ItemBlock(this).setRegistryName("blockInfestedLeaves"));
		this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, false));
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {CHECK_DECAY, DECAYABLE});
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(CHECK_DECAY, meta == 0 || meta == 1 ? true : false)
				.withProperty(DECAYABLE, meta == 0 || meta == 2 ? true : false);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
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

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		return new ArrayList<ItemStack>();
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		if (!world.isRemote)
		{
			TileInfestedLeaves leaves = (TileInfestedLeaves) world.getTileEntity(pos);

			if (leaves != null)
			{
				if (world.rand.nextFloat() < leaves.getProgress() * Config.stringChance)
					Util.dropItemInWorld(leaves, player, new ItemStack(Items.STRING, 1, 0), 0.02f);

				if (world.rand.nextFloat() < leaves.getProgress() * Config.stringChance / 4.0d)
					Util.dropItemInWorld(leaves, player, new ItemStack(Items.STRING, 1, 0), 0.02f);
			}
		}

		return world.setBlockToAir(pos);
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world,
			BlockPos pos, int fortune) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnumType getWoodType(int meta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileInfestedLeaves();
	}
	
	@Override
	public boolean isFullyOpaque(IBlockState state)
	{
		return false;
	}
	
	@Override
	 public boolean isFullBlock(IBlockState state)
    {
        return false;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
}
