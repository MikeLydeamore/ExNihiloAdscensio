package exnihiloadscensio.networking;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// When all else fails, do it yourself
// (I couldn't find any way to force a lighting update on the client without some blockstate hackery)
// (I thought packet hackery would be better)
public class MessageCheckLight implements IMessage
{
    @Getter
    private int x, y, z;
    
    public MessageCheckLight()
    {
        
    }
    
    public MessageCheckLight(BlockPos pos)
    {
        x = pos.getX();
        y = pos.getY();
        z = pos.getZ();
    }
    
    @Override
    public void fromBytes(ByteBuf buffer)
    {
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
    }

    public static class MessageCheckLightHandler implements IMessageHandler<MessageCheckLight, IMessage>
    {
        @Override @SideOnly(Side.CLIENT)
        public IMessage onMessage(MessageCheckLight message, MessageContext ctx)
        {
            BlockPos pos = new BlockPos(message.getX(), message.getY(), message.getZ());
            
            Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().world.checkLight(pos));
            
            return null;
        }
    }
}
