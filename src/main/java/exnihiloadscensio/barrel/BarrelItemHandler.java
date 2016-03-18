package exnihiloadscensio.barrel;

import java.util.ArrayList;

import exnihiloadscensio.networking.MessageBarrelModeUpdate;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.BarrelModeRegistry;
import exnihiloadscensio.registries.BarrelModeRegistry.TriggerType;
import exnihiloadscensio.tiles.TileBarrel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class BarrelItemHandler extends ItemStackHandler {
	
	private TileBarrel barrel;
	
	public BarrelItemHandler(TileBarrel barrel)
	{
		super(1);
		this.barrel = barrel;
	}
	
	@Override
	protected int getStackLimit(int slot, ItemStack stack)
	{
		return 1;
	}
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		ArrayList<IBarrelMode> modes = BarrelModeRegistry.getModes(TriggerType.ITEM);
		if (modes == null)
			return stack;
		for (IBarrelMode possibleMode : modes)
		{
			if (possibleMode.isTriggerItemStack(stack))
			{
				if (!simulate)
				{
					barrel.setMode(possibleMode.getClass().getName());
					PacketHandler.sendToAllAround(new MessageBarrelModeUpdate(barrel.getMode().getClass().getName(), barrel.getPos()), barrel);
					barrel.getMode().addItem(stack, barrel);
					barrel.markDirty();
					barrel.getWorld().markBlockForUpdate(barrel.getPos());
				}
				
				ItemStack ret = stack.copy();
				ret.stackSize--;
				return ret;
			}
		}
		
		return null;
	}

}
