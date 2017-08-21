package exnihiloadscensio.client.renderers;

import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class RenderBarrel extends TileEntitySpecialRenderer<TileBarrel>
{
    @Override
    public void render(TileBarrel tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        
        if (tile.getMode() != null)
        {
            float fillAmount = tile.getMode().getFilledLevelForRender(tile);
            
            if (fillAmount > 0)
            {
                Color color = tile.getMode().getColorForRender();
                
                if (color == null)
                {
                    color = Util.whiteColor;
                }
                
                TextureAtlasSprite icon = tile.getMode().getTextureForRender(tile);
                if (icon == null)
                	icon = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
                
                double minU = (double) icon.getMinU();
                double maxU = (double) icon.getMaxU();
                double minV = (double) icon.getMinV();
                double maxV = (double) icon.getMaxV();
                
                this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
                
                buffer.pos(0.125f, fillAmount, 0.125f).tex(minU, minV).color(color.r, color.g, color.b, color.a).normal(0, 1, 0).endVertex();
                buffer.pos(0.125f, fillAmount, 0.875f).tex(minU, maxV).color(color.r, color.g, color.b, color.a).normal(0, 1, 0).endVertex();
                buffer.pos(0.875f, fillAmount, 0.875f).tex(maxU, maxV).color(color.r, color.g, color.b, color.a).normal(0, 1, 0).endVertex();
                buffer.pos(0.875f, fillAmount, 0.125f).tex(maxU, minV).color(color.r, color.g, color.b, color.a).normal(0, 1, 0).endVertex();
                
                tessellator.draw();
            }
        }
        
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
}
