package exnihiloadscensio.blocks;

import exnihiloadscensio.tiles.TileBarrel;
import lombok.Getter;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBarrel extends BlockBase implements ITileEntityProvider {

	private AxisAlignedBB boundingBox = new AxisAlignedBB(0.0625f, 0, 0.0625f, 0.9375f, 1f, 0.9375f);
	@Getter
	private int tier;
	
	public BlockBarrel(int tier, Material material)
	{
		super(material, "blockBarrel" + tier);
        this.tier = tier;
		this.setHardness(2.0f);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
			return true;

		return ((TileBarrel) world.getTileEntity(pos)).onBlockActivated(world, pos, state, player, side, hitX, hitY, hitZ);
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

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileBarrel(this.tier);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return boundingBox;
	}
	
    @Override
    public boolean isBlockSolid(IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        return false;
    }
    
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

}
