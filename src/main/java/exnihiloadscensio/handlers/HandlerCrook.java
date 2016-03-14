package exnihiloadscensio.handlers;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeavesBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import exnihiloadscensio.items.tools.ICrook;
import exnihiloadscensio.registries.CrookRegistry;
import exnihiloadscensio.registries.CrookReward;

public class HandlerCrook {

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void crook(BlockEvent.HarvestDropsEvent event)
	{
		if (event.world.isRemote)
			return;

		if (event.harvester == null)
			return;

		if (event.isSilkTouching)
			return;

		ItemStack held = event.harvester.getHeldItem();
		if (!isCrook(held))
			return;
		
		ArrayList<CrookReward> rewards = CrookRegistry.getRewards(event.state);
		if (rewards != null && rewards.size() > 0)
		{
			event.drops.clear();
			event.dropChance = 1f;

			int fortune = EnchantmentHelper.getFortuneModifier(event.harvester);
			Iterator<CrookReward> it = rewards.iterator();
			while(it.hasNext())
			{
				CrookReward reward = it.next();

				if (event.world.rand.nextFloat() <= reward.getChance() + (reward.getFortuneChance() * fortune))
				{
					event.drops.add(reward.getStack().copy());
				}

			}
		}
		
		if (event.state.getBlock() instanceof BlockLeavesBase) //Simulate vanilla drops without firing event
		{
			Block block = event.state.getBlock();
			int fortune = EnchantmentHelper.getFortuneModifier(event.harvester);
			java.util.List<ItemStack> items = block.getDrops(event.world, event.pos, event.state, fortune);
            for (ItemStack item : items)
            {
                if (event.world.rand.nextFloat() <= event.dropChance)
                {
                    Block.spawnAsEntity(event.world, event.pos, item);
                }
            }
		}

	}


	public boolean isCrook(ItemStack stack)
	{
		if (stack == null)
			return false;
		
		if (stack.getItem() == null)
			return false;

		if (stack.getItem() instanceof ICrook)
			return ((ICrook) stack.getItem()).isCrook(stack);

		//if (stack.hasTagCompound() && stack.stackTagCompound.getBoolean("Hammered"))
		//	return true;

		return false;
	}
}
