package com.company;

public class Agent2 extends Agent
{
    Agent2(Board board)
    {
        super(board);
    }

    @Override
    protected int evaluateRed(Tile[][] currentBoard)
    {
        short score = 0;

        for (byte i = 0; i < currentBoard.length; i++)
        {
            for (byte j = 0; j < currentBoard.length; j++)
            {
                if (currentBoard[i][j].color == RED)
                {
                    score += (7 - i);
                    score += (7 - j);
                } 
            }
        }

        return score;
    }

    @Override
    protected int evaluateBlue(Tile[][] currentBoard)
    {
        short score = 0;

        for (byte i = 0; i < currentBoard.length; i++)
        {
            for (byte j = 0; j < currentBoard.length; j++)
            {
                if (currentBoard[i][j].color == BLUE)
                {
                    score += i;
                    score += j;
                } 
            }
        }

        return score;
    }
}
