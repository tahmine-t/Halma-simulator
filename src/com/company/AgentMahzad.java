package com.company;

public class AgentMahzad extends Agent
{
    AgentMahzad(Board board)
    {
        super(board);
    }

    @Override
    protected int evaluate(Tile[][] currentBoard, byte currentColor)
    {
        short score = 0;
        int sum1 = 0,sum2 = 0;

        for (byte i = 0; i < currentBoard.length; i++) {
            for (byte j = 0; j < currentBoard.length; j++) {
                if (currentBoard[i][j].color == 2) {
                    sum1 += i + j;
                    if(currentBoard[i][j].zone == 1)
                        score += 1;

                }
                else if (currentBoard[i][j].color == 1) {
                    sum2 += i + j;
                    if(currentBoard[i][j].zone == 2)
                        score -= 1;
                }
            }
        }
        score += 120 - sum1;
        score += sum2 - 20;
        // print("player: " + playerTurn);
        return score;
    }
}
