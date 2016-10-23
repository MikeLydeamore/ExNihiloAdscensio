package exnihiloadscensio.util;

import java.util.ArrayList;
import java.util.Arrays;

import exnihiloadscensio.texturing.Color;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class Util {
	
	public static Color whiteColor = new Color(1f, 1f, 1f, 1f);
	public static Color blackColor = new Color(0f, 0f, 0f, 1f);
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
	
	public static boolean isSurroundingBlocksAtLeastOneOf(BlockInfo[] blocks, BlockPos pos, World world) {

		ArrayList<BlockInfo> blockList = new ArrayList<BlockInfo>(Arrays.asList(blocks));
		for (int xShift = -1 ; xShift <= 1 ; xShift++) {
			for (int zShift = -1 ; zShift <= 1 ; zShift++) {
				BlockPos checkPos = pos.add(xShift, 0, zShift);
				BlockInfo checkBlock = new BlockInfo(world.getBlockState(checkPos));
				if (blockList.contains(checkBlock))
					return true;				
			}
		}
		
		
		return false;
	}
	
	public static int getNumSurroundingBlocksAtLeastOneOf(BlockInfo[] blocks, BlockPos pos, World world) {
		
		int ret = 0;
		ArrayList<BlockInfo> blockList = new ArrayList<BlockInfo>(Arrays.asList(blocks));
		for (int xShift = -1 ; xShift <= 1 ; xShift++) {
			for (int zShift = -1 ; zShift <= 1 ; zShift++) {
				BlockPos checkPos = pos.add(xShift, 0, zShift);
				BlockInfo checkBlock = new BlockInfo(world.getBlockState(checkPos));
				if (blockList.contains(checkBlock))
					ret++;				
			}
		}
		
		
		return ret;
	}
	
	public static int getLightValue(FluidStack fluid)
	{
	    if(fluid != null && fluid.getFluid() != null)
	    {
	        return fluid.getFluid().getLuminosity(fluid);
	    }
	    else
	    {
	        return 0;
	    }
	}
	
	public static float weightedAverage(float a, float b, float percent)
	{
	    return a * percent + b * (1 - percent);
	}
}
