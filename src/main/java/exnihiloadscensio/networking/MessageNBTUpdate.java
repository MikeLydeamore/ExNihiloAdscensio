package exnihiloadscensio.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageNBTUpdate implements IMessage {
	
	public MessageNBTUpdate(){}

	private int x, y, z;
	private NBTTagCompound tag;
	public MessageNBTUpdate(TileEntity te)
	{
		this.x = te.getPos().getX();
		this.y = te.getPos().getY();
		this.z = te.getPos().getZ();
		this.tag = te.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		ByteBufUtils.writeTag(buf, tag);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.tag = ByteBufUtils.readTag(buf);
	}

	@SideOnly(Side.CLIENT)
	public static class MessageNBTUpdateHandler implements IMessageHandler<MessageNBTUpdate, IMessage> 
	{
		@Override
		public IMessage onMessage(final MessageNBTUpdate msg, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run()
				{
					TileEntity entity =  Minecraft.getMinecraft().player.getEntityWorld().getTileEntity(new BlockPos(msg.x, msg.y, msg.z));

					if (entity != null) {
						entity.readFromNBT(msg.tag);
					}
				}
			});
			return null;
		}
	}

}
