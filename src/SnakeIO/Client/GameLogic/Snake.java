package SnakeIO.Client.GameLogic;

import SnakeIO.Client.Visual.GameObject;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Snake implements GameObject {
    private ArrayList<Point2D> positions;
    private final int SPEED = 1;
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
                head.setLocation(head.getX(), head.getY() - SPEED);

                break;
            case EAST:
                head.setLocation(head.getX() + SPEED, head.getY());

                break;
            case SOUTH:
                head.setLocation(head.getX(), head.getY() + SPEED);

                break;
            case WEST:
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

    public void setHasEaten(boolean hasEaten) {
        this.hasEaten = hasEaten;
    }

    public void draw(FXGraphics2D graphics) {
        for (Point2D pos : positions) {
            graphics.drawRect(20 * (int) pos.getX(), 20 * (int) pos.getY(), 20, 20);
        }
        ;
    }
}
