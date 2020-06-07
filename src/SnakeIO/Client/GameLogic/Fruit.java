package SnakeIO.Client.GameLogic;

import SnakeIO.Client.Visual.GameObject;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Point2D;

public class Fruit implements GameObject {

    private Point2D pos;

    public Fruit(Point2D pos) {
        this.pos = pos;
    }

    @Override
    public void draw(FXGraphics2D graphics2D) {
        graphics2D.drawRect((int) (20 * pos.getX()) - 10, (int) (20 * pos.getY()) + 10, 20, 20);
    }

    @Override
    public void update(double deltaTime) {

    }
}
