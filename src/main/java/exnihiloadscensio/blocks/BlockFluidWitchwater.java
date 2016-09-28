package exnihiloadscensio.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockFluidWitchwater extends BlockFluidClassic {
	
	public BlockFluidWitchwater() {
		super(ENBlocks.fluidWitchwater, Material.WATER);
		
		this.setRegistryName("blockWitchwater");
		this.setUnlocalizedName("blockWitchwater");
		GameRegistry.<Block>register(this);
	}

}
