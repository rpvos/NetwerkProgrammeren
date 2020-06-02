package SnakeIO.Client.Visual;

import org.jfree.fx.FXGraphics2D;

public interface GameObject {
    void draw(FXGraphics2D graphics2D);
    void update(double deltaTime);
}
