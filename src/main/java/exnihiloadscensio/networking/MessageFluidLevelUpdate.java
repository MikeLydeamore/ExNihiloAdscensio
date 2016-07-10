package exnihiloadscensio.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import exnihiloadscensio.barrel.modes.compost.BarrelModeCompost;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.tiles.TileBarrel;

public class MessageFluidLevelUpdate implements IMessage {
	
	public MessageFluidLevelUpdate(){}

	private int fillAmount;
	private int x, y, z;

	public MessageFluidLevelUpdate(int fillAmount, BlockPos pos)
	{
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.fillAmount = fillAmount;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(fillAmount);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.x = buf.readInt();
		this.y = buf.readInt();
		this.z = buf.readInt();
		this.fillAmount = buf.readInt();
	}

	public static class MessageFluidLevelUpdateHandler implements IMessageHandler<MessageFluidLevelUpdate, IMessage> 
	{
		@Override
		public IMessage onMessage(final MessageFluidLevelUpdate msg, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run()
				{
					TileEntity entity =  Minecraft.getMinecraft().thePlayer.worldObj.getTileEntity(new BlockPos(msg.x, msg.y, msg.z));
					if (entity instanceof TileBarrel)
					{
						TileBarrel te = (TileBarrel) entity;
						FluidStack f = te.getTank().getFluid();
						if (f != null) {
							System.out.println("test");
							f.amount = msg.fillAmount;
							te.getTank().setFluid(f);
						}
					}
				}
			});
			return null;
		}
	}
}
