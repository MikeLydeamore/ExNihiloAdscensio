package exnihiloadscensio.blocks;

import net.minecraft.block.material.Material;

public class BlockBarrel extends BlockBase {
	
	public BlockBarrel()
	{
		super(Material.wood, "blockBarrel");
		this.setBlockBounds(0.125f, 0, 0.125f, 0.875f, 1f, 0.875f);
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

}
