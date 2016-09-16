package exnihiloadscensio.util;

import exnihiloadscensio.texturing.Color;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Util {
	
	public static Color whiteColor = new Color(1f, 1f, 1f, 1f);
	public static Color greenColor = new Color(0f, 1f, 0f, 1f);
	
	public static void dropItemInWorld(TileEntity source, EntityPlayer player, ItemStack stack, double speedfactor) 
	{
		int hitOrientation = player == null ? 0 : MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		double stackCoordX = 0.0D, stackCoordY = 0.0D, stackCoordZ = 0.0D;

		switch (hitOrientation) {
		case 0:
			stackCoordX = source.getPos().getX() + 0.5D;
			stackCoordY = source.getPos().getY() + 0.5D;
			stackCoordZ = source.getPos().getZ() - 0.25D;
			break;
		case 1:
			stackCoordX = source.getPos().getX() + 1.25D;
			stackCoordY = source.getPos().getY() + 0.5D;
			stackCoordZ = source.getPos().getZ() + 0.5D;
			break;
		case 2:
			stackCoordX = source.getPos().getX() + 0.5D;
			stackCoordY = source.getPos().getY() + 0.5D;
			stackCoordZ = source.getPos().getZ() + 1.25D;
			break;
		case 3:
			stackCoordX = source.getPos().getX() - 0.25D;
			stackCoordY = source.getPos().getY() + 0.5D;
			stackCoordZ = source.getPos().getZ() + 0.5D;
			break;
		}

		EntityItem droppedEntity = new EntityItem(source.getWorld(), stackCoordX, stackCoordY, stackCoordZ, stack);

		if (player != null) {
			Vec3d motion = new Vec3d(player.posX - stackCoordX, player.posY - stackCoordY, player.posZ - stackCoordZ);
			motion.normalize();
			droppedEntity.motionX = motion.xCoord;
			droppedEntity.motionY = motion.yCoord;
			droppedEntity.motionZ = motion.zCoord;
			double offset = 0.25D;
			droppedEntity.moveEntity(motion.xCoord * offset, motion.yCoord * offset, motion.zCoord * offset);
		}

		droppedEntity.motionX *= speedfactor;
		droppedEntity.motionY *= speedfactor;
		droppedEntity.motionZ *= speedfactor;

		source.getWorld().spawnEntityInWorld(droppedEntity);
	}
	
	public static TextureAtlasSprite getTextureFromBlockState(IBlockState state) {
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes()
		.getTexture(state);
	}

}
