package exnihiloadscensio.networking;

import exnihiloadscensio.tiles.TileBarrel;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageBarrelModeUpdate implements IMessage {

	public MessageBarrelModeUpdate(){}

	private String modeName;
	private int x, y, z;
	public MessageBarrelModeUpdate(String modeName, BlockPos pos)
	{
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.modeName = modeName;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		ByteBufUtils.writeUTF8String(buf, modeName);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.modeName = ByteBufUtils.readUTF8String(buf);
	}

	public static class MessageBarrelModeUpdateHandler implements IMessageHandler<MessageBarrelModeUpdate, IMessage> 
	{
		@Override
		public IMessage onMessage(final MessageBarrelModeUpdate msg, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run()
				{
					TileEntity entity =  Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(new BlockPos(msg.x, msg.y, msg.z));
					if (entity instanceof TileBarrel)
					{
						TileBarrel te = (TileBarrel) entity;
						te.setMode(msg.modeName);
						//Minecraft.getMinecraft().thePlayer.worldObj.notifyBlockUpdate(new BlockPos(msg.x, msg.y, msg.z));
					}
				}
			});
			return null;
		}
	}
}
