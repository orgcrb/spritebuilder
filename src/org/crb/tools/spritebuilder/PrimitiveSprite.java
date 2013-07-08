package org.crb.tools.spritebuilder;

import java.awt.Color;

public class PrimitiveSprite implements java.io.Serializable
{
	public PrimitiveSprite(Color[][] colorGrid)
	{
		this.colorGrid = colorGrid;
	}
	
	public Color[][] getColorGrid()
	{
		return this.colorGrid;
	}
	
	private static final long serialVersionUID = 8660237601918241704L;

	private final Color[][] colorGrid;
}
