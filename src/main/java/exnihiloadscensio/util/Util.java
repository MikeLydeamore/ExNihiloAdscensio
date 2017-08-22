package exnihiloadscensio.util;

import exnihiloadscensio.texturing.Color;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;

public class Util {
	
	public static Color whiteColor = new Color(1f, 1f, 1f, 1f);
	public static Color blackColor = new Color(0f, 0f, 0f, 1f);
	public static Color greenColor = new Color(0f, 1f, 0f, 1f);
	
	public static void dropItemInWorld(TileEntity source, EntityPlayer player, ItemStack stack, double speedfactor) 
	{
		int hitOrientation = player == null ? 0 : MathHelper.floor(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		double stackCoordX = 0.0D, stackCoordY = 0.0D, stackCoordZ = 0.0D;

		switch (hitOrientation) {
		case 0:
			stackCoordX = source.getPos().getX() + 0.5D;
			stackCoordY = source.getPos().getY() + 0.5D + 1;
			stackCoordZ = source.getPos().getZ() - 0.25D;
			break;
		case 1:
			stackCoordX = source.getPos().getX() + 1.25D;
			stackCoordY = source.getPos().getY() + 0.5D + 1;
			stackCoordZ = source.getPos().getZ() + 0.5D;
			break;
		case 2:
			stackCoordX = source.getPos().getX() + 0.5D;
			stackCoordY = source.getPos().getY() + 0.5D + 1;
			stackCoordZ = source.getPos().getZ() + 1.25D;
			break;
		case 3:
			stackCoordX = source.getPos().getX() - 0.25D;
			stackCoordY = source.getPos().getY() + 0.5D + 1;
			stackCoordZ = source.getPos().getZ() + 0.5D;
			break;
		}

		EntityItem droppedEntity = new EntityItem(source.getWorld(), stackCoordX, stackCoordY, stackCoordZ, stack);

		if (player != null) {
			Vec3d motion = new Vec3d(player.posX - stackCoordX, player.posY - stackCoordY, player.posZ - stackCoordZ);
			motion.normalize();
			droppedEntity.motionX = motion.x;
			droppedEntity.motionY = motion.y;
			droppedEntity.motionZ = motion.z;
			double offset = 0.25D;
			droppedEntity.move(MoverType.SELF, motion.x * offset, motion.y * offset, motion.z * offset);
		}

		droppedEntity.motionX *= speedfactor;
		droppedEntity.motionY *= speedfactor;
		droppedEntity.motionZ *= speedfactor;
		
		droppedEntity.setNoPickupDelay();

		source.getWorld().spawnEntity(droppedEntity);
	}

	@SideOnly(Side.CLIENT)
	public static TextureAtlasSprite getTextureFromBlockState(IBlockState state) {
		if (state == null)
			return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
		
		TextureAtlasSprite ret = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes()
		.getTexture(state);
		
		return ret != null ? ret : Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
	}

	@SideOnly(Side.CLIENT)
    public static TextureAtlasSprite getTextureFromFluidStack(FluidStack stack)
    {
        if(stack.getFluid() != null)
        {
            Fluid fluid = stack.getFluid();
            
            if(fluid.getStill(stack) != null)
            {
                return Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluid.getStill().toString());
            }
        }
        
        return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
    }
    
	public static boolean isSurroundingBlocksAtLeastOneOf(BlockInfo[] blocks, BlockPos pos, World world, int radius) {
		ArrayList<BlockInfo> blockList = new ArrayList<BlockInfo>(Arrays.asList(blocks));
		for (int xShift = -1*radius ; xShift <= radius ; xShift++) {
			for (int zShift = -1*radius ; zShift <= radius ; zShift++) {
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
		for (int xShift = -2 ; xShift <= 2 ; xShift++) {
			for (int zShift = -2 ; zShift <= 2 ; zShift++) {
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
	
	public static ItemStack getBucketStack(Fluid fluid)
	{
	    return UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, fluid);
	}
}
