package org.ecs160.a2;

public enum TaskSize {
    NONE, S, M, L, XL;

    public static TaskSize parse(String sizeString) {
        switch (sizeString) {
            case "S":
                return TaskSize.S;
            case "M":
                return TaskSize.M;
            case "L":
                return TaskSize.L;
            case "XL":
                return TaskSize.XL;
            default:
                throw new IllegalArgumentException("Not a valid task size");
        }
    }
}
