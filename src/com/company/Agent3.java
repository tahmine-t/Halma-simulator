package com.company;

import java.util.LinkedList;
import java.util.List;

// this agent counts number of possible forward jumps
public class Agent3 extends Agent
{
    Move prevMove;

    Agent3(Board board)
    {
        super(board);

        byte x = -1;
        byte y = -1;
        this.prevMove = new Move(new Tile(x, y), new Tile(x, y));
    }

    @Override
    public Move doMinMax(Tile[][] tiles, byte playerTurn)
    {
        this.playerTurn = playerTurn;
        Pair temp = max(tiles, playerTurn, (byte)(0), Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.prevMove = temp.move;

        return temp.move;
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

    @Override
    protected int evaluateRed(Tile[][] currentBoard)
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

    @Override
    protected int evaluateBlue(Tile[][] currentBoard)
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
