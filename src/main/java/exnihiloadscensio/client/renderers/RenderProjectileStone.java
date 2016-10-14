package exnihiloadscensio.client.renderers;

import org.lwjgl.opengl.GL11;

import exnihiloadscensio.entities.ProjectileStone;
import net.minecraft.client.Minecraft;
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

        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        
        float size = 1;
        
        //bufferCuboid(buffer, 1, minU, minV, maxU, maxV, -8, -8, -8, 8, 8, 8);
        
        bufferCuboid(buffer, size, minU, minV, maxU, maxV, -4, -2, -2, -2,  2,  2);
        bufferCuboid(buffer, size, minU, minV, maxU, maxV,  4,  2,  2,  2, -2, -2);
        bufferCuboid(buffer, size, minU, minV, maxU, maxV, -2, -4, -2,  2, -2,  2);
        bufferCuboid(buffer, size, minU, minV, maxU, maxV,  2,  4,  2, -2,  2, -2);
        bufferCuboid(buffer, size, minU, minV, maxU, maxV, -2, -2, -4,  2,  2,  -2);
        bufferCuboid(buffer, size, minU, minV, maxU, maxV,  2,  2,  4, -2, -2,  2);
        
        tessellator.draw();
        
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
    
    private static void bufferCuboid(VertexBuffer buffer, double size, double minU, double minV, double maxU, double maxV, double x1, double y1, double z1, double x2, double y2, double z2)
    {
        size /= 16.0;
        
        double xMin = Math.min(x1, x2);
        double xMax = Math.max(x1, x2);
        
        double yMin = Math.min(y1, y2);
        double yMax = Math.max(y1, y2);
        
        double zMin = Math.min(z1, z2);
        double zMax = Math.max(z1, z2);

        double xUMin = minU + (maxU - minU) * (xMin + 8.0) / 16.0;
        double xUMax = minU + (maxU - minU) * (xMax + 8.0) / 16.0;
        
        //double yUMin = minU + (maxU - minU) * (yMin + 8.0) / 16.0;
        //double yUMax = minU + (maxU - minU) * (yMax + 8.0) / 16.0;
        
        double zUMin = minU + (maxU - minU) * (zMin + 8.0) / 16.0;
        double zUMax = minU + (maxU - minU) * (zMax + 8.0) / 16.0;
        
        //double xVMin = minV + (maxV - minV) * (xMin + 8.0) / 16.0;
        //double xVMax = minV + (maxV - minV) * (xMax + 8.0) / 16.0;
        
        double yVMin = minV + (maxV - minV) * (yMin + 8.0) / 16.0;
        double yVMax = minV + (maxV - minV) * (yMax + 8.0) / 16.0;
        
        double zVMin = minV + (maxV - minV) * (zMin + 8.0) / 16.0;
        double zVMax = minV + (maxV - minV) * (zMax + 8.0) / 16.0;

        xMin *= size;
        xMax *= size;
        
        yMin *= size;
        yMax *= size;
        
        zMin *= size;
        zMax *= size;

        buffer.pos(xMin, yMin, zMin).tex(zUMin, yVMax).normal(-1, 0, 0).endVertex();
        buffer.pos(xMin, yMin, zMax).tex(zUMax, yVMax).normal(-1, 0, 0).endVertex();
        buffer.pos(xMin, yMax, zMax).tex(zUMax, yVMin).normal(-1, 0, 0).endVertex();
        buffer.pos(xMin, yMax, zMin).tex(zUMin, yVMin).normal(-1, 0, 0).endVertex();

        buffer.pos(xMax, yMin, zMin).tex(zUMax, yVMax).normal(1, 0, 0).endVertex();
        buffer.pos(xMax, yMax, zMin).tex(zUMax, yVMin).normal(1, 0, 0).endVertex();
        buffer.pos(xMax, yMax, zMax).tex(zUMin, yVMin).normal(1, 0, 0).endVertex();
        buffer.pos(xMax, yMin, zMax).tex(zUMin, yVMax).normal(1, 0, 0).endVertex();
        
        buffer.pos(xMin, yMin, zMin).tex(xUMin, zVMax).normal(0, -1, 0).endVertex();
        buffer.pos(xMax, yMin, zMin).tex(xUMax, zVMax).normal(0, -1, 0).endVertex();
        buffer.pos(xMax, yMin, zMax).tex(xUMax, zVMin).normal(0, -1, 0).endVertex();
        buffer.pos(xMin, yMin, zMax).tex(xUMin, zVMin).normal(0, -1, 0).endVertex();

        buffer.pos(xMin, yMax, zMin).tex(xUMin, zVMin).normal(0, 1, 0).endVertex();
        buffer.pos(xMin, yMax, zMax).tex(xUMin, zVMax).normal(0, 1, 0).endVertex();
        buffer.pos(xMax, yMax, zMax).tex(xUMax, zVMax).normal(0, 1, 0).endVertex();
        buffer.pos(xMax, yMax, zMin).tex(xUMax, zVMin).normal(0, 1, 0).endVertex();

        buffer.pos(xMin, yMin, zMin).tex(xUMax, yVMax).normal(0, 0, -1).endVertex();
        buffer.pos(xMin, yMax, zMin).tex(xUMax, yVMin).normal(0, 0, -1).endVertex();
        buffer.pos(xMax, yMax, zMin).tex(xUMin, yVMin).normal(0, 0, -1).endVertex();
        buffer.pos(xMax, yMin, zMin).tex(xUMin, yVMax).normal(0, 0, -1).endVertex();
        
        buffer.pos(xMin, yMin, zMax).tex(xUMin, yVMax).normal(0, 0, 1).endVertex();
        buffer.pos(xMax, yMin, zMax).tex(xUMax, yVMax).normal(0, 0, 1).endVertex();
        buffer.pos(xMax, yMax, zMax).tex(xUMax, yVMin).normal(0, 0, 1).endVertex();
        buffer.pos(xMin, yMax, zMax).tex(xUMin, yVMin).normal(0, 0, 1).endVertex();
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
