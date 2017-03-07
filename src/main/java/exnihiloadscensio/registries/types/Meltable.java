package exnihiloadscensio.registries.types;

import exnihiloadscensio.util.BlockInfo;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor @EqualsAndHashCode(exclude={"textureOverride"})
public class Meltable {
	
	@Getter
	private String fluid;
	@Getter
	private int amount;
	@Getter
	private BlockInfo textureOverride;
	
	public Meltable(String fluid, int amount) {
		this.fluid = fluid;
		this.amount = amount;
		this.textureOverride = null;
	}

}
