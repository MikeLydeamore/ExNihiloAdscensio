package exnihiloadscensio.tiles;

import exnihiloadscensio.blocks.BlockSieve.MeshType;
import exnihiloadscensio.util.ItemInfo;
import net.minecraft.tileentity.TileEntity;

public class TileSieve extends TileEntity {
	
	private ItemInfo currentStack;
	private byte progress = 0;
	
	private MeshType mesh = MeshType.NONE;
	
	public TileSieve() {}
	
	/**
	 * Sets the mesh type in the sieve.
	 * @param newMesh
	 * @return true if setting is successful.
	 */
	public boolean setMesh(MeshType newMesh) {
		if (mesh == MeshType.NONE) {
			mesh = newMesh;
			return true;
		}
		
		return false;
	}

}
