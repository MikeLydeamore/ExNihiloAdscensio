package exnihiloadscensio.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
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
        block = state == null ? null : state.getBlock();
        meta = state == null ? -1 : state.getBlock().getMetaFromState(state);
    }
    
    public BlockInfo(ItemStack stack)
    {
        block = (stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemBlock)) ? null : Block.getBlockFromItem(stack.getItem());
        meta = (stack == null || stack.getItem() == null) ? null : stack.getItemDamage();
    }
    
    public BlockInfo(String string)
    {
        String[] split = string.split(":");
        
        if(split.length == 1)
        {
            block = Block.getBlockFromName("minecraft:" + split[0]);
        }
        else if(split.length == 2)
        {
            try
            {
                meta = split[1].equals("*") ? -1 : Integer.parseInt(split[1]);
                block = Block.getBlockFromName("minecraft:" + split[0]);
            }
            catch(NumberFormatException e)
            {
                meta = -1;
                block = Block.getBlockFromName(split[0] + ":" + split[1]);
            }
        }
        else if(split.length == 3)
        {
            try
            {
                meta = split[2].equals("*") ? -1 : Integer.parseInt(split[2]);
                block = Block.getBlockFromName(split[0] + ":" + split[1]);
            }
            catch(NumberFormatException e)
            {
                meta = -1;
            }
        }
        else
        {
            meta = -1;
        }
    }
    
    public String toString()
    {
        return Block.REGISTRY.getNameForObject(block) + (meta == -1 ? "" : (":" + meta));
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
        return block == null ? null : block.getStateFromMeta(meta == -1 ? 0 : meta);
    }
    
    public int hashCode()
    {
        return block == null ? 37 : block.hashCode();
    }
    
    public boolean equals(Object other)
    {
        if (other instanceof BlockInfo)
        {
            BlockInfo info = (BlockInfo) other;
            
            if(block == null || info.block == null)
            {
                return false;
            }
            
            if (meta == -1 || info.meta == -1)
            {
                return block.equals(info.block);
            }
            else
            {
                return meta == info.meta && block.equals(info.block);
            }
        }
        
        return false;
    }
    
    public static boolean areEqual(BlockInfo block1, BlockInfo block2) {
    	if (block1 == null && block2 == null)
    		return true;
    	
    	if (block1 == null && block2 != null)
    		return false;
    	
    	if (block1 != null && block2 == null)
    		return false;
    	
    	return block1.equals(block2);
    }
}
