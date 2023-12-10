package org.example;

public class Coordinate {
    public enum Direction {
        Up,
        Left,
        Down,
        Right
    }
    public int X;
    public int Y;
    public Direction direction;

    public Boolean equals(Coordinate other)
    {
        if ((other.X == this.X) && (other.Y == this.Y))
        {
            return true;
        }
        return false;
    }
}
