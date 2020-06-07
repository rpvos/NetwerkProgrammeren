package SnakeIO.Server.GameLogic;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GameField {
    private static final double SIZE = 20;
    private final int playingfieldSize = 20;
    private final int maxFruits = 3;


    private ArrayList<Snake> snakes;
    private ArrayList<Point2D> fruits;
    private ArrayList<Integer> snakesToRemove;

    public void addSnake(Snake snake) {
        snake.setID(snakes.size());
        this.snakes.add(snake);
    }


    public GameField() {
        this.snakes = new ArrayList<>();
        this.fruits = new ArrayList<>();
        this.snakesToRemove = new ArrayList<>();
    }

    public void update(double deltaTime) {
        removeSnakes();

        //update the movement
        for (Snake snake : snakes) {
            snake.update(deltaTime);
        }

        //check if he collides with a snake
        for (Snake snake : snakes) {
            int temp = 0;
            Point2D head = snake.getHead();

            for (Iterator<Snake> iterator = snakes.iterator(); iterator.hasNext(); ) {
                Snake otherSnake = iterator.next();
                if (otherSnake.collide(head))
                    temp++;
            }

            if (temp > 2)
                snake.died();
        }

        //check if he eats a fruit
        for (Snake snake : snakes) {
            Point2D head = snake.getHead();
            for (Iterator<Point2D> iterator = fruits.iterator(); iterator.hasNext(); ) {
                Point2D fruit = iterator.next();
                if (head.distance(fruit) < 0.5) {
                    snake.hasEaten();
                    iterator.remove();
                }
            }
        }

        //spawn new fruit
        if (fruits.size() < maxFruits) {
            Point2D spot = validSpot();
            if (spot != null) {
                fruits.add(spot);
            }
        }
    }

    public Point2D validSpot() {
        Random random = new Random();
        boolean foundPoint = false;
        Point2D point = null;
        int tries = 0;
        while (!foundPoint && tries < 10) {
            int x = random.nextInt(playingfieldSize);
            int y = random.nextInt(playingfieldSize);
            point = new Point2D.Double(x, y);
            boolean collision = false;
            for (Snake snake : snakes) {
                if (snake.collide(point))
                    collision = true;
            }
            if (!collision)
                foundPoint = true;

            tries++;
        }

        if (!foundPoint)
            return null;

        return point;
    }

    public static double getSIZE() {
        return SIZE;
    }

    public ArrayList<Point2D> getFruits() {
        return fruits;
    }


    public void removeSnake(Snake snake) {
        snakesToRemove.add(snake.getId());
    }

    private void removeSnakes() {
        for (int i : snakesToRemove) {
            snakes.removeIf(snake -> snake.getId() == i);
        }
    }
}