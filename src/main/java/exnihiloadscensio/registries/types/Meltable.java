package exnihiloadscensio.registries.types;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor @EqualsAndHashCode
public class Meltable {
	
	@Getter
	private String fluid;
	@Getter
	private int amount;

}
