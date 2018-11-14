package com.aws.codestar.projecttemplates.handler;

public class Calculator {


    public int add(Pair pair) {
        return pair.getX() + pair.getY();
    }
}

class Pair {
    private int x;
    private int y;

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