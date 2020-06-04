package SnakeIO.Client.Visual;

import SnakeIO.Client.GameLogic.Directions;
import SnakeIO.Client.LogicHub;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GameScreen extends Application {
    private Canvas canvas;
    private ArrayList<String> keyPressed;
    private LogicHub logicHub;
    private AnimationTimer animationTimer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.logicHub = LogicHub.getLogicHub();
        this.keyPressed = new ArrayList<>();

        logicHub.start();

        this.canvas = new Canvas(640, 420);
        Group root = new Group(canvas);
        Scene gameScene = new Scene(root);

        gameScene.setOnKeyPressed(event -> {
            if (!keyPressed.contains(event.getCode().toString())) {
                keyPressed.add(event.getCode().toString());
                setDirection(event);
            }
        });

        gameScene.setOnKeyReleased(event -> keyPressed.remove(event.getCode().toString()));

        this.animationTimer = new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1)
                    last = now;
                update((now - last) / 1000000000.0);
                last = now;

                draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
            }
        };

        BorderPane gridPane = new BorderPane();
        VBox vBox = new VBox();
        Label label = new Label("Username:");
        TextField textField = new TextField();
        Button button = new Button("Start");
        button.setOnAction(event -> {
            logicHub.setUsername(textField.getText());
            primaryStage.setScene(gameScene);
            animationTimer.start();
            primaryStage.show();
        });

        vBox.getChildren().add(label);
        vBox.getChildren().add(textField);
        vBox.getChildren().add(button);

        gridPane.setCenter(vBox);

        Scene menuScene = new Scene(gridPane);
        primaryStage.setScene(menuScene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            logicHub.disconnect();
        });
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
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());
        logicHub.draw(graphics);
    }

    private void update(double deltaTime) {
        logicHub.update(deltaTime);
    }




}
