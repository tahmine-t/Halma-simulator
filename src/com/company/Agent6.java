package com.company;

import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

// this agent counts closeness the closest enemy empty tile
public class Agent6 extends Agent
{
    private final int JUMP_WEIGHT = 1;
    private final int POSITION_WEIGHT = 1;
    
    Agent6(Board board)
    {
        super(board);

        this.CUTOFF = 2;
    }
    
    @Override
    protected int evaluateRed(Tile[][] currentBoard)
    {
        int eval = POSITION_WEIGHT * evalPosition(currentBoard, RED);

        return eval;
    }

    @Override
    protected int evaluateBlue(Tile[][] currentBoard)
    {
        int eval = POSITION_WEIGHT * evalPosition(currentBoard, BLUE);

        return eval;
    }

    protected int evalPosition(Tile[][] currentBoard, final int color)
    {
        LinkedList<Integer> redTokensPos = new LinkedList<>();
        LinkedList<Integer> redEmptyZones = new LinkedList<>();
        LinkedList<Integer> blueTokensPos = new LinkedList<>();
        LinkedList<Integer> blueEmptyZones = new LinkedList<>();

        for(int i = 0; i < currentBoard.length; ++i)
        {
            for(int j = 0; j < currentBoard.length; ++j)
            {
                if(currentBoard[i][j].zone == RED && currentBoard[i][j].color != BLUE)
                {
                    redEmptyZones.add(i + j);
                }
                else if(currentBoard[i][j].zone == BLUE &&  currentBoard[i][j].color != RED)
                {
                    blueEmptyZones.add(i + j);
                }

                if(currentBoard[i][j].zone != BLUE &&  currentBoard[i][j].color == RED)
                {
                    redTokensPos.add(i + j);
                }
                else if(currentBoard[i][j].zone != RED && currentBoard[i][j].color == BLUE)
                {
                    blueTokensPos.add(i + j);
                }
            }
        }

        Collections.sort(redTokensPos);
        Collections.sort(blueTokensPos);
        Collections.sort(redEmptyZones);
        Collections.sort(blueEmptyZones);

        // eval for red tokens
        int redEval = 0;
        int emptyBlueZones = redTokensPos.size();
        for(int i = 1; i <= emptyBlueZones; ++i)
        {
            int redFirst = redTokensPos.pollFirst();
            int blueLast = blueEmptyZones.pollLast();
            redEval += (redFirst - blueLast);
        }

        redEval = 100 - redEval;

        // eval for blue tokens
        int blueEval = 0;
        int emptyRedZones = blueTokensPos.size();
        for(int i = 1; i <= emptyRedZones; ++i)
        {
            int redFirst = redEmptyZones.pollFirst();
            int blueLast = blueTokensPos.pollLast();
            blueEval += (redFirst - blueLast);
        }
        
        blueEval = 100 - blueEval;

        // total eval
        int eval = 0;

        if(color == RED)
        {
            if(emptyBlueZones <= 2)
            {
                eval = redEval;
            }

            else
            {
                eval = redEval - blueEval;
            }
        }

        else if(color == BLUE)
        {
            if(emptyRedZones <= 2)
            {
                eval = blueEval;
            }

            else
            {
                eval = blueEval - redEval;
            }
        }

        return eval;
    }
}