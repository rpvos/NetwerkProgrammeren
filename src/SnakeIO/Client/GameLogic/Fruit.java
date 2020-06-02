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
        graphics2D.drawRect(20 * (int) pos.getX(), 20 * (int) pos.getY(), 20, 20);
    }

    @Override
    public void update(double deltaTime) {

    }
}
