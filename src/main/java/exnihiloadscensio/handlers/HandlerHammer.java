package exnihiloadscensio.handlers;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import exnihiloadscensio.items.tools.IHammer;
import exnihiloadscensio.registries.HammerRegistry;
import exnihiloadscensio.registries.HammerReward;

public class HandlerHammer {
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void hammer(BlockEvent.HarvestDropsEvent event)
	{
		if (event.world.isRemote)
			return;

		if (event.harvester == null)
			return;

		if (event.isSilkTouching)
			return;

		ItemStack held = event.harvester.getHeldItem();
		if (!isHammer(held))
			return;
		
		int miningLevel = ((IHammer) held.getItem()).getMiningLevel(held);
		ArrayList<HammerReward> rewards = HammerRegistry.getRewards(event.state, miningLevel);
		if (rewards != null && rewards.size() > 0)
		{
			event.drops.clear();
			event.dropChance = 1f;

			int fortune = EnchantmentHelper.getFortuneModifier(event.harvester);
			Iterator<HammerReward> it = rewards.iterator();
			while(it.hasNext())
			{
				HammerReward reward = it.next();

				if (event.world.rand.nextFloat() <= reward.getChance() + (reward.getFortuneChance() * fortune))
				{
					event.drops.add(reward.getStack().copy());
				}

			}
		}

	}


	public boolean isHammer(ItemStack stack)
	{
		if (stack == null)
			return false;
		
		if (stack.getItem() == null)
			return false;

		if (stack.getItem() instanceof IHammer)
			return ((IHammer) stack.getItem()).isHammer(stack);

		//if (stack.hasTagCompound() && stack.stackTagCompound.getBoolean("Hammered"))
		//	return true;

		return false;
	}

}
