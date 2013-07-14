/**
 * Copyright: Chris Bielak 2013
 * @author: Chris Bielak
 */
package org.crb.tools;

import org.crb.tools.spritebuilder.SpriteBuilder;

public class BuilderMain
{

	public static BuilderMain builder;

	public static void main(String[] args)
	{
		_main = new SpriteBuilder(IMAGE_SIZE, IMAGE_SIZE);
	}

	public static int IMAGE_SIZE = 20;

	public static SpriteBuilder _main;
}
