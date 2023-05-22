/**
 * Name: Haotian Yang
 * netId: hyang57
 * email: hyang57@u.rochester.edu
 * Project 3
 */

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class TTTGame extends JComponent
{
    protected ArrayList<Integer> Xcoordinate = new ArrayList<Integer>();
    //arraylist to store the coordinate that should draw "X"
    protected ArrayList<Integer> Ocoordinate = new ArrayList<Integer>();
    //arraylist to store the coordinate that should draw "O"
    protected int gameProcess = 1;
    //record the process of the game: 1 - normal; 0 - new game; 2 - get result
    protected int sequence = 0;
    //the sequence of drawing: 0 - "X"; 1 - "O"
    protected int player = 1;
    protected int p1Win = 0;
    //number of games that player 1 wins
    protected int p2Win = 0;
    //number of games that player 1 wins
    protected int draw = 0;
    //number of games that draw
    protected String state = "'s Turn";
    JFrame frame = new JFrame();
    JPanel statePanel = new JPanel();
    //to put new game button and labels
    board_panel boardPanel = new board_panel();
    //to draw the game
    JButton newGameButton = new JButton("New Game"); // new game button
    JLabel stateLabel = new JLabel("                    P" + player + state); // player/ status indication
    JLabel scoreLabel = new JLabel("<html>P1 Wins: " + p1Win + "<br>P2 Wins: " + p2Win +"<br>Draws: " + draw + "</html>"); // win/ lose/ draw indication

    public TTTGame()
    {
        frame.setTitle("TTTGame");
        frame.setLayout(new BorderLayout());
        frame.setSize(480, 580);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        statePanel.setLayout(new BorderLayout());
        statePanel.setBounds(0, 0, 480, 100);
        statePanel.setOpaque(true);
        boardPanel.setOpaque(true);
        newGameButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                gameProcess = 0;
                state = "'s Turn";
                stateLabel.setText("                    P" + player + state);
                boardPanel.repaint();
            }
            //override for pressing the new game button
            //repaint the game board
        });
        statePanel.add(newGameButton,BorderLayout.WEST);
        statePanel.add(stateLabel,BorderLayout.CENTER);
        statePanel.add(scoreLabel,BorderLayout.EAST);
        frame.add(statePanel, BorderLayout.NORTH);
        frame.add(boardPanel);
        frame.setVisible(true);
    }

    public void paint(Graphics g)
    {
        super.paint(g);
    }

    public class board_panel extends JPanel implements MouseListener
    {
        protected int boardPanelWidth = 0;
        protected int boardPanelHeight = 0;

        public void paint(Graphics g)
        {
            super.paint(g);
            boardPanelWidth = this.getWidth();
            boardPanelHeight = this.getHeight();
            //get the height and width of the game board
            g.drawLine(boardPanelWidth / 3, boardPanelHeight / 8, boardPanelWidth / 3, boardPanelHeight / 8 * 7);
            g.drawLine(boardPanelWidth / 3 * 2, boardPanelHeight / 8, boardPanelWidth / 3 * 2, boardPanelHeight / 8 * 7);
            g.drawLine(boardPanelWidth / 8, boardPanelHeight / 3, boardPanelWidth / 8 * 7, boardPanelHeight / 3);
            g.drawLine(boardPanelWidth / 8, boardPanelHeight / 3 * 2, boardPanelWidth / 8 * 7, boardPanelHeight / 3 * 2);
            //draw the grid
            if(gameProcess != 0)
            {
                for (int index = 0; index < Xcoordinate.size(); index ++)
                {
                    int[] drawCoordinate = getCoordinate(Xcoordinate.get(index));
                    int x = drawCoordinate[0];
                    int y = drawCoordinate[1];
                    g.drawLine(x-10, y-10,x+10,y+10);
                    g.drawLine(x+10,y-10,x-10,y+10);
                }
                //draw "X"
                for (int index = 0; index < Ocoordinate.size(); index ++) {
                    int[] drawCoordinate = getCoordinate(Ocoordinate.get(index));
                    int x = drawCoordinate[0];
                    int y = drawCoordinate[1];
                    g.drawOval(x - 10,y - 10,20,20);
                }
                //draw "O"
            }
            else
            {
                Xcoordinate.clear();
                Ocoordinate.clear();
            }
            //clear the coordinate because of the new game
        }

        public board_panel()
        {
            super();
            setLayout(new GridLayout(3,3));
            setBackground(Color.WHITE);
            addMouseListener(this);
        }
        public void mouseClicked(MouseEvent mouse)
        {
            {
                int x = mouse.getX();
                int y = mouse.getY();
                if (gameProcess != 2)
                {
                    int checkWinTempPlayer = -1;
                    state = "'s Turn";
                    if (x >= boardPanelWidth / 8  & x <= boardPanelWidth / 8 * 7 & y >= boardPanelHeight / 8 & y <= boardPanelHeight / 8 * 7)
                    {
                        if (x != boardPanelWidth / 3 & x != boardPanelWidth / 3 * 2 & y != boardPanelHeight / 3 & y != boardPanelHeight / 3 * 2)
                        {
                            if (Xcoordinate.contains(getPosition(x, y)) == false & Ocoordinate.contains(getPosition(x, y)) == false)
                            {
                                if (sequence == 0)
                                {
                                    Xcoordinate.add(getPosition(x, y));
                                    sequence = 1;
                                    player = 2;
                                }
                                else if (sequence == 1)
                                {
                                    Ocoordinate.add(getPosition(x, y));
                                    sequence = 0;
                                    player = 1;
                                }
                                gameProcess = 1;
                                boardPanel.repaint();
                            }
                        }
                    }
                    //use the coordinate of the clicked position to process the drawing
                    if (checkWinner(Xcoordinate, Ocoordinate) != 0)
                    {
                        gameProcess = 2;
                        switch (checkWinner(Xcoordinate, Ocoordinate))
                        {
                            case 1:
                                p1Win += 1;
                                break;

                            case 2:
                                p2Win += 1;
                                break;
                        }
                        state = " Wins!";
                        checkWinTempPlayer = player;
                        player = checkWinner(Xcoordinate, Ocoordinate);
                    }
                    //someone wins the game
                    stateLabel.setText("                    P" + player + state);
                    if (Xcoordinate.size() + Ocoordinate.size() == 9)
                    {
                        if (checkWinner(Xcoordinate, Ocoordinate) == 0)
                        {
                            gameProcess = 2;
                            draw += 1;
                            stateLabel.setText("                    Draw!");
                        }
                    }
                    //draw
                    scoreLabel.setText("<html>P1 Wins: " + p1Win + "<br>P2 Wins: " + p2Win + "<br>Draws: " + draw + "</html>");
                    if (checkWinTempPlayer != -1)
                    {
                        player = checkWinTempPlayer;
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Auto-generated method stub
        }
        @Override
        public void mouseReleased(MouseEvent e) {
            // TODO Auto-generated method stub
        }
        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub
        }
        @Override
        public void mouseExited(MouseEvent e)
        {
            // TODO Auto-generated method stub
        }
    }

    public int getPosition(int x, int y)
            //used to give the coordinate clicked a grid position.
            //grid position:
            //123
            //456
            //789
    {
        if (x < boardPanel.boardPanelWidth / 3)
        {
            if (y < boardPanel.boardPanelHeight / 3)
            {
                return 1;
            }
            else if (y > boardPanel.boardPanelHeight / 3 & y < boardPanel.boardPanelHeight / 3 * 2)
            {
                return 4;
            }
            else
            {
                return 7;
            }
        }
        else if (x > boardPanel.boardPanelWidth / 3 * 2)
        {
            if (y < boardPanel.boardPanelHeight / 3)
            {
                return 3;
            }
            else if (y > boardPanel.boardPanelHeight / 3 & y < boardPanel.boardPanelHeight / 3 * 2)
            {
                return 6;
            }
            else
            {
                return 9;
            }
        }
        else
        {
            if (y < boardPanel.boardPanelHeight / 3)
            {
                return 2;
            }
            else if (y > boardPanel.boardPanelHeight / 3 & y < boardPanel.boardPanelHeight / 3 * 2)
            {
                return 5;
            }
            else
            {
                return 8;
            }
        }
    }

    public int[] getCoordinate(int position)
            //use the grid position to get the approximated midpoint coordinate of each grid
    {
        int x = 0;
        int y = 0;
        int[] coordinate = new int[2];
        switch(position)
        {
            case 1:
                x = boardPanel.boardPanelWidth / 6;
                y = boardPanel.boardPanelHeight / 6;
                break;

            case 2:
                x = boardPanel.boardPanelWidth / 2;
                y = boardPanel.boardPanelHeight / 6;
                break;

            case 3:
                x = boardPanel.boardPanelWidth / 6 * 5;
                y = boardPanel.boardPanelHeight / 6;
                break;

            case 4:
                x = boardPanel.boardPanelWidth / 6;
                y = boardPanel.boardPanelHeight / 2;
                break;

            case 5:
                x = boardPanel.boardPanelWidth / 2;
                y = boardPanel.boardPanelHeight / 2;
                break;

            case 6:
                x = boardPanel.boardPanelWidth / 6 * 5;
                y = boardPanel.boardPanelHeight / 2;
                break;

            case 7:
                x = boardPanel.boardPanelWidth / 6;
                y = boardPanel.boardPanelHeight / 6 * 5;
                break;

            case 8:
                x = boardPanel.boardPanelWidth / 2;
                y = boardPanel.boardPanelHeight / 6 * 5;
                break;

            case 9:
                x = boardPanel.boardPanelWidth / 6 * 5;
                y = boardPanel.boardPanelHeight / 6 * 5;
                break;
        }
        coordinate[0] = x;
        coordinate[1] = y;
        return coordinate;
    }

    public int checkWinner(ArrayList<Integer> xList, ArrayList<Integer> oList)
            //use the grid position coordinate arraylist to check the winner
    {
        if(xList.contains(1) & xList.contains(4) & xList.contains(7))
        {
            return 1;
        }
        else if(xList.contains(2) & xList.contains(5) & xList.contains(8))
        {
            return 1;
        }
        else if(xList.contains(3) & xList.contains(6) & xList.contains(9))
        {
            return 1;
        }
        else if(xList.contains(1) & xList.contains(2) & xList.contains(3))
        {
            return 1;
        }
        else if(xList.contains(4) & xList.contains(5) & xList.contains(6))
        {
            return 1;
        }
        else if(xList.contains(7) & xList.contains(8) & xList.contains(9))
        {
            return 1;
        }
        else if(xList.contains(1) & xList.contains(5) & xList.contains(9))
        {
            return 1;
        }
        else if(xList.contains(3) & xList.contains(5) & xList.contains(7))
        {
            return 1;
        }
        if(oList.contains(1) & oList.contains(4) & oList.contains(7))
        {
            return 2;
        }
        else if(oList.contains(2) & oList.contains(5) & oList.contains(8))
        {
            return 2;
        }
        else if(oList.contains(3) & oList.contains(6) & oList.contains(9))
        {
            return 2;
        }
        else if(oList.contains(1) & oList.contains(2) & oList.contains(3))
        {
            return 2;
        }
        else if(oList.contains(4) & oList.contains(5) & oList.contains(6))
        {
            return 2;
        }
        else if(oList.contains(7) & oList.contains(8) & oList.contains(9))
        {
            return 2;
        }
        else if(oList.contains(1) & oList.contains(5) & oList.contains(9))
        {
            return 2;
        }
        else if(oList.contains(3) & oList.contains(5) & oList.contains(7))
        {
            return 2;
        }
        return 0;
    }

    public static void main(String[] args)
    {
        new TTTGame();
    }
}



