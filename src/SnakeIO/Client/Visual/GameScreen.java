package SnakeIO.Client.Visual;

import SnakeIO.Client.GameLogic.Directions;
import SnakeIO.Client.LogicHub;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GameScreen extends Application {
    private Canvas canvas;
    private ArrayList<String> keyPressed;
    private LogicHub logicHub;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.logicHub = LogicHub.getLogicHub();
        this.keyPressed = new ArrayList<>();

        logicHub.start(new Point2D.Double(20, 20));

        this.canvas = new Canvas(640, 420);
        Group root = new Group(canvas);
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(event -> {
            if (!keyPressed.contains(event.getCode().toString())) {
                keyPressed.add(event.getCode().toString());
                setDirection(event);
            }
        });

        scene.setOnKeyReleased(event -> keyPressed.remove(event.getCode().toString()));

        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;

                draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
            }
        }.start();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setDirection(KeyEvent event) {
        switch (event.getCode()) {
            case W:
                logicHub.getSnake().setDirection(Directions.NORTH);
                break;
            case D:
                logicHub.getSnake().setDirection(Directions.EAST);
                break;
            case S:
                logicHub.getSnake().setDirection(Directions.SOUTH);
                break;
            case A:
                logicHub.getSnake().setDirection(Directions.WEST);
                break;
        }
    }

    private void draw(FXGraphics2D graphics) {
        graphics.setColor(Color.GRAY);
        graphics.clearRect(0, 0, (int)canvas.getWidth(), (int)canvas.getHeight());
        logicHub.draw(graphics);
    }

    private void update(double deltaTime) {
        logicHub.update(deltaTime);
    }


}
