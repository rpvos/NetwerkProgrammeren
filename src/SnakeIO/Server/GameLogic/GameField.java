package SnakeIO.Server.GameLogic;

import SnakeIO.Client.GameLogic.Snake;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GameField {
    private ArrayList<Snake> snakes;
    private ArrayList<Point2D> fruits;

    public void addSnake(Snake snake){
        this.snakes.add(snake);
    }

    public Snake getSnake(int clientIndex){
        return snakes.get(clientIndex);
    }

    public GameField() {
        this.snakes = new ArrayList<>();
        this.fruits = new ArrayList<>();
    }

    public void update(double deltaTime){
        for (Snake snake : snakes) {
            snake.update(deltaTime);
        }


    }
}