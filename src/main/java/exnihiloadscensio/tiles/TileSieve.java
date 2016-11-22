package exnihiloadscensio.tiles;

import java.util.List;
import java.util.Random;

import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.registries.SieveRegistry;
import exnihiloadscensio.registries.types.Siftable;
import exnihiloadscensio.util.BlockInfo;
import exnihiloadscensio.util.Util;
import lombok.Getter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileSieve extends TileEntity {
	
	private BlockInfo currentStack;
	@Getter
	private byte progress = 0;
	@Getter
	private ItemStack meshStack;
	
	private long lastSieveAction = 0;
	
	private static Random rand = new Random();
	
	public TileSieve() {}
	
	/**
	 * Sets the mesh type in the sieve.
	 * @param newMesh
	 * @return true if setting is successful.
	 */
	public boolean setMesh(ItemStack newMesh) {
		return setMesh(newMesh, false);
	}
	
	public boolean setMesh(ItemStack newMesh, boolean simulate) {
		if (progress != 0)
			return false;
		
		if (meshStack == null) {
			if (!simulate) {
				meshStack = newMesh.copy();
				this.markDirty();
			}
			return true;
		}
		
		if (meshStack != null && newMesh == null) {
			//Removing
			if (!simulate) {
				meshStack = null;
				this.markDirty();
			}
			return true;
		}
		
		return false;
		
	}
	
	public boolean addBlock(ItemStack stack) {
		if (currentStack == null && SieveRegistry.canBeSifted(stack)) {
			if (meshStack == null)
				return false;
			int meshLevel = meshStack.getItemDamage();
			for (Siftable siftable : SieveRegistry.getDrops(stack)) {
				if (siftable.getMeshLevel() == meshLevel) {
					currentStack = new BlockInfo(stack);
					PacketHandler.sendNBTUpdate(this);
					return true;
				}
			}
		}
		
		return false;
	}
    
    public boolean doSieving(EntityPlayer player)
    {
        if (currentStack == null) {
            return false;
        }
        
        if (lastSieveAction == worldObj.getWorldTime())
        	return false;
        
        lastSieveAction = worldObj.getWorldTime();
        
        int efficiency = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, meshStack);
        int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, meshStack);
        
        progress += 10 + 5 * efficiency;
        PacketHandler.sendNBTUpdate(this);
        
        if (progress >= 100)
        {
            List<ItemStack> drops = SieveRegistry.getRewardDrops(rand, currentStack.getBlockState(), meshStack.getMetadata(), fortune);
            
            if(drops != null)
            {
                drops.forEach(stack -> Util.dropItemInWorld(this, player, stack, 1));
            }
            
            resetSieve();
        }
        
        return true;
    }
	
	private void resetSieve() {
		progress = 0;
		currentStack = null;
		PacketHandler.sendNBTUpdate(this);
	}
	
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getTexture() {
		if (currentStack != null) {
			return Util.getTextureFromBlockState(currentStack.getBlockState());
		}
		return null;
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		if (currentStack != null) {
			NBTTagCompound stackTag = currentStack.writeToNBT(new NBTTagCompound());
			tag.setTag("stack", stackTag);
		}
		
		if (meshStack != null) {
			NBTTagCompound meshTag = meshStack.writeToNBT(new NBTTagCompound());
			tag.setTag("mesh", meshTag);
		}
		
		tag.setByte("progress", progress);
		
		return super.writeToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		
		if (tag.hasKey("stack"))
			currentStack = BlockInfo.readFromNBT(tag.getCompoundTag("stack"));
		else 
			currentStack = null;
		
		if (tag.hasKey("mesh"))
			meshStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("mesh"));
		else
			meshStack = null;
		
		progress = tag.getByte("progress");
		
		super.readFromNBT(tag);
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);

		return new SPacketUpdateTileEntity(this.pos, this.getBlockMetadata(), tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		NBTTagCompound tag = pkt.getNbtCompound();
		readFromNBT(tag);
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound tag = writeToNBT(new NBTTagCompound());
		return tag;
	}

}
