/**
 * Richard Liu
 * 4/9/18
 * Main.java
 * Operators game
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.imageio.ImageIO;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;



// Main class containing main method which is first ran
public class Main
{
    public static void main(String[] args)
    {
        Main main = new Main();
        main.run();
    }
    
    public Main() {}
    
    public void run()
    {
        // create a frame
        JFrame frame = new JFrame("Operators");
        
        // set size, close operation, location, and resizability
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLocation(400, 200);
        frame.setResizable(true);
        
        // create a new panel using the panel below
        MainPanel panel = new MainPanel();
        // add to frame
        frame.getContentPane().add(panel);
        
        // show the frame
        frame.setVisible(true);
    }
}



// MainPanel contains the CardLayout and all of the visible game panels
class MainPanel extends JPanel
{
    // card layout containing all of the panels
    private CardLayout cards;
    // what level the user selected
    protected int level;
    // the next level the user can do (also the largest level that is unlocked)
    protected int nextLevel;
    // game panel
    protected GamePanel gamePanel;
    // larger font for instructions and high score
    protected Font arial;
    // the user's current score
    protected int score;
    // levelsPanel reference for padlock refresh
    protected LevelsPanel levelsPanel;
    
    // initialize panel
    public MainPanel()
    {
        setBackground(Color.WHITE);
        
        // create and set CardLayout
        cards = new CardLayout();
        setLayout(cards);
        
        // initialize with defaults
        level = 1;
        nextLevel = 1;
        score = 0;
        gamePanel = new GamePanel(this);
        arial = new Font("Arial", Font.PLAIN, 20);
        levelsPanel = new LevelsPanel(this);
        
        // add 2 panels, home and instructions
        add(new HomePanel(this), "home");
        add(new InstructionPanel(this), "instructions");
        add(levelsPanel, "levels");
        add(new HighScorePanel(this), "highScore");
        add(new ResetPanel(this), "reset");
        add(gamePanel, "game");
    }
    
    
    // go to the a certain card, called by other classes within this class
    public void showCard(String name)
    {
        cards.show(this, name);
    }
}



// home screen, as welcome text and buttons going to InstructionPanel and LevelsPanel
class HomePanel extends JPanel
{
    // MainPanel reference for using the card layout
    private MainPanel mainP;
    
    public HomePanel(MainPanel mainPIn)
    {
        mainP = mainPIn;
        
        // use a flowlayout to position buttons and label in the center
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 100));
        
        // add center panel containing the buttons and title text
        add(new CenterPanel());
    }
    
    
    // contains the buttons and label and is positioned in the center of homepanel
    class CenterPanel extends JPanel implements ActionListener
    {
        public CenterPanel()
        {
            // use a grid layout with 10px of vertical spacing
            setLayout(new GridLayout(6, 1, 0, 10));
            
            // welcome label
            JLabel label = new JLabel("Welcome to Operators!");
            
            // new game button
            JButton newGame = new JButton("New Game");
            // use this class as an action listener
            newGame.addActionListener(this);
            
            // instructions button
            JButton instructions = new JButton("Instructions");
            // use this class as an action listener
            instructions.addActionListener(this);
            
            // high score button
            JButton highScore = new JButton("High score");
            // use this class as an action listener
            highScore.addActionListener(this);
            
            // reset score button
            JButton reset = new JButton("Reset");
            // use this class as an action listener
            reset.addActionListener(this);
            
            // quit button
            JButton quit = new JButton("Quit");
            // use this class as an action listener
            quit.addActionListener(this);
            
            
            // add label and buttons to FlowLayout
            add(label);
            add(newGame);
            add(instructions);
            add(highScore);
            add(reset);
            add(quit);
        }
        
        // button clicked
        public void actionPerformed(ActionEvent e)
        {
            // get the name of the button
            String cmd = e.getActionCommand();
            
            // go to levels for new game
            if (cmd.equals("New Game"))
                mainP.showCard("levels");
            // go to instructions on click
            else if (cmd.equals("Instructions"))
                mainP.showCard("instructions");
            // go to high score on click
            else if (cmd.equals("High score"))
                mainP.showCard("highScore");
            // go to reset confirmation on click
            else if (cmd.equals("Reset"))
                mainP.showCard("reset");
            // exit the program on click quit
            else if (cmd.equals("Quit"))
                System.exit(1);
        }
    }
}



// reset panel allows the user to confirm resetting the score
class ResetPanel extends JPanel implements ActionListener
{
    // MainPanel reference for using the card layout
    private MainPanel mainP;
    
    public ResetPanel(MainPanel mainPIn)
    {
        mainP = mainPIn;
        
        // create labels and confirmation buttons
        JLabel confirm = new JLabel("Are you sure you want to reset your score?", JLabel.CENTER);
        // force confirm label to take up a whole line
        confirm.setPreferredSize(new Dimension(1000, 40));
        
        JButton yes = new JButton("Yes");
        JButton no = new JButton("No");
        
        // use this class as an action listener
        yes.addActionListener(this);
        no.addActionListener(this);
        
        // add to flow layout
        add(confirm);
        add(yes);
        add(no);
    }
    
    // button clicked
    public void actionPerformed(ActionEvent e)
    {
        // get the text of the button
        String cmd = e.getActionCommand();
        
        // clear the score if user clicked "Yes"
        if (cmd.equals("Yes"))
            mainP.score = 0;
        
        // go back to home no matter what the user clicked
        mainP.showCard("home");
    }
}



// instructions panel teaches the user how to play the game
class InstructionPanel extends JPanel implements ActionListener
{
    // MainPanel reference for using the card layout
    private MainPanel mainP;
    
    public InstructionPanel(MainPanel mainPIn)
    {
        mainP = mainPIn;
        
        // use a border layout for the button in the north
        setLayout(new BorderLayout(20, 20));
        
        // text area containing instructions text
        JTextArea textArea = new JTextArea(
            "The order of operations are:\n" +
            "    1. parenthesis ()\n" +
            "    2. exponents (^)\n" +
            "    3. multiplication and division (*, /)\n" +
            "    4. addition and subtraction (+, -)\n\n" +
            "For levels 1-10, calculate the correct answer using the order of operations\n" +
            "For levels 11-20, drag the numbers and operations to create the desired number"
        , 20, 4);
        // use an larger arial font for the instructions
        textArea.setFont(mainP.arial);
        textArea.setEditable(false);
        
        // add back button, which uses this class as an action listener
        JButton back = new JButton("Back");
        back.addActionListener(this);
        
        // add to border layout
        add(back, BorderLayout.NORTH);
        add(textArea, BorderLayout.CENTER);
    }
    
    // button clicked
    public void actionPerformed(ActionEvent e)
    {
        // get the text of the button
        String cmd = e.getActionCommand();
        
        // go back to home if "back" is clicked
        if (cmd.equals("Back"))
            mainP.showCard("home");
    }
}



// allows the user to choose between different levels
class LevelsPanel extends JPanel implements ActionListener
{
    // MainPanel reference for using the card layout
    private MainPanel mainP;
    // gridpanel reference for refreshing padlock icons
    protected GridPanel gridPanel;
    
    public LevelsPanel(MainPanel mainPIn)
    {
        mainP = mainPIn;
        
        // border layout
        setLayout(new BorderLayout(20, 20));
        
        // add back button, which uses this class as an action listener
        JButton back = new JButton("Back");
        back.addActionListener(this);
        
        add(back, BorderLayout.NORTH);
        
        gridPanel = new GridPanel();
        // actual level selection
        add(gridPanel, BorderLayout.CENTER);
    }
    
    // back button clicked
    public void actionPerformed(ActionEvent e)
    {
        // button text
        String cmd = e.getActionCommand();
        
        // go back to home
        if (cmd.equals("Back"))
            mainP.showCard("home");
    }
    
    
    // contains the actual level selection (#1, #2, #3, etc)
    class GridPanel extends JPanel implements ActionListener
    {
        // level selection buttons
        private JButton[] buttons;
        
        public GridPanel ()
        {
            // 4 by 5 grid layout with plenty of space
            setLayout(new GridLayout(4, 5, 60, 60));
            
            // level buttons
            buttons = new JButton[20];
            
            // initialize each button
            for (int i = 0; i < buttons.length; i++)
            {
                // use padlock icon if locked
                if (i < mainP.nextLevel)
                    // create new button with text of i+1 (since arrays start at 0, but levels start at 1)
                    buttons[i] = new JButton((i+1) + "");
                else
                    // create new button with text of i+1 (since arrays start at 0, but levels start at 1)
                    buttons[i] = new JButton((i+1) + "", new PadlockIcon());
                
                // use this class as an action listener
                buttons[i].addActionListener(this);
                
                // add to gridlayout
                add(buttons[i]);
            }
        }
        
        // refresh button padlock icon according to which levels are unlocked
        public void refresh()
        {
            // for each button
            for (int i = 0; i < buttons.length; i++)
            {
                // remove padlock icon
                if (i < mainP.nextLevel)
                    buttons[i].setIcon(null);
            }
        }
    
        // level button clicked
        public void actionPerformed(ActionEvent e)
        {
            // level user clicked on
            int lvl = Integer.parseInt(e.getActionCommand());
            
            // set the level accordingly
            if (lvl <= mainP.nextLevel)
            {
                mainP.level = lvl;
                
                // show the game card
                mainP.showCard("game");
                
                // refresh the game card (since we set a new level)
                mainP.gamePanel.refreshAll();
            }
        }
    }
    
    
    // pad lock icon that is drawn on top of the button
    class PadlockIcon implements Icon
    {
        public PadlockIcon() {}
        
        // draw the icon
        public void paintIcon(Component c, Graphics g, int x, int y)
        {
            // black
            g.setColor(new Color(0, 0, 0, 120));
            // padlock body
            g.fillRect(x-15, y, 50, 30);
            // padlock arc
            g.fillArc(x-10, y-20, 40, 40, 0, 180);
            
            // padlock center white fill
            g.setColor(Color.WHITE);
            g.fillArc(x-5, y-15, 30, 30, 0, 180);
        }
        
        // not actually 1px by 1px, this is to center the padlock on the button
        public int getIconHeight()
        {
            return 1;
        }
        
        public int getIconWidth()
        {
            return 1;
        }
    }
}



// this panel shows the user the previous high scores by loading a file
class HighScorePanel extends JPanel implements ActionListener
{
    // MainPanel reference for using the card layout
    private MainPanel mainP;
    
    public HighScorePanel(MainPanel mainPIn)
    {
        mainP = mainPIn;
        
        // borderlayout for the butotn
        setLayout(new BorderLayout(20, 20));
        
        // load file with high score data
        File highScoreFile = new File("highScore.txt");
        Scanner highScoreScanner;
        // the text in the high score file
        String allText = "";
        
        try
        {
            // initialize the scanner with the high score file
            highScoreScanner = new Scanner(highScoreFile);
            
            // read all the lines into allText
            while(highScoreScanner.hasNext())
                allText += highScoreScanner.nextLine() + "\n";
        } catch (FileNotFoundException e)
        {
            // tell the user if error
            System.err.println("\n\nERROR: Cannot find/open highScore.txt file to read\n\n\n");
        }
        
        // make high score text area with allText
        JTextArea textArea = new JTextArea(allText, 20, 4);
        textArea.setFont(mainP.arial);
        // don't let the user edit!
        textArea.setEditable(false);
        
        // add back button, which uses this class as an action listener
        JButton back = new JButton("Back");
        back.addActionListener(this);
        
        // add to border layout
        add(back, BorderLayout.NORTH);
        add(textArea, BorderLayout.CENTER);
    }
    
    // button clicked
    public void actionPerformed(ActionEvent e)
    {
        // button text
        String cmd = e.getActionCommand();
        
        // go back to home
        if (cmd.equals("Back"))
            mainP.showCard("home");
    }
}



class GamePanel extends JPanel
{
    // card layout for CenterPanel
    private CardLayout gameCards;
    // center panel
    protected CenterPanel centerPanel;
    // menu panel reference for repaint
    protected MenuPanel menuPanel;
    // main panel for card layout
    private MainPanel mainP;
    // questions for all the levels
    private String[] questions;
    
    public GamePanel(MainPanel mainPIn)
    {
        mainP = mainPIn;
        
        // load the questions from a file
        File f = new File("questions.txt");
        // read text into array
        String allText = "";
        // try to get the scanner
        Scanner in;
        
        try
        {
            in = new Scanner(f);
            
            while(in.hasNext())
                allText += in.nextLine() + '\n';
            
            questions = allText.split("\n");
        } catch (FileNotFoundException e)
        {
            System.err.println("\n\nERROR: Cannot find/open questions.txt file to read\n\n\n");
        }
        
        // center panel is a card layout
        centerPanel = new CenterPanel(mainP);
        menuPanel = new MenuPanel();
        
        // border layout with 10px vertical gap
        setLayout(new BorderLayout(0, 10));
        
        // put the menu buttons in the north and centerPanel in the center
        add(menuPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
    
    // refresh menuPanel and centerPanel according to new level
    public void refreshAll()
    {
        centerPanel.refresh();
        menuPanel.progress.repaint();
        mainP.levelsPanel.gridPanel.refresh();
    }
    
    // has the back and pause buttons
    class MenuPanel extends JPanel implements ActionListener
    {
        // progress bar panel reference for repaint
        private ProgressBarPanel progress;
        
        public MenuPanel()
        {
            // align the menu buttons to the left
            setLayout(new FlowLayout(FlowLayout.LEFT));
            
            // declare and initialize buttons
            JButton back = new JButton("Back");
            JButton exit = new JButton("Exit");
            JButton pause = new JButton("Pause");
            
            // use this class as an action listener
            back.addActionListener(this);
            exit.addActionListener(this);
            pause.addActionListener(this);
            
            progress = new ProgressBarPanel();
            
            // add to flow layout
            add(back);
            add(exit);
            add(pause);
            add(progress);
        }
        
        public void actionPerformed(ActionEvent e)
        {
            // get button text
            String cmd = e.getActionCommand();
            
            // back goes back to levels screen
            if (cmd.equals("Back"))
                mainP.showCard("levels");
            // exit goes back to home screen
            else if (cmd.equals("Exit"))
                mainP.showCard("home");
            // pause shows pause screen
            else if (cmd.equals("Pause"))
                gameCards.show(centerPanel, "pause");
        }
        
        
        // progress bar, so user can see how much of the game they've completed
        class ProgressBarPanel extends JPanel
        {
            // constructor
            public ProgressBarPanel()
            {
                // white background for progress bar
                setBackground(Color.WHITE);
                // set the preferred size to prevent the default of 10px by 10px
                setPreferredSize(new Dimension(550, 20));
            }
            
            // draw the progress bar and current score
            public void paintComponent(Graphics g)
            {
                // paint the background
                super.paintComponent(g);

                // draw with black color
                g.setColor(Color.BLACK);
                
                System.out.println("current level: " + mainP.level);
                // draw rectangles corresponding to the level the user is on
                for (int i = 0; i < mainP.level; i++)
                {
                    g.fillRect(i*25, 0, 23, 25);
                }
                
                // draw the score
                g.drawString(mainP.score + "", 520, 15);
            }
        }
    }
    
    
    // card layout panel for PausePanel, EarlyLevelPanel, and LateLevelPanel
    class CenterPanel extends JPanel
    {
        // early and later level variables
        private EarlyLevelPanel early;
        private LateLevelPanel late;
        
        public CenterPanel(MainPanel mainP)
        {
            // cardlayout
            gameCards = new CardLayout();
            setLayout(gameCards);
            
            // initialize variables
            early = new EarlyLevelPanel(mainP);
            late = new LateLevelPanel();
            
            // add 3 panels into card layout
            add(early, "early");
            add(late, "late");                
            add(new PausePanel(), "pause");
            
            // refresh according to level
            refresh();
        }
        
        // show early levels or late levels depending on level number
        public void refresh()
        {
            // for lower levels, show the "early" card layout
            if (mainP.level < 11)
            {
                gameCards.show(this, "early");
                early.reset();
                early.repaint();
            }
            // for later levels, show the "late" card layout
            else
            {
                gameCards.show(this, "late");
                late.repaint();
            }
        }
    }
    
    
    // shown instead of one of the levelPanel's when pause is clicked
    class PausePanel extends JPanel implements ActionListener
    {
        public PausePanel()
        {
            // position unpause button in the middle
            setLayout(new FlowLayout(FlowLayout.CENTER, 300, 200));
            
            // unpause button declare and initialize
            JButton unpause = new JButton("Click to unpause");
            unpause.addActionListener(this);
            
            // add to flowlayout
            add(unpause);
        }
        
        public void actionPerformed(ActionEvent e)
        {
            // call refresh when button is clicked to show either early or late
            centerPanel.refresh();
        }
    }
    
    
    // game view for levels 1-10
    class EarlyLevelPanel extends JPanel implements KeyListener, ActionListener, MouseListener
    {
        // main panel reference
        private MainPanel mainP;
        // textfield where the user enters their answer
        private JTextField answerField;
        // useful fonts for drawing texts
        private Font bigArial;
        private Font bigSerif;
        // help the user find out why they're wrong
        private String errorMsg;
        // characters within the question string where the user did something wrong
        private int[] arrowPos;
        // allows the user to go the the next level once they got the correct answer
        private JButton nextBtn;
        // how many times the user attempted to solve the problem
        private int tries;
        // currect question the user is solving
        private String question;
        // simplified question the user will solve next 
        private String simpQuestion;
        // box to draw around the operations the user is currently solving
        private int boxPos;
        private int boxSize;
        // answer the user should enter into the text field
        private double answer;
        
        public EarlyLevelPanel(MainPanel mainPIn)
        {
            mainP = mainPIn;
            
            // fonts used for drawing texts
            bigArial = new Font(Font.MONOSPACED, Font.PLAIN, 40);
            bigSerif = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
            
            // null layout, position answerField manually
            setLayout(null);
            
            // position answer field and use a big font
            answerField = new JTextField();
            answerField.addKeyListener(this);
            answerField.setFont(bigArial);
            answerField.setBounds(600, 85, 80, 50);
            // don't show the answer field until the user clicks on an operation
            answerField.setVisible(false);
            
            // position next level button in the bottom of the screen
            nextBtn = new JButton("Next level");
            nextBtn.addActionListener(this);
            nextBtn.setBounds(350, 450, 100, 30);
            nextBtn.setVisible(false);
            
            // reset can initialize field variables errorMsg, arrowPos
            reset();
            
            // this class will have action, mouse, and key listeners
            addMouseListener(this);
            
            // add to flow layout
            add(answerField);
            add(nextBtn);
        }
        
        // draw the math question
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            
            g.setFont(bigArial);
            // draw the question and an equal sign
            g.drawString(question, 50, 120);
            
            g.setFont(bigSerif);
            // draw error message
            g.drawString(errorMsg, 50, 250);
            
            // only draw the "=" symbol if boxPos is positive
            if (boxPos >= 0)
                // draw "=" symbol before answerField
                g.drawString("=", 50+boxPos*24, 180);
            
            g.setColor(Color.RED);
            // draw arrows if needed
            for (int i = 0; i < arrowPos.length; i++)
            {
                // draw arrow in 24px intervals starting from 60px
                if (arrowPos[i] != -1)
                    g.fillRect(60 + arrowPos[i] * 24, 130, 5, 40);
            }
            
            // draw boxes around current operation the user is calculating
            if (boxPos >= 0 && boxSize > 0)
            {
                g.drawRect(48 + boxPos*24, 90, boxSize*24, 40);
            }
        }
        
        // reset error message and arrows
        public void reset()
        {
            errorMsg = "Click an operation to start solving!";
            // shouldn't need more than 4 arrows
            arrowPos = new int[]{-1, -1, -1, -1};
            
            // currect question the user is solving
            question = questions[mainP.level-1];
            simpQuestion = "";
            
            // hide answer field and next button until usable
            answerField.setVisible(false);
            nextBtn.setVisible(false);

            // reset variables to sensible defaults
            tries = 0;
            boxPos = -1;
            boxSize = -1;

            // 1 million should never be the correct answer
            answer = 1000000;
        }
        
        // user got question correct, calculate score and allow them to move onto next level
        public void correct()
        {
            // calculate score
            mainP.score += (10 - tries) * 100;
            
            // show next button
            nextBtn.setVisible(true);
        }
        
        // next level button clicked
        public void actionPerformed(ActionEvent e)
        {
            // unlock the next level if user has yet to unlock it
            mainP.nextLevel = Math.max(mainP.nextLevel, mainP.level + 1);
            
            // go the next level
            mainP.level++;
            
            // debug printout
            System.out.println("nextLevel: " + mainP.nextLevel + ", level: " + mainP.level);
            
            // repaint gamePanel
            refreshAll();
        }
        
        // user pressed a key within the input text field
        public void keyPressed(KeyEvent e)
        {
            int keyCode = e.getKeyCode();
            
            // if enter key is pressed
            if (keyCode == 10)
            {
                try
                {
                    // check the answer by parsing the integer from text field
                    checkAnswer(Double.parseDouble(answerField.getText()));
                    
                    // clear textfield
                    answerField.setText("");
                // might not be an integer, so catch appropriately
                } catch (NumberFormatException exception)
                {
                    // give user helpful message
                    errorMsg = "Please enter a number!";
                    repaint();
                }
                
            }
        }
        public void keyReleased(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {}
        
        // user clicked an operation
        public void mouseClicked(MouseEvent e)
        {
            int x = e.getX();
            int y = e.getY();
            // index character user clicked on within string
            int index = (x-50)/24;
            // character user clicked on
            char operation = ' ';
            // part of problem in between parenthesis
            String inside = "";
            
            // player clicked on a character within the string
            if (y > 95 && y < 120 && index >= 0 && index < question.length())
            {
                operation = question.charAt(index);
                
                // valid operation
                if (operation == '+' || operation == '-' || operation == '*' || operation == '/')
                {
                    // do operation within parenthesis first
                    if (question.indexOf("(") < index && question.indexOf(")") > index)
                    {
                        // figure out correct order of operations
                        inside = question.substring(question.indexOf("("), question.indexOf(")"));
                        System.out.println(inside);
                        // shift index accordingly
                        index -= question.indexOf("(");
                        
                        
                        // do addition/subtraction only if no multiply/divide
                        if (inside.indexOf("*") == -1 && inside.indexOf("/") == -1)
                        {
                            if (inside.indexOf("-") == -1)
                            {
                                // add
                                // ensure user's operation is the correct operation, and draw the box and move the answer field
                                if (index == inside.indexOf("+")) solveAndDraw('+', index+question.indexOf("("));
                                // otherwise give helpful error message
                                else errorMsg = "Do addition first, from left to right";
                            } else if (inside.indexOf("+") == -1)
                            {
                                // subtract
                                if (index == inside.indexOf("-")) solveAndDraw('+', index+question.indexOf("("));
                                else errorMsg = "Do subraction first, from left to right";
                            } else if (inside.indexOf("+") < inside.indexOf("-"))
                            {
                                // add
                                if (index == inside.indexOf("+")) solveAndDraw('+', index+question.indexOf("("));
                                else errorMsg = "Do addition first, from left to right";
                            } else {
                                // subtract
                                if (index == inside.indexOf("-")) solveAndDraw('+', index+question.indexOf("("));
                                else errorMsg = "Do subraction first, from left to right";
                            }
                        // do multiply/divide whichever comes first
                        } else if (inside.indexOf("/") == -1)
                        {
                            // multiply
                            System.out.println("index: " + index);
                            System.out.println("indexof: " + inside.indexOf("*")); 
                            if (index == inside.indexOf("*")) solveAndDraw('*', index+question.indexOf("("));
                            else errorMsg = "Do multiplication first, from left to right";
                        } else if (inside.indexOf("*") == -1)
                        {
                            // divide
                            if (index == inside.indexOf("/")) solveAndDraw('/', index+question.indexOf("("));
                            else errorMsg = "Do division first, from left to right";
                        } else if (inside.indexOf("*") < inside.indexOf("/"))
                        {
                            // multiply
                            if (index == inside.indexOf("*")) solveAndDraw('*', index+question.indexOf("("));
                            else errorMsg = "Do multiplication first, from left to right";
                        } else
                        {
                            // divide
                            if (index == inside.indexOf("/")) solveAndDraw('/', index+question.indexOf("("));
                            else errorMsg = "Do division first, from left to right";
                        }
                    // operation user selecetd is not in parenthesis
                    } else if (question.indexOf("(") >= 0 && question.indexOf(")") >= 0)
                    {
                        errorMsg = "Do operations within first parenthesis first!";
                    // not within parenthesis
                    } else
                    {
                        // do in the correct order, similar to above, to be implemented later
                    }
                    
                    repaint();
                }
            }
        }
        // unused MouseListener methods
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        
        // checks if the user is correct and provides feedback
        public void checkAnswer(double input)
        {
            tries++;
            
            // check if the user is correct
            if (input == answer)
            {
                errorMsg = "Correct!";
                // reset arrows
                arrowPos = new int[]{-1, -1, -1, -1};

                // replace question with simplified question
                question = simpQuestion;
                System.out.println(simpQuestion);
                simpQuestion = "";

                boxPos = -1;
                boxSize = -1;
                
                if (tries == 2) correct();
                repaint();
                // return to prevent later code from running
                return;
            } else
            {

            }
                
            
            // no idea what the error is
            errorMsg = "Incorrect! Remember, the order of operation is PEMDAS";
            arrowPos = new int[]{-1, -1, -1, -1};
            repaint();
        }
        
        // solve a math problem using "js" ScriptEngine
        public double solve(String problem)
        {
            // create new solver engine
            ScriptEngine solver = new ScriptEngineManager().getEngineByName("js");
            // answer of problem, should never be 1 million, this is so that I know something is wrong
            double answer = 1000000;
            
            try
            {
                // try to solve the problem by passing it to the ScriptEngie via eval
                answer = (double)solver.eval(problem);
            } catch (ScriptException e)
            {
                // print out if a problem isn't formatte correctly
                System.err.println(problem + " is badly formatted!");
            }
            
            return answer;
        }
        
        // calculate the correct answer for this operator at index and draw onscreen
        public void solveAndDraw(char operator, int index)
        {
            // get the answer
            answer = solve(question.substring(index-2, index+3));

            // simplified question user will solve next
            simpQuestion = question.replace(question.substring(index-2, index+3)+"", (int)answer+"");
            
            // draw the box
            boxPos = index-2;
            boxSize = 5;
            
            // move the answerfield
            answerField.setBounds(30 + index*24, 150, 100, 50);
            answerField.setVisible(true);
            
            // debug output
            System.out.println("expected answer: " + answer);
            
            repaint();
        }
    }
    
    
    // game view for levels 11-20
    class LateLevelPanel extends JPanel
    {
        public LateLevelPanel()
        {
            // currently this panel only has a label, I will add game logic later
            add(new JLabel("LateLevelPanel"));
        }
    }
}
