package com.company;

public class Tile
{
    byte x;
    byte y;
    // 0 is empty 1 is blue and 2 is red
    byte color = 0;
    // 0 is none, 1 is blue zone and 2 is red zone

    byte zone = 0;
    public Tile(byte x,byte y){
        this.x = x;
        this.y = y;
    }
}
