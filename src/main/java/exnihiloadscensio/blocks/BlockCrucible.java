package exnihiloadscensio.blocks;

import exnihiloadscensio.tiles.TileCrucible;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockCrucible extends BlockBase implements ITileEntityProvider {

	public BlockCrucible() {
		super(Material.ROCK, "blockCrucible");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileCrucible();
	}

}
