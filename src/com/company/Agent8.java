package com.company;

import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

// this agent counts closeness the closest enemy empty tile
public class Agent8 extends Agent
{
    private final int JUMP_WEIGHT = 1;
    private final int POSITION_WEIGHT = 1;
    
    Agent8(Board board)
    {
        super(board);

        this.CUTOFF = 3;
    }
    
    @Override
    protected int evaluateRed(Tile[][] currentBoard)
    {
        int eval = POSITION_WEIGHT * evalPosition(currentBoard, RED) +
            JUMP_WEIGHT * evalJumps(currentBoard, RED);

        return eval;
    }

    @Override
    protected int evaluateBlue(Tile[][] currentBoard)
    {
        int eval = POSITION_WEIGHT * evalPosition(currentBoard, BLUE) +
            JUMP_WEIGHT * evalJumps(currentBoard, BLUE);

        return eval;
    }
    
    protected int evalPosition(Tile[][] currentBoard, final int color)
    {
        LinkedList<Tile> redTokensPos = new LinkedList<>();
        LinkedList<Tile> redEmptyZones = new LinkedList<>();
        LinkedList<Tile> blueTokensPos = new LinkedList<>();
        LinkedList<Tile> blueEmptyZones = new LinkedList<>();

        for(int i = 0; i < currentBoard.length; ++i)
        {
            for(int j = 0; j < currentBoard.length; ++j)
            {
                if(currentBoard[i][j].zone == RED && currentBoard[i][j].color != BLUE)
                {
                    redEmptyZones.add(currentBoard[i][j]);
                }
                else if(currentBoard[i][j].zone == BLUE &&  currentBoard[i][j].color != RED)
                {
                    blueEmptyZones.add(currentBoard[i][j]);
                }

                if(currentBoard[i][j].zone != BLUE &&  currentBoard[i][j].color == RED)
                {
                    redTokensPos.add(currentBoard[i][j]);
                }
                else if(currentBoard[i][j].zone != RED && currentBoard[i][j].color == BLUE)
                {
                    blueTokensPos.add(currentBoard[i][j]);
                }
            }
        }

        redTokensPos.sort(new TileComparator());
        redEmptyZones.sort(new TileComparator());
        blueTokensPos.sort(new TileComparator());
        blueEmptyZones.sort(new TileComparator());

        // eval for red tokens
        int redEval = 0;
        int emptyBlueZones = redTokensPos.size();
        for(int i = 1; i <= emptyBlueZones; ++i)
        {
            Tile redFirst = redTokensPos.pollFirst();
            Tile blueLast = blueEmptyZones.pollLast();
            redEval += Math.abs(redFirst.x - blueLast.x);
            redEval += Math.abs(redFirst.y - blueLast.y);
        }

        redEval = 100 - redEval;

        // eval for blue tokens
        int blueEval = 0;
        int emptyRedZones = blueTokensPos.size();
        for(int i = 1; i <= emptyRedZones; ++i)
        {
            Tile redFirst = redEmptyZones.pollFirst();
            Tile blueLast = blueTokensPos.pollLast();
            blueEval += Math.abs(redFirst.x - blueLast.x);
            blueEval += Math.abs(redFirst.y - blueLast.y);
        }
        
        blueEval = 100 - blueEval;

        // total eval
        int eval = 0;

        if(color == RED)
        {
            eval = redEval - blueEval;
        }

        else if(color == BLUE)
        {
            eval = blueEval - blueEval;
        }

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

class TileComparator implements Comparator<Tile>
{
    @Override
    public int compare(Tile o1, Tile o2) 
    {
        int val1 = o1.x + o1.y;
        int val2 = o1.x + o1.y;

        if(val1 > val2)
        {
            return 1;
        }

        if(val2 < val1)
        {
            return -1;
        }

        else
        {
            return 0;
        }
    }
    
    
}