package exnihiloadscensio.handlers;

import java.util.List;

import exnihiloadscensio.items.tools.IHammer;
import exnihiloadscensio.registries.HammerRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HandlerHammer {
	
	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void hammer(BlockEvent.HarvestDropsEvent event)
	{
		if (event.getWorld().isRemote || event.getHarvester() == null || event.isSilkTouching())
			return;

		ItemStack held = event.getHarvester().getHeldItemMainhand();
		
		if (!isHammer(held))
			return;
		
		List<ItemStack> rewards = HammerRegistry.getRewardDrops(event.getWorld().rand, event.getState(), ((IHammer) held.getItem()).getMiningLevel(held), event.getFortuneLevel());
		
		if (rewards != null && rewards.size() > 0)
		{
			event.getDrops().clear();
			event.setDropChance(1.0F);
			event.getDrops().addAll(rewards);
		}
	}


	public boolean isHammer(ItemStack stack)
	{
		if (stack == null || stack.getItem() == null)
			return false;
		
		if (stack.getItem() instanceof IHammer)
			return ((IHammer) stack.getItem()).isHammer(stack);
		
		return false;
	}

}
