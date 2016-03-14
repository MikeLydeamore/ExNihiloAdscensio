package exnihiloadscensio.blocks;

import exnihiloadscensio.tiles.TileBarrel;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockBarrel extends BlockBase implements ITileEntityProvider {
	
	public BlockBarrel()
	{
		super(Material.wood, "blockBarrel");
		this.setBlockBounds(0.0625f, 0, 0.0625f, 0.9375f, 1f, 0.9375f);
		this.setHardness(2.0f);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		if (world.isRemote)
			return true;
		
		return ((TileBarrel) world.getTileEntity(pos)).onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);
    }
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileBarrel();
	}

}
