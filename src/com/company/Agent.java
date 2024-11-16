package com.company;

import java.util.LinkedList;
import java.util.List;

public class Agent 
{
    final static int BLUE = 1, RED = 2, NEUTRAL = 0;
    int CUTOFF;
    protected Board board;
    protected byte playerTurn;
    public Agent(Board board)
    {
        this.board = board;
        this.CUTOFF = 2;
    }

    public Move doMinMax(Tile[][] tiles, byte playerTurn)
    {        
        this.playerTurn = playerTurn;

        int someEval = evaluate(tiles, playerTurn);
        Pair temp = max(tiles, playerTurn, (byte)(0), Integer.MIN_VALUE, Integer.MAX_VALUE);
        return temp.move;
    }

    protected Pair max(Tile[][] currentBoard, byte currentColor, byte depth, int alpha, int beta)
    {
        if (checkTerminal(currentBoard) != NEUTRAL)
            return new Pair(null, evaluate(currentBoard, this.playerTurn));

        if(depth > CUTOFF)
            return new Pair(null, evaluate(currentBoard, this.playerTurn));

        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);

        Pair maxMove =  new Pair(null, Integer.MIN_VALUE);
        Pair nextMove = null;

        for(Move move: possibleMoves)
        {
            if(beta < alpha)
            {
                break;
            }

            Tile[][] nextTile = board.doMove(move, currentBoard);

            nextMove = min(nextTile, (byte) (3 - currentColor), (byte)(depth + 1), alpha, beta);

            // if(nextMove.value > beta)
            // {
            //     return new Pair(null, beta);
            // }

            if(nextMove.value > maxMove.value)
            {
                maxMove.value = nextMove.value;
                maxMove.move = move;

                alpha = maxMove.value;
            }
        }

        return maxMove;
    }

    protected Pair min(Tile[][] currentBoard, byte currentColor, byte depth, int alpha, int beta) 
    {
        if (checkTerminal(currentBoard) != NEUTRAL)
            return new Pair(null, evaluate(currentBoard, this.playerTurn));

        if(depth > CUTOFF)
            return new Pair(null, evaluate(currentBoard, this.playerTurn));
            
        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);

        Pair minMove = new Pair(null, Integer.MAX_VALUE);

        for(Move move: possibleMoves)
        {
            if(beta < alpha)
            {
                break;
            }

            Tile[][] nextTile = board.doMove(move, currentBoard);

            Pair nextMove = max(nextTile, (byte) (3 - currentColor), (byte)(depth + 1), alpha, beta);

            // if(nextMove.value < alpha)
            // {
            //     return new Pair(null, alpha);
            // }

            if(nextMove.value < minMove.value)
            {
                minMove.value = nextMove.value;
                minMove.move = move;

                beta = minMove.value;
            }
        }

        return minMove;
    }

    protected int evaluate(Tile[][] currentBoard, byte currentColor)
    {
        int score = 0;

        if(currentColor == BLUE)
        {
            score = evaluateBlue(currentBoard);
        }

        else if(currentColor == RED)
        {
            score = evaluateRed(currentBoard);
        }

        return score;
    }

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
                
                else if (currentBoard[i][j].color == RED) 
                {
                    score -= (7 - i);
                    score -= (7 - j);
                }
            }
        }

        return score;
    }

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
                
                else if (currentBoard[i][j].color == BLUE) 
                {
                    score -= i;
                    score -= j;
                }
            }
        }

        return score;
    }

    public List<Move> createPossibleMoves(Tile[][] newBoard, int currentColor) {
        List<Move> possibleMoves = new LinkedList<>();
        for (byte i = 0; i < 8; i++)
            for (byte j = 0; j < 8; j++)
                if (newBoard[i][j].color == currentColor) {
                    List<Tile> legalTiles = new LinkedList<>();
                    board.findPossibleMoves(newBoard, newBoard[i][j], legalTiles, newBoard[i][j], true);
                    for (Tile tile : legalTiles)
                        possibleMoves.add(new Move(newBoard[i][j], tile));
                }
        return possibleMoves;
    }


    public int checkTerminal(Tile[][] currentTiles) 
    {
        byte redCounter = 0;
        byte blueCounter = 0;

        for (byte x = 0; x < 8; x++) {
            for (byte y = 0; y < 8; y++) {
                if (currentTiles[x][y].zone == 1) {
                    if (currentTiles[x][y].color == 2) {
                        redCounter++;
                        if (redCounter >= 10) {
                            return RED;
                        }
                    }
                } else if (currentTiles[x][y].zone == 2) {
                    if (currentTiles[x][y].color == 1) {
                        blueCounter++;
                        if (blueCounter >= 10) {
                            return BLUE;
                        }
                    }
                }
            }
        }
        
        return NEUTRAL;
    }

    public void print(String str)
    {
        System.out.println(str);
    }
}