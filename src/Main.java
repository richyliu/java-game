/**
 * Richard Liu
 * 4/9/18
 * Main.java
 * Operators game
 * 
 * General overview:
 * The user starts off on the home panel. From there they can view the levels panel, high score, instructions,
 * or reset. High score panel allows them to view the past high scores. The instructions panel tells the user
 * what to do on the levels. The user can also reset the score if they choose to do so. From the levels panel
 * they can select different levels (currently out of 20, but easily changeable). The first 10 are easy levels;
 * the user only has to click on operators in the correct order to solve the problem. They are represented by
 * an player which shoots fireballs to destroy the enemy. In the last 10 levels, the user has to drag operators
 * to the correct location in order to make the desired level. They lose health according to the number of
 * times the tried. Once they finished all 20 levels, they can enter in their score into the high score file.
 */


// common layouts
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
// for drawing
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Font;
// for null/strict flow layouts
import java.awt.Dimension;
// events for buttons, text fields, mouse
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
// for reading files
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.imageio.ImageIO;
// for writing files
import java.io.IOException;
import java.io.PrintWriter;

// for "js" eval of math equations
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
// for 10px margin around the whole game screen
import javax.swing.BorderFactory;
// for padlock icon on top of levels
import javax.swing.Icon;
import javax.swing.ImageIcon;
// components
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


// Main class containing main method which is first ran
public class Main
{
    public static void main(String[] args)
    {
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
        frame.setSize(820, 620);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLocation(400, 200);
        frame.setResizable(true);

        // add empty 10px margin on all sides
        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
    // complete panel reference for score refreshing
    protected CompletePanel completePanel;
    // high score panel reference for high score refreshing
    protected HighScorePanel highScorePanel;

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
        nextLevel = 1;
        // starts at 0 score
        score = 0;

        gamePanel = new GamePanel(this);
        arial = new Font("Arial", Font.PLAIN, 20);
        levelsPanel = new LevelsPanel(this);
        highScorePanel = new HighScorePanel(this);
        completePanel = new CompletePanel(this);

        // add 2 panels, home and instructions
        add(new HomePanel(this), "home");
        add(new InstructionPanel(this), "instructions");
        add(levelsPanel, "levels");
        add(highScorePanel, "highScore");
        add(new ResetPanel(this), "reset");
        add(completePanel, "complete");
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
    // background image
    private Image homeBackground;

    public HomePanel(MainPanel mainPIn)
    {
        mainP = mainPIn;

        try
        {
            homeBackground = ImageIO.read(new File("assets/background.png"));
        }
        catch (IOException e)
        {
            // print error message
            System.out.println("Unable to load images!");
        }

        // use a flowlayout to position buttons and label in the center
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 150));

