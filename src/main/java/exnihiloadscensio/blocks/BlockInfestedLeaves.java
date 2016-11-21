package exnihiloadscensio.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import exnihiloadscensio.config.Config;
import exnihiloadscensio.items.tools.ICrook;
import exnihiloadscensio.tiles.TileInfestedLeaves;
import exnihiloadscensio.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
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

public class BlockInfestedLeaves extends BlockLeaves implements ITileEntityProvider {

	public BlockInfestedLeaves()
	{
		super();
		this.setUnlocalizedName("blockInfestedLeaves");
		this.setRegistryName("blockInfestedLeaves");
		GameRegistry.<Block>register(this);
		GameRegistry.register(new ItemBlock(this).setRegistryName("blockInfestedLeaves"));
		this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, false));
		this.leavesFancy = true;
	}
	
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
	    return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}
	
	public static void infestLeafBlock(World world, BlockPos pos)
	{
	    IBlockState block = world.getBlockState(pos);
	    
	    if(block.getBlock().isLeaves(block, world, pos) && !block.getBlock().equals(ENBlocks.infestedLeaves))
	    {
	        world.setBlockState(pos, ENBlocks.infestedLeaves.getDefaultState());
	        
	        TileInfestedLeaves tile = (TileInfestedLeaves) world.getTileEntity(pos);
	        
	        if(tile != null)
	        {
	            tile.setLeafBlock(block);
	        }
	    }
	}
	
	public void initModel()
	{
	    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
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
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		if (!world.isRemote)
		{
			TileInfestedLeaves leaves = (TileInfestedLeaves) world.getTileEntity(pos);

			if (leaves != null && player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ICrook)
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
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
    {
		return new ArrayList<>();
	}

	@Override
	public EnumType getWoodType(int meta)
	{
		return null;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileInfestedLeaves();
    }
}
