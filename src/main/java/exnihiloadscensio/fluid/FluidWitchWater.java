package exnihiloadscensio.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class FluidWitchWater extends Fluid {

	public FluidWitchWater() {
		super("witchwater", new ResourceLocation("exnihiloadscensio:blocks/fluidWitchWaterStill"), new ResourceLocation("exnihiloadscensio:blocks/fluidWitchWaterFlowing"));
		
		FluidRegistry.registerFluid(this);
	}

}
