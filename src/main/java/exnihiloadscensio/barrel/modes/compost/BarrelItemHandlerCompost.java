package exnihiloadscensio.barrel.modes.compost;

import lombok.Setter;
import exnihiloadscensio.networking.MessageBarrelModeUpdate;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.CompostRegistry;
import exnihiloadscensio.tiles.TileBarrel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class BarrelItemHandlerCompost extends ItemStackHandler {
	
	@Setter
	TileBarrel barrel;
	public BarrelItemHandlerCompost(TileBarrel barrel)
	{
		super(1);
		this.barrel = barrel;
	}
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		if (CompostRegistry.containsItem(stack))
		{
			BarrelModeCompost mode = (BarrelModeCompost) this.barrel.getMode();
			if (mode != null && mode.getFillAmount() < 1)
			{
				ItemStack ret = stack.copy();
				ret.stackSize--;
				
				if (!simulate)
					mode.addItem(stack, barrel);
				
				return ret;
			}
		}
		
		return stack;
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		BarrelModeCompost mode = (BarrelModeCompost) this.barrel.getMode();
		if (mode != null && mode.getProgress() >= 1)
		{
			if (!simulate)
				mode.removeItem(barrel);
			barrel.setMode("null");
			PacketHandler.sendToAllAround(new MessageBarrelModeUpdate("null", barrel.getPos()), barrel);
			return super.extractItem(slot, amount, simulate);
		}
		
		return null;
	}

}
