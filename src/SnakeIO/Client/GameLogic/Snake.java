package SnakeIO.Client.GameLogic;

import SnakeIO.Client.Visual.GameObject;
import SnakeIO.Data;
import SnakeIO.DataSnake;
import SnakeIO.Directions;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Snake implements GameObject {
    private ArrayList<Point2D> positions;
    private static final double TIME_TO_MOVE = Data.TIME_TO_MOVE;

    private Directions direction;
    private boolean hasEaten;
    private boolean isDead;
    private double timer;

    public Snake(Point2D startingPosition) {
        this.positions = new ArrayList<>();
        this.positions.add(startingPosition);
        this.direction = Directions.NORTH;
        this.hasEaten = false;
        this.isDead = false;
        this.timer = 0;
    }

    public Snake(DataSnake dataSnake) {
        this.positions = dataSnake.getSnakePositions();
        this.direction = dataSnake.getDirection();
        this.hasEaten = false;
        this.isDead = false;
        this.timer = 0;
    }

    public void update(double deltaTime) {
        if (!isDead) {
            timer += deltaTime;
            if (timer >= TIME_TO_MOVE) {
                move();
                timer -= TIME_TO_MOVE;
            }
        }
    }

    private void move() {
        //get the head
        Point2D head = positions.get(0);
        if (hasEaten) {
            //add a new part to snake
            positions.add(new Point2D.Double());
            hasEaten = false;
        }


        for (int i = positions.size() - 1; i > 0; i--) {
            positions.set(i, positions.get(i - 1));
        }

        double speed = 1;

        switch (direction) {

            case NORTH:
                positions.set(0, new Point2D.Double(head.getX(), head.getY() - speed));

                break;
            case EAST:
                positions.set(0, new Point2D.Double(head.getX() + speed, head.getY()));

                break;
            case SOUTH:
                positions.set(0, new Point2D.Double(head.getX(), head.getY() + speed));

                break;
            case WEST:
                positions.set(0, new Point2D.Double(head.getX() - speed, head.getY()));

                break;
        }
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }

    public void hasEaten() {
        this.hasEaten = true;
    }

    public void draw(FXGraphics2D graphics) {
        for (Point2D pos : positions) {
            graphics.setColor(Color.RED);
            int blocksize = Data.BLOCKSIZE;
            graphics.fillRect((int) (blocksize * pos.getX() - blocksize / 2), (int) (blocksize * pos.getY() - blocksize / 2), blocksize, blocksize);
        }
    }

    public Directions getDirection() {
        return direction;
    }

    public ArrayList<Point2D> getPositions() {
        return positions;
    }

    public boolean isDead() {
        return isDead;
    }

    public void died() {
        isDead = true;
    }
}
