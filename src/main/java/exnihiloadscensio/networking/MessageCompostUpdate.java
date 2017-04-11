package exnihiloadscensio.networking;

import exnihiloadscensio.barrel.modes.compost.BarrelModeCompost;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.tiles.TileBarrel;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageCompostUpdate implements IMessage {
	
	public MessageCompostUpdate(){}

	private float fillAmount;
	private int x, y, z;
	private float r, g, b, a;
	private float progress;
	public MessageCompostUpdate(float fillAmount, Color color, float progress, BlockPos pos)
	{
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.fillAmount = fillAmount;
		this.r = color.r;
		this.g = color.g;
		this.b = color.b;
		this.a = color.a;
		this.progress = progress;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeFloat(fillAmount);
		buf.writeFloat(r);
		buf.writeFloat(g);
		buf.writeFloat(b);
		buf.writeFloat(a);
		buf.writeFloat(progress);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.fillAmount = buf.readFloat();
		this.r = buf.readFloat();
		this.g = buf.readFloat();
		this.b = buf.readFloat();
		this.a = buf.readFloat();
		this.progress = buf.readFloat();
	}

	public static class MessageCompostAmountUpdateHandler implements IMessageHandler<MessageCompostUpdate, IMessage> 
	{
		@Override
		public IMessage onMessage(final MessageCompostUpdate msg, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run()
				{
					TileEntity entity =  Minecraft.getMinecraft().player.world.getTileEntity(new BlockPos(msg.x, msg.y, msg.z));
					if (entity instanceof TileBarrel)
					{
						TileBarrel te = (TileBarrel) entity;
						BarrelModeCompost mode = (BarrelModeCompost) te.getMode();
						mode.setFillAmount(msg.fillAmount);
						mode.setColor(new Color(msg.r, msg.g, msg.b, msg.a));
						mode.setProgress(msg.progress);
					}
				}
			});
			return null;
		}
	}

}
