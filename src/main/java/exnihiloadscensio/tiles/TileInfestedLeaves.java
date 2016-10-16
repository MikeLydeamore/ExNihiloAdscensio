package exnihiloadscensio.tiles;

import exnihiloadscensio.blocks.ENBlocks;
import exnihiloadscensio.config.Config;
import exnihiloadscensio.networking.MessageInfestedLeavesUpdate;
import exnihiloadscensio.networking.PacketHandler;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.util.Util;
import lombok.Getter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileInfestedLeaves extends TileEntity implements ITickable {
    private static int tileId = 0;
    
	@Getter
	private float progress = 0;
	@Getter
	private boolean hasNearbyLeaves = true;
	
	// Stop ALL infested leaves from updating on the same tick always - this way they're evenly spread out
	// Let's hope no one gets 2 billion in their server
	private int updateIndex = tileId++ % Config.leavesUpdateFrequency;
	
	@Override
	public void update() 
    {
	    if(progress < 1.0F)
	    {
            progress += 1.0 / Config.infestedLeavesTicks;
            markDirty();
            
            if (progress > 1.0F)
            {
                progress = 1.0F;
            }
            
            if(!worldObj.isRemote)
            {
                PacketHandler.sendToAllAround(new MessageInfestedLeavesUpdate(progress, pos), this);
            }
            else if(Config.doLeavesUpdateClient && worldObj.getWorldTime() % Config.leavesUpdateFrequency == updateIndex)
            {
                IBlockState state = worldObj.getBlockState(pos);
                worldObj.notifyBlockUpdate(pos, state, state, 2);
            }
	    }
        
        // Don't update unless there's leaves nearby, or we haven't checked for leavesUpdateFrequency ticks. And only update on the server
        if (!worldObj.isRemote && hasNearbyLeaves || worldObj.getWorldTime() % Config.leavesUpdateFrequency == updateIndex)
        {
            hasNearbyLeaves = false;
            
            for (int x = -1; x <= 1; x++)
            {
                for (int y = -1; y <= 1; y++)
                {
                    for (int z = -1; z <= 1; z++)
                    {
                        BlockPos newPos = new BlockPos(pos.add(x, y, z));
                        IBlockState state = worldObj.getBlockState(newPos);
                        
                        if (state != null && state.getBlock() != null && (state.getBlock() == Blocks.LEAVES || state.getBlock() == Blocks.LEAVES2))
                        {
                            hasNearbyLeaves = true;
                            
                            if (worldObj.rand.nextFloat() < Config.leavesSpreadChance)
                            {
                                worldObj.setBlockState(newPos, ENBlocks.infestedLeaves.getDefaultState());
                            }
                        }
                    }
                }
            }
        }
    }
    
	@SideOnly(Side.CLIENT)
    public int getColor()
	{
	    if(worldObj == null || pos == null)
	    {
	        return Util.whiteColor.toInt();
	    }
	    else
	    {
	        Color green = new Color(BiomeColorHelper.getFoliageColorAtPos(worldObj, pos));
	        return Color.average(green, Util.whiteColor, progress).toInt();
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
