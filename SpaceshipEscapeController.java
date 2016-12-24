import Acme.*;
import objectdraw.*;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.lang.InterruptedException;
import java.lang.Thread;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class SpaceshipEscapeController extends WindowController implements
ActionListener, MouseListener, MouseMotionListener {

  // Math constants
  private static final int HALF = 2;

  // Applet specifications
  private static final int APPLET_WIDTH = 1275;
  private static final int APPLET_HEIGHT = 730;

  // Spaceship specifications
  private Spaceship spaceship;
  private String spaceshipPic = "./Spaceship Images/Spaceship Up.png";
  private Location spaceshipInitialLocation;
  private static final int SPACESHIP_SIZE = 100;

  // Star specificaitons
  private Location starInitialLocation;
  private Direction starInitialDirection;
  private static final int STAR_SIZE = 10;
  private long checkpoint;
  private long checkpointDifference;
  private static final long ELAPSED_TIME = 5000;
  private RandomIntGenerator randomX;
  private RandomIntGenerator randomY;

  // Menu specifications
  private static boolean onMenu;
  private static final String TITLE_FONT = "Zero Velocity BRK";
  private static final String MENU_FONT = "Press Start 2P";
  private static final int TITLE_FONT_SIZE = 70;
  private static final int MENU_FONT_SIZE = 30;
  private static final Color TITLE_FONT_COLOR = Color.BLACK;
  private static final Color MENU_FONT_COLOR = Color.WHITE;
  private static final Color MENU_FONT_HOVER_COLOR = Color.BLUE;
  private static final int TITLE_TOP_GAP = 150;
  private static final int TITLE_TEXT_GAP = 180;
  private static final int MENU_TEXT_GAP = 20;
  private Text title;
  private Text newGameMenu;
  private Text highscoresMenu;
  private Text helpMenu;
  private Text quitMenu;
  private int canvasMiddleX;
  private static final int NEW_GAME = 0;
  private static final int HIGH_SCORES = 1;
  private static final int HELP = 2;
  private static final int QUIT = 3;

  // Sub-menu stuffs
  private JPanel southPanel;
  private JButton restartButton;
  private JButton pauseButton;
  private JButton helpButton;
  private JButton quitButton;

  // Controller
  private static boolean running;
  private static boolean gameOver;
  private static final String CONTROLLER_FONT = "ArcadeClassic";
  private static final int CONTROLLER_MIDDLE_SIZE = 70;
  private static final Color CONTROLLER_COLOR = Color.WHITE;
  private Text pauseText;
  private static Text gameOverText;

  public SpaceshipEscapeController() {

    System.out.println("Hello");
  }

  public void begin() {

    setUpController();

    setUpMenu();

    setUpSubMenu();

    pauseText.moveTo((APPLET_WIDTH - pauseText.getWidth()) / HALF,
    (APPLET_HEIGHT - pauseText.getHeight()) / HALF - southPanel.getHeight());

    gameOverText.moveTo((APPLET_WIDTH - gameOverText.getWidth()) / HALF,
    (APPLET_HEIGHT - gameOverText.getHeight()) / HALF - southPanel.getHeight());

    runGame();
  }

  private void setUpController() {

    // Make background
    VisibleImage bg = null;

    try {

      bg = new VisibleImage(ImageIO.read(
      new File("./Spaceship Images/Galaxy4.png")), 0, 0, canvas);
    }

    catch (IOException e) {

      //nothing
    }

    onMenu = true;
    gameOver = false;

    spaceshipInitialLocation = new Location((APPLET_WIDTH - SPACESHIP_SIZE) /
    HALF, (APPLET_HEIGHT - SPACESHIP_SIZE) / HALF);
    spaceship = new Spaceship(spaceshipPic, SPACESHIP_SIZE,
    spaceshipInitialLocation, canvas);

    Spaceship.moving = false;

    canvas.requestFocusInWindow();
    canvas.addMouseListener(this);
    canvas.addMouseMotionListener(this);

    pauseText = new Text("Pause", 0, 0, canvas);
    pauseText.hide();
    pauseText.setFont(CONTROLLER_FONT);
    pauseText.setFontSize(CONTROLLER_MIDDLE_SIZE);
    pauseText.setColor(CONTROLLER_COLOR);

    gameOverText = new Text("Game Over", 0, 0, canvas);
    gameOverText.hide();
    gameOverText.setFont(CONTROLLER_FONT);
    gameOverText.setFontSize(CONTROLLER_MIDDLE_SIZE);
    gameOverText.setColor(CONTROLLER_COLOR);

  }

  private void setUpMenu() {

    // calculate the middle point of the applet to place the texts
    canvasMiddleX = APPLET_WIDTH / HALF;

    // Game title
    title = new Text("Spaceship Escape", 0, 0, canvas);
    title.hide();
    title.setFont(TITLE_FONT);
    title.setFontSize(TITLE_FONT_SIZE);
    title.setColor(TITLE_FONT_COLOR);
    title.moveTo(canvasMiddleX - title.getWidth() / HALF, TITLE_TOP_GAP);
    title.show();

    // new game selection text
    newGameMenu = new Text("New Game", 0, 0, canvas);
    newGameMenu.hide();
    newGameMenu.setFont(MENU_FONT);
    newGameMenu.setFontSize(MENU_FONT_SIZE);
    newGameMenu.setColor(MENU_FONT_COLOR);
    newGameMenu.moveTo(canvasMiddleX - newGameMenu.getWidth() / HALF,
    title.getY() + title.getHeight() + TITLE_TEXT_GAP);
    newGameMenu.show();

    // high scores selection text
    highscoresMenu = new Text("High Scores", 0, newGameMenu.getY() +
    MENU_FONT_SIZE + MENU_TEXT_GAP, canvas);
    highscoresMenu.hide();
    highscoresMenu.setFont(MENU_FONT);
    highscoresMenu.setFontSize(MENU_FONT_SIZE);
    highscoresMenu.setColor(MENU_FONT_COLOR);
    highscoresMenu.moveTo(canvasMiddleX - highscoresMenu.getWidth() / HALF,
    highscoresMenu.getY());
    highscoresMenu.show();

    // help selection text
    helpMenu = new Text("Help", 0, highscoresMenu.getY() + MENU_FONT_SIZE +
    MENU_TEXT_GAP, canvas);
    helpMenu.hide();
    helpMenu.setFont(MENU_FONT);
    helpMenu.setFontSize(MENU_FONT_SIZE);
    helpMenu.setColor(MENU_FONT_COLOR);
    helpMenu.moveTo(canvasMiddleX - helpMenu.getWidth() / HALF,
    helpMenu.getY());
    helpMenu.show();

    // quit selection text
    quitMenu = new Text("Quit Game", 0, helpMenu.getY() + MENU_FONT_SIZE +
    MENU_TEXT_GAP, canvas);
    quitMenu.hide();
    quitMenu.setFont(MENU_FONT);
    quitMenu.setFontSize(MENU_FONT_SIZE);
    quitMenu.setColor(MENU_FONT_COLOR);
    quitMenu.moveTo(canvasMiddleX - quitMenu.getWidth() / HALF,
    quitMenu.getY());
    quitMenu.show();
  }

  private void showMenu() {

    title.show();
    newGameMenu.show();
    helpMenu.show();
    quitMenu.show();
    southPanel.setVisible(false);

    // make stars disappear
  }

  private void hideMenu() {

    title.hide();
    newGameMenu.hide();
    highscoresMenu.hide();
    helpMenu.hide();
    quitMenu.hide();
  }

  private void menuTextNoHover() {

    newGameMenu.setColor(MENU_FONT_COLOR);
    highscoresMenu.setColor(MENU_FONT_COLOR);
    helpMenu.setColor(MENU_FONT_COLOR);
    quitMenu.setColor(MENU_FONT_COLOR);
  }

  private void setUpSubMenu() {

    // sub-menu during game
    southPanel = new JPanel();

    // don't show yet til game starts
    southPanel.setVisible(false);

    // restart button
    restartButton = new JButton("Restart");
    restartButton.addActionListener(this);
    southPanel.add(restartButton);

    // pause button
    pauseButton = new JButton("Pause");
    pauseButton.addActionListener(this);
    southPanel.add(pauseButton);

    // help button
    helpButton = new JButton("Help");
    helpButton.addActionListener(this);
    southPanel.add(helpButton);

    // quit button
    quitButton = new JButton("Quit");
    quitButton.addActionListener(this);
    southPanel.add(quitButton);

    // southPanel transparent background
    southPanel.setBackground(Color.BLACK);
    this.add(southPanel, BorderLayout.SOUTH);

    // validate buttons
    this.validate();
  }

  public void makeStar() {

    starInitialLocation = chooseLocation();
    starInitialDirection = chooseDirection();

    new Star(APPLET_WIDTH, APPLET_HEIGHT, STAR_SIZE, starInitialLocation,
    starInitialDirection, spaceship, SPACESHIP_SIZE, canvas);
  }

  public static void isHit() {

    running = false;
    Star.moving = false;
    Spaceship.moving = false;
    gameOver = true;

    // pass this to star
    gameOverText.show();
  }

  private Location chooseLocation() {

    int xLocation = randomX.nextValue();
    int yLocation = 0;

    if (xLocation >= 0 && xLocation < APPLET_WIDTH) {

      randomY = new RandomIntGenerator(0, 1);

      if (randomY.nextValue() == 0) {

        yLocation = -STAR_SIZE;
      }

      else {

        yLocation = APPLET_HEIGHT - southPanel.getHeight();
      }

      randomY = new RandomIntGenerator(-STAR_SIZE, APPLET_HEIGHT -
      southPanel.getHeight());
    }

    else {

      yLocation = randomY.nextValue();
    }

    return new Location(xLocation, yLocation);
  }


  private Direction chooseDirection() {

    if (starInitialLocation.getX() == spaceshipInitialLocation.getX()) {

      if (starInitialLocation.getY() <= spaceshipInitialLocation.getY()) {

        return Direction.DOWN;
      }

      else {

        return Direction.UP;
      }
    }

    if (starInitialLocation.getY() == spaceshipInitialLocation.getY()) {

      if (starInitialLocation.getX() <= spaceshipInitialLocation.getX()) {

        return Direction.RIGHT;
      }

      else {

        return Direction.LEFT;
      }
    }

    if (starInitialLocation.getX() < spaceshipInitialLocation.getX() &&
    starInitialLocation.getY() < spaceshipInitialLocation.getY()) {

      return Direction.SE;
    }

    if (starInitialLocation.getX() > spaceshipInitialLocation.getX() &&
    starInitialLocation.getY() < spaceshipInitialLocation.getY()) {

      return Direction.SW;
    }

    if (starInitialLocation.getX() > spaceshipInitialLocation.getX() &&
    starInitialLocation.getY() > spaceshipInitialLocation.getY()) {

      return Direction.NW;
    }

    else {

      return Direction.NE;
    }
  }

  private int checkMousePosition(MouseEvent evt) {

    int mouseX = evt.getX();
    int mouseY = evt.getY();

    if (mouseX >= newGameMenu.getX() && mouseX <= newGameMenu.getX() +
    newGameMenu.getWidth() && mouseY >= newGameMenu.getY() && mouseY <=
    newGameMenu.getY() + newGameMenu.getHeight()) {

      return NEW_GAME;
    }

    else if (mouseX >= highscoresMenu.getX() && mouseX <= highscoresMenu.getX()
    + highscoresMenu.getWidth() && mouseY >= highscoresMenu.getY() && mouseY
    <= highscoresMenu.getY() + highscoresMenu.getHeight()) {

      return HIGH_SCORES;
    }

    else if (mouseX >= helpMenu.getX() && mouseX <= helpMenu.getX() +
    helpMenu.getWidth() && mouseY >= helpMenu.getY() && mouseY <=
    helpMenu.getY() + helpMenu.getHeight()) {

      return HELP;
    }

    else if (mouseX >= quitMenu.getX() && mouseX <= quitMenu.getX() +
    newGameMenu.getWidth() && mouseY >= quitMenu.getY() && mouseY <=
    quitMenu.getY() + quitMenu.getHeight()) {

      return QUIT;
    }

    // no menu selection
    else {

      return -1;
    }
  }

  public void actionPerformed(ActionEvent evt) {

    Object event = evt.getSource();

    if (event == restartButton) {

      gameOverText.hide();

      spaceship.initial();
      Star.clear();

      System.gc();

      running = true;
      gameOver = false;
      Star.moving = true;

      // subtract elapsed time so it can star making star when run
      checkpoint = System.currentTimeMillis() - ELAPSED_TIME;
    }

    else if (event == pauseButton) {

      if (running && !gameOver) {

        // make it stop
        Spaceship.moving = false;
        Star.moving = false;
        running = false;

        // change thread sleep time in runGame()

        checkpointDifference = System.currentTimeMillis() - checkpoint;

        pauseText.show();
      }

      else if (!running && !gameOver) {

        Spaceship.moving = true;
        Star.moving = true;
        running = true;

        canvas.requestFocusInWindow();

        // obtain the before pause checkpoint
        checkpoint = System.currentTimeMillis() - checkpointDifference;

        pauseText.hide();
      }
    }
  }

  public void mouseClicked(MouseEvent evt) {

    if (onMenu) {

      switch (checkMousePosition(evt)) {

        case NEW_GAME:

          onMenu = false;
          running = true;
          Spaceship.moving = true;
          Star.moving = true;
          hideMenu();
          southPanel.setVisible(true);
          break;

        case HIGH_SCORES:

          highscoresMenu.setColor(MENU_FONT_HOVER_COLOR);
          break;

        case HELP:

          helpMenu.setColor(MENU_FONT_HOVER_COLOR);
          break;

        case QUIT:

          System.exit(0);
        }
    }
  }

  public void mouseEntered(MouseEvent evt) {

    System.out.println("enter!" + evt.getPoint());
  }

  public void mouseExited(MouseEvent evt) {

    System.out.println("exit!" + evt.getPoint());
  }

  public void mousePressed(MouseEvent evt) {

    System.out.println("press!");
  }

  public void mouseReleased(MouseEvent evt) {

    System.out.println("release!");
  }

  public void mouseDragged(MouseEvent evt) {

    System.out.println("drag!");
  }

  public void mouseMoved(MouseEvent evt) {

    if (onMenu) {

      menuTextNoHover();

      switch (checkMousePosition(evt)) {

        case NEW_GAME:

          newGameMenu.setColor(MENU_FONT_HOVER_COLOR);
          break;

        case HIGH_SCORES:

          highscoresMenu.setColor(MENU_FONT_HOVER_COLOR);
          break;

        case HELP:

          helpMenu.setColor(MENU_FONT_HOVER_COLOR);
          break;

        case QUIT:

          quitMenu.setColor(MENU_FONT_HOVER_COLOR);
          break;
      }
    }
  }

  private void runGame() {

    randomX = new RandomIntGenerator(-STAR_SIZE, APPLET_WIDTH);
    randomY = new RandomIntGenerator(-STAR_SIZE, APPLET_HEIGHT -
    southPanel.getHeight());

    // subtract elapsed time so it can star making star when run
    checkpoint = System.currentTimeMillis() - ELAPSED_TIME;

    while (true) {

      while (running) {

        if (System.currentTimeMillis() - checkpoint > ELAPSED_TIME &&
        Star.moving) {

          makeStar();

          checkpoint = System.currentTimeMillis();
        }

        try {

          Thread.sleep(ELAPSED_TIME);
        }

        catch (InterruptedException e) {

          // do nothing
        }
      }

      if (!running) {

        try {

          Thread.sleep(1000);
        }

        catch (InterruptedException e) {

          // do nothing
        }
      }
    }
  }

  public static void main (String [] args) {

    new Acme.MainFrame(new SpaceshipEscapeController(), args, APPLET_WIDTH,
    APPLET_HEIGHT);
  }
}
