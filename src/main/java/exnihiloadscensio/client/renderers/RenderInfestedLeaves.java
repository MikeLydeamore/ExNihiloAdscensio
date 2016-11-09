package exnihiloadscensio.client.renderers;

import java.util.List;

import org.lwjgl.opengl.GL11;

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
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.LightUtil;

public class RenderInfestedLeaves extends TileEntitySpecialRenderer<TileInfestedLeaves>
{
    @Override
    public void renderTileEntityAt(TileInfestedLeaves tile, double x, double y, double z, float partialTicks, int destroyStage)
    {
        if(tile != null)
        {
            IBlockState leafBlock = tile.getLeafBlock();
            IBakedModel leafModel = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(leafBlock);
            
            if(leafModel == null)
            {
                leafModel = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(Blocks.LEAVES.getDefaultState());
            }
            
            List<BakedQuad> leafQuads = Lists.newArrayList();
            
            long seed = tile.getWorld().rand.nextLong();
            
            for(EnumFacing side : EnumFacing.VALUES)
            {
                if(leafBlock.shouldSideBeRendered(tile.getWorld(), tile.getPos(), side))
                {
                    leafQuads.addAll(leafModel.getQuads(leafBlock, side, seed));
                }
            }
            
            int color = tile.getColor();
            
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer buffer = tessellator.getBuffer();
            
            RenderHelper.disableStandardItemLighting();
            
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.enableAlpha();
            
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
