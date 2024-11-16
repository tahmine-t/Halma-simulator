package com.company;

import java.util.List;

// this agent counts number of possible forward jumps + closeness to enemy base
public class Agent4 extends Agent
{
    private final int JUMP_WEIGHT = 1;
    private final int POSITION_WEIGHT = 1;

    Agent4(Board board)
    {
        super(board);

        this.CUTOFF = 2;
    }

    @Override
    protected int evaluateRed(Tile[][] currentBoard)
    {
        int eval1 = JUMP_WEIGHT * evalRedJumps(currentBoard);
        int eval2 = POSITION_WEIGHT * evalRedPosition(currentBoard);
        int eval = eval1 + eval2;
        // int eval = JUMP_WEIGHT * evalRedJumps(currentBoard) +
        //     POSITION_WEIGHT * evalRedPosition(currentBoard);

        return eval;
    }

    @Override
    protected int evaluateBlue(Tile[][] currentBoard)
    {
        int eval = JUMP_WEIGHT * evalBlueJumps(currentBoard) +
            POSITION_WEIGHT * evalBluePosition(currentBoard);

        return eval;
    }

    protected int evalRedJumps(Tile[][] currentBoard)
    {
        int eval = 0;

        List<Move> redPossibleMoves = createPossibleMoves(currentBoard, RED);

        for(Move move: redPossibleMoves)
        {
            if(!isAdjacent(move.startPos, move.finalPos) && isForwardMove(move, RED))
            {
                ++eval;
            }
        }

        List<Move> bluePossibleMoves = createPossibleMoves(currentBoard, BLUE);

        for(Move move: bluePossibleMoves)
        {
            if(!isAdjacent(move.startPos, move.finalPos) && isForwardMove(move, BLUE))
            {
                --eval;
            }
        }

        return eval;
    }

    protected int evalBlueJumps(Tile[][] currentBoard)
    {
        int eval = 0;

        List<Move> bluePossibleMoves = createPossibleMoves(currentBoard, BLUE);

        for(Move move: bluePossibleMoves)
        {
            if(!isAdjacent(move.startPos, move.finalPos) && isForwardMove(move, BLUE))
            {
                ++eval;
            }
        }

        List<Move> redPossibleMoves = createPossibleMoves(currentBoard, RED);

        for(Move move: redPossibleMoves)
        {
            if(!isAdjacent(move.startPos, move.finalPos) && isForwardMove(move, RED))
            {
                --eval;
            }
        }

        return eval;
    }

    protected int evalRedPosition(Tile[][] currentBoard)
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
                
                else if (currentBoard[i][j].color == BLUE) 
                {
                    score -= i;
                    score -= j;
                }
            }
        }

        return score;
    }

    protected int evalBluePosition(Tile[][] currentBoard)
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
                
                else if (currentBoard[i][j].color == RED) 
                {
                    score -= (7 - i);
                    score -= (7 - j);
                }
            }
        }

        return score;
    }

    private boolean isAdjacent(Tile pos1, Tile pos2)
    {
        boolean adjacent = false;

        int xDiff = Math.abs(pos1.x - pos2.x);
        int yDiff = Math.abs(pos1.y - pos2.y);

        if(xDiff <= 1 && yDiff <= 1)
        {
            // false if pos1 and pos2 are the same tile
            if((xDiff == 0) && (yDiff == 0))
            {
                adjacent = false;
            }

            adjacent = true;
        }

        return adjacent;
    }

    private boolean isForwardMove(Move move, int color)
    {
        boolean isForward = false;

        int xDiff = move.finalPos.x - move.startPos.x;
        int yDiff = move.finalPos.y - move.startPos.y;

        // checking for (move to current tile)
        if(xDiff == 0 && yDiff == 0)
        {
            isForward = false;
        }

        else if(color == RED)
        {
            isForward = ((xDiff <= 0) && (yDiff <= 0));
        }

        else if(color == BLUE)
        {
            isForward = ((xDiff >= 0) && (yDiff >= 0));
        }

        return isForward;
    }
}
