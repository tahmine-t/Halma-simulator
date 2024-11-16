package com.company;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class Halma {

    private Board board;
    private Tile[][] tiles;

    private final static byte maxDepth = 3;
    private byte playerTurn;
    private short totalMoves = 0;
    private int blueWins;
    private int redWins;
    private Agent blueAgent;
    private Agent redAgent;
    private byte firstX, firstY, secondX, secondY;

    GUI gameUI = new GUI();

    public Halma() 
    {}

    private void assignCoordinates() {
        for (byte i = 0; i < 8; i++) {
            for (byte j = 0; j < 8; j++) {
                tiles[i][j] = new Tile(i, j);

                if ((i + j) <= 3) {
                    tiles[i][j].color = 1;
                    tiles[i][j].zone = 1;
                } else if ((i + j) >= 11) {
                    tiles[i][j].color = 2;
                    tiles[i][j].zone = 2;
                }
            }
        }
    }

    private void startGame() 
    {
        if(checkWinner())
        {
            return;
        }

        if (playerTurn == 1)
        {
            var move = this.blueAgent.doMinMax(tiles, playerTurn);

            if(move != null)
            {
                  movePiece(move);
            }
            else 
                doRandomAction(playerTurn);
        }

        else 
        {
            var move = this.redAgent.doMinMax(tiles, playerTurn);
            ++totalMoves;

            if(move != null)
            {
                movePiece(move);
            }
            else
                doRandomAction(playerTurn);
        }

        startGame();
    }

    private boolean checkWinner()
    {
        int status = this.redAgent.checkTerminal(this.tiles);

        if(status == Agent.BLUE || status == Agent.RED)
        {
            gameUI.printText("\n Game has ended! \n");
            System.out.println("number of moves: " + this.totalMoves);
            
            if(status == Agent.BLUE)
            {
                System.out.println("BLUE wins");
                ++this.blueWins;
            }

            else if(status == Agent.RED)
            {
                System.out.println("RED wins");
                ++this.redWins;
            }

            return true;
        }
        
        return false;

        // if (status == Agent.BLUE)
        // {
            
        //     return status;
        //     // try {
        //     //     TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        //     // } catch (Exception ignored) {}
        // }
    }

    private void doRandomAction(int playerTurn) {

        var possibleMoves = this.redAgent.createPossibleMoves(tiles, playerTurn);
        var random = new Random().nextInt(possibleMoves.size() - 1);
        firstX = possibleMoves.get(random).startPos.x;
        firstY = possibleMoves.get(random).startPos.y;
        secondX = possibleMoves.get(random).finalPos.x;
        secondY = possibleMoves.get(random).finalPos.y;
        movePiece(possibleMoves.get(random));
    }



    public void changeTurn(short player) {
        gameUI.printText("Player %d has ended their turn.\n", player, player);
        playerTurn = (byte) (3 - player);
    }

    public void movePiece(Move move) {
        firstX = move.startPos.x;
        firstY = move.startPos.y;
        secondX = move.finalPos.x;
        secondY = move.finalPos.y;
        tiles[secondX][secondY].color = tiles[firstX][firstY].color;
        tiles[firstX][firstY].color = 0;
        changeTurn(playerTurn);
        gameUI.updateGUI(tiles);
    }



    public void runGame(int n, String blueAgent, String redAgent)
    {
        int sum = 0;
        this.blueWins = 0;
        this.redWins = 0;

        for(int i = 1; i <= n; ++i)
        {
            this.tiles = new Tile[8][8];
            this.playerTurn = 1;
            this.totalMoves = 0;
            assignCoordinates();
            board = new Board();

            this.blueAgent = getAgent(blueAgent, board);
            this.redAgent = getAgent(redAgent, board);

            GUI jk = new GUI();
            jk.createBoard();
            jk.createTextBoxArea();
            gameUI = jk;
            setUpGame();
            createLayout(jk);

            sum += this.totalMoves;
        }

        print("--------------------");
        print("Avg moves to win: " + sum / n);
        print("Blue wins: " + this.blueWins);
        print("red wins: " + this.redWins);
    }

    private void createLayout(GUI jk) {
        jk.setTitle("Halma");
        jk.setVisible(true);
        jk.pack();
        jk.setSize(648, 800);
        jk.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // closes frame
        jk.setLocationRelativeTo(null);
        jk.setVisible(true); // makes HalmaBoard visible
        startGame();
    }

    private Agent getAgent(String name, Board board)
    {
        Agent agent = null;

        switch(name)
        {
            case "agent":
                agent = new Agent(board);
                break;
            case "agent2":
                agent = new Agent2(board);
                break;
            case "agent3":
                agent = new Agent3(board);
                break;
            case "agent4":
                agent = new Agent4(board);
                break;
            case "agent5":
                agent = new Agent5(board);
                break;
            case "agent6":
                agent = new Agent6(board);
                break;
            case "agent7":
                agent = new Agent7(board);
                break;
            case "agent8":
                agent = new Agent8(board);
                break;
            case "mahzad":
                agent = new AgentMahzad(board);
                break;
            case "random":
                agent = new RandomAgent(board);
                break;
            default:
                print("Program shouldn't enter default case!");
        }

        return agent;
}


    public void setUpGame() {

        gameUI.setCampColors();
        gameUI.addMarbles();
        gameUI.addFrame();
    }

    public static void print(String str)
    {
        System.out.println(str);
    }
}
