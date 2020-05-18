package Client;

import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public abstract class GameObject {

	private Point2D location;
	private Ellipse2D shape;

	public abstract void draw(FXGraphics2D graphics);

	public abstract void update(double deltaTime);

}