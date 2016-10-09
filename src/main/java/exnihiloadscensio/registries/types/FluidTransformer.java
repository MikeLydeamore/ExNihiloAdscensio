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
	
	@Getter
	private int duration;
	
	public FluidTransformer(String inputFluid, String outputFluid, int duration, BlockInfo[] transformingBlocks, BlockInfo[] blocksToSpawn) {
		this.inputFluid = inputFluid;
		this.outputFluid = outputFluid;
		this.transformingBlocks = transformingBlocks;
		this.blocksToSpawn = blocksToSpawn;
		this.duration = duration;
	}

}
