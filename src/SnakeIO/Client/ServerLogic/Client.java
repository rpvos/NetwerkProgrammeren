package SnakeIO.Client.ServerLogic;

import SnakeIO.Client.LogicHub;
import SnakeIO.Client.Visual.GameScreen;

import static javafx.application.Application.launch;

public class Client {


    public static void main(String[] args) {
        Client client = new Client();
    }

    private Client() {
        LogicHub.getLogicHub();
        launch(GameScreen.class);
    }

}
