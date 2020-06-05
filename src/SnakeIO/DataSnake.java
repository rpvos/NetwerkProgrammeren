package SnakeIO;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * this is a data object which we send to clients and receive from clients containing everything we need
 */
public class DataSnake implements Serializable {
    private ArrayList<Point2D> snakePositions;
    private Directions direction;

    private boolean ate;
    private boolean isDead;

    public DataSnake(ArrayList<Point2D> snakePositions, Directions direction, boolean hasEaten, boolean isDead) {
        this.snakePositions = snakePositions;
        this.direction = direction;
        this.ate = hasEaten;
        this.isDead = isDead;
    }

    public ArrayList<Point2D> getSnakePositions() {
        return snakePositions;
    }

    public Directions getDirection() {
        return direction;
    }

    public boolean isAte() {
        return ate;
    }

    public boolean isDead() {
        return isDead;
    }
}
