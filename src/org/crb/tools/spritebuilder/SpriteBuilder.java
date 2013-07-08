package org.crb.tools.spritebuilder;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;


public class SpriteBuilder
{
	public SpriteBuilder(int width, int height)
	{

		this.frameMenuBar.add(this.frameMenu);
		this.frameMenu.add(new JMenuItem("(A)dd Frame"));
		this.frameMenu.add(new JMenuItem("(C)opy Frame to New Frame"));
		this.frameMenu.add(new JMenuItem("(R)emove Frame"));
		this.frameMenu.add(this.frameMenuBar);
		this.frameMenu.setVisible(true);

		this.toolPanelFrame.add(this.toolsPanel);
		this.toolPanelFrame.setTitle("Tools Panel");
		this.toolPanelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.toolPanelFrame.setVisible(true);
		this.toolPanelFrame.pack();
		this.toolPanelFrame.setSize(
				this.toolPanelFrame.getWidth(),
				this.toolPanelFrame.getHeight() + 100);

		this.toolPanelFrame.setJMenuBar(this.toolsPanel.getJMenuBar());

		this.previewPanel = new PreviewPanel(width, height, this);

		this.previewPanelFrame.setSize(this.toolPanelFrame.getWidth(), 300);
		this.previewPanelFrame.add(this.previewPanel);
		this.previewPanelFrame.setTitle("Preview Panel");
		this.previewPanelFrame.setLocation(0, this.toolPanelFrame.getY()
				+ this.toolPanelFrame.getHeight());
		this.previewPanelFrame.setVisible(true);

		this.createDrawPanel(width, height);
	}
	
	public void createDrawPanel(int width, int height)
	{
		this.drawPanel = new DrawPanel(width, height, this);
		this.drawPanelTabbedPane.add(this.drawPanel, "frame" + this.drawPanelTabbedPane.getTabCount());
		this.drawPanelTabbedPane.addKeyListener(new HotkeyListener(this));

		this.drawPanelFrame.setLocation(this.toolPanelFrame.getX()
				+ this.toolPanelFrame.getWidth(), 0);
		this.drawPanelFrame.setJMenuBar(this.frameMenuBar);
		this.drawPanelFrame.setSize(600, 600);
		this.drawPanelFrame.add(drawPanelTabbedPane);
		this.drawPanelFrame.setTitle("Draw Panel");
		this.drawPanelFrame.setVisible(true);
		this.drawPanel.repaint();
	}

	public void calculateCursor()
	{
		boolean eraseOn = this.toolsPanel.isErase();
		boolean fillOn = this.toolsPanel.isFill();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image fillCursor = toolkit.getImage("art/fillTool.png");
		Image eraseCursor = toolkit.getImage("art/eraseTool.png");
		Image fillPlusEraseCursor = toolkit.getImage("art/fillPlusEraseTool.png");
		Image eyeDroperCursor = toolkit.getImage("art/eyeDropperTool.png");

		Cursor newCursor;// = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
		if (isEyedropper)
		{
			newCursor = toolkit.createCustomCursor(eyeDroperCursor, new Point(
					0, 0), "eyedropper");
		}
		else if (eraseOn)
		{
			if (fillOn)
			{
				newCursor = toolkit.createCustomCursor(
						fillPlusEraseCursor,
						new Point(0, 0),
						"fillPlusErase");
			}
			else
			{
				newCursor = toolkit.createCustomCursor(eraseCursor, new Point(
						0, 0), "Erase");
			}
		}
		else if (fillOn)
		{
			newCursor = toolkit.createCustomCursor(
					fillCursor,
					new Point(0, 0),
					"Fill");
		}
		else
		{
			newCursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
		}
		drawPanel.setCursor(newCursor);
	}

	/* Menu Actions */

	public boolean isEyedropper()
	{
		return this.isEyedropper;
	}

	public void setEyedropper(boolean eyedropperVal)
	{
		this.isEyedropper = eyedropperVal;
	}

	/* Getters */

	public ToolsPanel getToolsPanel()
	{
		return this.toolsPanel;
	}

	public DrawPanel getDrawPanel()
	{
		return this.drawPanel;
	}

	public PreviewPanel getPreviewPanel()
	{
		return this.previewPanel;
	}
	
	public JFrame getToolsFrame()
	{
		return this.toolPanelFrame;
	}

	public JFrame getDrawFrame()
	{
		return this.drawPanelFrame;
	}

	public JFrame getPreviewFrame()
	{
		return this.previewPanelFrame;
	}

	private JMenuBar frameMenuBar = new JMenuBar();
	private JMenu frameMenu = new JMenu("Frame Actions");

	private JTabbedPane drawPanelTabbedPane = new JTabbedPane();

	private JFrame drawPanelFrame = new JFrame();
	private DrawPanel drawPanel;

	private JFrame toolPanelFrame = new JFrame();
	private ToolsPanel toolsPanel = new ToolsPanel(this);

	private JFrame previewPanelFrame = new JFrame();
	private PreviewPanel previewPanel;

	private boolean isEyedropper;
}
