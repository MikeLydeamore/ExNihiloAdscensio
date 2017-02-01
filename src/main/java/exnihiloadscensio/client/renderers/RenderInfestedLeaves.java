package exnihiloadscensio.client.renderers;


import com.google.common.collect.Lists;
import exnihiloadscensio.tiles.TileInfestedLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class RenderInfestedLeaves extends TileEntitySpecialRenderer<TileInfestedLeaves>
{
    @Override
    public void renderTileEntityAt(TileInfestedLeaves tile, double x, double y, double z, float partialTicks, int destroyStage)
    {
        if(tile != null)
        {
            long seed = tile.getWorld().rand.nextLong();
            int color = tile.getColor();
            
            IBlockState leafBlock = tile.getLeafBlock();
            IBakedModel leafModel = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(leafBlock);

            if(leafModel == Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getMissingModel())
            {
                leafModel = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(Blocks.LEAVES.getDefaultState());
            }
            
            List<BakedQuad> leafQuads = Lists.newArrayList();
            
            for(EnumFacing side : EnumFacing.VALUES)
            {
                if(leafBlock.shouldSideBeRendered(tile.getWorld(), tile.getPos(), side))
                {
                    leafQuads.addAll(leafModel.getQuads(leafBlock, side, seed));
                }
            }
            
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer buffer = tessellator.getBuffer();
            
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            RenderHelper.disableStandardItemLighting();
            
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);
            
            for(BakedQuad quad : leafQuads)
            {
                LightUtil.renderQuadColor(buffer, quad, color);
            }
            
            tessellator.draw();
            
            RenderHelper.enableStandardItemLighting();
            GlStateManager.popMatrix();
        }
    }
}
