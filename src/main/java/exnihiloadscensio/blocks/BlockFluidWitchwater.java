package exnihiloadscensio.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockFluidWitchwater extends BlockFluidClassic {
	
	public BlockFluidWitchwater() {
		super(ENBlocks.fluidWitchwater, Material.WATER);
		
		this.setRegistryName("witchwater");
		this.setUnlocalizedName("witchwater");
		GameRegistry.<Block>register(this);
	}

}
