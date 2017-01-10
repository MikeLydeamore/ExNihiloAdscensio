package exnihiloadscensio.blocks;

import exnihiloadscensio.tiles.TileSievePress;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSievePress extends BlockBase implements ITileEntityProvider {

	public BlockSievePress() {
		super(Material.ROCK, "blockSievePress");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileSievePress();
	}

}
