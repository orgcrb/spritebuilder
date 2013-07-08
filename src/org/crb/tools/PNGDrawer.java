package org.crb.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


public class PNGDrawer
{

	public static void drawPNG(Color[][] colorGrid, int pixelSize)
	{
		int confirm = JOptionPane.showConfirmDialog(
				null,
				"Are you sure you want to convert this image to a .png?");
		if (confirm != 0)
		{
			return;
		}

		RenderedImage image = createImage(colorGrid, pixelSize);
		String fileName = JOptionPane
				.showInputDialog("What would you like to save this file as?");
		File file = new File((fileName.isEmpty() ? "newimage" : fileName)
				+ ".png");
		try
		{
			ImageIO.write(image, "png", file);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static RenderedImage createImage(Color[][] colorGrid, int pixelSize)
	{
		int width = pixelSize;
		int height = pixelSize;
		int imageSize = BuilderMain.IMAGE_SIZE;
		// Create a buffered image in which to draw
		BufferedImage bufferedImage = new BufferedImage(width * imageSize,
				height * imageSize, BufferedImage.TYPE_INT_ARGB);

		// Create a graphics contents on the buffered image
		Graphics2D g2d = bufferedImage.createGraphics();

		for (int w = 0; w < imageSize; w++)
		{
			for (int h = 0; h < imageSize; h++)
			{
				if (colorGrid[w][h] == null)
				{
					g2d.setColor(new Color(255, 0, 0, 0));
				}
				else
				{
					g2d.setColor(colorGrid[w][h]);
				}
				g2d.fillRect(w * width, h * height, width, height);
			}
		}
		// Draw graphics

		// Graphics context no longer needed so dispose it
		g2d.dispose();

		return bufferedImage;
	}
}
