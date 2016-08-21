package exnihiloadscensio.blocks;

import exnihiloadscensio.tiles.TileCrucible;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCrucible extends BlockBase implements ITileEntityProvider {

	public BlockCrucible() {
		super(Material.ROCK, "blockCrucible");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCrucible();
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
			return true;
		
		TileCrucible te = (TileCrucible) world.getTileEntity(pos);
		return te.onBlockActivated(heldItem, player);
	}
	
	@Override
	public boolean isFullyOpaque(IBlockState state)	{
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

}
