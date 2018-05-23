/***************************************************************************************************
* Name:        Who Wants to be a Millionaire?
* Authors:     Quinton Yong and Connor Pickles
* Date:        Created: November 18, 2013 Updated: December 8, 2013
* Purpose:     A Java version of the popular British game show
* Copyright:   Most questions in this game are from a list of questions that states the following:
*              This guide is Copyright © Labmaster, 2005. You are free to reproduce this
*              guide on the condition that this copyright notice is retained.
*
*              All trademarks contained within this guide are the property of their
*              respective owners
***************************************************************************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class WWTBAM extends JPanel {
    static JPanel panel;                                             // main drawing panel
    static JFrame frame;                                             // window frame which contains the panel
    static Image background;                                         // background to display questions on
    static Image logo;                                               // displays logo at the beginning of the game
    static Image menu;                                               // main menu of the game
    static Image correctScreen;                                      // screen shown when an answer is correct
    static Image loseScreen;                                         // screen shown when the player loses
    static Image moneyWon;                                           // screen that shows the user the money they won
    static Image millionaire;                                        // screen shown when all questions are answered correctly
    static Image finalAnswer;                                        // screen to show final answer
    static Image blank;                                              // blank background that instructions and records are displayed on
    static Image lifelines;                                          // screen to display lifeline information on
    static final int WINDOW_WIDTH = 800;                             // width of display window
    static final int WINDOW_HEIGHT = 600;                            // height of display window
    static final int LEVELS = 5;                                     // number of difficulty levels
    static final int QPL = 3;                                        // Questions Per Level
    static final int QPF = 11;                                       // Questions Per File

    static int gameStage = 0;                                        // stage of game
    static final int WELCOME_SCREEN = 0;                             // Welcome Screen
    static final int MENU = 1;                                       // Menu
    static final int INSTRUCTIONS = 2;                               // Instructions
    static final int PLAY = 3;                                       // Play
    static final int END_GAME = 4;                                   // End game

    static int playStage = 0;                                        // stage of actual play
    static int lastStage = 0;                                        // last screen displayed
    static final int QUESTION = 0;                                   // Question
    static final int FINAL_ANSWER = 1;                               // Final Answer
    static final int CORRECT = 2;                                    // If correct
    static final int LOSE = 3;                                       // If lose
    static final int MILLIONAIRE = 4;                                // If Win
    static final int MONEY_WON = 5;                                  // Money Won
    static final int FIFTY_FIFTY = 6;                                // Fifty Fifty
    static final int DOUBLE_DIP = 7;                                 // Switch the Question
    static final int SWITCH_THE_QUESTION = 8;                        // Error Screen
    static final int ERROR_SCREEN = 9;                               // Records
    static final int RECORDS = 10;

    static boolean waitingForKeyPress = false;                       // true when we are waiting for a user to make a specific choice

    static String name = "";
    static String playOutput = "";                                   // output to panel after play
    static String playOutput2 = "";                                  // used during Double Dip
    static String errorMessage = "";                                 // stores an error message to display
    static int correctAnswer = 0;                                    // stores the correct answer for the current question
    static int randomAnswer = 0;                                     // stores a random answer as part of 50/50
    static int outcome = 0;                                          // stores the result of processing the users choice
    static int outcome2 = 0;                                         // stores the result of the users second answer, used during Double Dip
    static int diffCounter = 0;                                      // keeps track of the difficulty level
    static int qCounter = 0;                                         // keeps track of the question within a difficulty level
    static int total = 0;                                            // the money that can be won by answering the current question
    static int lastTotal = 0;                                        // the money that will be won if the user chooses to leave
    static int garTotal = 0;                                         // the total the user will leave with if they get a question wrong
    static int DDCount = 0;                                          // counts the number of answers while using Double Dip
    static int timesPlayed = 0;                                      // number of times the player has played the game
    static int timesWon = 0;                                         // number of times the player has won the game
    static int timesLost = 0;                                        // number of times the player has lost the game
    static int maxWon = 0;                                           // highest amount the player has won
    static int totalWon = 0;                                         // total amount of money the player has won
    static int nextJackpot = 0;                                      // next jackpot for the player
    static int yCoord = 0;                                           // y coordinates for displaying records in an interation
    static int length = ((int)getLength("records.txt") / 7);         // number of players in the records file
    static int currentScreen = 0;                                    // track of how many screens have been displayed
    static int totalScreens = 0;                                     // total amount of screens that need to be displayed
    static boolean lifeline1 = true;                                 // 50/50
    static boolean lifeline2 = true;                                 // Double Dip
    static boolean lifeline3 = true;                                 // Switch the Question
    static boolean lifelineUsed = false;                             // remembers if a lifeline has been used for the current question
    static boolean usingFF = false;                                  // used to tell the program if 50/50 is in use
    static boolean usingDD = false;                                  // used to tell the progrma if Double Dip is in use
    static String [][][] questions = new String [LEVELS][QPL][6];    // the 3D string array containing 15 random questions
    static String [][][] allQuestions = new String [LEVELS][QPF][6]; // 3D string array containing all the questions
    static String [] newQuestion = new String [6];                   // used to store the new question while executing the method switchTheQuestion
    static String [][] records = new String [length][7];             // stores the records of all players
    static String [] currentQuestion = null;                         // used to split the questions into multiple lines

    // start main program
    public static void main (String args[]) {

        // Create Image Object
        Toolkit tk = Toolkit.getDefaultToolkit();

        background = tk.getImage("questionsPic.jpg");
        logo = tk.getImage("logo.png");
        menu = tk.getImage("menu.jpg");
        correctScreen = tk.getImage("correctScreen.jpg");
        loseScreen = tk.getImage("loseScreen.jpg");
        moneyWon = tk.getImage("moneyWon.jpg");
        millionaire = tk.getImage("millionaire.jpg");
        finalAnswer = tk.getImage("finalAnswer.jpg");
        blank = tk.getImage("blank.jpg");
        lifelines = tk.getImage("lifelines.jpg");

        // Create Frame and Panel to display graphics in
        panel = new WWTBAM(); /*****MUST CALL THIS CLASS (ie same as filename) ****/

        panel.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));  // set size of application window
        frame = new JFrame ("Who Wants to be a Millionaire?");               // set title of window
        frame.add (panel);

        // add a key input listener (defined below) to our canvas so we can respond to key pressed
        frame.addKeyListener(new KeyInputHandler());
        
        // exits window if close button pressed
        frame.addWindowListener(new ExitListener());
  
  
        // request the focus so key events come to the frame
        frame.requestFocus();
        frame.pack();
        frame.setVisible(true);
  
    } // main

   /*
   * paintComponent gets called whenever panel.repaint() is
   * called or when frame.pack()/frame.show() is called. It paints
   * to the screen.  Since we want to paint different things
   * depending on what stage of the game we are in, a variable
   * "gamestage" will keep track of this.
   */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);   // calls the paintComponent method of JPanel to display the background

        // display welcome screen
        if (gameStage == WELCOME_SCREEN) {
            g.drawImage(logo, -140, -200, this);

        // display game
        } else if (gameStage == MENU) {
            g.drawImage(menu, 0, 0, this);
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            g.drawString("1) Display Instructions",180,220);
            g.drawString("2) Begin Game",180,295);
            g.drawString("3) Display Records",180,370);
            g.drawString("4) Change Player Name", 180, 460);
            g.drawString("5) Exit", 180, 535);

        // display instructions
        } else if (gameStage == INSTRUCTIONS) {
            g.drawImage(blank, 0, 0, this);
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 36));
            g.drawString("Instructions!",300,50);
            g.setFont(new Font("SansSerif", Font.BOLD, 18));
            g.drawString("                                                                                  ", 25, 100);
            g.drawString("This is a simple question and answer game with 15 questions that get harder as you", 25, 100);
            g.drawString("progess. Each question has 4 possible answers. On each question, you have the     ", 25, 130);
            g.drawString("option to use one of your 3 lifelines; 50/50, which gets rid of 2 choices, Double ", 25, 160);
            g.drawString("Dip, which allows you to choose 2 answers, and Switch the Question, which will get", 25, 190);
            g.drawString("rid of the current question and replace it with a new one. Each lifeline can only ", 25, 220);
            g.drawString("be used once, and only one lifeline can be used per question.                     ", 25, 250);
            g.drawString("                                                                                  ", 25, 280);
            g.drawString("As you progress through the game, each question will be worth more and more money.", 25, 310);
            g.drawString("The $1,000 prize and the $32,000 prize are garunteed; if you get a question wrong ", 25, 340);
            g.drawString("above these levels, you will leave with the last garunteed amount. For example, if", 25, 370);
            g.drawString("you get the $4,000 question wrong, you will leave with $1,000. After viewing any  ", 25, 400);
            g.drawString("question, you have the option to leave with the amount you have currently won.    ", 25, 430);
            g.drawString("                                                                                  ", 25, 460);
            g.drawString("To select an answer, hit the key that matches the number in front of that option. ", 25, 490);
            g.drawString("If no specific number is given, hitting any key on the keyboard will advance you  ", 25, 520);
            g.drawString("to the next screen. Hit '5' to use 50/50, '6' to use Double Dip, and '7' to use   ", 25, 550);
            g.drawString("Switch the Question. Good luck!                                                   ", 25, 580);

        // display records
        } else if (gameStage == RECORDS) {
            g.drawImage(blank, 0, 0, this);
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 36));
            g.drawString("Records",320,50);
            g.setFont(new Font("SansSerif", Font.BOLD, 18));
            g.drawString("Name", 25, 110);
            g.drawString("Times Played", 125, 140);
            g.drawString("Times Won", 225, 110);
            g.drawString("Times Lost", 325, 140);
            g.drawString("Highest Amount Won", 425, 110);
            g.drawString("Total Money Won", 525, 140);
            g.drawString("Next Jackpot", 660, 110);
            g.drawString("-----------------------------------------------------------------------------------------------------------------------------", 25, 155);
            yCoord = 180;

            g.drawString(name, 25, yCoord);
            g.drawString((timesPlayed + ""), 145, yCoord);
            g.drawString((timesWon + ""), 245, yCoord);
            g.drawString((timesLost + ""), 345, yCoord);
            g.drawString("$" + (getStringTotal(maxWon) + ""), 425, yCoord);
            g.drawString("$" + (getStringTotal(totalWon) + ""), 550, yCoord);
            g.drawString("$" + (getStringTotal(nextJackpot) + ""), 675, yCoord);

            g.setFont(new Font("SansSerif", Font.BOLD, 12));

            yCoord = 210;

            for (int i = 0; (i + (currentScreen * 20)) < length; i++) {
                g.drawString(records[i + (currentScreen * 20)][0], 025, yCoord);
                g.drawString(records[i + (currentScreen * 20)][1], 145, yCoord);
                g.drawString(records[i + (currentScreen * 20)][2], 245, yCoord);
                g.drawString(records[i + (currentScreen * 20)][3], 345, yCoord);
                g.drawString("$" + getDisplayString(records[i + (currentScreen * 20)][4]), 425, yCoord);
                g.drawString("$" + getDisplayString(records[i + (currentScreen * 20)][5]), 550, yCoord);
                g.drawString("$" + getDisplayString(records[i + (currentScreen * 20)][6]), 675, yCoord);
                yCoord += 20;
            } // for i

        // display game
        } else if (gameStage == PLAY) {

            // set font and colour
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 16));

            g.drawImage(background, 0, 0, this);

            // show stage one of play
            if (playStage == QUESTION) {
                g.drawString(name, 50, 40);
                g.drawString("$" + getStringTotal(total), 50, 87);

                currentQuestion = splitQuestion();
                if (currentQuestion.length == 1) {
                    g.drawString(currentQuestion[0], 100, 245);
                } else {
                    g.drawString(currentQuestion[0], 100, 235);
                    g.drawString(currentQuestion[1], 100, 255);
                } // else

                g.setFont(new Font("SansSerif", Font.BOLD, 20));                    
                if (usingFF == false) {
                    g.drawString("1) " + (questions[diffCounter][qCounter][1]), 100, 350);
                    g.drawString("2) " + (questions[diffCounter][qCounter][2]), 475, 350);
                    g.drawString("3) " + (questions[diffCounter][qCounter][3]), 100, 415);
                    g.drawString("4) " + (questions[diffCounter][qCounter][4]), 475, 415);
                } else {
                    switch (correctAnswer) {
                        case 1: g.drawString("1) " + (questions[diffCounter][qCounter][1]), 100, 350); break;
                        case 2: g.drawString("2) " + (questions[diffCounter][qCounter][2]), 475, 350); break;
                        case 3: g.drawString("3) " + (questions[diffCounter][qCounter][3]), 100, 415); break;
                        case 4: g.drawString("4) " + (questions[diffCounter][qCounter][4]), 475, 415); break;
                    } // switch

                    switch (randomAnswer) {
                        case 1: g.drawString("1) " + (questions[diffCounter][qCounter][1]), 100, 350); break;
                        case 2: g.drawString("2) " + (questions[diffCounter][qCounter][2]), 475, 350); break;
                        case 3: g.drawString("3) " + (questions[diffCounter][qCounter][3]), 100, 415); break;
                        case 4: g.drawString("4) " + (questions[diffCounter][qCounter][4]), 475, 415); break;
                    } // switch
                } // else
                g.setFont(new Font("SansSerif", Font.BOLD, 16));
                if (lifeline1 == true && usingFF == false) {g.drawString("50/50", 460, 52);}
                if (lifeline2 == true && usingDD == false) {g.drawString("2x Dip", 540, 52);}
                if (lifeline3 == true) {g.drawString("STQ", 630, 52);}
                g.drawString("Leave", 708, 52);

            // show stage two of play
            } else if (playStage == FINAL_ANSWER) {
                g.drawImage(finalAnswer, 0, 0, this);
                if (currentQuestion.length == 1) {
                    g.drawString(currentQuestion[0], 100, 245);
                } else {
                    g.drawString(currentQuestion[0], 100, 235);
                    g.drawString(currentQuestion[1], 100, 255);
                } // else
                g.drawString("1) Yes", 180, 413);
                g.drawString("2) No", 560, 413);
                if (usingDD == true && DDCount == 1) {
                    g.drawString("You chose: \"" + playOutput2 + "\". Is this your final answer?", 100, 140);
                    g.drawString("Your other answer: \"" + playOutput + "\".", 300, 160);
                } else {
                    g.drawString("You chose: \"" + playOutput + "\". Is this your final answer?", 100, 350);
                } // else

            // correct
            } else if (playStage == CORRECT) {
                g.drawImage(correctScreen, 0, 0, this);

            // lose
            } else if (playStage == LOSE) {
                g.drawImage(loseScreen, 0, 0, this);

            // millionaire
            } else if (playStage == MILLIONAIRE) {
                g.drawImage(millionaire, 0, 0, this);
                g.setFont(new Font("SansSerif", Font.BOLD, 32));
                g.drawString("$" + getStringTotal(lastTotal), 300, 250);

            // money won
            } else if (playStage == MONEY_WON) {
                g.drawImage(moneyWon, 0, 0, this);
                if (lastStage == LOSE) {
                    g.setColor(Color.white);
                    g.setFont(new Font("SansSerif", Font.BOLD, 32));
                    g.drawString("$" + getStringTotal(garTotal), 300, 250);
                } else {
                    g.setColor(Color.white);
                    g.setFont(new Font("SansSerif", Font.BOLD, 32));
                    g.drawString("$" + getStringTotal(lastTotal), 330, 250);
                } // if

            // 50/50
            } else if (playStage == FIFTY_FIFTY) {
                g.drawImage(lifelines, 0, 0, this);
                g.setColor(Color.white);
                g.setFont(new Font("SansSerif", Font.BOLD, 18));
                g.drawString("You've choosen to use Fifty Fifty!", 100, 235);
                g.drawString("2 of the incorrect answers will be eliminated.", 100, 255);

            // double dip
            } else if (playStage == DOUBLE_DIP) {
                g.drawImage(lifelines, 0, 0, this);
                g.setColor(Color.white);
                g.setFont(new Font("SansSerif", Font.BOLD, 18));
                g.drawString("You've choosen to use Double Dip!", 100, 235);
                g.drawString("You will now be allowed to choose 2 answers.", 100, 255);

            // switch the question
            } else if (playStage == SWITCH_THE_QUESTION) {
                g.drawImage(lifelines, 0, 0, this);
                g.setColor(Color.white);
                g.setFont(new Font("SansSerif", Font.BOLD, 18));
                g.drawString("You've choosen to use Switch the Question!", 100, 235);
                g.drawString("The question will now be switched.", 100, 255);

            // display an error message
            } else if (playStage == ERROR_SCREEN) {
                g.drawImage(lifelines, 0, 0, this);
                g.setColor(Color.red);
                g.setFont(new Font("SansSerif", Font.BOLD, 18));
                g.drawString(errorMessage, 100, 245);

            } // if
        } // if
    } // paintComponent

    /* A class to handle keyboard input from the user.
    * Implemented as a inner class because it is not
    * needed outside the WWTBAM class.
    */
    private static class KeyInputHandler extends KeyAdapter {
        public void keyReleased(KeyEvent e) {
            // quit if the user presses "escape"
            if (e.getKeyChar() == 27) {
                if (gameStage == PLAY) {timesLost++;}
                writeRecords();
                System.exit(0);

            // responds to welcome screen
            } else if (gameStage == WELCOME_SCREEN) {
                getRecords();
                getPlayerName();
                changePlayer();
                if (nextJackpot == 0) {
                    nextJackpot = 1000000;
                } // if
                showMenu();
                panel.repaint();

            } else if (waitingForKeyPress == true && gameStage != PLAY && gameStage != RECORDS) {

                // respond to menu selection
                switch (e.getKeyChar()) {
                    case 49: showInstructions(); break; // Key "1" pressed
                    case 50: playGame();  break;        // Key "2" pressed
                    case 51: showRecords(); break;      // Key "3" pressed
                    case 52:                            // Key "4" pressed
                        getPlayerName();
                        changePlayer(); 
                        break;
                    case 53:                            // Key "5" pressed
                        writeRecords();
                        System.exit(0);
                } // switch

            } else if (waitingForKeyPress == true && gameStage == RECORDS) {
                if (e.getKeyChar() >= 0 && currentScreen < totalScreens - 1) {
                    currentScreen++;
                    panel.repaint();
                } else {
                    currentScreen = 0;
                    showMenu();
                }// else

            // respond to choice
            } else if (waitingForKeyPress == true && gameStage == PLAY && playStage == QUESTION) {
                switch (e.getKeyChar()) {
                    case 49: // Key "1" pressed
                        if ((usingFF == true && (correctAnswer == 1 || randomAnswer == 1)) || usingFF == false) {
                            showFinalAnswer(1, questions[diffCounter][qCounter][1]);
                        } // if
                        break;
                    case 50: // Key "2" pressed
                        if ((usingFF == true && (correctAnswer == 2 || randomAnswer == 2)) || usingFF == false) {
                            showFinalAnswer(2, questions[diffCounter][qCounter][2]);
                        } // if
                        break;
                    case 51: // Key "3" pressed
                        if ((usingFF == true && (correctAnswer == 3 || randomAnswer == 3)) || usingFF == false) {
                            showFinalAnswer(3, questions[diffCounter][qCounter][3]);
                        } // if
                        break;
                    case 52: // Key "4" pressed
                        if ((usingFF == true && (correctAnswer == 4 || randomAnswer == 4)) || usingFF == false) {
                            showFinalAnswer(4, questions[diffCounter][qCounter][4]);
                        } // if
                        break;
                    case 53: // Key "5" pressed (50/50)
                        if (lifeline1 == true && lifelineUsed == false) {
                            showFinalAnswer(5, "50/50");
                        } else if (lifelineUsed == true) {
                            showError("You have already used a lifeline on this question.");
                        } else if (lifeline1 == false) {
                            showError("You have already used 50/50");
                        } // if
                        break;
                    case 54: // Key "6" pressed (Double Dip)
                        if (lifeline2 == true && lifelineUsed == false) {
                            showFinalAnswer(6, "Double Dip");
                        } else if (lifelineUsed == true) {
                            showError("You have already used a lifeline on this question.");
                        } else if (lifeline2 == false) {
                            showError("You have already used Double Dip");
                        } // if
                        break;
                    case 55: // Key "7" pressed (Switch the Question)
                        if (lifeline3 == true && lifelineUsed == false) {
                            showFinalAnswer(7, "Switch the Question");
                        } else if (lifelineUsed == true) {
                            showError("You have already used a lifeline on this question.");
                        } else if (lifeline3 == false) {
                            showError("You have already used Switch the Question");
                        } // if
                        break;
                    case 56:  // Key "8" pressed (Leave with winnings)
                        showFinalAnswer(8, "Leave now");
                        break;
                } // switch

            // respond to final answer
            } else if (waitingForKeyPress == true && gameStage == PLAY && playStage == FINAL_ANSWER) {
                switch (e.getKeyChar()) {
                    case 49:
                        if (usingDD == true) {DDCount++;}
                        if (DDCount == 1) {
                            playStage = QUESTION; 
                            panel.repaint();
                        } else {displayResult();} 
                        break;
                    case 50:
                        if (DDCount == 0) {playOutput = "";} 
                        playStage = QUESTION; 
                        panel.repaint(); 
                        break;
                } // switch

            // respond to correct
            } else if (waitingForKeyPress == true && gameStage == PLAY && playStage == CORRECT) {
                if (diffCounter == (LEVELS - 1) && qCounter == (QPL - 1)) {
                    playStage = MILLIONAIRE;
                    timesWon++;
                    getTotal();
                    lastTotal = total;
                    panel.repaint();
                } else {
                    if (qCounter == (QPL - 1)) {
                        qCounter = 0; 
                        diffCounter++;
                    } else {qCounter++;}
                    playStage = QUESTION;
                    getCA();
                    lastTotal = total;
                    getTotal();
                    lifelineUsed = false;
                    if (usingFF == true) {
                        usingFF = false; 
                        lifeline1 = false;
                    } // if
                    panel.repaint();
                } // if

            // respond to lose
            } else if (waitingForKeyPress == true && gameStage == PLAY && playStage == LOSE) {
                lastStage = playStage;
                playStage = MONEY_WON;
                panel.repaint();

            // respond to win
            } else if (waitingForKeyPress == true && gameStage == PLAY && playStage == MILLIONAIRE) {
                totalWon += lastTotal;
                maxWon = getMaxWon();
                nextJackpot = 1000000;
                writeRecords();
                showMenu();

            // respond to lifeline
            } else if (waitingForKeyPress == true && gameStage == PLAY && (playStage == FIFTY_FIFTY || playStage == DOUBLE_DIP || playStage == SWITCH_THE_QUESTION)) {
                playStage = QUESTION;
                lifelineUsed = true;
                panel.repaint();

            // respond to error screen
            } else if (waitingForKeyPress == true && gameStage == PLAY && playStage == ERROR_SCREEN) {
                playStage = lastStage;
                panel.repaint();
            } else {
                showMenu();
            } // else
        } // keyTyped
    } // KeyInputHandler class

    // Shuts program down when close button pressed
    private static class ExitListener extends WindowAdapter {
        public void windowClosing(WindowEvent event) {
            if (gameStage == PLAY) {timesLost++;}
            writeRecords();
            System.exit(0);
        } // windowClosing
    } // ExitListener

    // shows menu
    private static void showMenu() {
        gameStage = MENU;
        waitingForKeyPress = true;
        panel.repaint();
    } // showMenu

    // show instructions
    private static void showInstructions() {
        gameStage = INSTRUCTIONS;
        waitingForKeyPress = false;
        panel.repaint();
    } // showInstructions
    
    // show records
    private static void showRecords() {
        gameStage = RECORDS;
        waitingForKeyPress = true;
        totalScreens = getTotalScreens();
        panel.repaint();
    } // showRecord

    // sets the playStage appropriately
    public static void displayResult() {
        if (DDCount == 2) {
            if (outcome == 1 || outcome2 == 1) {
                playStage = CORRECT;
                lifeline2 = false;
                usingDD = false;
                DDCount = 0;
                panel.repaint();
            } else {
                playStage = LOSE;
                timesLost++;
                totalWon += garTotal;
                nextJackpot += 50000;
                maxWon = getMaxWon();
                writeRecords();
                panel.repaint();
            } // else
        } else {
            switch (outcome) {
                case 0: 
                    playStage = LOSE;
                    totalWon += garTotal;
                    nextJackpot += 50000;
                    maxWon = getMaxWon();
                    timesLost++;
                    writeRecords();
                    panel.repaint();
                    break;
                case 1: 
                    playStage = CORRECT; 
                    panel.repaint();
                    break;
                case 2: 
                    playStage = FIFTY_FIFTY; 
                    fiftyFifty(); 
                    panel.repaint();  
                    break;
                case 3: 
                    playStage = DOUBLE_DIP; 
                    usingDD = true; 
                    panel.repaint(); 
                    break;
                case 4: 
                    playStage = SWITCH_THE_QUESTION; 
                    switchTheQuestion(); 
                    panel.repaint(); 
                    break;
                case 5: 
                    playStage = MONEY_WON;
                    timesWon++;
                    totalWon += lastTotal;
                    maxWon = getMaxWon();
                    writeRecords();
                    panel.repaint(); 
                    break;
            } // switch
        } // else
    } // displayResult

    // shows an error screen
    public static void showError (String s) {
        lastStage = playStage;
        playStage = ERROR_SCREEN;
        errorMessage = s;
        panel.repaint();
    } // showError
    
    // shows Final Answer
    public static void showFinalAnswer (int x, String s) {
        if (DDCount == 1) {outcome2 = processAnswer(x, correctAnswer);} 
        else {outcome = processAnswer(x, correctAnswer);}
        if (DDCount == 1) {playOutput2 = s;} 
        else {playOutput = s;}
        if (((diffCounter < 1 && qCounter < 3) || (diffCounter < 2 && qCounter < 2)) && usingDD == true && DDCount != 2) {
            playStage = QUESTION;
        } else if ((diffCounter < 1 && qCounter < 3) || (diffCounter < 2 && qCounter < 2)) {
            displayResult();
        } else {
            playStage = FINAL_ANSWER;
        } // else
        if (((diffCounter < 1 && qCounter < 3) || (diffCounter < 2 && qCounter < 2)) && usingDD == true) {DDCount++;}
        panel.repaint();
    } // showFinalAnswer

    // play the game
    private static void playGame() {

        // reset variables
        lifeline1 = true;
        lifeline2 = true;
        lifeline3 = true;
        lifelineUsed = false;
        usingFF = false;
        usingDD = false;
        playOutput = "";
        correctAnswer = 0;
        outcome = 0;
        diffCounter = 0;
        qCounter = 0;
        total = 0;
        lastTotal = 0;
        garTotal = 0;
        timesPlayed++;

        // reads in questions
        getQuestions();

        // display this stage of game
        gameStage = PLAY;
        playStage = QUESTION;
        waitingForKeyPress = true;

        getCA();
        getTotal();

        panel.repaint();
    } // playGame

    // takes the users input and determines if they are correct, incorrect, chose a lifeline, or left the game
    public static int processAnswer(int answer, int correctAnswer) {
        switch (answer) {
            case 1:
            case 2:
            case 3:
            case 4: if (answer == correctAnswer) {return 1;} else {return 0;}
            case 5: return 2; // 50/50
            case 6: return 3; // Double Dip
            case 7: return 4; // Switch the Question
            case 8: return 5; // Leave
            default: return -1;
        } //switch
    } // processAnswer

    // gets rid of 2 possible answers
    public static void fiftyFifty() {

        // gives randomNumber a random value
        randomAnswer = (int)(Math.random() * 4) + 1;

        // gives correct answer the index of the correct answer
        correctAnswer = Integer.parseInt(questions[diffCounter][qCounter][5]);

        // if correctAnswer is the same as randomNumber, it gives randomNumber a new value
        while (correctAnswer == randomAnswer) {
            randomAnswer = (int)(Math.random() * 4) + 1;
        } // while
        
        usingFF = true;
    } // fiftyFifty

    // switches the current question to a new one
    public static void switchTheQuestion() {

        // sets new question and correct answer to a random question in the file depending on current difficulty level
        int randomNumber = (int)(Math.random() * QPF);
        for (int i = 0; i < 6; i++) {
            newQuestion[i] = allQuestions[diffCounter][randomNumber][i];
        } // for

        // checks to make sure that the new question has not already been asked
        while (newQuestion[0].equals(questions[diffCounter][0][0]) || newQuestion[0].equals(questions[diffCounter][1][0]) || newQuestion[0].equals(questions[diffCounter][2][0])) {
            randomNumber = (int)(Math.random() * QPF);
            for (int j = 0; j < 6; j++) {
                newQuestion[j] = allQuestions[diffCounter][randomNumber][j];
            } // for
        } // while

        // assigns the new question to the questions array
        for (int i = 0; i < 6; i++) {
            questions[diffCounter][qCounter][i] = newQuestion[i];
        } // for

        getCA();
        lifeline3 = false;
    } // switchTheQuestion
    
    // gets player records from a file called "records.txt"
    public static void getRecords () {
        try {
            BufferedReader in = new BufferedReader(new FileReader("records.txt"));
            in.mark(Short.MAX_VALUE);

            for (int i = 0; i < length; i++) {
                for (int j = 0; j < 7; j++) {
                    records[i][j] = in.readLine();
                } // for
            } // for i

            in.close();
        } catch (Exception e) {
            System.out.println("GET RECORDS ERROR");
            System.out.println(e);
        } // catch
        getPlayerData();
    } // getRecords
    
    // writes the updated records to a file called "records.txt"
    public static void writeRecords () {
        
        // takes the new information and puts it back into the records array
        for (int i = 0; i < length; i++) {
            if (name.equalsIgnoreCase(records[i][0])) {
                records[i][1] = timesPlayed + "";
                records[i][2] = timesWon + "";
                records[i][3] = timesLost + "";
                records[i][4] = maxWon + "";
                records[i][5] = totalWon + "";
                records[i][6] = nextJackpot + "";
            } // if
        } // for i

        // writes to file
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("records.txt"));

            for (int i = 0; i < length; i++) {
                for (int j = 0; j < 7; j++) {
                    out.write(records[i][j]);
                    out.newLine();
                } // for j
            } // for i

            out.close();
        } catch (Exception e) {
            System.out.println("WRITE RECORDS ERROR");
            System.out.println(e);
        } // catch
    } // writeRecords
    
    // gets the players data from the array records
    public static void getPlayerData () {
        for (int counter = 0; counter < length; counter++) {
            if (name.equalsIgnoreCase(records[counter][0])) {
                try {
                    timesPlayed = Integer.parseInt(records[counter][1]);
                    timesWon = Integer.parseInt(records[counter][2]);
                    timesLost = Integer.parseInt(records[counter][3]);
                    maxWon = Integer.parseInt(records[counter][4]);
                    totalWon = Integer.parseInt(records[counter][5]);
                    nextJackpot = Integer.parseInt(records[counter][6]);
                } catch (Exception e) {
                    System.out.println("PLAYER DATA ERROR");
                    System.out.println(e);
                } // catch
            } // if
        } // for i
    } // getPlayerData

    // changes the current player
    public static void changePlayer () {
        for (int i = 0; i < length; i++) {

            // if the player already exists
            if (name.equalsIgnoreCase(records[i][0])) {
                getRecords();
                getPlayerData();
                if (!name.equals(records[i][0])) {
                    name = records[i][0];
                } // if
                break;
                
            // if the player doesn't exist
            } else if (i == length - 1) {
                length++;
                String [][] newRecords = new String [length][7];

                // backs up records into a new array
                for (int j = 0; j < length - 1; j++) {
                    for (int k = 0; k < 7; k++) {
                        newRecords[j][k] = records[j][k];
                    } // for k
                } // for j

                records = new String [length][7];
                
                // puts the data back into the records array that is now 1 larger
                for (int j = 0; j < length - 1; j++) {
                    for (int k = 0; k < 7; k++) {
                        records[j][k] = newRecords[j][k];
                    } // for k
                } // for j

                // makes the new player's data the defaults
                records[length - 1][0] = name;
                records[length - 1][1] = "0";
                records[length - 1][2] = "0";
                records[length - 1][3] = "0";
                records[length - 1][4] = "0";
                records[length - 1][5] = "0";
                records[length - 1][6] = "1000000";

                // resets the player's data to the defaults
                timesPlayed = 0;
                timesWon = 0;
                timesLost = 0;
                maxWon = 0;
                totalWon = 0;
                nextJackpot = 1000000;
                break;
            } // else
        } // for i
    } // changePlayer

    // gets 15 random questions in 5 difficulty levels, ensuring the questions don't repeat
    public static void getQuestions() {
        String filename = "";                           // filename to read from
        int randomNumber = 0;                           // random number to get random question
        int q1 = 0;                                     // question 1 random number
        int q2 = 0;                                     // question 2 random number

        for (int i = 0; i < LEVELS; i++ ) {
            randomNumber = 0;
            q1 = 0;
            q2 = 0;

            // gives filename depending on diffculty level
            filename = "difficulty" + ((i + 1) + "") + ".txt";

            // opens files for reading
            try {

                // declares reader
                BufferedReader in = new BufferedReader(new FileReader(filename));
                in.mark(Short.MAX_VALUE);

                // makes allQuestions equal to all the questions in the file
                for (int j = 0; j < QPF; j++) {
                    for (int k = 0; k < 6; k++) {
                        allQuestions[i][j][k] = in.readLine();
                    } // for k
                } // for j

                // resets in
                in.reset();

                // sets questions to a random question within the file
                for (int j = 0; j < QPL; j++) {
                    do {
                        randomNumber = (int)(Math.random()*QPF);

                        if (randomNumber != q1 && randomNumber != q2) {
                            for (int k = 0; k < 6; k++) {
                                questions[i][j][k] = allQuestions[i][randomNumber][k];
                                if (j == 0) {
                                    q1 = randomNumber;
                                } else if (j == 1) {
                                    q2 = randomNumber;
                                } // if
                            } // for k
                            break;
                        } // if
                    } while (true);
                } // for j

            } catch (Exception e) {
                System.out.println("GET QUESTIONS ERROR");
                System.out.println(e);
            } // catch
        } // for i
    } // getQuestions
    
    // gets the correct answer for the question
    public static void getCA () {
        try {
            correctAnswer = Integer.parseInt(questions[diffCounter][qCounter][5]);
        } catch (Exception e) {
            System.out.println("GET CORRECT ANSWER ERROR");
            System.out.println(e);
        } // catch
    } // getCA
    
    // gets the new total
    public static void getTotal () {
        if (total == 1000 || total == 32000) {
            garTotal = total;
        } // if
        
        switch (total) {
            case 0:
            case 100:
            case 200: total += 100; break;
            case 300: total += 200; break;
            case 500:
            case 1000:
            case 2000:
            case 4000:
            case 8000:
            case 16000:
            case 32000: 
            case 125000:
            case 250000: total *= 2; break;
            case 500000: total = nextJackpot; break;
            case 64000: total = 125000; break;
        } // switch
    } // getTotal
    
    // gets the total as a string
    public static String getStringTotal (int x) {
        String s = "";

        switch (x) {
            case 0:
            case 100:
            case 200:
            case 300:
            case 500: s = (x + ""); break;
            case 1000: s = "1,000"; break;
            case 2000: s = "2,000"; break;
            case 4000: s = "4,000"; break;
            case 8000: s = "8,000"; break;
            case 16000: s = "16,000"; break;
            case 32000: s = "32,000"; break;
            case 64000: s = "64,000"; break;
            case 125000: s = "125,000"; break;
            case 250000: s = "250,000"; break;
            case 500000: s = "500,000"; break;
            case 1000000: s = "1,000,000"; break;
            default: s = getJackpotString(x);
        } // switch
        
        return s;
    } // getStringTotal

    // gets the String of an int that is larger than 1 million
    public static String getJackpotString (int x) {
        String s = x + "";
        char [] c = s.toCharArray();
        char [] cCopy = new char [(c.length + ((int)c.length / 3))];
        int j = c.length;
        int k = cCopy.length - 1;

        // adds in commas
        for (int i = 0; i < cCopy.length; i++) {
            if (i > 0 && i % 4 == 3) {
                cCopy[k] = ',';
            } else {
                cCopy[k] = c[j - 1];
                j--;
            } // else
            k--;
        } // for

        // if the String starts with a comma, remove it
        if (cCopy[0] == ',') {
            c = new char [cCopy.length - 1];
            for (int i = 0; i < c.length; i++) {
                c[i] = cCopy[i + 1];
            } // for
            s = new String (c);
        } else {
            s = new String (cCopy);
        } // else

        return s;
    } // getJackpotString

    // adds in commas to a String consisting of the chars 0-9
    public static String getDisplayString (String s) {
        int x = 0;

        try {
            x = Integer.parseInt(s);
        } catch (Exception e) {
            System.out.println("GET DISPLAY STRING ERROR");
            System.out.println(e);
        } // catch
        
        return getJackpotString(x);
    } // getDisplayString

    // gets the length of a file
    public static int getLength (String filename) {
        int length = 0;

        try {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            in.mark(Short.MAX_VALUE);

            // get length of file
            while (in.readLine() != null) {
                length++;
            } // while
            in.reset();

            in.close();
        } catch (Exception e) {
            System.out.println("GET LENGTH ERROR");
            System.out.println(e);
        } // catch
        
        return length;
    } // getLength

    // gets the players name and all the data related to the player
    public static void getPlayerName () {
        while (true) {
            try {
                name = JOptionPane.showInputDialog(panel, "Please enter your name:");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(panel, "What you entered was not valid input");
                continue;
            } // catch
    
            if (name == null || name.equals("")) {
                JOptionPane.showMessageDialog(panel, "What you entered was not valid input");
                continue;
            } else {
                break;
            } // else
        } // while
    } // getUserName
    
    // splits a question into an array that will display on multiple lines
    public static String[] splitQuestion () {
        String s = questions[diffCounter][qCounter][0];
        String [] a = s.split("~");

        return a;
    } // splitQuestion

    // returns the maximum amount that the player has won
    public static int getMaxWon () {
        return ((playStage == LOSE)? (maxWon > garTotal)? maxWon : garTotal : (maxWon > lastTotal)? maxWon : lastTotal);
    } // getMaxWon

    public static int getTotalScreens () {
      int x = 0;
      
      for (int i = 0; i < length; i++) {
        if (i % 20 == 0 && i != 0) {x++;}
      } // for
      
      return (x + 1);
    } // getTotalScreens
} // WWTBAM