package SnakeIO.Client;

import SnakeIO.Client.GameLogic.Snake;
import SnakeIO.Client.ServerLogic.Server;
import SnakeIO.Data;

import java.awt.geom.Point2D;

public class LogicHub {
    private Snake snake;
    private Server server;

    private static LogicHub logicHub = null;

    public static LogicHub getLogicHub() {
        if (logicHub == null) {
            synchronized (LogicHub.class) {
                logicHub = new LogicHub();
            }
        }

        return logicHub;
    }

    private LogicHub() {
        this.server = new Server(Data.host, Data.port);
    }

    public void start(Point2D startingPosition) {
        snake = new Snake(startingPosition);
    }

    public Snake getSnake() {
        return snake;
    }


}
