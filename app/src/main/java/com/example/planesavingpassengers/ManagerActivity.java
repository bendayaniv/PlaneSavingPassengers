package com.example.planesavingpassengers;

import java.util.ArrayList;
import java.util.Random;

public class ManagerActivity {

    private Plane plane;
    private Object[][] objectsBoard;
    private int numOfObjects;

    public ManagerActivity(int life, int yLength, int xLength, int defaultXForPlane, int defaultYForPlane) {
        plane = new Plane(life, defaultXForPlane, defaultYForPlane);
        objectsBoard = new Object[yLength][xLength];
        numOfObjects = 0;
    }

    public int getNumOfObjects() {
        return numOfObjects;
    }

    public Object[][] getBoard() {
        return objectsBoard;
    }

    public Plane getPlane() {
        return plane;
    }

    /**
     * Can not move directly from [10][0] to [10][2] and the opposite
     */
    public void movePlane(int planeMove) {
        int planeOnX = plane.getX();
        if (planeMove == -1) {
            if (plane.getX() != 0) {
                plane.setX(planeOnX - 1);
            }
        } else if (planeMove == 1) {
            if (plane.getX() != 2) {
                plane.setX(planeOnX + 1);
            }
        }
    }

    /**
     * Move all exists objects one step down
     *
     * @param boardLimit the limit to the objects on the board
     */
    public void moveObjectsDown(int boardLimit, int xScale) {
        for (int i = objectsBoard.length - 1; i >= 0; i--) {
            for (int j = 0; j < objectsBoard[i].length; j++) {
                if (objectsBoard[i][j] != null && objectsBoard[i][j].getY() == boardLimit) {
                    objectsBoard[i][j] = null;
                } else if (objectsBoard[i][j] != null) {
                    int tmpY = objectsBoard[i][j].getY() + 1;
                    objectsBoard[i][j].setY(tmpY);
                    objectsBoard[i + 1][j] = objectsBoard[i][j];
                    objectsBoard[i][j] = null;
                }
            }
        }
        checkTheAbilityToCreateNewBird(xScale);
    }

    public void clearAllObjects() {
        for (int i = objectsBoard.length - 1; i >= 0; i--) {
            for (int j = 0; j < objectsBoard[i].length; j++) {
                objectsBoard[i][j] = null;
            }
        }
        numOfObjects = 0;
    }

    /**
     * Check if we can create new bird this step
     * (so that the player can continue to play without being disqualified)
     *
     * @param xScale the limit on the x scale
     */
    public void checkTheAbilityToCreateNewBird(int xScale) {
        int theLimitedIndex = -1;
        if (numOfObjects > 0) {

            //Array list of all the x-scales of the potential problematic birds for creating a new one
            ArrayList<Integer> checkList = new ArrayList<>();

            //Our indication if there can be a problem to create a new bird in certain spot
            boolean checking = true;

            //Getting all the x-scales of the birds that currently on the board
            for (int i = xScale - 1; i > 0; i--) {
                for (int j = 0; j < objectsBoard[i].length; j++) {
                    if (objectsBoard[i][j] instanceof Bird) {
                        checkList.add(objectsBoard[i][j].getX());
                        break;
                    }
                }
            }

            //Checking if there is possibility to problem to create new bird
            if (checkList.size() == xScale - 1) {
                //Checking one option [secondary diagonal]
                if (checkList.get(0) == 0) {
                    for (int i = 1; i < checkList.size(); i++) {
                        if (checkList.get(i - 1) + 1 != checkList.get(i)) {
                            checking = false;
                            break;
                        }
                    }
                    if (checking == true) {
                        theLimitedIndex = xScale - 1;
                    }
                    createNewBird(theLimitedIndex, xScale);
                }
                // Checking second option[main diagonal]
                else if (checkList.get(0) == xScale - 2) {
                    for (int i = 1; i < checkList.size(); i++) {
                        if (checkList.get(i - 1) - 1 != checkList.get(i)) {
                            checking = false;
                            break;
                        }
                    }
                    if (checking == true) {
                        theLimitedIndex = 0;
                    }
                    createNewBird(theLimitedIndex, xScale);
                }
            }
        } else {
            createNewBird(theLimitedIndex, xScale);
        }
    }

    /**
     * Create new bird
     *
     * @param index == indicates if can be anywhere (-1) or can not be in specific place(!-1)
     */
    public void createNewBird(int index, int howManyOptions) {
        //The extra 1 is for the option to not create
        int randomX = new Random().nextInt(howManyOptions + 1);
        if (randomX >= 0 && randomX <= 2) {
            if (index != -1) {
                while (randomX == index) {
                    randomX = new Random().nextInt(3);
                }
            }
            objectsBoard[1][randomX] = new Bird(randomX, 1);
            numOfObjects++;
        }
    }

    /**
     * Check if there is a clash between a bird and the plane
     * If it does, it raise the number of crashes in 1
     * (if it lost all his life, it stop to raise the number of crashes)
     *
     * @return true if does, false if does not
     */
    public boolean checkIfCrash(int boardLimit) {
        for (int i = 0; i < objectsBoard[boardLimit].length; i++) {
            if (objectsBoard[boardLimit][i] != null && plane.getX() == i) {
                raiseCrashNumber();
                return true;
            }
        }
        return false;
    }

    private void raiseCrashNumber() {
        int crash = getPlane().getNumOfCrash();
        if (crash < getPlane().getLife()) {
            crash++;
            getPlane().setNumOfCrash(crash);
        }
    }
}
