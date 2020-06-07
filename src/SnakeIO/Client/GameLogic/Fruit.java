package SnakeIO.Client.GameLogic;

import SnakeIO.Client.Visual.GameObject;
import SnakeIO.Data;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Point2D;

public class Fruit implements GameObject {

    private Point2D pos;

    public Fruit(Point2D pos) {
        this.pos = pos;
    }

    @Override
    public void draw(FXGraphics2D graphics2D) {
        int blocksize = Data.BLOCKSIZE;

        graphics2D.setColor(Color.blue);
        graphics2D.drawRect((int) (blocksize * pos.getX()) - 10, (int) (blocksize * pos.getY()) + 10, blocksize, blocksize);
    }

    @Override
    public void update(double deltaTime) {

    }
}
