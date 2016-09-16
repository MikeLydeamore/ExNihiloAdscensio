package exnihiloadscensio.registries.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.item.ItemStack;

@AllArgsConstructor
public class CrookReward {
	
	@Getter
	private ItemStack stack;
	@Getter
	private float chance;
	@Getter
	private float fortuneChance;

}
