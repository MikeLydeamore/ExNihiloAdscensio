package exnihiloadscensio.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

@AllArgsConstructor
public class BlockInfo
{
    @Getter
    private Block block;
    
    @Getter
    private int meta;
    
    public BlockInfo(IBlockState state)
    {
        block = state.getBlock();
        meta = state.getBlock().getMetaFromState(state);
    }
    
    public BlockInfo(ItemStack stack)
    {
        block = Block.getBlockFromItem(stack.getItem());
        meta = stack.getItemDamage();
    }
    
    public BlockInfo(String string)
    {
        String[] arr = string.split(":");
        block = Block.getBlockFromName(arr[0] + ":" + arr[1]);
        
        if (arr.length == 3)
        {
            meta = Integer.parseInt(arr[2]);
        }
        else
        {
            meta = -1;
        }
    }
    
    public String toString()
    {
        return Block.REGISTRY.getNameForObject(block) + ":" + meta;
    }
    
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setString("block", Block.REGISTRY.getNameForObject(block).toString());
        tag.setInteger("meta", meta);
        
        return tag;
    }
    
    public static BlockInfo readFromNBT(NBTTagCompound tag)
    {
        Block item_ = Block.REGISTRY.getObject(new ResourceLocation(tag.getString("block")));
        int meta_ = tag.getInteger("meta");
        
        return new BlockInfo(item_, meta_);
    }
    
    @SuppressWarnings("deprecation")
    public IBlockState getBlockState()
    {
        return block.getStateFromMeta(meta == -1 ? 0 : meta);
    }
    
    public int hashCode()
    {
        return block.hashCode();
    }
    
    public boolean equals(Object other)
    {
        if (other instanceof BlockInfo)
        {
            BlockInfo info = (BlockInfo) other;
            
            if (meta == -1 || info.meta == -1)
            {
                return block.equals(info.block);
            }
            else
            {
                return block.equals(info.block) && meta == info.meta;
            }
        }
        
        return false;
    }
}
