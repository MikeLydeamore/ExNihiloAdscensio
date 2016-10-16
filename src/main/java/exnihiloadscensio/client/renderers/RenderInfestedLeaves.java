package exnihiloadscensio.client.renderers;

import javax.annotation.Nullable;

import exnihiloadscensio.tiles.TileInfestedLeaves;
import exnihiloadscensio.util.Util;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class RenderInfestedLeaves implements IBlockColor
{
    @Override
    public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex)
    {
        if (worldIn == null || pos == null)
            return Util.whiteColor.toInt();
        
        TileInfestedLeaves tile = (TileInfestedLeaves) worldIn.getTileEntity(pos);
        
        if (tile != null)
        {
            return tile.getProgress() >= 1.0F ? Util.whiteColor.toInt() : tile.getColor();
        }
        
        return Util.whiteColor.toInt();
    }
}
