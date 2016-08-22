package exnihiloadscensio.tiles;

import java.util.ArrayList;
import java.util.Random;

import lombok.Getter;
import exnihiloadscensio.registries.SieveRegistry;
import exnihiloadscensio.registries.types.Siftable;
import exnihiloadscensio.util.ItemInfo;
import exnihiloadscensio.util.Util;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileSieve extends TileEntity {
	
	private ItemInfo currentStack;
	private byte progress = 0;
	@Getter
	private ItemStack meshStack;
	
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
			currentStack = new ItemInfo(stack);
			return true;
		}
		
		return false;
	}
	
	public void doSieving(EntityPlayer player) {
		if (currentStack == null)
			return;
		
		progress += 10;
		
		if (progress >= 100) {
			//Time to drop!
			ArrayList<Siftable> siftables = SieveRegistry.getDrops(currentStack);
			if (siftables == null || siftables.size() == 0) {
				//Probably shouldn't happen, but weird shit always does.
				resetSieve();
				return;
			}
			int meshLevel = meshStack.getItemDamage();
			for (Siftable siftable : siftables) {
				if (meshLevel != siftable.getMeshLevel())
					continue;
				if (rand.nextDouble() < siftable.getChance()) {
					ItemStack dropStack = siftable.getDrop().getItemStack();
					Util.dropItemInWorld(this, player, dropStack, 0.02d);
				}
			}
			resetSieve();
		}
	}
	
	private void resetSieve() {
		progress = 0;
		currentStack = null;
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
			currentStack = ItemInfo.readFromNBT(tag.getCompoundTag("stack"));
		
		if (tag.hasKey("mesh"))
			meshStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("mesh"));
		
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
