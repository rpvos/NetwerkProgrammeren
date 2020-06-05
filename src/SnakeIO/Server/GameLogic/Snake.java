package SnakeIO.Server.GameLogic;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Snake {
    private static final double SIZE = 20;
    private static final int SPEED = 1;

    private ArrayList<Point2D> positions;
    private Directions direction;
    private boolean hasEaten;
    private boolean isDead;
    private double timer;
    private int id;
    private boolean ate;

    public Snake(Point2D startingPosition) {
        this.positions = new ArrayList<>();
        this.positions.add(startingPosition);
        this.direction = Directions.NORTH;
        this.hasEaten = false;
        this.isDead = false;
        this.timer = 0;
        this.ate= false;
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

    public void setPositions(ArrayList<Point2D> positions) {

        for (int i = 0; i < this.positions.size(); i++) {
            if (this.positions.get(i).distance(positions.get(i)) < 2 * SIZE)
                this.positions.get(i).setLocation(positions.get(i));
        }

    }

    public ArrayList<Point2D> getPositions() {
        return positions;
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }

    public Directions getDirection() {
        return direction;
    }

    public void hasEaten() {
        this.hasEaten = true;
        this.ate = true;
        //todo notify the client that the snake has eaten
    }

    public Point2D getHead() {
        return positions.get(0);
    }


    public boolean collide(Point2D point) {
        for (Point2D pos : positions) {
            if (pos.distance(point) < SIZE / 2)
                return true;
        }
        return false;
    }

    public void died() {
        isDead = true;
        //todo notify the client that the snake has died
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isAte() {
        boolean temp = this.ate;
        if (this.ate == true){
            this.ate = false;
        }
        return temp;
    }

    public void setID(int id) {
        this.id = id;
    }
}
