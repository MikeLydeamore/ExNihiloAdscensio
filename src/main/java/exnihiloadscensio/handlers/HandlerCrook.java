package exnihiloadscensio.handlers;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import exnihiloadscensio.items.tools.ICrook;
import exnihiloadscensio.registries.CrookRegistry;
import exnihiloadscensio.registries.types.CrookReward;

public class HandlerCrook {

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void crook(BlockEvent.HarvestDropsEvent event)
	{
		if (event.getWorld().isRemote)
			return;

		if (event.getHarvester() == null)
			return;

		if (event.isSilkTouching())
			return;

		ItemStack held = event.getHarvester().getHeldItemMainhand();
		if (!isCrook(held))
			return;
		
		ArrayList<CrookReward> rewards = CrookRegistry.getRewards(event.getState());
		
		if (rewards != null && rewards.size() > 0)
		{
			event.getDrops().clear();
			event.setDropChance(1f);

			int fortune = event.getFortuneLevel();
			Iterator<CrookReward> it = rewards.iterator();
			while(it.hasNext())
			{
				CrookReward reward = it.next();

				if (event.getWorld().rand.nextFloat() <= reward.getChance() + (reward.getFortuneChance() * fortune))
				{
					event.getDrops().add(reward.getStack().copy());
				}

			}
		}
		
		if (event.getState().getBlock() instanceof BlockLeaves) //Simulate vanilla drops without firing event
		{
			Block block = event.getState().getBlock();
			int fortune = event.getFortuneLevel();
			java.util.List<ItemStack> items = block.getDrops(event.getWorld(), event.getPos(), event.getState(), fortune);
            for (ItemStack item : items)
            {
                if (event.getWorld().rand.nextFloat() <= event.getDropChance())
                {
                    Block.spawnAsEntity(event.getWorld(), event.getPos(), item);
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
