package com.example.web4.utils;

public enum Bounds {
    X_BOUNDS(-8, 8, true),
    Y_BOUNDS(-8, 8, true),
    R_BOUNDS(0, 5, true);

    private final double left;
    private final double right;
    private final boolean inclusive;

    Bounds(double left, double right, boolean inclusive) {
        this.left = left;
        this.right = right;
        this.inclusive = inclusive;
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }

    public boolean isInclusive() {
        return inclusive;
    }
}
