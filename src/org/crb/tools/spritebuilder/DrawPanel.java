package org.crb.tools.spritebuilder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


@SuppressWarnings("serial")
public class DrawPanel extends JPanel
{

	public DrawPanel(int width, int height, SpriteBuilder parentBuilder)
	{
		this.width = width;
		this.height = height;
		
		this.parentBuilder = parentBuilder;
		this.drawPanelFrame = this.parentBuilder.getDrawFrame();
		this.toolsPanel = this.parentBuilder.getToolsPanel();
	
		this.colorGrid = new Color[width][height];

		this.incrementGeneration = false;
		
		this.addMouseMotionListener(new DrawListener());
		this.addMouseListener(new DrawListener());
	}

	protected void paintComponent(Graphics g)
	{

		g.setColor(this.getBackground());
		g.fillRect(0, 0, 2000, 2000);

		this.tileDim.width = drawPanelFrame.getWidth() / this.width;
		this.tileDim.height = drawPanelFrame.getHeight() / this.height;

		while (this.tileDim.width * this.width > drawPanelFrame.getWidth() - 15)
		{
			this.tileDim.width--;
		}

		while (this.tileDim.height * this.height > drawPanelFrame
				.getHeight() - 100)
		{
			this.tileDim.height--;
		}

		for (int w = 0; w < this.width; w++)
		{
			for (int h = 0; h < this.height; h++)
			{
				g.setColor(Color.BLACK);
				if (!toolsPanel.isGrid())
					g.setColor(Color.WHITE);

				g.clearRect(
						w * this.tileDim.width,
						h * this.tileDim.height,
						this.tileDim.width,
						this.tileDim.height);
				g.drawRect(
						w * this.tileDim.width,
						h * this.tileDim.height,
						this.tileDim.width,
						this.tileDim.height);

				if (this.colorGrid[w][h] != null)
				{
					g.setColor(this.colorGrid[w][h]);
					g.fillRect(
							w * this.tileDim.width,
							h * this.tileDim.height,
							this.tileDim.width,
							this.tileDim.height);
				}
			}
		}
		this.parentBuilder.getPreviewPanel().updatePreview(this.colorGrid);
		this.parentBuilder.calculateCursor();
	}

	public void undo()
	{
		if (head != null)
		{
			this.head.decrementGeneration();
			this.repaint();
		}
	}

	public Color[][] getColorGrid()
	{
		return this.colorGrid;
	}
	
	public void
	setColorGrid(Color[][] newColorGrid)
	{
		this.colorGrid = newColorGrid;
	}

