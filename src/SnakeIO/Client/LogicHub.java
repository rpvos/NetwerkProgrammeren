package SnakeIO.Client;

import SnakeIO.Client.GameLogic.Snake;
import SnakeIO.Client.ServerLogic.Server;
import SnakeIO.Client.Visual.GameObject;
import SnakeIO.Data;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class LogicHub {
    private Snake snake;
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
        server.connect();
    }

    public void start(Point2D startingPosition) {
        snake = new Snake(startingPosition);
        gameObjects.add(snake);
    }

    public Snake getSnake() {
        return snake;
    }


    public void update(double deltaTime) {
        for (GameObject gameObject : gameObjects) {
            gameObject.update(deltaTime);
        }
    }

    public void draw(FXGraphics2D graphics) {
        for (GameObject gameObject : gameObjects) {
            gameObject.draw(graphics);
        }
    }
}
