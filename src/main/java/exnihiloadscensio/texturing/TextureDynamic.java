package exnihiloadscensio.texturing;

import exnihiloadscensio.ExNihiloAdscensio;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class TextureDynamic extends TextureAtlasSprite {

	  private ResourceLocation template;
	  private ResourceLocation base;
	  private Color color;

	  public TextureDynamic(String name, ResourceLocation base, ResourceLocation template, Color color) 
	  {
	    super(name);
	    
	    this.template = template;
	    this.base = base;
	    this.color = color;
	  }
	  
	  public static String getTextureName(String name) 
	  {
	    return ExNihiloAdscensio.MODID + ":" + name;
	  }

	  @Override
	  public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) 
	  {	    
	    try 
	    {
	      manager.getResource(location);
	    } 
	    catch (Exception e) 
	    {
	      return true;
	    }

	   // ExNihiloAdscensio.log.info("Icon: " + template + " was overwritten by a texturepack or embedded resource.");
	    return false;
	  }

	  // creates the textures
	  // originally based on code from DenseOres, but refactored down to what you see here.
	  @Override
	  public boolean load(IResourceManager manager, ResourceLocation location) {
	    // get mipmapping levels
	    int mipmapLevels = Minecraft.getMinecraft().gameSettings.mipmapLevels;

	    try {
	      BufferedImage[] imgFinal = new BufferedImage[1 + mipmapLevels];
	      imgFinal[0] = tryLoadImage(manager, template);
	      
	      if (color != null)
	      {
	        imgFinal[0] = ImageManipulator.Recolor(imgFinal[0], color);
	      }
	      
	      if (this.base != null)
	      {
	        BufferedImage imgBase = tryLoadImage(manager, base);
	        
	        if (imgBase != null)
	        {
	          imgFinal[0] = ImageManipulator.Composite(imgBase, imgFinal[0]);
	        }
	      }

	      // load the texture (note the null is where animation data would normally go)
	      //this.loadSprite(new PngSizeInfo(imgFinal), null);
	    } 
	    catch (Exception e) 
	    {
	      e.printStackTrace();
	      return true;
	    }

	    //ExNihilo.log.info("Succesfully generated texture for '" + this.getIconName() + "'. Place " + this.getIconName() + ".png in the assets folder to override.");
	    return false;
	  }
	  
	  //Loads an image into memory.
	  private BufferedImage tryLoadImage(IResourceManager manager, ResourceLocation location)
	  {
	    try
	    {
	      IResource res = manager.getResource(location);
	      
	      return ImageIO.read(res.getInputStream());
	    }
	    catch (Exception e)
	    {
	      //ExNihilo.log.error("Failed to load image: " + location.toString());
	      return null;
	    }
	  }
	}