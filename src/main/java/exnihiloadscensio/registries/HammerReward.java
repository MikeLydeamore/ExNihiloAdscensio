package exnihiloadscensio.registries;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.item.ItemStack;

@AllArgsConstructor
public class HammerReward {
	
	@Getter
	private ItemStack stack;
	@Getter
	private int miningLevel;
	@Getter
	private float chance;
	@Getter
	private float fortuneChance;
}
