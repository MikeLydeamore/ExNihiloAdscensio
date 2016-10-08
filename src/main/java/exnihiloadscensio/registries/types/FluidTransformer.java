package exnihiloadscensio.registries.types;

import exnihiloadscensio.util.BlockInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class FluidTransformer {
	
	@Getter
	private String inputFluid;
	
	@Getter
	private String outputFluid;
	
	@Getter
	private BlockInfo[] transformingBlocks;
	
	@Getter
	private BlockInfo[] blocksToSpawn;
	
	public FluidTransformer(String inputFluid, String outputFluid, BlockInfo[] transformingBlocks, BlockInfo[] blocksToSpawn) {
		this.inputFluid = inputFluid;
		this.outputFluid = outputFluid;
		this.transformingBlocks = transformingBlocks;
	}

}
