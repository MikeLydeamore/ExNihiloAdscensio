package exnihiloadscensio.client.renderers;

import org.lwjgl.opengl.GL11;

import exnihiloadscensio.tiles.TileCrucible;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RenderCrucible extends TileEntitySpecialRenderer<TileCrucible> {
	@Override
	public void renderTileEntityAt(TileCrucible te, double x, double y, double z,
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
			//wr.begin(GL11.GL_QUADS, new VertexFormat().addElement(DefaultVertexFormats.POSITION_3F).addElement(DefaultVertexFormats.COLOR_4UB).addElement(DefaultVertexFormats.NORMAL_3B));
			// Offset by bottome of crucible, which is 4 pixels above the base of the block
			float fillAmount = (12F / 16F) * te.getFilledAmount() + (4F / 16F);
			
			wr.pos(0.125F, fillAmount, 0.125F).tex(minU, minV).normal(0, 1, 0).endVertex();
			wr.pos(0.125F, fillAmount, 0.875F).tex(minU, maxV).normal(0, 1, 0).endVertex();
			wr.pos(0.875F, fillAmount, 0.875F).tex(maxU, maxV).normal(0, 1, 0).endVertex();
			wr.pos(0.875F, fillAmount, 0.125F).tex(maxU, minV).normal(0, 1, 0).endVertex();

			tes.draw();
		}

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();

	}

}