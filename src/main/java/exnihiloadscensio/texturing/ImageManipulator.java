package exnihiloadscensio.texturing;

import java.awt.image.BufferedImage;

public class ImageManipulator
{
	//Recolors an image based on the input color
	public static BufferedImage Recolor(BufferedImage template, Color colorNew)
	{
		int w = template.getWidth();
		int h = template.getHeight();

		// create an output image that we will use to override
		BufferedImage imgOutput = new BufferedImage(w, h, template.getType());

		int[] templateData = new int[w * h];
		int[] outputData = new int[w * h];

		// read the rgb color data into our array
		template.getRGB(0, 0, w, h, templateData, 0, w);

		// blend each pixel where alpha > 0
		for (int i = 0; i < templateData.length; i++) {
			Color colorRaw = new Color(templateData[i], false);

			if (colorRaw.a > 0)
			{
				float a = colorRaw.a;     
				float r = (colorNew.r);
				float g = (colorNew.g);
				float b = (colorNew.b);

				//System.out.println("Blended pixel r:" + r + ", g:" +  g + ", b:" + b + ", a:" +  a);
				outputData[i] = (new Color(r, g, b, a).toInt());
			}else
			{
				//System.out.println("Raw pixel r:" + colorRaw.r + ", g:" +  colorRaw.g + ", b:" + colorRaw.b + ", a:" +  colorRaw.a);
				outputData[i] = templateData[i];
			}
		}

		// write the new image data to the output image buffer
		imgOutput.setRGB(0, 0, w, h, outputData, 0, w);

		return imgOutput;
	}

	//Combines two images and returns the composite.
	public static BufferedImage Composite(BufferedImage imgBackground, BufferedImage imgForeground)
	{
		if (!normalCompositePossible(imgBackground, imgForeground))
		{
			return null;
		}

		int w = imgBackground.getWidth();
		int h = imgBackground.getHeight();

		// create an output image that we will use to override
		BufferedImage imgOutput = new BufferedImage(w, h, imgBackground.getType());

		int[] backgroundData = new int[w * h];
		int[] foregroundData = new int[w * h];
		int[] outputData = new int[w * h];

		// read the rgb color data into our array
		imgBackground.getRGB(0, 0, w, h, backgroundData, 0, w);
		imgForeground.getRGB(0, 0, w, h, foregroundData, 0, w);

		// 'over' blend each pixel
		for (int i = 0; i < backgroundData.length; i++) {
			Color colorBackground = new Color(backgroundData[i]);
			Color colorForeground = new Color(foregroundData[i], false);

			//Debug code!
			//System.out.println("Background pixel r:" + colorBackground.r + ", g:" +  colorBackground.g + ", b:" + colorBackground.b + ", a:" +  colorBackground.a);
			//System.out.println("Foreground pixel r:" + colorForeground.r + ", g:" +  colorForeground.g + ", b:" + colorForeground.b + ", a:" +  colorForeground.a);

			outputData[i] = backgroundData[i];

			if (colorForeground.a > 0)
			{
				float alpha = colorForeground.a;

				float a = colorBackground.a;
				float r = (colorForeground.r * alpha) + (colorBackground.r) * (1.0f - alpha);
				float g = (colorForeground.g * alpha) + (colorBackground.g) * (1.0f - alpha);
				float b = (colorForeground.b * alpha) + (colorBackground.b) * (1.0f - alpha);

				//System.out.println("Blended pixel r:" + r + ", g:" +  g + ", b:" + b + ", a:" +  a);
				outputData[i] = (new Color(r, g, b, a).toInt());
			}else
			{
				outputData[i] = backgroundData[i];
			}
		}

		// write the new image data to the output image buffer
		imgOutput.setRGB(0, 0, w, h, outputData, 0, w);

		return imgOutput;
	}

	private static boolean normalCompositePossible(BufferedImage imgBackground, BufferedImage imgForeground)
	{
		//if the size is identical, then compositing in normal mode is possible. 
		return (imgBackground.getWidth() == imgForeground.getWidth()) && (imgBackground.getHeight() == imgForeground.getHeight());
	}

}