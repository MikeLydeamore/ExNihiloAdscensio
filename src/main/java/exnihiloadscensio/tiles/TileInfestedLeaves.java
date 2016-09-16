package exnihiloadscensio.tiles;

import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.networking.MessageInfestedLeavesUpdate;
import exnihiloadscensio.networking.PacketHandler;
import lombok.Getter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileInfestedLeaves extends TileEntity implements ITickable {
	
	@Getter
	private float progress = 0;
	
	@Override
	public void update() 
	{
		
		if (!worldObj.isRemote && progress < 1)
		{
			progress += 1.0/200;
			this.markDirty();
			
			if (progress > 1)
				progress = 1;
			PacketHandler.sendToAllAround(new MessageInfestedLeavesUpdate(progress, pos), this);
		}
		
		if (worldObj.getWorldTime() % 40 == 0)
		{
			IBlockState state = worldObj.getBlockState(pos);
			this.worldObj.notifyBlockUpdate(pos, state, state, 2);
		}
		
		for (int x = -1; x <=1 ; x++) {
			for (int y = -1; y <= 1 ; y++) {
				for (int z = -1; z <= 1 ; z++) {
					BlockPos newPos = new BlockPos(pos.add(x, y, z));
					IBlockState state = worldObj.getBlockState(newPos);
					if (state != null && state.getBlock() != null && (state.getBlock() == Blocks.LEAVES || state.getBlock() == Blocks.LEAVES2) ) {
						if (worldObj.rand.nextFloat() < 0.002)
							worldObj.setBlockState(newPos, ENBlocks.infestedLeaves.getDefaultState());
					}
				}
			}
		}
	}
	
	public void setProgress(float progress)
	{
		this.progress = progress;
		this.markDirty();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setFloat("progress", progress);
		return super.writeToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		progress = tag.getFloat("progress");
		super.readFromNBT(tag);
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
    {
		NBTTagCompound tag = this.writeToNBT(new NBTTagCompound());

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
