package SnakeIO.Server.GameLogic;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Snake {
    private ArrayList<Point2D> positions;

    public Snake(Point2D startingPosition) {
        this.positions = new ArrayList<>();
        this.positions.add(startingPosition);
    }




}
