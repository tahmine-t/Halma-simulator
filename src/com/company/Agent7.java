package com.company;

import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

// this agent counts closeness the closest enemy empty tile
public class Agent7 extends Agent
{
    private final int JUMP_WEIGHT = 1;
    private final int POSITION_WEIGHT = 2;
    Move prevMove;

    Agent7(Board board)
    {
        super(board);

        this.CUTOFF = 2;
        byte x = -1;
        byte y = -1;
        this.prevMove = new Move(new Tile(x, y), new Tile(x, y));
    }

    @Override
    public Move doMinMax(Tile[][] tiles, byte playerTurn)
    {        
        this.playerTurn = playerTurn;

        evaluate(tiles, playerTurn);
        Pair temp = max(tiles, playerTurn, (byte)(0), Integer.MIN_VALUE, Integer.MAX_VALUE);

        this.prevMove = temp.move;

        return temp.move;
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

    @Override
    public List<Move> createPossibleMoves(Tile[][] newBoard, int currentColor) {
        List<Move> possibleMoves = new LinkedList<>();
        for (byte i = 0; i < 8; i++)
            for (byte j = 0; j < 8; j++)
                if (newBoard[i][j].color == currentColor) {
                    List<Tile> legalTiles = new LinkedList<>();
                    board.findPossibleMoves(newBoard, newBoard[i][j], legalTiles, newBoard[i][j], true);
                    for (Tile tile : legalTiles)
                    {
                        Move newMove = new Move(newBoard[i][j], tile);

                        // check not to return to previous position and fall into a loop
                        if(!((newMove.finalPos.x == this.prevMove.startPos.x) &&
                            (newMove.finalPos.y == this.prevMove.startPos.y)))
                        {
                            possibleMoves.add(newMove);
                        }

                        else
                        {
                            int dummy;
                        }
                    }
                }
        return possibleMoves;
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