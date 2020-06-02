package SnakeIO.Client;

import SnakeIO.Client.GameLogic.Fruit;
import SnakeIO.Client.GameLogic.Snake;
import SnakeIO.Client.ServerLogic.Server;
import SnakeIO.Client.Visual.GameObject;
import SnakeIO.Data;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class LogicHub {
    private Snake snake;
    private ArrayList<Fruit> fruits;
    private Server server;

    private ArrayList<GameObject> gameObjects;

    private static LogicHub logicHub = null;

    public synchronized static LogicHub getLogicHub() {
        if (logicHub == null) {
            logicHub = new LogicHub();
        }

        return logicHub;
    }

    private LogicHub() {
        this.server = new Server(Data.host, Data.port);
        this.gameObjects = new ArrayList<>();
        this.fruits = new ArrayList<>();
        server.connect();
    }

    public void start(Point2D startingPosition) {
        snake = new Snake(startingPosition);
    }

    public void update(double deltaTime) {
        snake.update(deltaTime);
    }

    public void draw(FXGraphics2D graphics) {
        for (Fruit fruit : fruits) {
            fruit.draw(graphics);
        }
        snake.draw(graphics);
    }

    public Snake getSnake() {
        return snake;
    }

    public void setFruits(ArrayList<Fruit> fruits) {
        this.fruits = fruits;
    }

    public void setUsername(String username) {
        server.setUsername(username);
    }
}