        // add center panel containing the buttons and title text
        add(new CenterPanel());
    }

    // draw background image
    public void paintComponent(Graphics g)
    {
        // paint the background
        super.paintComponent(g);
        g.drawImage(homeBackground, 0, 0, this);
    }

    // contains the buttons and label and is positioned in the center of homepanel
    class CenterPanel extends JPanel implements ActionListener
    {
        public CenterPanel()
        {
            // use a grid layout with 10px of vertical spacing
            setLayout(new GridLayout(5, 1, 0, 10));

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

            // add buttons to FlowLayout
            add(newGame);
            add(instructions);
            add(highScore);
            add(reset);
            add(quit);

            // use white background
            setBackground(Color.GRAY);
        }

        // draw background image
        public void paintComponent(Graphics g)
        {
            // paint the background
            super.paintComponent(g);
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


// when the user finishes the game, enter their name into the high score file
class CompletePanel extends JPanel implements ActionListener
{
    // MainPanel reference for using the card layout
    private MainPanel mainP;
    // congratulate user for finishing game
    private JLabel congrats;
    // name input
    private JTextField nameField;
    // to display high score
    private JTextArea highScore;
    private File highScoreFile;

    public CompletePanel(MainPanel mainPIn)
    {
        setLayout(new FlowLayout(FlowLayout.CENTER, 400, 10));

        mainP = mainPIn;
        highScoreFile = new File("assets/highScore.txt");

        // create label and name input
        congrats = new JLabel("Congratulations! You finished with " + mainP.score +
                " points. Enter your name", JLabel.CENTER);
        nameField = new JTextField(10);
        // use this class as an action listener
        nameField.addActionListener(new NameFieldHandler());

        // text area to display high scores
        highScore = new JTextArea(10, 15);
        highScore.setEditable(false);

        // exit button to go back to home screen
        JButton exit = new JButton("Exit");
        exit.addActionListener(this);

        refresh();

        // add to flow layout
        add(congrats);
        add(nameField);
        add(highScore);
        add(exit);
    }

    // for getting input from the name field
    class NameFieldHandler implements ActionListener
    {
        public NameFieldHandler()
        {}

        // called when enter is pressed in the name field
        public void actionPerformed(ActionEvent e)
        {
            // get the text in the text field
            String text = e.getActionCommand();

            // prevent the user from changing their name
            nameField.setEnabled(false);

            // add a player to the file based on their score
            addPlayer(text + " - " + mainP.score);
            // refresh so user can see their name on the high score leaderboard
            refresh();
        }

    }

    // refresh the high score contents
    public void refresh()
    {
        // load players high scores and join by newline
        String highScores = "";
        String[] players = loadPlayers();
        for (int i = 0; i < players.length; i++)
            highScores += players[i] + "\n";


        // set high score text to string (joined by newline) loaded from file
        highScore.setText(highScores);
        // congratulate user with however many points they got
        congrats.setText("Congratulations! You finished with " + mainP.score + " points. Enter your name");
        // refresh the home page's high score panel
        mainP.highScorePanel.refresh();
    }

    // button clicked
    public void actionPerformed(ActionEvent e)
    {
        // go back to home screen
        mainP.showCard("home");

    }

    // load player high/current score data from file
    public String[] loadPlayers()
    {
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

            highScoreScanner.close();
        }
        catch (FileNotFoundException e)
        {
            // tell the user if error
            System.out.println("\n\nERROR: Cannot find/open assets/highScore.txt file to read\n\n\n");
        }


        return allText.split("\n");
    }

    // adds a player name and high score to the file
    public void addPlayer(String nameAndScore)
    {
        PrintWriter writer;
        String[] highScores = loadPlayers();
        // inserted score already, no need to do it again
        boolean inserted = false;

        try
        {
            writer = new PrintWriter(highScoreFile);

            // add nameAndScore to current high scores by inserting the score once less than the highest
            for (int i = 0; i < highScores.length; i++)
            {
                if (getScore(highScores[i]) < getScore(nameAndScore) && !inserted)
                {
                    // write new score to file
                    writer.println(nameAndScore);
                    inserted = true;
                }

                // write score to file
                writer.println(highScores[i]);
            }

            // write at the end if not written yet
            if (!inserted)
                writer.println(nameAndScore);

            writer.close();
        }
        catch (IOException e)
        {
            // tell the user if error
            System.out.println("\n\nERROR: Cannot find/open assets/highScore.txt file to write to\n\n\n");
        }
    }

    // get the score (an integer) from the name and score string ("bob - 3000")
    public int getScore(String nameAndScore)
    {
        int split = nameAndScore.indexOf(" - ") + 3;
        String substring = nameAndScore.substring(split, nameAndScore.length());

        return Integer.parseInt(substring);
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

        // load instructions
        Scanner reader;
        // the text in the instructions file
        String allText = "";

        try
        {
            // initialize the scanner with the instructions file
            reader = new Scanner(new File("assets/instructions.txt"));

            // read all the lines into allText
            while (reader.hasNext())
                allText += reader.nextLine() + "\n";

            reader.close();
        }
        catch (FileNotFoundException e)
        {
            // tell the user if error
            System.out.println("\n\nERROR: Cannot find/open assets/instructions.txt file to read\n\n\n");
        }

        // text area containing instructions text loaded from file
        JTextArea textArea = new JTextArea(allText, 20, 4);
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
    // high score text 
    private JTextArea textArea;

    public HighScorePanel(MainPanel mainPIn)
    {
        mainP = mainPIn;

        // border layout for the button
        setLayout(new BorderLayout(20, 20));

        // make high score text area with allText
        textArea = new JTextArea("", 20, 4);
        textArea.setFont(mainP.arial);
        // don't let the user edit!
        textArea.setEditable(false);

        // add back button, which uses this class as an action listener
        JButton back = new JButton("Back");
        back.addActionListener(this);

        refresh();

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

    // refresh high scores by loading them from the file
    public void refresh()
    {
        // load file with high score data
        File highScoreFile = new File("assets/highScore.txt");
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

            textArea.setText(allText);
        }
        catch (FileNotFoundException e)
        {
            // tell the user if error
            System.out.println("\n\nERROR: Cannot find/open assets/highScore.txt file to read\n\n\n");
        }
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
    // questions for levels 1 to 10
    private String[] questions;
    // questions for levels 11 to 20
    // first 4 second-layer elements are the question, last element is the expected result
    private int[][] opQuestions;

    // player image
    private Image player;
    // enemy image
    private Image enemy;
    // player health image
    private Image healthImage;
    // arrow attack image
    private Image arrow;
    // fireball attack image
    private Image fireball;
    // fire image for when enmy dies
    private Image fire;

    // health of the enemy. starts out as the number of operators
    private int enemyHealth;
    // max health the enemy can have. starts out as the number of operators
    private int maxEnemyHealth;
    // player's health. number between 0 and 1
    private double health;

    public GamePanel(MainPanel mainPIn)
    {
        mainP = mainPIn;

        // try to get images
        try
        {
            player = ImageIO.read(new File("assets/person1.png"));
            enemy = new ImageIcon("assets/monster.gif").getImage();
            healthImage = ImageIO.read(new File("assets/hearts.png"));
            arrow = ImageIO.read(new File("assets/arrow.png"));
            fireball = new ImageIcon("assets/fireball.gif").getImage();
            fire = new ImageIcon("assets/fire.gif").getImage();
        }
        catch (IOException e)
        {
            // print error message
            System.out.println("Unable to load images!");
        }

        // NOTE: questions must have space between operators and on the outside of parenthesis
        // questions must NOT have nested parenthesis
        // questions must contain 1 or 2 digit numbers only
        // assets/questions.txt must contain 10 lines of the above questions
        // it must also contain 10 lines of fill in the operator questions
        // 4 numbers are separated by commas and the expected answer is also separated by a comma

        // load the questions from a file
        File f = new File("assets/questions.txt");
        // read text into array
        String allText = "";
        // try to get the scanner
        Scanner input;

        questions = new String[10];
        opQuestions = new int[10][5];

        try
        {
            input = new Scanner(f);

            // load early level questions from file
            for (int i = 0; i < 10; i++)
                questions[i] = input.nextLine();

            // load late level questions
            for (int i = 0; i < 10; i++)
            {
                // get current line, split by commas
                String[] line = input.nextLine().split(",");
                // turn string into into and put into array
                for (int j = 0; j < 5; j++)
                    opQuestions[i][j] = Integer.parseInt(line[j]);
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("\n\nERROR: Cannot find/open assets/questions.txt file to read\n\n\n");
        }

        health = 1;

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

    // solves a math problem using the "js" evaluator
    public double solve(String question)
    {
        // solve a math problem using "js" ScriptEngine
        ScriptEngine solver = new ScriptEngineManager().getEngineByName("js");
        double answer = 1000000;

        try
        {
            // try to get the answer to the problem using "js" eval
            answer = (double) solver.eval(question);
        }
        catch (ScriptException e)
        {
            System.out.println(question + " is badly formatted!");
        }

        return answer;
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
            {
                mainP.showCard("levels");
                // refresh padlock icons
                refreshAll();
            }
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

                // draw rectangles corresponding to the level the user is on
                for (int i = 0; i < questions.length + opQuestions.length; i++)
                {
                    // draw rectangle if level is completed
                    if (i < mainP.level)
                    {
                        g.setColor(Color.BLACK);
                        g.fillRect(i * 25, 0, 23, 25);
                        // color for number
                        g.setColor(Color.WHITE);
                    }
                    else
                    {
                        // color for number
                        g.setColor(Color.BLACK);
                    }
                    // draw number for the level
                    g.drawString(i + 1 + "", i * 25 + 5, 15);
                }

                // draw the score
                g.setColor(Color.RED);
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
                late.reset();
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
    class EarlyLevelPanel extends JPanel implements KeyListener, ActionListener, MouseListener, MouseMotionListener
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
        // currect question the user is solving
        private String question;
        // simplified question the user will solve next 
        private String simpQuestion;
        // box to draw around the operations the user is currently solving
        private int boxPos;
        private int boxSize;
        // answer the user should enter into the text field
        private double answer;
        // which frame the fireball is on while travelling (-1 for no fireball)
        private int fireballFrame;
        // which frame the arrow is on while travelling (-1 for no arrow)
        private int arrowFrame;
        // position of blue box to show which operator the user is hovering over
        private int hoverPos;

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
            answerField.setBounds(600, 35, 80, 50);
            // don't show the answer field until the user clicks on an operation
            answerField.setVisible(false);

            // position next level button in the bottom of the screen
            nextBtn = new JButton("Next level");
            nextBtn.addActionListener(this);
            nextBtn.setBounds(300, 480, 130, 30);
            nextBtn.setVisible(false);

            // reset can initialize field variables errorMsg
            reset();

            addMouseListener(this);
            addMouseMotionListener(this);

            add(answerField);
            add(nextBtn);
        }

        // draw the math question
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            g.setFont(bigArial);
            // draw the question and an equal sign
            g.drawString(question, 50, 70);

            g.setFont(bigSerif);
            // draw error message
            g.drawString(errorMsg, 50, 180);

            if (boxPos >= 0)
                // draw "=" symbol before answerField
                g.drawString("=", 30 + boxPos * 24, 130);

            g.setColor(Color.RED);
            // draw boxes around current operation the user is calculating
            if (boxPos >= 0 && boxSize > 0)
            {
                g.drawRect(48 + boxPos * 24, 40, boxSize * 24, 40);
            }

            // draw hoverPos if non negative
            g.setColor(Color.BLUE);
            if (hoverPos >= 0)
                g.drawRect(48 + hoverPos * 24, 40, 24, 40);

            // draw player and enemy
            g.drawImage(player, 100, 250, 150, 225, this);
            g.drawImage(enemy, 500, 300, 150, 150, this);

            // draw enemy health bar
            g.setColor(Color.BLACK);
            g.drawRect(520, 250, 106, 26);
            g.setColor(Color.RED);
            if (enemyHealth >= 0)
                // fill in from 0 to 100 according to enemyHealth percentage
                g.fillRect(523, 253, (int)(enemyHealth / (double) maxEnemyHealth * 100), 20);

            // draw player health bar
            g.drawImage(healthImage, 140, 230, 140 + (int)(health * 90), 260, 0, 0,
                    (int)(health * 450), 150, this);

            // draw fireball
            if (fireballFrame > -1)
            {
                fireballFrame -= 15;

                g.drawImage(fireball, 200 + (240 - fireballFrame), 300, 100, 100, this);

                // "hit" the enemy
                if (fireballFrame < 0)
                {
                    enemyHealth--;

                    // special code to draw the fire death
                    if (enemyHealth == 0)
                        enemyHealth = -1;
                }
            }

            // draw arrow
            if (arrowFrame > -1)
            {
                arrowFrame -= 30;
                g.drawImage(arrow, 210 + arrowFrame, 300, 160, 20, this);
            }

            // show fire death of enemy
            if (enemyHealth == -1)
                g.drawImage(fire, 500, 300, 150, 150, this);

            // die if ran out of health
            if (health < 0.01)
            {
                refreshAll();
                errorMsg = "You died! Try again";
                health = 1;
            }
        }

        // reset error message and field variables
        public void reset()
        {
            errorMsg = "Click an operator to start solving and press enter to continue";

            // currect question the user is solving
            question = questions[mainP.level - 1];
            simpQuestion = "";

            answerField.setVisible(false);
            nextBtn.setVisible(false);

            boxPos = -1;
            boxSize = -1;
            hoverPos = -1;

            // 1 million should never be the correct answer
            answer = 1000000;

            // max enemy health calculated by counting number of operators
            maxEnemyHealth = 0;
            for (int i = 0; i < question.length(); i++)
                if ("+-*/".indexOf(question.charAt(i)) != -1)
                    maxEnemyHealth++;
            // enemy health
            enemyHealth = maxEnemyHealth;

            fireballFrame = -1;
            arrowFrame = -1;
        }

        // next level button clicked
        public void actionPerformed(ActionEvent e)
        {
            // go the next level
            mainP.level++;

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
                        simpQuestion = "";

                        boxPos = -1;
                        boxSize = -1;

                        // hide answer field until user clicks on another operator
                        answerField.setVisible(false);

                        // release fireball to hit monster
                        fireballFrame = 240;

                        // show next level button if user completed the level (no more operators)
                        if (question.length() <= 3)
                        {
                            // calculate score (cannot be negative)
                            points = (int) Math.max(0, health * 100) * 10;
                            // only add to score if this is their first time playing level
                            if (mainP.level == mainP.nextLevel)
                            {
                                mainP.score += points;
                                // unlock next level
                                mainP.nextLevel++;
                            }

                            // reset health for next level
                            health = 1;

                            // show next button to allow user to move onto next level
                            nextBtn.setVisible(true);

                            errorMsg = "Correct! You finished the level with " + points + " points";
                            // didn't complete level yet
                        }
                        else
                        {
                            errorMsg = "Correct! Click on another operation";
                        }
                    }
                    else
                    {
                        // user incorrect
                        errorMsg = "Incorrect!";

                        health -= 0.333;
                        // release arrow to hit user
                        arrowFrame = 200;
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

            // player clicked on a character within the string
            if (y > 45 && y < 70 && index >= 0 && index < question.length())
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

                        // inside of parens
                        orderOfOps(operation, insideIndex, inside, question.indexOf("(") + 1);
                    }
                    else if (question.indexOf("(") >= 0 && question.indexOf(")") >= 0)
                    {
                        // incorrect order
                        health -= 0.1666;
                        // release arrow to hit user
                        arrowFrame = 200;
                        errorMsg = "Do operators within first parenthesis first!";
                    }
                    else
                    {
                        // do in the correct order
                        orderOfOps(operation, index, question, 0);
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

        public void mouseDragged(MouseEvent e)
        {}
        public void mouseMoved(MouseEvent e)
        {
            // mouse x and y positions
            int x = e.getX();
            int y = e.getY();
            // index of character user clicked on within string
            int index = (x - 50) / 24;

            // player clicked on a character within the string and is a valid operator
            if (y > 45 && y < 70 && index >= 0 && index < question.length() &&
                    "+-/*".indexOf(question.charAt(index)) >= 0)
                hoverPos = index;
            else
                hoverPos = -1;
        }

        // checks whether op is the correct operator to calculate in str
        // set simpQuestion to simplified question, set answer to answer of
        // operation, and errorMsg to correct message
        public void orderOfOps(char op, int index, String str, int offset)
        {
            // addition/subtraction only if no multiply/divide
            if (str.indexOf("*") == -1 && str.indexOf("/") == -1)
            {
                if (str.indexOf("-") <= 0)
                {
                    // add
                    if (index == str.indexOf("+")) correctOp(op, index, str, offset);
                    else errorMsg = "Do addition first, from left to right";
                }
                else if (str.indexOf("+") == -1)
                {
                    // subtract
                    if (index == str.indexOf("-")) correctOp(op, index, str, offset);
                    else errorMsg = "Do subraction first, from left to right";
                }
                else if (str.indexOf("+") < str.indexOf("-"))
                {
                    // add
                    if (index == str.indexOf("+")) correctOp(op, index, str, offset);
                    else errorMsg = "Do addition first, from left to right";
                }
                else
                {
                    // subtract
                    if (index == str.indexOf("-")) correctOp(op, index, str, offset);
                    else errorMsg = "Do subraction first, from left to right";
                }
                // do multiply/divide whichever comes first
            }
            else if (str.indexOf("/") == -1)
            {
                // multiply
                if (index == str.indexOf("*")) correctOp(op, index, str, offset);
                else errorMsg = "Do multiplication first, from left to right";
            }
            else if (str.indexOf("*") == -1)
            {
                // divide
                if (index == str.indexOf("/")) correctOp(op, index, str, offset);
                else errorMsg = "Do division first, from left to right";
            }
            else if (str.indexOf("*") < str.indexOf("/"))
            {
                // multiply
                if (index == str.indexOf("*")) correctOp(op, index, str, offset);
                else errorMsg = "Do multiplication first, from left to right";
            }
            else
            {
                // divide
                if (index == str.indexOf("/")) correctOp(op, index, str, offset);
                else errorMsg = "Do division first, from left to right";
            }

            // user got operation incorrect
            if (simpQuestion.length() == 0)
            {
                health -= 0.1666;
                // release arrow to hit user
                arrowFrame = 200;
            }
        }

        // calculate the correct answer for this operator at index and draw onscreen
        public void correctOp(char op, int index, String str, int offset)
        {
            // impossible value for answer for debug
            answer = 1000000;
            // start and end indicies problem of the current operator
            // defaults in case start and end are not found
            int start = 0;
            int end = str.length();
            // exit loop var
            boolean exit = false;

            // go back to find start
            for (int i = index - 2; i >= 0 && !exit; i--)
            {
                // look for anything besides a number or decimal point or negative sign
                if (".0123456789-".indexOf(str.charAt(i)) == -1)
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
                if (".0123456789-".indexOf(str.charAt(i)) == -1)
                {
                    end = i;
                    exit = true;
                }
            }

            // part of the problem the user is currently solving
            String problem = str.substring(start, end);

            // user solver function above in GamePanel
            answer = solve(problem);

            // draw the box
            boxPos = offset + start;
            boxSize = problem.length();

            StringBuilder sqBuilder = new StringBuilder(question);
            // only one operator left, remove parenthesis around number if applicable
            if (str.length() < 9 && str.length() < question.length())
            {
                // remove beginning parenthesis by looking back from operator
                sqBuilder.deleteCharAt(sqBuilder.lastIndexOf("(", index + offset));
                // remove end parenthesis by looking forward from operator
                sqBuilder.deleteCharAt(sqBuilder.indexOf(")", index + offset - 1));
                // replace simpQuestion with sqBuilder w/o parens
                simpQuestion = sqBuilder.toString();

                // account for removed beginning parenthesis
                offset--;
            }
            else
            {
                // just use question as simpQuestion
                simpQuestion = question;
            }

            // if not floating point number
            if (answer - (int) answer == 0)
                // show as int
                //simpQuestion = simpQuestion.replace(problem, (int) answer + "");
                simpQuestion =
                simpQuestion.substring(0, offset + start) +
                (int) answer +
                simpQuestion.substring(offset + end, simpQuestion.length());
            else
                // show with decimal point
                simpQuestion = simpQuestion.replace(problem, answer + "");


            // move the answerfield
            answerField.setBounds(30 + (index + offset) * 24, 100, 100, 50);
            answerField.setVisible(true);
            // put focus on answer field so user can enter in the answer
            answerField.requestFocusInWindow();

            // redraw the answerField and red box
            repaint();
        }
    }


    // game view for levels 11-20
    class LateLevelPanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener
    {
        // x and y position of currect operator user is dragging
        private int x;
        private int y;
        // current operator
        private char op;
        // operators currently in place
        private char[] operators;
        // operators at the bottom where user drag from
        private char[] sourceOps;
        // useful fonts for drawing texts
        private Font bigArial;
        // currect question the user is solving
        private int[] question;
        // answer user should try to get to by putting operators
        private double answer;
        // user's current answer attempt
        private double curAnswer;
        // button to allow the user to move on to the next level
        private JButton nextBtn;
        // draw instructions for late levels if true
        private boolean drawInstructions;
        // which frame the fireball is on while travelling (0 for no fireball)
        private int fireballFrame;
        // which frame the arrow is on while travelling (-1 for no arrow)
        private int arrowFrame;

        public LateLevelPanel()
        {
            // initialize to defaults
            x = 0;
            y = 0;
            op = ' ';
            operators = new char[]
            {
                ' ',
                ' ',
                ' '
            };
            // operators the user can drag from
            sourceOps = new char[]
            {
                '+',
                '-',
                '*',
                '/'
            };

            drawInstructions = true;

            // fonts used for drawing texts
            bigArial = new Font(Font.MONOSPACED, Font.PLAIN, 40);

            // background clear color
            setBackground(Color.WHITE);

            // position next level button in the bottom of the screen
            nextBtn = new JButton("Next level");
            nextBtn.addActionListener(this);
            nextBtn.setBounds(350, 450, 130, 30);
            nextBtn.setVisible(false);

            add(nextBtn);

            addMouseListener(this);
            addMouseMotionListener(this);
        }


        // reset error message and field variables
        public void reset()
        {
            // currect question the user is solving
            question = new int[]
            {
                opQuestions[mainP.level - 11][0],
                opQuestions[mainP.level - 11][1],
                opQuestions[mainP.level - 11][2],
                opQuestions[mainP.level - 11][3]
            };

            // get the expected answer
            answer = opQuestions[mainP.level - 11][4];

            // reset operators
            operators = new char[]
            {
                ' ',
                ' ',
                ' '
            };
            curAnswer = 0;

            nextBtn.setVisible(false);

            // change button from "next level" to "finish game" once user is on the last level
            if (mainP.level == questions.length + opQuestions.length)
            {
                nextBtn.setText("Finish game!");
            }

            maxEnemyHealth = 1;
            enemyHealth = 1;
            health = 1;

            fireballFrame = -1;
            arrowFrame = -1;
        }

        public void paintComponent(Graphics g)
        {
            // paint white background
            super.paintComponent(g);

            g.setFont(bigArial);
            g.setColor(Color.BLUE);
            // draw operators
            for (int i = 0; i < operators.length; i++)
            {
                // draw number boxes
                g.drawRect(190 + i * 100, 85, 40, 40);

                // draw the operators spaced 100px starting from 200px
                g.drawString(operators[i] + "", 200 + i * 100, 120);
            }

            g.setColor(Color.BLACK);
            // draw numbers
            for (int i = 0; i < question.length; i++)
                // draw the operators spaced 100px starting from 140px
                g.drawString(question[i] + "", 140 + i * 100, 120);

            // draw answer
            g.drawString(answer + "", 550, 70);

            // draw equal sign and user's answer, truncated to 3rd decimal place
            g.drawString("= " + Math.round(curAnswer * 1000) / 1000.0, 500, 120);

            // draw source operators
            for (int i = 0; i < sourceOps.length; i++)
            {
                // draw number boxes
                g.drawRect(190 + i * 100, 170, 40, 40);

                // use sourceLoc to draw sourceOps
                g.drawString(sourceOps[i] + "", 200 + i * 100, 200);
            }

            // draw operators in boxes
            g.setColor(Color.BLUE);
            if (op != ' ')
                g.drawString(op + "", x, y);

            // draw labels
            g.setFont(new Font("serif", Font.PLAIN, 15));
            g.setColor(Color.BLACK);
            g.drawString("Expected answer", 550, 30);
            g.drawString("Current answer", 550, 85);

            // draw instructions
            if (drawInstructions)
                g.drawString("Draw an operator from the black box to the blue box " +
                        "to make current match expected answer", 50, 250);


            // draw player and enemy
            g.drawImage(player, 100, 300, 150, 225, this);
            g.drawImage(enemy, 500, 350, 150, 150, this);

            // draw enemy health bar
            g.setColor(Color.BLACK);
            g.drawRect(520, 300, 106, 26);
            g.setColor(Color.RED);
            if (enemyHealth >= 0)
                // fill in from 0 to 100 according to enemyHealth percentage
                g.fillRect(523, 303, (int)(enemyHealth / (double) maxEnemyHealth * 100), 20);

            // draw player health bar
            g.drawImage(healthImage, 140, 280, 140 + (int)(health*90), 310, 0, 0,
                    (int)(health*450), 150, this);

            // draw fireball
            if (fireballFrame > -1)
            {
                fireballFrame -= 15;

                g.drawImage(fireball, 200 + (240 - fireballFrame), 350, 100, 100, this);

                // "hit" the enemy
                if (fireballFrame < 0)
                {
                    // special code to draw the fire death
                    enemyHealth = -1;
                }
            }

            // draw arrow
            if (arrowFrame > -1)
            {
                arrowFrame -= 30;

                g.drawImage(arrow, 210 + arrowFrame, 350, 160, 20, this);

                if (arrowFrame < 0)
                    health -= 0.08333;
            }

            // show fire death of enemy
            if (enemyHealth == -1)
                g.drawImage(fire, 500, 350, 150, 150, this);

            // die if ran out of health
            if (health < 0.01)
            {
                refreshAll();
                health = 1;
            }
        }

        // next level button clicked
        public void actionPerformed(ActionEvent e)
        {
            // go the next level
            mainP.level++;

            // if finished with game, show complete game panel for high score
            if (mainP.level > questions.length + opQuestions.length)
            {
                mainP.showCard("complete");
                mainP.completePanel.refresh();
            }
            else
            {
                // repaint gamePanel
                refreshAll();
            }
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

            // loop through all different operators
            for (int i = 0; i < sourceOps.length; i++)
            {
                // check if one was clicked
                if (curX > 200 + i * 100 - 10 && curX < 230 + i * 100 && curY > 170 && curY < 210)
                    // draw the operator that was clicked
                    op = sourceOps[i];
            }
        }
        public void mouseReleased(MouseEvent e)
        {
            // x and y position of release
            int curX = e.getX();
            int curY = e.getY();

            // loop through all different operators positions
            for (int i = 0; i < operators.length; i++)
            {
                // check if release in position
                if (curX > 190 + i * 100 && curX < 230 + i * 100 && curY > 85 && curY < 125)
                    // set that operator
                    operators[i] = op;
            }

            // no operator is being dragged anymore
            op = ' ';

            // calculate the user's answer
            calcAnswer();

            // draw new operator in place
            repaint();
        }

        public void mouseDragged(MouseEvent e)
        {
            // get the x and y of the center of the operator
            x = e.getX() - 12;
            y = e.getY() + 13;

            // draw the moving operator
            repaint();
        }
        public void mouseMoved(MouseEvent e)
        {}

        // calculates the user's answers using the user's operation
        // also checks if the answer is correct
        public void calcAnswer()
        {
            // question with the numbers and operations to be fed into the eval
            String ques = "";
            // points user earned on this level
            int points = 0;

            // add all 4 question numbers and all 3 operators
            for (int i = 0; i < 3; i++)
                ques += "" + question[i] + operators[i];
            ques += question[3];

            // problem not fully solved yet
            if (ques.indexOf(' ') != -1)
            {
                // do nothing
                curAnswer = 0;
            }
            else
            {
                curAnswer = solve(ques);

                // user got correct, move onto next level
                if (answer == curAnswer)
                {
                    // hit enemy
                    fireballFrame = 240;

                    // calculate score (cannot be negative)
                    points = (int) Math.max(0, health * 100) * 10;

                    // show next button
                    nextBtn.setVisible(true);
                    // hide instructions
                    drawInstructions = false;

                    repaint();

                    // only add to score if this is their first time playing level
                    if (mainP.level == mainP.nextLevel)
                    {
                        mainP.score += points;
                        // unlock next level
                        mainP.nextLevel++;
                    }
                }
                else
                {
                    // user got incorrect, health decrease
                    arrowFrame = 200;
                }
            }

            repaint();
        }
    }
}
