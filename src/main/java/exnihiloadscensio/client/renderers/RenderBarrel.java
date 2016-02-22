package exnihiloadscensio.client.renderers;

import org.lwjgl.opengl.GL11;

import exnihiloadscensio.tiles.TileBarrel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderBarrel extends TileEntitySpecialRenderer<TileBarrel> {

	@Override
	public void renderTileEntityAt(TileBarrel te, double x, double y, double z,
			float partialTicks, int destroyStage) 
	{
		Tessellator tes = Tessellator.getInstance();
		WorldRenderer wr = tes.getWorldRenderer();

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		GlStateManager.disableLighting();
		if (te.getMode() != null)
		{
			
			TextureAtlasSprite icon = te.getMode().getTextureForRender();
			double minU = (double) icon.getMinU();
			double maxU = (double) icon.getMaxU();
			double minV = (double) icon.getMinV();
			double maxV = (double) icon.getMaxV();
			
			this.bindTexture(TextureMap.locationBlocksTexture);

			wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
			
			float fillAmount = te.getMode().getFilledLevelForRender();
			wr.pos(0.0625,fillAmount,0.0625).tex(minU, minV).endVertex();
			wr.pos(0.0625,fillAmount,0.9375).tex(minU,maxV).endVertex();
			wr.pos(0.9375,fillAmount,0.9375).tex(maxU,maxV).endVertex();
			wr.pos(0.9375,fillAmount,0.0625).tex(maxU,minV).endVertex();
			
			tes.draw();
		}
		
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
		//tessellator.setColorRGBA_F(color.r, color.g, color.b, color.a);

	}

}
