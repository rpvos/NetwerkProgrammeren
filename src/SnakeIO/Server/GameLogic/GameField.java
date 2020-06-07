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

    public void addSnake(Snake snake) {
        snake.setID(snakes.size());
        this.snakes.add(snake);
    }


    public GameField() {
        this.snakes = new ArrayList<>();
        this.fruits = new ArrayList<>();
    }

    public void update(double deltaTime) {
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
            for (Point2D fruit : fruits) {
                if (head.distance(fruit) < 0.5) {
                    snake.hasEaten();
                }
            }
        }

        //spawn new fruit
        if (fruits.size() < maxFruits) {
            fruits.add(validSpot());
        }
    }

    public Point2D validSpot() {
        Random random = new Random();
        boolean foundPoint = false;
        Point2D point = new Point2D.Double();
        int tries = 0;
        while (!foundPoint && tries < 10) {
            int x = random.nextInt(playingfieldSize);
            int y = random.nextInt(playingfieldSize);
            point.setLocation(x, y);
            boolean collision = false;
            for (Snake snake : snakes) {
                if (snake.collide(point))
                    collision = true;
            }
            if (!collision)
                foundPoint = true;

            tries++;
        }
        return point;
    }

    public static double getSIZE() {
        return SIZE;
    }

    public ArrayList<Point2D> getFruits() {
        return fruits;
    }


}