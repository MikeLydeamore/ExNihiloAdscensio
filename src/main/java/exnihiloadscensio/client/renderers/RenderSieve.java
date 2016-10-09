package exnihiloadscensio.client.renderers;

import org.lwjgl.opengl.GL11;

import exnihiloadscensio.tiles.TileSieve;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderSieve extends TileEntitySpecialRenderer<TileSieve> {
	@Override
	public void renderTileEntityAt(TileSieve te, double x, double y, double z,
			float partialTicks, int destroyStage) 
	{
		Tessellator tes = Tessellator.getInstance();
		VertexBuffer wr = tes.getBuffer();

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
		if (te.getTexture() != null)
		{
			TextureAtlasSprite icon = te.getTexture();
			double minU = (double) icon.getMinU();
			double maxU = (double) icon.getMaxU();
			double minV = (double) icon.getMinV();
			double maxV = (double) icon.getMaxV();

			this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
			double height = (100.0-te.getProgress()) / 100;
			float fillAmount = (float) (0.15625*height + 0.84375);
			
			wr.pos(0.0625f,fillAmount,0.0625f).tex(minU, minV).normal(0, 1, 0).endVertex();
			wr.pos(0.0625f,fillAmount,0.9375f).tex(minU,maxV).normal(0, 1, 0).endVertex();
			wr.pos(0.9375f,fillAmount,0.9375f).tex(maxU,maxV).normal(0, 1, 0).endVertex();
			wr.pos(0.9375f,fillAmount,0.0625f).tex(maxU,minV).normal(0, 1, 0).endVertex();

			tes.draw();
		}

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();

	}

}