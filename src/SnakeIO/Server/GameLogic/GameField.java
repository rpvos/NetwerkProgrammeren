package SnakeIO.Server.GameLogic;

import SnakeIO.Data;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Random;

public class GameField {
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
        for (Iterator<Snake> iterator = snakes.iterator(); iterator.hasNext(); ) {
            Snake snake = iterator.next();
            snake.update(deltaTime);
        }

        //check if he collides with a snake
        try {
            for (Snake snake : snakes) {
                int temp = 0;
                Point2D head = snake.getHead();

                for (Snake otherSnake : snakes) {
                    if (otherSnake.collide(head))
                        temp++;
                }

                if (temp > 2)
                    snake.died();
            }
        } catch (ConcurrentModificationException ignored) {
        }

        //check if he eats a fruit
        try {
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
        } catch (ConcurrentModificationException ignored) {
        }

        //spawn new fruit
        int maxFruits = Data.MAXFRUITS;
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
            int playingfieldSize = Data.FIELDSIZE;
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

    public ArrayList<Point2D> getFruits() {
        return fruits;
    }


    public void removeSnake(Snake snake) {
        snakesToRemove.add(snake.getId());
    }

    private void removeSnakes() {
        try {
            for (Iterator<Integer> iterator = snakesToRemove.iterator(); iterator.hasNext(); ) {
                int i = iterator.next();
                snakes.removeIf(snake -> snake.getId() == i);
            }
        } catch (NullPointerException e) {
            System.out.println("No players left");
        }
    }
}