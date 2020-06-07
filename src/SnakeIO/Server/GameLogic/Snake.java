package SnakeIO.Server.GameLogic;

import SnakeIO.Directions;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Snake {
    private static final double BLOCKSIZE = 20;
    private static final double TIME_TO_MOVE = 0.5;

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
        this.ate = false;
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

    public void setPositions(ArrayList<Point2D> positions) {
        this.positions = positions;

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
        if (!hasEaten) {
            this.hasEaten = true;
            this.ate = true;
        }
    }

    public Point2D getHead() {
        return positions.get(0);
    }


    public boolean collide(Point2D point) {
        for (Point2D pos : positions) {
            if (pos.distance(point) < BLOCKSIZE / 2)
                return true;
        }
        return false;
    }

    public void died() {
        isDead = true;
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isAte() {
        boolean temp = this.ate;
        if (this.ate) {
            this.ate = false;
        }
        return temp;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
