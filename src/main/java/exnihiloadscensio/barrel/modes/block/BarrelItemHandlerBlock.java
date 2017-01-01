package exnihiloadscensio.barrel.modes.block;

import exnihiloadscensio.networking.MessageBarrelModeUpdate;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.tiles.TileBarrel;
import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class BarrelItemHandlerBlock extends ItemStackHandler
{
    @Setter
    private TileBarrel barrel;
    
    public BarrelItemHandlerBlock(TileBarrel barrel)
    {
        super(1);
        this.barrel = barrel;
    }
    
    protected int getStackLimit(int slot, ItemStack stack)
    {
        return stack.getMaxStackSize();
    }
    
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
    {
        ItemStack returned = super.insertItem(slot, stack, simulate);
        
        if (!simulate)
        {
            PacketHandler.sendNBTUpdate(barrel);
        }
        
        return returned;
    }
    
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        ItemStack ret = super.extractItem(slot, amount, simulate);
        
        checkEmpty();
        
        return ret;
    }
    
    @Override
    public void setStackInSlot(int slot, ItemStack stack)
    {
        super.setStackInSlot(slot, stack);
        
        checkEmpty();
    }
    
    private void checkEmpty()
    {
        if (getStackInSlot(0) == null)
        {
            barrel.setMode("null");
            PacketHandler.sendToAllAround(new MessageBarrelModeUpdate("null", barrel.getPos()), barrel);
        }
    }
}
