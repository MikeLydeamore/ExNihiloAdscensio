package exnihiloadscensio.networking;

import exnihiloadscensio.tiles.TileInfestedLeaves;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageInfestedLeavesUpdate implements IMessage {

	public MessageInfestedLeavesUpdate(){}

	private int x, y, z;
	private float progress;
	public MessageInfestedLeavesUpdate(float progress, BlockPos pos)
	{
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.progress = progress;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeFloat(progress);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.progress = buf.readFloat();
	}

	public static class MessageInfestedLeavesUpdateHandler implements IMessageHandler<MessageInfestedLeavesUpdate, IMessage> 
	{
		@Override
		public IMessage onMessage(final MessageInfestedLeavesUpdate msg, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run()
				{
					TileEntity entity =  Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(new BlockPos(msg.x, msg.y, msg.z));
					if (entity instanceof TileInfestedLeaves)
					{
						TileInfestedLeaves te = (TileInfestedLeaves) entity;
						te.setProgress(msg.progress);
					}
				}
			});
			return null;
		}
	}
}