	private class DrawListener implements MouseMotionListener,
			MouseListener
	{

		private Color newColor;
		private Color oldColor;

		@Override
		public void mouseDragged(MouseEvent e)
		{
			int x = e.getX() / tileDim.width;
			int y = e.getY() / tileDim.height;

			if (x >= 0 && y >= 0 && x < width && y < height)
			{
				if (SwingUtilities.isRightMouseButton(e))
				{
					this.colorGrab(x, y);
				}
				else
				{
					this.replaceColor(
							x,
							y,
							toolsPanel.isErase() ? null : toolsPanel
									.getColor());
				}
			}
			toolsPanel.repaint();
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseClicked(MouseEvent arg0)
		{

		}

		@Override
		public void mouseEntered(MouseEvent arg0)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0)
		{
			int x = arg0.getX() / tileDim.width;
			int y = arg0.getY() / tileDim.height;
			if (x >= 0 && y >= 0 && x < width && y < height)
			{
				if (arg0.getButton() == MouseEvent.BUTTON3)
				{
					this.colorGrab(x, y);
				}
				else
				{
					this.oldColor = colorGrid[x][y] == null ? null : new Color(
							colorGrid[x][y].getRed(),
							colorGrid[x][y].getGreen(),
							colorGrid[x][y].getBlue(), 255);
					this.replaceColor(
							x,
							y,
							toolsPanel.isErase() ? null : toolsPanel
									.getColor());
					if (toolsPanel.isFill())
					{
						this.newColor = colorGrid[x][y];
						this.fill(x - 1, y);
						this.fill(x + 1, y);
						this.fill(x, y - 1);
						this.fill(x, y + 1);
					}
				}
			}
			toolsPanel.repaint();
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent arg0)
		{
			parentBuilder.setEyedropper(false);
			parentBuilder.calculateCursor();
			toolsPanel.repaint();
			repaint();
			if (head != null && incrementGeneration)
			{
				head.incrementGeneration();
				incrementGeneration = false;
			}
		}

		private void colorGrab(int x, int y)
		{
			parentBuilder.setEyedropper(true);
			if (colorGrid[x][y] == null)
			{
				toolsPanel.setErase(true);
				return;
			}
			toolsPanel.setErase(false);
			Color newColor = colorGrid[x][y];
			toolsPanel.setColor(newColor);
		}

		private void fill(int x, int y)
		{
			LinkedList<IntegerPair> tiles = new LinkedList<IntegerPair>();
			tiles.add(new IntegerPair(x, y));

			while (tiles.size() > 0)
			{
				IntegerPair tile = tiles.pop();
				int tileX = tile.a;
				int tileY = tile.b;
				if (tileY >= height || tileY < 0 || tileX >= width
						|| tileX < 0)
					continue;
				if ((newColor == null && oldColor == null)
						|| (newColor != null && oldColor != null && newColor
								.equals(oldColor)))
					continue;

				boolean isReplaced = colorGrid[tileX][tileY] == null ? oldColor == null : colorGrid[tileX][tileY]
						.equals(oldColor);

				if (isReplaced)
				{
					this.replaceColor(tileX, tileY, newColor);

					tiles.add(new IntegerPair(tileX - 1, tileY));
					tiles.add(new IntegerPair(tileX + 1, tileY));
					tiles.add(new IntegerPair(tileX, tileY - 1));
					tiles.add(new IntegerPair(tileX, tileY + 1));
				}
			}
		}

		private void replaceColor(int x, int y, Color newColor)
		{
			if ((newColor == null && colorGrid[x][y] == null)
					|| (newColor != null && newColor
							.equals(colorGrid[x][y])))
				return;
			incrementGeneration = true;
			PreviousDrawAction prev = new PreviousDrawAction(x, y,
					colorGrid[x][y] == null ? null : new Color(
							colorGrid[x][y].getRed(),
							colorGrid[x][y].getGreen(),
							colorGrid[x][y].getBlue()));
			if (head == null)
				head = prev;
			else
				head.add(prev);
			colorGrid[x][y] = newColor;
		}
	}

	private class PreviousDrawAction
	{
		private final Color prevColor;
		private final IntegerPair xy;

		private PreviousDrawAction next;
		private int undoGeneration;

		public PreviousDrawAction(int x, int y, Color prevColor)
		{
			this.xy = new IntegerPair(x, y);
			this.prevColor = prevColor;
			this.undoGeneration = 0;
		}

		public void add(PreviousDrawAction newAction)
		{
			newAction.next = this;
			head = newAction;

		}

		public void incrementGeneration()
		{
			this.undoGeneration++;
			if (this.next != null)
			{
				this.next.incrementGeneration();
			}
		}

		public void decrementGeneration()
		{
			if (this.undoGeneration <= 1)
			{
				colorGrid[xy.a][xy.b] = prevColor;
				if (head.equals(this))
				{
					head = head.next;
					if (head != null)
						head.decrementGeneration();
				}
			}
			else
			{
				this.undoGeneration--;
				if (head == null)
				{
					head = this;
				}
				if (this.next != null)
				{
					this.next.decrementGeneration();
				}
			}
		}
	}

	private class IntegerPair
	{
		int a;
		int b;

		public IntegerPair(int a, int b)
		{
			this.a = a;
			this.b = b;
		}
	}
	
	private Dimension tileDim = new Dimension(20, 20);

	private Color[][] colorGrid;

	private PreviousDrawAction head;

	private boolean incrementGeneration;

	private int width;
	private int height;
		
	private SpriteBuilder parentBuilder;
	private JFrame drawPanelFrame;
	private ToolsPanel toolsPanel;
}
