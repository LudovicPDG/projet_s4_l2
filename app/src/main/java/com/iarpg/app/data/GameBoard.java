package com.iarpg.app.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBoard {
//    private final List<List<Room>> grid;
//    private final int height;
//    private final int width;
//    private Position currentPosition;
//    private final Position startPosition;
//    private final Position exitPosition;
//
//    public GameBoard(int height, int width) {
//        this.height = height;
//        this.width = width;
//
//        startPosition = generateRandomStartPosition();
//        exitPosition = generateRandomExitPosition();
//        currentPosition = new Position(startPosition.getX(), startPosition.getY());
//
//        grid = this.generateGrid();
//    }
//
//    public Position getStartPosition() {
//        return startPosition;
//    }
//
//    public Position getExitPosition() {
//        return exitPosition;
//    }
//
//    public Position getCurrentPosition() {
//        return currentPosition;
//    }
//
//    private List<List<Room>> generateGrid() {
//        List<List<Room>> grid = new ArrayList<>();
//
//        for (int y = 0; y < height; y++) {
//            grid.add(new ArrayList<>());
//        }
//
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                if (new Position(x, y).equals(exitPosition)) {
//                    grid.get(y).add(new Room("", "")); // todo : ajouter Room finale
//                }
//                else {
//                    grid.get(y).add(new Room("", "")); // todo : Ajouter description et scenario (générés par IA)
//                }
//
//            }
//        }
//        return grid;
//    }
//
//    public void movePlayer(String direction) {
//        switch (direction) {
//            case "left":
//                currentPosition.setX(currentPosition.getX() - 1);
//                break;
//
//            case "right":
//                currentPosition.setX(currentPosition.getX() + 1);
//                break;
//
//            case "up":
//                currentPosition.setY(currentPosition.getY() - 1);
//                break;
//
//            case "down":
//                currentPosition.setY(currentPosition.getY() + 1);
//                break;
//        }
//    }
//
//    Position generateRandomExitPosition() {
//        int i = (int) ((Math.random() * (width - 1)));
//
//        return new Position(i, 0);
//    }
//
//    private Position generateRandomStartPosition() {
//        int i = (int) ((Math.random() * (width - 1)));
//
//        return new Position(i, height - 1);
//    }
//
//    public List<List<Room>> getGrid() {
//        return grid;
//    }
}
