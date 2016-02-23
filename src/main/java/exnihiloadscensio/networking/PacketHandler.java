package exnihiloadscensio.networking;

import exnihiloadscensio.ExNihiloAdscensio;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ExNihiloAdscensio.MODID);
	private static int id = 0;
	
	public static void initPackets()
	{
		INSTANCE.registerMessage(MessageBarrelModeUpdate.MessageBarrelModeUpdateHandler.class, MessageBarrelModeUpdate.class, id++, Side.CLIENT);
	}
	
	public static void sendToAllAround(IMessage message, TileEntity te, int range) 
	{
		BlockPos pos = te.getPos();
        INSTANCE.sendToAllAround(message, new TargetPoint(te.getWorld().provider.getDimensionId(), pos.getX(), pos.getY(), pos.getZ(), range));
    }
	
	public static void sendToAllAround(IMessage message, TileEntity te) 
	{
        sendToAllAround(message, te, 64);
    }
}
