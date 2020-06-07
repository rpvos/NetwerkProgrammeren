package SnakeIO.Client;

import SnakeIO.Client.GameLogic.Fruit;
import SnakeIO.Client.GameLogic.Snake;
import SnakeIO.Client.ServerLogic.Server;
import SnakeIO.Data;
import SnakeIO.DataSnake;
import org.jfree.fx.FXGraphics2D;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class LogicHub {
    private Snake snake;
    private ArrayList<Fruit> fruits;
    private ArrayList<Fruit> newFruits;
    private ArrayList<Snake> otherSnakes;
    private Server server;

    private static LogicHub logicHub = null;

    public synchronized static LogicHub getLogicHub() {
        if (logicHub == null) {
            logicHub = new LogicHub();
        }

        return logicHub;
    }

    private LogicHub() {
        this.server = new Server(Data.host, Data.port);
        this.fruits = new ArrayList<>();
        server.connect();
    }

    public void start() {
        this.server.startInput();

    }

    public void update(double deltaTime) {
        try {
            fruits = newFruits;
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
        }

        if (snake != null)
            snake.update(deltaTime);

        for (Snake snake : otherSnakes)
            snake.update(deltaTime);
    }

    public void draw(FXGraphics2D graphics) {
        for (Fruit fruit : fruits) {
            fruit.draw(graphics);
        }

        if (snake != null)
            snake.draw(graphics);

        for (Snake snake : otherSnakes)
            snake.draw(graphics);
    }

    public Snake getSnake() {
        return snake;
    }

    public void setFruits(ArrayList<Point2D> positions) {
        this.newFruits = new ArrayList<>();
        for (Point2D pos : positions) {
            newFruits.add(new Fruit(pos));
        }
    }

    public void setUsername(String username) {
        server.setUsername(username);
    }

    public void disconnect() {
        server.disconnect();
    }

    public void setStart(int x, int y) {
        Point2D.Double startingPosition = new Point2D.Double(x, y);
        this.snake = new Snake(startingPosition);
        this.server.startOutput();
    }

    public void setOtherSnakes(ArrayList<DataSnake> dataSnakes) {
        this.otherSnakes = new ArrayList<>();
        for (DataSnake dataSnake : dataSnakes) {
            otherSnakes.add(new Snake(dataSnake));
        }
    }
}
