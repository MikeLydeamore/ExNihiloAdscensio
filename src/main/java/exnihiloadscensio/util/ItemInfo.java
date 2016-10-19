package exnihiloadscensio.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

@AllArgsConstructor
public class ItemInfo
{
    
    @Getter
    private Item item;
    @Getter
    private int meta;
    
    public static ItemInfo getItemInfoFromStack(ItemStack stack)
    {
        return new ItemInfo(stack.getItem(), stack.getItemDamage());
    }
    
    public ItemInfo(ItemStack stack)
    {
        item = stack.getItem();
        meta = stack.getItemDamage();
    }
    
    public ItemInfo(Block block, int meta)
    {
        item = Item.getItemFromBlock(block);
        this.meta = meta;
    }
    
    public ItemInfo(String string)
    {
        String[] arr = string.split(":");
        
        item = Item.getByNameOrId(arr[0] + ":" + arr[1]);
        
        if (arr.length == 3)
        {
            meta = Integer.parseInt(arr[2]);
        }
        else
        {
            meta = -1;
        }
    }
    
    public ItemInfo(IBlockState state)
    {
        this.item = Item.getItemFromBlock(state.getBlock());
        this.meta = state.getBlock().getMetaFromState(state);
    }
    
    public String toString()
    {
        return Item.REGISTRY.getNameForObject(item) + (meta == -1 ? "" : (":" + meta));
    }
    
    public ItemStack getItemStack()
    {
        return new ItemStack(item, 1, meta == -1 ? 0 : meta);
    }
    
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setString("item", Item.REGISTRY.getNameForObject(item).toString());
        tag.setInteger("meta", meta);
        
        return tag;
    }
    
    public static ItemInfo readFromNBT(NBTTagCompound tag)
    {
        Item item_ = Item.REGISTRY.getObject(new ResourceLocation(tag.getString("item")));
        int meta_ = tag.getInteger("meta");
        
        return new ItemInfo(item_, meta_);
    }
    
    public int hashCode()
    {
        return item == null ? 41 : item.hashCode();
    }
    
    public boolean equals(Object other)
    {
        if (other instanceof ItemInfo)
        {
            ItemInfo info = (ItemInfo) other;
            
            if (meta == -1 || info.meta == -1)
            {
                return item == null ? info.item == null : item.equals(info.item);
            }
            else
            {
                return  meta == info.meta && (item == null ? info.item == null : item.equals(info.item));
            }
        }
        
        return false;
    }
}
