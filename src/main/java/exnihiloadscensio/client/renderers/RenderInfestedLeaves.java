package exnihiloadscensio.client.renderers;

import javax.annotation.Nullable;

import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.tiles.TileInfestedLeaves;
import exnihiloadscensio.util.Util;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;

public class RenderInfestedLeaves implements IBlockColor
{
    
    @Override
    public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex)
    {
        if (worldIn == null || pos == null)
            return 0;
        
        TileInfestedLeaves te = (TileInfestedLeaves) worldIn.getTileEntity(pos);
        
        if (te != null)
        {
            float progress = te.getProgress();
            
            Color green = new Color(BiomeColorHelper.getFoliageColorAtPos(worldIn, pos));
            Color white = Util.whiteColor;
            
            return Color.average(green, white, progress).toInt();
        }
        
        return 0;
    }
    
}
