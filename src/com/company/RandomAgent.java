package com.company;

import java.util.List;
import java.util.Random;

public class RandomAgent extends Agent
{
    RandomAgent(Board board)
    {
        super(board);
    }

    @Override
    public Move doMinMax(Tile[][] tiles, byte playerTurn)
    {
        // basically random agent doesn't use minmax but 
        // we override minmax to avoid further changing of the original model
        List<Move> possibleMoves = createPossibleMoves(tiles, playerTurn);
        int randIndex = new Random().nextInt(possibleMoves.size() - 1);
        Move randMove = possibleMoves.get(randIndex);

        return randMove;
    }
}
