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
        System.out.println("\n\n\n");

        Main main = new Main();
        main.run();
    }

    public Main()
    {}

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

        // user starts on level 1
        level = 1;
        // only level 1 is unlocked
        // TODO: change this
        nextLevel = 11;
        // starts at 0 score
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
            "For levels 11-20, drag the numbers and operations to create the desired number", 20, 4);
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

        public GridPanel()
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
                    buttons[i] = new JButton((i + 1) + "");
                else
                    // create new button with text of i+1 (since arrays start at 0, but levels start at 1)
                    buttons[i] = new JButton((i + 1) + "", new PadlockIcon());

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
        public PadlockIcon()
        {}

        // draw the icon
        public void paintIcon(Component c, Graphics g, int x, int y)
        {
            // black
            g.setColor(new Color(0, 0, 0, 120));
            // padlock body
            g.fillRect(x - 15, y, 50, 30);
            // padlock arc
            g.fillArc(x - 10, y - 20, 40, 40, 0, 180);

            // padlock center white fill
            g.setColor(Color.WHITE);
            g.fillArc(x - 5, y - 15, 30, 30, 0, 180);
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
            while (highScoreScanner.hasNext())
                allText += highScoreScanner.nextLine() + "\n";
        }
        catch (FileNotFoundException e)
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

        // NOTE: questions must have space between operators and on the outside of parenthesis
        // questions must not contain 3 digit numbers

        // load the questions from a file
        File f = new File("questions.txt");
        // read text into array
        String allText = "";
        // try to get the scanner
        Scanner in ;

        try
        { in = new Scanner(f);

            while ( in .hasNext())
                allText += in .nextLine() + '\n';

            questions = allText.split("\n");
        }
        catch (FileNotFoundException e)
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
                    g.fillRect(i * 25, 0, 23, 25);
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
            nextBtn.setBounds(350, 450, 130, 30);
            nextBtn.setVisible(false);

            // reset can initialize field variables errorMsg
            reset();

            addMouseListener(this);

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

            if (boxPos >= 0)
                // draw "=" symbol before answerField
                g.drawString("=", 50 + boxPos * 24, 180);

            g.setColor(Color.RED);
            // draw boxes around current operation the user is calculating
            if (boxPos >= 0 && boxSize > 0)
            {
                g.drawRect(48 + boxPos * 24, 90, boxSize * 24, 40);
            }
        }

        // reset error message and field variables
        public void reset()
        {
            errorMsg = "Click an operation to start solving! Remember to press enter in the text field";

            // currect question the user is solving
            question = questions[mainP.level - 1];
            simpQuestion = "";

            answerField.setVisible(false);
            nextBtn.setVisible(false);
            tries = 0;

            boxPos = -1;
            boxSize = -1;

            // 1 million should never be the correct answer
            answer = 1000000;
        }

        // next level button clicked
        public void actionPerformed(ActionEvent e)
        {
            // unlock the next level if user has yet to unlock it
            mainP.nextLevel = Math.max(mainP.nextLevel, mainP.level + 1);

            // go the next level
            mainP.level++;

            System.out.println("nextLevel: " + mainP.nextLevel + ", level: " + mainP.level);

            // repaint gamePanel
            refreshAll();
        }

        // enter pressed within answerField, check if user's input is the
        // same as the correct answer (of the simplified question)
        public void keyPressed(KeyEvent e)
        {
            int keyCode = e.getKeyCode();
            // points user got (if they finished the level)
            int points = -1;

            // if enter key is pressed
            if (keyCode == 10)
            {
                try
                {
                    // check if the user is correct by parsing the integer from text field
                    if (Double.parseDouble(answerField.getText()) == answer)
                    {
                        // replace question with simplified question
                        question = simpQuestion;
                        System.out.println("simpQuestion: " + simpQuestion);
                        simpQuestion = "";

                        boxPos = -1;
                        boxSize = -1;

                        // hide answer field until user clicks on another operator
                        answerField.setVisible(false);


                        // show next level button if user completed the level (no more operators)
                        if (question.length() <= 2)
                        {
                            // calculate score
                            points = (10 - tries) * 100;
                            // only add to score if this is their first time playing level
                            if (mainP.level == mainP.nextLevel)
                            {
                                mainP.score += points;
                                mainP.nextLevel++;
                                refreshAll();
                            }

                            // show next button to allow user to move onto next level
                            nextBtn.setVisible(true);

                            errorMsg = "Correct! You finished the level with " + points + " points";
                            // didn't complete level yet
                        }
                        else
                        {
                            errorMsg = "Correct! Click on another operator";
                        }
                    }
                    else
                    {
                        // number of tries user attempted to get the correct answer
                        tries++;
                        // user incorrect
                        errorMsg = "Incorrect!";
                    }

                    // clear textfield
                    answerField.setText("");
                    repaint();
                    // might not be an integer, so catch appropriately
                }
                catch (NumberFormatException exception)
                {
                    errorMsg = "Please enter a number!";

                    answerField.setText("");
                    repaint();
                }

            }
        }
        public void keyReleased(KeyEvent e)
        {}
        public void keyTyped(KeyEvent e)
        {}

        // user clicked an operation, check if correct operation based on
        // order of operations, calculate answer of just the operation,
        // and work out new simplified question
        public void mouseClicked(MouseEvent e)
        {
            // mouse x and y positions
            int x = e.getX();
            int y = e.getY();
            // index of character user clicked on within string
            int index = (x - 50) / 24;
            // index of character user clicked on within the inside string
            int insideIndex = -1;
            // character user clicked on
            char operation = ' ';
            // part of problem in between parenthesis
            String inside = "";

            System.out.println("\n\n");

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
                        inside = question.substring(question.indexOf("(") + 1, question.indexOf(")"));
                        // calc by subtracting the index of the start of substring
                        insideIndex = index - question.indexOf("(") - 1;

                        System.out.println("inside: " + inside);
                        System.out.println("insideIndex: " + insideIndex);
                        // inside of parens
                        orderOfOps(operation, insideIndex, inside);
                    }
                    else if (question.indexOf("(") >= 0 && question.indexOf(")") >= 0)
                    {
                        // number of tries user attempted to get the correct answer
                        tries++;
                        errorMsg = "Do operations within first parenthesis first!";
                    }
                    else
                    {
                        // do in the correct order
                        orderOfOps(operation, index, question);
                    }

                    repaint();
                }
            }
        }
        public void mousePressed(MouseEvent e)
        {}
        public void mouseReleased(MouseEvent e)
        {}
        public void mouseEntered(MouseEvent e)
        {}
        public void mouseExited(MouseEvent e)
        {}

        // checks whether op is the correct operator to calculate in str
        // set simpQuestion to simplified question, set answer to answer of
        // operation, and errorMsg to correct message
        public void orderOfOps(char op, int index, String str)
        {
            // addition/subtraction only if no multiply/divide
            if (str.indexOf("*") == -1 && str.indexOf("/") == -1)
            {
                if (str.indexOf("-") == -1)
                {
                    // add
                    if (index == str.indexOf("+")) correctOp(op, index, str);
                    else errorMsg = "Do addition first, from left to right";
                }
                else if (str.indexOf("+") == -1)
                {
                    // subtract
                    if (index == str.indexOf("-")) correctOp(op, index, str);
                    else errorMsg = "Do subraction first, from left to right";
                }
                else if (str.indexOf("+") < str.indexOf("-"))
                {
                    // add
                    if (index == str.indexOf("+")) correctOp(op, index, str);
                    else errorMsg = "Do addition first, from left to right";
                }
                else
                {
                    // subtract
                    if (index == str.indexOf("-")) correctOp(op, index, str);
                    else errorMsg = "Do subraction first, from left to right";
                }
                // do multiply/divide whichever comes first
            }
            else if (str.indexOf("/") == -1)
            {
                // multiply
                if (index == str.indexOf("*")) correctOp(op, index, str);
                else errorMsg = "Do multiplication first, from left to right";
            }
            else if (str.indexOf("*") == -1)
            {
                // divide
                if (index == str.indexOf("/")) correctOp(op, index, str);
                else errorMsg = "Do division first, from left to right";
            }
            else if (str.indexOf("*") < str.indexOf("/"))
            {
                // multiply
                if (index == str.indexOf("*")) correctOp(op, index, str);
                else errorMsg = "Do multiplication first, from left to right";
            }
            else
            {
                // divide
                if (index == str.indexOf("/")) correctOp(op, index, str);
                else errorMsg = "Do division first, from left to right";
            }

            System.out.println("sq: " + simpQuestion);
            if (simpQuestion.length() == 0)
                // number of tries user attempted to get the correct answer
                tries++;
        }


        // calculate the correct answer for this operator at index and draw onscreen
        public void correctOp(char op, int index, String str)
        {
            // solve a math problem using "js" ScriptEngine
            ScriptEngine solver = new ScriptEngineManager().getEngineByName("js");
            // impossible value for answer for debug
            answer = 1000000;
            // start and end indicies problem of the current operator
            // defaults in case start and end are not found
            int start = 0;
            int end = str.length();
            // exit loop var
            boolean exit = false;
            // how much index is offset from the original string
            int offset = question.indexOf(str);

            // go back to find start
            for (int i = index - 2; i >= 0 && !exit; i--)
            {
                // look for anything besides a number or decimal point
                if (".0123456789".indexOf(str.charAt(i)) == -1)
                {
                    start = i + 1;
                    exit = true;
                }
            }
            // reset exit variable for next loop
            exit = false;

            // look forward to find end
            for (int i = index + 2; i < str.length() && !exit; i++)
            {
                // look for anything besides a number or decimal point
                if (".0123456789".indexOf(str.charAt(i)) == -1)
                {
                    end = i;
                    exit = true;
                }
            }

            System.out.println("str: " + str);
            System.out.println("start: " + start);
            System.out.println("end: " + end);
            System.out.println("offset: " + offset);
            System.out.println("index: " + index);

            String problem = str.substring(start, end);
            System.out.println("problem: " + problem);

            try
            {
                // TODO: remove one
                answer = (double) solver.eval(problem);
                //answer = (double)((Integer)solver.eval(problem));
            }
            catch (ScriptException e)
            {
                System.err.println(problem + " is badly formatted!");
            }

            System.out.println("answer: " + answer);

            StringBuilder sqBuilder = new StringBuilder(question);
            // only one operator left, remove parenthesis around number if applicable
            if (str.length() < 9 && str.length() < question.length())
            {
                System.out.println("removing parenthesis...");
                // remove beginning parenthesis by looking back from operator
                sqBuilder.deleteCharAt(sqBuilder.lastIndexOf("(", index + offset));
                // remove end parenthesis by looking forward from operator
                sqBuilder.deleteCharAt(sqBuilder.indexOf(")", index + offset - 1));
                // replace simpQuestion with sqBuilder w/o parens
                simpQuestion = sqBuilder.toString();

                System.out.println("simpQuestion (2): " + simpQuestion);
            }
            else
            {
                simpQuestion = question;
            }

            // TODO: same problem (4 + 2 + (4 + 2)) will not work with this simple replace
            // if not floating point number
            if (answer - (int) answer == 0)
                // show as int
                simpQuestion = simpQuestion.replace(problem, (int) answer + "");
            else
                // show with decimal point
                simpQuestion = simpQuestion.replace(problem, answer + "");


            // draw the box
            boxPos = index + offset - (index - start);
            boxSize = problem.length();

            // move the answerfield
            answerField.setBounds(30 + (index + offset) * 24, 150, 100, 50);
            answerField.setVisible(true);
            // put focus on answer field so user can enter in the answer
            answerField.requestFocusInWindow();

            System.out.println("expected answer: " + answer);

            repaint();
        }
    }

    // game view for levels 11-20
    class LateLevelPanel extends JPanel implements MouseListener, MouseMotionListener
    {
        // x and y position of currect operator user is dragging
        private int x;
        private int y;
        // current operator
        private char op;
        // operators currently in place
        private char[] operators;
        // location of operators at the bottom where user drag from
        private int[][] sourceLoc;
        // operators at the bottom where user drag from
        private char[] sourceOps;
        // useful fonts for drawing texts
        private Font bigArial;

        public LateLevelPanel()
        {
            // initialize to defaults
            x = 0;
            y = 0;
            op = ' ';
            operators = new char[4];
            sourceLoc = new int[][]
            {
                new int[]
                {
                    200,
                    300
                }, new int[]
                {
                    300,
                    300
                }, new int[]
                {
                    400,
                    300
                }, new int[]
                {
                    500,
                    300
                }
            };
            sourceOps = new char[]
            {
                '+',
                '-',
                '*',
                '/'
            };

            // fonts used for drawing texts
            bigArial = new Font(Font.MONOSPACED, Font.PLAIN, 40);

            // background clear color
            setBackground(Color.WHITE);

            addMouseListener(this);
            addMouseMotionListener(this);
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            g.setColor(Color.BLACK);
            // draw operators
            for (int i = 0; i < operators.length; i++)
            {
                g.drawString(operators[i] + "", 100 + i * 20, 50);
            }

            g.setFont(bigArial);
            // draw source operators
            for (int i = 0; i < sourceOps.length; i++)
            {
                g.drawString(sourceOps[i] + "", sourceLoc[i][0], sourceLoc[i][1]);
            }

            g.setColor(Color.BLUE);
            //System.out.println(op);
            if (op != ' ')
                g.drawString(op + "", x, y);
        }

        public void mouseClicked(MouseEvent e)
        {}

        public void mouseEntered(MouseEvent e)
        {}
        public void mouseExited(MouseEvent e)
        {}
        public void mousePressed(MouseEvent e)
        {
            // x and y position of click
            int curX = e.getX();
            int curY = e.getY();

            for (int i = 0; i < sourceLoc.length; i++)
            {
                if (curX > sourceLoc[i][0] - 10 && curX < sourceLoc[i][0] + 30 && curY > sourceLoc[i][1] - 30 && curY < sourceLoc[i][1] + 10)
                    op = sourceOps[i];
            }

            System.out.println(op);
        }
        public void mouseReleased(MouseEvent e)
        {
            op = ' ';
        }

        public void mouseDragged(MouseEvent e)
        {
            x = e.getX() - 12;
            y = e.getY() + 13;

            repaint();
        }
        public void mouseMoved(MouseEvent e)
        {

        }
    }
}
