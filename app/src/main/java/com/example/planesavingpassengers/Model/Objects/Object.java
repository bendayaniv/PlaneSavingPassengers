package com.example.planesavingpassengers.Model.Objects;

public class Object {
    private int x;
    private int y;
    private int image;

    protected Object(int x, int y, int image) {
        setX(x);
        setY(y);
        setImage(image);
    }

    public int getObjectImage() {
        return image;
    }

    private void setImage(int imageNum) {
        this.image = imageNum;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
