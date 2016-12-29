package exnihiloadscensio.barrel;

import java.util.ArrayList;
import java.util.List;

import exnihiloadscensio.networking.MessageBarrelModeUpdate;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.BarrelModeRegistry;
import exnihiloadscensio.registries.BarrelModeRegistry.TriggerType;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.util.LogUtil;
import net.minecraft.block.state.IBlockState;
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
    public ItemStack getStackInSlot(int slot)
    {
        if (barrel.getMode() != null && barrel.getMode().getHandler(barrel) != null)
        {
            return barrel.getMode().getHandler(barrel).getStackInSlot(slot);
        }
        
        return null;
    }
    
    @Override
    public void setStackInSlot(int slot, ItemStack stack)
    {
        if(barrel.getMode() != null && barrel.getMode().isTriggerItemStack(stack))
        {
            barrel.getMode().addItem(stack, barrel);
            barrel.markDirty();
            
            IBlockState state = barrel.getWorld().getBlockState(barrel.getPos());
            barrel.getWorld().setBlockState(barrel.getPos(), state);
        }
        else if (barrel.getMode() != null && barrel.getMode().getHandler(barrel) != null)
        {
            barrel.getMode().getHandler(barrel).setStackInSlot(slot, stack);
        }
        else if(barrel.getMode() == null)
        {
            List<IBarrelMode> modes = BarrelModeRegistry.getModes(TriggerType.ITEM);
            
            if(modes != null)
            {
                for (IBarrelMode possibleMode : modes)
                {
                    if (possibleMode.isTriggerItemStack(stack))
                    {
                        barrel.setMode(possibleMode.getName());
                        PacketHandler.sendToAllAround(new MessageBarrelModeUpdate(barrel.getMode().getName(), barrel.getPos()), barrel);
                        
                        barrel.getMode().addItem(stack, barrel);
                        barrel.markDirty();
                        
                        IBlockState state = barrel.getWorld().getBlockState(barrel.getPos());
                        barrel.getWorld().setBlockState(barrel.getPos(), state);
                    }
                }
            }
        }
    }
    
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        if (barrel.getMode() != null && barrel.getMode().getHandler(barrel) != null)
        {
			return barrel.getMode().getHandler(barrel).extractItem(slot, amount, simulate);
        }
		
		return null;
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
	    if(stack != null)
	    {
	        LogUtil.info("Item Inserted: " + stack.stackSize + " x " + stack.getItem().getRegistryName().toString() + " Simulate: " + simulate);
	    }
	    
		if (barrel.getMode() == null)
		{
			ArrayList<IBarrelMode> modes = BarrelModeRegistry.getModes(TriggerType.ITEM);
			
			if (modes == null)
			{
				return stack;
			}
			
			for (IBarrelMode possibleMode : modes)
			{
				if (possibleMode.isTriggerItemStack(stack))
				{
					if (!simulate)
					{
						barrel.setMode(possibleMode.getName());
						PacketHandler.sendToAllAround(new MessageBarrelModeUpdate(barrel.getMode().getName(), barrel.getPos()), barrel);
						barrel.getMode().addItem(stack, barrel);
						barrel.markDirty();
						IBlockState state = barrel.getWorld().getBlockState(barrel.getPos());
						barrel.getWorld().setBlockState(barrel.getPos(), state);
					}

					ItemStack ret = stack.copy();
					ret.stackSize--;
					return ret;
				}
			}

			return stack;
		}
		else
		{
			if (barrel.getMode().getHandler(barrel)!= null)
				return barrel.getMode().getHandler(barrel).insertItem(slot, stack, simulate);
		}
		
		return stack;
	}

}
