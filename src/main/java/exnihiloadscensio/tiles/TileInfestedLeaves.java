package exnihiloadscensio.tiles;

import exnihiloadscensio.config.Config;
import exnihiloadscensio.networking.MessageInfestedLeavesUpdate;
import exnihiloadscensio.networking.PacketHandler;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileInfestedLeaves extends TileEntity implements ITickable {
	
	@Getter
	private float progress;

	public TileInfestedLeaves()
	{
		progress = 0;
	}
	
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
			worldObj.notifyBlockUpdate(pos, state, state, 3);
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
		System.out.println(tag.getFloat("progress"));
		progress = tag.getFloat("progress");
		super.writeToNBT(tag);
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
	

}
