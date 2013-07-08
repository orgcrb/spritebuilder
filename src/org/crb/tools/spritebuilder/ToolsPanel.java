package org.crb.tools.spritebuilder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

import org.crb.tools.PNGDrawer;


public class ToolsPanel extends JPanel
{

	public ToolsPanel(SpriteBuilder parentBuilder)
	{

		this.parentBuilder = parentBuilder;

		// this.fillButton.setIcon(new ImageIcon("fillTool.png"));
		this.fillButton.addKeyListener(new HotkeyListener(parentBuilder));

		// this.eraseButton.setIcon(new ImageIcon("eraseTool.png"));
		this.eraseButton.addKeyListener(new HotkeyListener(parentBuilder));

		this.gridButton.setSelected(true);
		this.gridButton.addActionListener(new GridListener());
		this.gridButton.addKeyListener(new HotkeyListener(parentBuilder));

		this.convertToPng.addActionListener(new PNGConverstionListener());
		this.convertToPng.addKeyListener(new HotkeyListener(parentBuilder));

		this.colorChooser = new JColorChooser();

		// menu bar
		
		this.menuBar = new JMenuBar();
		this.fileMenu = new JMenu("File");
		this.newSpriteOption = new JMenuItem("New Sprite", new ImageIcon(
				"art/newFileIcon.png"));
		this.newSpriteOption.addActionListener(new NewFileListener());
		this.saveOption = new JMenuItem("Save", new ImageIcon("art/saveIcon.png"));
		this.saveOption.addActionListener(new SaveFileListener());
		this.loadOption = new JMenuItem("Load", new ImageIcon("art/loadIcon.png"));
		this.loadOption.addActionListener(new LoadFileListener());
		this.exitOption = new JMenuItem("Exit");
		this.exitOption.addActionListener(new ExitListener());

		//this.fileMenu.add(this.newSpriteOption);
		this.fileMenu.add(this.saveOption);
		this.fileMenu.add(this.loadOption);
		this.fileMenu.addSeparator();
		this.fileMenu.add(this.exitOption);

		this.viewMenu = new JMenu("View");
		this.previewMenuOption = new JMenuItem("Preview Panel");
		this.previewMenuOption.addActionListener(new PanelOptionListener(
				PanelOptionListener.PANEL_PREVIEW));
		this.drawMenuOption = new JMenuItem("Draw Panel");
		this.drawMenuOption.addActionListener(new PanelOptionListener(
				PanelOptionListener.PANEL_DRAW));

		this.viewMenu.add(this.previewMenuOption);
		this.viewMenu.add(this.drawMenuOption);

		this.menuBar.add(this.fileMenu);
		this.menuBar.add(this.viewMenu);

		this.drawComponents();
	}

	private void drawComponents()
	{
		this.setLayout(new BorderLayout());
		this.add(this.colorChooser, BorderLayout.NORTH);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(0, 2));

		JPanel pixelAdjust = new JPanel();
		
		pixelAdjust.setLayout(new BoxLayout(pixelAdjust, BoxLayout.Y_AXIS));
		
		JSlider j = new JSlider();
		j.setName("test");
		
		JLabel l = new JLabel("Adjust Pixel Size");
		l.setAlignmentY(200f);
		
		pixelAdjust.add(l);
		pixelAdjust.add(j);
		//this.add(pixelAdjust, BorderLayout.SOUTH);
		buttonsPanel.add(this.eraseButton);
		buttonsPanel.add(this.fillButton);
		buttonsPanel.add(this.gridButton);
		buttonsPanel.add(this.convertToPng);
		this.add(buttonsPanel, BorderLayout.CENTER);

	}

	public JMenuBar getJMenuBar()
	{
		return this.menuBar;
	}

	public boolean isErase()
	{
		return this.eraseButton.isSelected();
	}

	public void setErase(boolean eraseVal)
	{
		this.eraseButton.setSelected(eraseVal);
	}

	public boolean isFill()
	{
		return this.fillButton.isSelected();
	}

	public void setFill(boolean fillVal)
	{
		this.fillButton.setSelected(fillVal);
	}

	public boolean isGrid()
	{
		return this.gridButton.isSelected();
	}

	public void setGrid(boolean gridVal)
	{
		this.gridButton.setSelected(gridVal);
	}

	public Color getColor()
	{
		return this.colorChooser.getColor();
	}

	public void setColor(Color color)
	{
		this.colorChooser.setColor(color);
	}

	private class PNGConverstionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			PNGDrawer.drawPNG(parentBuilder.getDrawPanel().getColorGrid(), parentBuilder.getPreviewPanel().getPixelSize());

		}

	}

	private class GridListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			parentBuilder.getDrawPanel().repaint();
		}

	}

	private class NewFileListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			parentBuilder.createDrawPanel(20,10);
		}

	}

	private class SaveFileListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.showSaveDialog(ToolsPanel.this);
			File file = fileChooser.getSelectedFile();
			if (file==null) return;
			PrimitiveSprite sprite = new PrimitiveSprite(parentBuilder
					.getDrawPanel().getColorGrid());
			try
			{
				FileOutputStream fileOut = new FileOutputStream(
						file.getAbsolutePath() + ".ser");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(sprite);
				out.close();
			} catch (Exception e1)
			{
				System.out.println("Save failed. file=" + file + " exception="
						+ e1.getMessage());
			}
		}

	}

	private class LoadFileListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.showOpenDialog(ToolsPanel.this);
			File file = fileChooser.getSelectedFile();
			if (file==null) return;
			try
			{
				FileInputStream fileIn = new FileInputStream(file.getAbsoluteFile());
				ObjectInputStream in = new ObjectInputStream(fileIn);
				PrimitiveSprite sprite = (PrimitiveSprite) in.readObject();
				in.close();
				fileIn.close();
				parentBuilder.getDrawPanel().setColorGrid(sprite.getColorGrid());
				parentBuilder.getDrawPanel().repaint();
			} catch (Exception e1)
			{
				System.out.println("Load failed. file=" + file + " exception="
						+ e1.getMessage());
			}
		}

	}

	private class ExitListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			System.exit(0);

		}

	}

	private class PanelOptionListener implements ActionListener
	{

		public PanelOptionListener(String panelType_)
		{
			_panelType = panelType_;
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (_panelType.equals(PANEL_PREVIEW))
				parentBuilder.getPreviewFrame().setVisible(true);
			else if (_panelType.equals(PANEL_DRAW))
				parentBuilder.getDrawFrame().setVisible(true);
			else
				System.out.println("Unknown panel type.");

		}

		static final String PANEL_PREVIEW = "PREVIEW";
		static final String PANEL_DRAW = "DRAW";

		private String _panelType;

	}

	private JMenuBar menuBar;

	private JMenu fileMenu;
	private JMenuItem newSpriteOption;
	private JMenuItem saveOption;
	private JMenuItem loadOption;
	private JMenuItem exitOption;

	private JMenu viewMenu;
	private JMenuItem previewMenuOption;
	private JMenuItem drawMenuOption;

	private JColorChooser colorChooser;
	private JToggleButton eraseButton = new JToggleButton("(E)rase");
	private JToggleButton fillButton = new JToggleButton("(F)ill");
	private JToggleButton gridButton = new JToggleButton("(G)rid");

	private final SpriteBuilder parentBuilder;

	private JButton convertToPng = new JButton("(C)onvert to .png");

}
