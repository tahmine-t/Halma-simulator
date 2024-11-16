package com.company;

import java.util.Hashtable;
import java.util.List;

// this agent counts the max forward jump for each token + closeness to enemy base
public class Agent5 extends Agent
{
    private final int JUMP_WEIGHT = 1;
    private final int POSITION_WEIGHT = 2;

    Agent5(Board board)
    {
        super(board);

        this.CUTOFF = 2;
    }

    @Override
    protected int evaluateRed(Tile[][] currentBoard)
    {
        int eval1 = JUMP_WEIGHT * evalJumps(currentBoard, RED);
        int eval2 = POSITION_WEIGHT * evalRedPosition(currentBoard);
        int eval = eval1 + eval2;
        // int eval = JUMP_WEIGHT * evalRedJumps(currentBoard) +
        //     POSITION_WEIGHT * evalRedPosition(currentBoard);

        return eval;
    }

    @Override
    protected int evaluateBlue(Tile[][] currentBoard)
    {
        int eval = JUMP_WEIGHT * evalJumps(currentBoard, BLUE) +
            POSITION_WEIGHT * evalBluePosition(currentBoard);

        return eval;
    }

    protected int evalJumps(Tile[][] currentBoard, int color)
    {
        int eval = 0;

        List<Move> redPossibleMoves = createPossibleMoves(currentBoard, RED);
        Hashtable<String, Integer> redTokens = new Hashtable<>();

        for(Move move: redPossibleMoves)
        {
            if(isForwardMove(move, RED))
            {
                String key = "" + move.startPos.x + "," + move.startPos.y;
                int moveValue = getMoveValue(move, RED);
                Integer maxValue = redTokens.get(key);

                if(maxValue != null)
                {
                    if(moveValue > maxValue)
                    {
                        maxValue = moveValue;
                        redTokens.put(key, maxValue);
                    }
                }

                else
                {
                    redTokens.put(key, moveValue);
                }
            }
        }

        List<Move> bluePossibleMoves = createPossibleMoves(currentBoard, BLUE);
        Hashtable<String, Integer> blueTokens = new Hashtable<>(); 

        for(Move move: bluePossibleMoves)
        {
            if(isForwardMove(move, BLUE))
            {
                String key = "" + move.startPos.x + "," + move.startPos.y;
                int moveValue = getMoveValue(move, BLUE);
                Integer maxValue = redTokens.get(key);
                
                if(maxValue != null)
                {
                    if(moveValue > maxValue)
                    {
                        maxValue = moveValue;
                        blueTokens.put(key, maxValue);
                    }
                }

                else
                {
                    blueTokens.put(key, moveValue);
                }
            }
        }

        if(color == RED)
        {            
            for(Integer maxJump: redTokens.values())
            {
                eval += maxJump;
            }

            for(Integer maxJump: blueTokens.values())
            {
                eval -= maxJump;
            }    
        }

        else if(color == BLUE)
        {
            for(Integer maxJump: blueTokens.values())
            {
                eval += maxJump;
            }    

            for(Integer maxJump: redTokens.values())
            {
                eval -= maxJump;
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

    private int getMoveValue(Move move, int color)
    {
        int xDiff = move.finalPos.x - move.startPos.x;
        int yDiff = move.finalPos.y - move.startPos.y;

        if(color == RED)
        {
            xDiff *= -1;
            yDiff *= -1;
        }

        int value = xDiff + yDiff;

        return value;
    }
}
