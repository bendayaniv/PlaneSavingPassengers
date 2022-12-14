package com.example.planesavingpassengers.Models;

import com.example.planesavingpassengers.Models.Objects.Bird;
import com.example.planesavingpassengers.Models.Objects.Object;
import com.example.planesavingpassengers.Models.Objects.Passenger;
import com.example.planesavingpassengers.Models.Objects.Plane;

import java.util.Random;

public class GameManager {

    private Plane plane;
    private Object[][] objectsBoard;
    private boolean emptyBoard;

    public GameManager(int life, int yLength, int xLength, int defaultXForPlane, int defaultYForPlane) {
        plane = new Plane(life, defaultXForPlane, defaultYForPlane);
        objectsBoard = new Object[yLength][xLength];
        emptyBoard = true;
    }

    /**
     * @return true if the board is empty, false otherwise
     */
    public boolean isEmptyBoard() {
        return emptyBoard;
    }

    /**
     * @return the board of objects
     */
    public Object[][] getBoard() {
        return objectsBoard;
    }

    /**
     * @return the plane
     */
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
            if (plane.getX() != 4) {
                plane.setX(planeOnX + 1);
            }
        }
    }

    /**
     * Create new bird
     *
     * @param index          == indicates if can be anywhere (-1) or can not be in specific place(!-1)
     * @param howManyOptions == the limit on the x scale
     */
    public void createNewBird(int index, int howManyOptions) {
        //The extra 1 is for the option to not create
        int randomX = new Random().nextInt(howManyOptions);
        if (index != -1 || objectsBoard[1][randomX] != null) {
            while (objectsBoard[1].length == randomX || randomX == index || objectsBoard[1][randomX] != null) {
                randomX = new Random().nextInt(howManyOptions + 1);
            }
        }
        objectsBoard[1][randomX] = new Bird(randomX, 1);
        emptyBoard = false;
    }


    /**
     * Create new passenger
     *
     * @param howManyOptions == the limit on the x scale
     */
    private void createNewPassenger(int howManyOptions) {
        int randomX = new Random().nextInt(howManyOptions);
        while (objectsBoard[1][randomX] != null) {
            randomX = new Random().nextInt(howManyOptions);
        }
        objectsBoard[1][randomX] = new Passenger(randomX, 1);
        emptyBoard = false;
    }

    /**
     * Check if we can create new bird this step
     * (so that the player can continue to play without being disqualified)
     *
     * @param xScale the limit on the x scale
     */
    public void checkTheAbilityToCreateNewBird(int xScale) {
        // Default value
        int theLimitedIndex = -1;
        if (emptyBoard == false) {

            //Our indication if there can be a problem to create a new bird in certain spots
            //The default is true
            boolean canBeProblem = true;

            //Checking one option [main diagonal]
            for (int i = xScale - 1; i > 0; i--) {
                if (!(objectsBoard[i + 1][i] instanceof Bird)) {
                    canBeProblem = false;
                    break;
                }
            }
            if (canBeProblem == true) {
                theLimitedIndex = 0;
                createNewBird(theLimitedIndex, xScale);
            } else {
                canBeProblem = true;
                //Checking second option [secondary diagonal]
                for (int i = xScale - 1; i > 0; i--) {
                    if (!(objectsBoard[i + 1][objectsBoard[i].length - 1 - i] instanceof Bird)) {
                        canBeProblem = false;
                        break;
                    }
                }
                if (canBeProblem == true) {
                    theLimitedIndex = xScale - 1;
                    createNewBird(theLimitedIndex, xScale);
                }
            }
        } else {
            createNewBird(theLimitedIndex, xScale);
        }
    }

    /**
     * Create new objects on the board
     *
     * @param xScale == the limit to the objects on the board (x axis)
     */
    public void createObjects(int xScale) {
        int random = new Random().nextInt(10);
        int randomNumOfBirds = new Random().nextInt(4);
        if (random > 0 && randomNumOfBirds != 0) {
            // Create only passengers
            if (random < 3) {
                createNewPassenger(xScale);
            }
            // Create birds
            else {
                for (int i = 0; i < randomNumOfBirds; i++) {
                    checkTheAbilityToCreateNewBird(xScale);
                }
                // Create also passengers
                if (random >= 8) {
                    createNewPassenger(xScale);
                }
            }
        }
    }

    /**
     * Move all exists objects one step down
     *
     * @param boardLimit the limit to the objects on the board
     */
    public void moveObjectsDown(int boardLimit, int xScale) {
        if (emptyBoard == false) {
            for (int i = objectsBoard.length - 1; i >= 0; i--) {
                for (int j = 0; j < objectsBoard[i].length; j++) {
                    if (objectsBoard[i][j] != null && objectsBoard[i][j].getY() == boardLimit + 1) {
                        objectsBoard[i][j] = null;
                    } else if (objectsBoard[i][j] != null) {
                        int tmpY = objectsBoard[i][j].getY() + 1;
                        objectsBoard[i][j].setY(tmpY);
                        objectsBoard[i + 1][j] = objectsBoard[i][j];
                        objectsBoard[i][j] = null;
                    }
                }
            }
        }
        createObjects(xScale);
    }


    /**
     * Clear the board from all objects
     */
    public void clearAllObjects() {
        for (int i = objectsBoard.length - 1; i >= 0; i--) {
            for (int j = 0; j < objectsBoard[i].length; j++) {
                objectsBoard[i][j] = null;
            }
        }
        emptyBoard = true;
    }


    /**
     * Check if there is a clash between a bird and the plane
     * If it does, it raise the number of crashes in 1
     * (if it lost all his life, it stop to raise the number of crashes)
     *
     * @return true if does, false if does not
     */
    public boolean checkIfCrash(int planeLine) {
        for (int i = 0; i < objectsBoard[planeLine].length; i++) {
            if (objectsBoard[planeLine][i] instanceof Bird && plane.getX() == i) {
                raiseCrashNumber();
                return true;
            }
        }
        return false;
    }

    /**
     * Raise the number of crashes in 1
     */
    private void raiseCrashNumber() {
        int crash = getPlane().getNumOfCrash();
        if (crash < getPlane().getLife()) {
            crash++;
            getPlane().setNumOfCrash(crash);
        }
    }

    /**
     * Check if there is a clash between a passenger and the plane
     * If it does, it raise the score
     *
     * @return true if does, false if does not
     */
    public boolean checkIfSave(int planeLine) {
        for (int i = 0; i < objectsBoard[planeLine].length; i++) {
            if (objectsBoard[planeLine][i] instanceof Passenger && plane.getX() == i) {
                // Clear the good hit from the screen and raise the player score
                objectsBoard[planeLine][i] = null;
                getPlane().setScore();
                return true;
            }
        }
        return false;
    }
}
