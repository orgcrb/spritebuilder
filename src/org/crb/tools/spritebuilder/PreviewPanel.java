package org.crb.tools.spritebuilder;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class PreviewPanel extends JPanel
{
	public PreviewPanel(int width, int height, SpriteBuilder parentBuilder)
	{
		this.gridWidth = width;
		this.gridHeight = height;
		this.parentBuilder = parentBuilder;

		this.pixelSize = 1;
	}

	public void updatePreview(Color[][] colorGrid)
	{
		this.colorGrid = colorGrid;
		this.gridWidth = colorGrid[0].length;
		this.gridHeight = colorGrid[1].length;
		repaint();
	}

	protected void paintComponent(Graphics g)
	{
		if (this.colorGrid == null)
			return;
		g.setColor(this.getBackground());
		g.fillRect(0, 0, 2000, 2000);

		int widthStart = 0;
		int heightStart = 0;

		parentBuilder.getPreviewFrame().setSize(
				this.pixelSize * this.gridWidth + 15,
				this.pixelSize * this.gridHeight + 38);

		for (int w = 0; w < this.gridWidth; w++)
		{
			for (int h = 0; h < this.gridHeight; h++)
			{
				if (colorGrid[w][h] == null)
					continue;
				g.setColor(colorGrid[w][h]);
				g.fillRect(widthStart + (w * pixelSize), heightStart
						+ (h * pixelSize), pixelSize, pixelSize);
			}
		}
	}

	public int getPixelSize()
	{
		return this.pixelSize;
	}

	private static final long serialVersionUID = 1L;

	private Color[][] colorGrid;

	private int gridWidth;
	private int gridHeight;

	private SpriteBuilder parentBuilder;

	private int pixelSize;
}
