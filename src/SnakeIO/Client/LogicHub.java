package SnakeIO.Client;

import SnakeIO.Client.GameLogic.Fruit;
import SnakeIO.Client.GameLogic.Snake;
import SnakeIO.Client.ServerLogic.Server;
import SnakeIO.Client.Visual.GameObject;
import SnakeIO.Data;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class LogicHub {
    private Snake snake;
    private ArrayList<Fruit> fruits;
    private Server server;

    private ArrayList<GameObject> gameObjects;

    private static LogicHub logicHub = null;
    private Point2D.Double startingPosition;

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

    public void start() {
        this.server.startInput();

    }

    public void update(double deltaTime) {
        if (snake != null)//todo ugly
            snake.update(deltaTime);
    }

    public void draw(FXGraphics2D graphics) {
        for (Fruit fruit : fruits) {
            fruit.draw(graphics);
        }
        if (snake != null)//todo ugly
            snake.draw(graphics);
    }

    public Snake getSnake() {
        return snake;
    }

    public void setFruits(ArrayList<Point2D> positions) {
        fruits.clear();
        for (Point2D pos : positions) {
            fruits.add(new Fruit(pos));
        }
    }

    public void setUsername(String username) {
        server.setUsername(username);
    }

    public void disconnect() {
        server.disconnect();
    }

    public void setStart(int x, int y) {
        this.startingPosition = new Point2D.Double(x, y);
        this.snake = new Snake(startingPosition);
        this.server.startOutput();
    }
}
