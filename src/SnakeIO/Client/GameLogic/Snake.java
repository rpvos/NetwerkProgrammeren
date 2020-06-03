package SnakeIO.Client.GameLogic;

import SnakeIO.Client.Visual.GameObject;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Snake implements GameObject {
    private ArrayList<Point2D> positions;
    private final int SPEED = 1;
    private Directions direction;
    private boolean hasEaten;
    private boolean isDead;
    private double timer;
    private double playingfieldSize = 20;
    private final int SIZE = 20;

    public Snake(Point2D startingPosition) {
        this.positions = new ArrayList<>();
        this.positions.add(startingPosition);
        this.direction = Directions.NORTH;
        this.hasEaten = false;
        this.isDead = false;
        this.timer = 0;
    }


    public void update(double deltaTime) {
        if (!isDead) {
            timer += deltaTime;
            if (timer >= 1.0 / SPEED) {
                move();
                timer -= 1.0 / SPEED;
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
        switch (direction) {

            case NORTH:
                if (head.getY() - SPEED < 0)
                    isDead = true;
                else
                    head.setLocation(head.getX(), head.getY() - SPEED);

                break;
            case EAST:
                if (head.getX() + SPEED > playingfieldSize)
                    isDead = true;
                else
                    head.setLocation(head.getX() + SPEED, head.getY());

                break;
            case SOUTH:
                if (head.getY() + SPEED > playingfieldSize)
                    isDead = true;
                else
                    head.setLocation(head.getX(), head.getY() + SPEED);

                break;
            case WEST:
                if (head.getX() - SPEED < 0)
                    isDead = true;
                else
                    head.setLocation(head.getX() - SPEED, head.getY());

                break;
        }
        for (int i = 1; i < positions.size(); i++) {
            positions.get(i).setLocation(positions.get(i - 1));
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
            graphics.fillRect((int)(SIZE * pos.getX() - SIZE/2), (int)(SIZE * pos.getY() - SIZE/2), SIZE, SIZE);
        }
    }

    public Directions getDirection() {
        return direction;
    }

    public ArrayList<Point2D> getPositions() {
        return positions;
    }
}
