package SnakeIO.Server.GameLogic;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;

public class GameField {
    private ArrayList<Snake> snakes;
    private ArrayList<Point2D> fruits;
    private final int playingfieldSize = 20;
    private final int maxFruits = 3;

    public void addSnake(Snake snake) {
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
            //todo add collision detection with other snakes
        }

        //check if he eats a fruit
        for (Snake snake : snakes) {
            for (Point2D fruit : fruits) {
                if(snake.collide(fruit))
                    snake.hasEaten();
            }
        }

        //spawn new fruit
        if (fruits.size() < maxFruits) {
            fruits.add(validSpot());
        }
    }

    private Point2D validSpot() {
        Random random = new Random();
        boolean foundPoint = false;
        Point2D point = new Point2D.Double();
        while (!foundPoint) {
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
        }
        return point;
    }
}