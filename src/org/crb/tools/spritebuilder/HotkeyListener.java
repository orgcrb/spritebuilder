package org.crb.tools.spritebuilder;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.crb.tools.PNGDrawer;



public class HotkeyListener implements KeyListener
{
	
	SpriteBuilder parentBuilder;
	
	private ToolsPanel toolsPanel;
	private DrawPanel drawPanel;

	public HotkeyListener(SpriteBuilder parentBuilder)
	{
		this.parentBuilder = parentBuilder;
		this.toolsPanel = this.parentBuilder.getToolsPanel();
		this.drawPanel = this.parentBuilder.getDrawPanel();
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{

		if (e.getKeyCode() == KeyEvent.VK_E)
		{
			this.toolsPanel.setErase(!this.toolsPanel.isErase());
		}
		if (e.getKeyCode() == KeyEvent.VK_F)
		{
			this.toolsPanel.setFill(!this.toolsPanel.isFill());
		}
		if (e.getKeyCode() == KeyEvent.VK_G)
		{
			this.toolsPanel.setGrid(!this.toolsPanel.isGrid());
			this.toolsPanel.repaint();
			this.drawPanel.repaint();
		}

		if (e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown())
		{
			this.drawPanel.undo();
		}

		if (e.getKeyCode() == KeyEvent.VK_C)
		{
			PNGDrawer.drawPNG(this.drawPanel.getColorGrid(), 5);
		}
		this.parentBuilder.calculateCursor();
		this.toolsPanel.repaint(); //TODO This is sometimes null. Fix it.
		this.drawPanel.repaint();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		this.keyPressed(e);

	}

}
