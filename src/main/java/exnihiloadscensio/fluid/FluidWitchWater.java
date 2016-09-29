package exnihiloadscensio.fluid;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FluidWitchWater extends Fluid {

	public FluidWitchWater() {
		super("witchwater", new ResourceLocation("exnihiloadscensio:blocks/fluidWitchWaterStill"), new ResourceLocation("exnihiloadscensio:blocks/fluidWitchWaterFlow"));
		
		FluidRegistry.registerFluid(this);
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		Block block = this.getBlock();
		
		FluidStateMapper mapper = new FluidStateMapper(this);
		
		Item item = Item.getItemFromBlock(block);
		if (item != null) {
			ModelLoader.registerItemVariants(item);
			ModelLoader.setCustomMeshDefinition(item, mapper);
		}
		
		ModelLoader.setCustomStateMapper(block, mapper);		
	}

}
