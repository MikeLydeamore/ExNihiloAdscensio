package exnihiloadscensio.client.renderers;

import org.lwjgl.opengl.GL11;

import exnihiloadscensio.entities.ProjectileStone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderProjectileStone extends Render<ProjectileStone>
{
    private TextureAtlasSprite texture;
    
    public RenderProjectileStone(RenderManager renderManager)
    {
        super(renderManager);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(ProjectileStone stone)
    {
        return new ResourceLocation("minecraft:blocks/stone");
    }
    
    @Override
    public void doRender(ProjectileStone entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if(texture == null)
        {
            texture = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.STONE.getDefaultState());
        }
        
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        
        double minU = (double) texture.getMinU();
        double maxU = (double) texture.getMaxU();
        double minV = (double) texture.getMinV();
        double maxV = (double) texture.getMaxV();

        double pixelU = (maxU - minU) / 16.0;
        double pixelV = (maxV - minV) / 16.0;
        
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        
        float size = 1.0F / 16.0F;
        
        // Outer Edges
        buffer.pos(-2 * size, -size, -size).tex(minU + 0 * pixelU   , minV + 0 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-2 * size, -size,  size).tex(minU + 0 * pixelU   , minV + 4 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-2 * size,  size,  size).tex(minU + 4 * pixelU   , minV + 4 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-2 * size,  size, -size).tex(minU + 4 * pixelU   , minV + 0 * pixelV ).normal(0, 1, 0).endVertex();

        buffer.pos( size, -2 * size, -size).tex(minU + 6 * pixelU   , minV + 2 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size, -2 * size,  size).tex(minU + 6 * pixelU   , minV + 6 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-size, -2 * size,  size).tex(minU + 2 * pixelU   , minV + 6 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-size, -2 * size, -size).tex(minU + 2 * pixelU   , minV + 2 * pixelV ).normal(0, 1, 0).endVertex();

        buffer.pos(-size,  size, -2 * size).tex(minU + 4 * pixelU   , minV + 8 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size,  size, -2 * size).tex(minU + 8 * pixelU   , minV + 8 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size, -size, -2 * size).tex(minU + 8 * pixelU   , minV + 4 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-size, -size, -2 * size).tex(minU + 4 * pixelU   , minV + 4 * pixelV ).normal(0, 1, 0).endVertex();

        buffer.pos( 2 * size,  size, -size).tex(minU + 10* pixelU   , minV + 6 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( 2 * size,  size,  size).tex(minU + 10* pixelU   , minV + 10* pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( 2 * size, -size,  size).tex(minU + 6 * pixelU   , minV + 10* pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( 2 * size, -size, -size).tex(minU + 6 * pixelU   , minV + 6 * pixelV ).normal(0, 1, 0).endVertex();

        buffer.pos(-size,  2 * size, -size).tex(minU + 10* pixelU   , minV + 10* pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-size,  2 * size,  size).tex(minU + 10* pixelU   , minV + 12* pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size,  2 * size,  size).tex(minU + 12* pixelU   , minV + 12* pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size,  2 * size, -size).tex(minU + 12* pixelU   , minV + 10* pixelV ).normal(0, 1, 0).endVertex();

        buffer.pos( size, -size,  2 * size).tex(minU + 12* pixelU   , minV + 14* pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size,  size,  2 * size).tex(minU + 14* pixelU   , minV + 14* pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-size,  size,  2 * size).tex(minU + 14* pixelU   , minV + 12* pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-size, -size,  2 * size).tex(minU + 12* pixelU   , minV + 12* pixelV ).normal(0, 1, 0).endVertex();

        // Inner Edges 1
        buffer.pos( 2 * size, -size, -size).tex(minU + 8 * pixelU   , minV + 0 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( 2 * size, -size,  size).tex(minU + 8 * pixelU   , minV + 4 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-2 * size, -size,  size).tex(minU + 0 * pixelU   , minV + 4 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-2 * size, -size, -size).tex(minU + 0 * pixelU   , minV + 0 * pixelV ).normal(0, 1, 0).endVertex();

        buffer.pos(-2 * size,  size, -size).tex(minU + 2 * pixelU   , minV + 2 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-2 * size,  size,  size).tex(minU + 2 * pixelU   , minV + 6 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( 2 * size,  size,  size).tex(minU + 10* pixelU   , minV + 6 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( 2 * size,  size, -size).tex(minU + 10* pixelU   , minV + 2 * pixelV ).normal(0, 1, 0).endVertex();

        buffer.pos(-2 * size, -size, -size).tex(minU + 4 * pixelU   , minV + 4 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-2 * size,  size, -size).tex(minU + 4 * pixelU   , minV + 8 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( 2 * size,  size, -size).tex(minU + 12* pixelU   , minV + 8 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( 2 * size, -size, -size).tex(minU + 12* pixelU   , minV + 4 * pixelV ).normal(0, 1, 0).endVertex();
        
        buffer.pos( 2 * size, -size,  size).tex(minU + 14* pixelU   , minV + 6 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( 2 * size,  size,  size).tex(minU + 14* pixelU   , minV + 10* pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-2 * size,  size,  size).tex(minU + 6 * pixelU   , minV + 10* pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-2 * size, -size,  size).tex(minU + 6 * pixelU   , minV + 6 * pixelV ).normal(0, 1, 0).endVertex();
        
        
        // Inner Edges 2
        buffer.pos(-size, -2 * size, -size).tex(minU + 6 * pixelU   , minV + 6 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-size, -2 * size,  size).tex(minU + 6 * pixelU   , minV + 10* pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-size,  2 * size,  size).tex(minU + 14* pixelU   , minV + 10* pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-size,  2 * size, -size).tex(minU + 14* pixelU   , minV + 6 * pixelV ).normal(0, 1, 0).endVertex();

        buffer.pos( size,  2 * size, -size).tex(minU + 12* pixelU   , minV + 4 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size,  2 * size,  size).tex(minU + 12* pixelU   , minV + 8 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size, -2 * size,  size).tex(minU + 4 * pixelU   , minV + 8 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size, -2 * size, -size).tex(minU + 4 * pixelU   , minV + 4 * pixelV ).normal(0, 1, 0).endVertex();

        buffer.pos(-size,  2 * size, -size).tex(minU + 10* pixelU   , minV + 2 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size,  2 * size, -size).tex(minU + 10* pixelU   , minV + 6 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size, -2 * size, -size).tex(minU + 2 * pixelU   , minV + 6 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-size, -2 * size, -size).tex(minU + 2 * pixelU   , minV + 2 * pixelV ).normal(0, 1, 0).endVertex();
        
        buffer.pos(-size, -2 * size,  size).tex(minU + 0 * pixelU   , minV + 0 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size, -2 * size,  size).tex(minU + 0 * pixelU   , minV + 4 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size,  2 * size,  size).tex(minU + 8 * pixelU   , minV + 4 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-size,  2 * size,  size).tex(minU + 8 * pixelU   , minV + 0 * pixelV ).normal(0, 1, 0).endVertex();
        
        // Inner Edges 3
        buffer.pos(-size, -size,  2 * size).tex(minU + 12* pixelU   , minV + 4 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-size,  size,  2 * size).tex(minU + 12* pixelU   , minV + 8 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-size,  size, -2 * size).tex(minU + 4 * pixelU   , minV + 8 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-size, -size, -2 * size).tex(minU + 4 * pixelU   , minV + 4 * pixelV ).normal(0, 1, 0).endVertex();

        buffer.pos( size, -size, -2 * size).tex(minU + 0 * pixelU   , minV + 0 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size,  size, -2 * size).tex(minU + 0 * pixelU   , minV + 4 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size,  size,  2 * size).tex(minU + 8 * pixelU   , minV + 4 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size, -size,  2 * size).tex(minU + 8 * pixelU   , minV + 0 * pixelV ).normal(0, 1, 0).endVertex();

        buffer.pos(-size, -size, -2 * size).tex(minU + 2 * pixelU   , minV + 2 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size, -size, -2 * size).tex(minU + 2 * pixelU   , minV + 6 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size, -size,  2 * size).tex(minU + 10* pixelU   , minV + 6 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-size, -size,  2 * size).tex(minU + 10* pixelU   , minV + 2 * pixelV ).normal(0, 1, 0).endVertex();
        
        buffer.pos(-size,  size,  2 * size).tex(minU + 14* pixelU   , minV + 6 * pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size,  size,  2 * size).tex(minU + 14* pixelU   , minV + 10* pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos( size,  size, -2 * size).tex(minU + 6 * pixelU   , minV + 10* pixelV ).normal(0, 1, 0).endVertex();
        buffer.pos(-size,  size, -2 * size).tex(minU + 6 * pixelU   , minV + 6 * pixelV ).normal(0, 1, 0).endVertex();
        
        tessellator.draw();
        
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
    
    public static class Factory implements IRenderFactory<ProjectileStone>
    {
        @Override
        public Render<ProjectileStone> createRenderFor(RenderManager manager)
        {
            return new RenderProjectileStone(manager);
        }
    }
}
