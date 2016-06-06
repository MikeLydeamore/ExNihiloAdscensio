package exnihiloadscensio.client.renderers;

import org.lwjgl.opengl.GL11;

import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.tiles.TileBarrel;
import exnihiloadscensio.util.Util;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class RenderBarrel extends TileEntitySpecialRenderer<TileBarrel> {

	@Override
	public void renderTileEntityAt(TileBarrel te, double x, double y, double z,
			float partialTicks, int destroyStage) 
	{
		Tessellator tes = Tessellator.getInstance();
		VertexBuffer wr = tes.getBuffer();

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

			this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
			//wr.begin(GL11.GL_QUADS, new VertexFormat().addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.NORMAL_3B));
			float fillAmount = te.getMode().getFilledLevelForRender();

			Color color = te.getMode().getColorForRender();
			if (color == null)
				color = Util.whiteColor;
			wr.pos(0.125f,fillAmount,0.125f).tex(minU, minV).color(color.r, color.g, color.b, color.a).normal(0, 1, 0).endVertex();
			wr.pos(0.125f,fillAmount,0.875f).tex(minU,maxV).color(color.r, color.g, color.b, color.a).normal(0, 1, 0).endVertex();
			wr.pos(0.875f,fillAmount,0.875f).tex(maxU,maxV).color(color.r, color.g, color.b, color.a).normal(0, 1, 0).endVertex();
			wr.pos(0.875f,fillAmount,0.125f).tex(maxU,minV).color(color.r, color.g, color.b, color.a).normal(0, 1, 0).endVertex();

			tes.draw();
		}

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();

	}

}
