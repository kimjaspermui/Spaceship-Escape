import objectdraw.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.lang.Math;
import java.awt.Color;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.lang.InterruptedException;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Spaceship extends ActiveObject implements KeyListener {

  // constants plus degrees
  private static final int DELAY = 50;
  private static final int HALF = 2;
  private static final int FORTY_FIVE_DEGREES = 45;
  private static final int NINETY_DEGREES = 90;
  private static final int UP_DEGREES = 0;
  private static final int LEFT_DEGREES = UP_DEGREES + NINETY_DEGREES;
  private static final int DOWN_DEGREES = LEFT_DEGREES + NINETY_DEGREES;
  private static final int RIGHT_DEGREES = DOWN_DEGREES + NINETY_DEGREES;
  private static final int NE_DEGREES = RIGHT_DEGREES + FORTY_FIVE_DEGREES;
  private static final int NW_DEGREES = UP_DEGREES + FORTY_FIVE_DEGREES;
  private static final int SW_DEGREES = LEFT_DEGREES + FORTY_FIVE_DEGREES;
  private static final int SE_DEGREES = DOWN_DEGREES + FORTY_FIVE_DEGREES;

  private static final int PIXEL_MOVE = 10;

  private VisibleImage spaceship;

  private BufferedImage image = null;
  private BufferedImage mainImage = null;

  private int spaceshipSize;

  private Direction currentDir = Direction.STILL;

  private LinkedList<Integer> currentKeyPressed;

  private String spaceshipPic;

  public static boolean moving;

  private Location initialLocation;

  private DrawingCanvas canvas;

  private boolean initial;

  public Spaceship(String spaceshipPic, int spaceshipSize, Location initial,
  DrawingCanvas canvas) {

    this.spaceshipPic = spaceshipPic;

    this.spaceshipSize = spaceshipSize;

    initialLocation = initial;

    this.canvas = canvas;

    Image spaceshipImage = null;

    try {

      spaceshipImage = ImageIO.read(new File(spaceshipPic));
      image = ImageIO.read(new File(spaceshipPic));
    }

    catch (IOException e) {

      System.out.println("Error opening the image!");
    }

    spaceship = new VisibleImage(spaceshipImage, initial, canvas);

    mainImage = new BufferedImage(spaceshipSize, spaceshipSize,
    BufferedImage.TYPE_INT_ARGB);

    currentKeyPressed = new LinkedList<Integer>();

    canvas.addKeyListener(this);

    start();
  }

  public void initial() {

    initial = true;
  }

  private void setInitial() {

    currentDir = Direction.STILL;
    rotate(UP_DEGREES);
    spaceship.moveTo(initialLocation);

    initial = false;

    canvas.requestFocusInWindow();
  }

  public Location getSpaceshipLocation() {

    return spaceship.getLocation();
  }

  public VisibleImage getImage() {

    return spaceship;
  }

  private void rotate(double degrees) {

    degrees = degrees * -1;

    Graphics2D g2D;

    g2D = mainImage.createGraphics();

    // clearing the image to transparent
    g2D.setBackground(new Color(0,0,0,0));
    g2D.clearRect(0, 0, spaceshipSize, spaceshipSize);

    // reposition the origin
    g2D.translate(spaceshipSize / HALF, spaceshipSize / HALF);

    // rotate the board
    g2D.rotate(Math.toRadians(degrees));

    // draw the spaceship image from top left
    g2D.drawImage(image, - spaceshipSize / HALF, - spaceshipSize / HALF, null);

    // set the spaceship image with the edited image
    spaceship.setImage(mainImage);
  }

  public void keyPressed(KeyEvent evt) {

    System.out.println("key!");
    if (moving) {

      if (!currentKeyPressed.contains(evt.getKeyCode())) {

        currentKeyPressed.add(evt.getKeyCode());
      }

      if (evt.getKeyCode() == KeyEvent.VK_SPACE) {

        currentKeyPressed.clear();
        currentDir = Direction.STILL;
      }

      else if (currentKeyPressed.contains(KeyEvent.VK_UP) &&
      currentKeyPressed.contains(KeyEvent.VK_RIGHT)) {

        rotate(NE_DEGREES);

        currentDir = Direction.NE;
      }

      else if (currentKeyPressed.contains(KeyEvent.VK_DOWN) &&
      currentKeyPressed.contains(KeyEvent.VK_RIGHT)) {

        rotate(SE_DEGREES);

        currentDir = Direction.SE;
      }

      else if (currentKeyPressed.contains(KeyEvent.VK_DOWN) &&
      currentKeyPressed.contains(KeyEvent.VK_LEFT)) {

        rotate(SW_DEGREES);

        currentDir = Direction.SW;
      }

      else if (currentKeyPressed.contains(KeyEvent.VK_UP) &&
      currentKeyPressed.contains(KeyEvent.VK_LEFT)) {

        rotate(NW_DEGREES);

        currentDir = Direction.NW;
      }

      else if (currentKeyPressed.contains(KeyEvent.VK_UP)) {

        rotate(UP_DEGREES);

        currentDir = Direction.UP;
      }

      else if (currentKeyPressed.contains(KeyEvent.VK_RIGHT)) {

        rotate(RIGHT_DEGREES);

        currentDir = Direction.RIGHT;
      }

      else if (currentKeyPressed.contains(KeyEvent.VK_DOWN)) {

        rotate(DOWN_DEGREES);

        currentDir = Direction.DOWN;
      }

      else if (currentKeyPressed.contains(KeyEvent.VK_LEFT)) {

        rotate(LEFT_DEGREES);

        currentDir = Direction.LEFT;
      }
    }
  }

  public void keyReleased(KeyEvent evt) {

    if (moving) {

      currentKeyPressed.clear();
    }
  }

  public void keyTyped(KeyEvent evt) {

  }

  private void move() {

    switch(currentDir) {

      case UP:
        spaceship.move(0, -PIXEL_MOVE);
        break;

      case DOWN:
        spaceship.move(0, PIXEL_MOVE);
        break;

      case RIGHT:
        spaceship.move(PIXEL_MOVE, 0);
        break;

      case LEFT:
        spaceship.move(-PIXEL_MOVE, 0);
        break;

      case NE:
        spaceship.move(PIXEL_MOVE, -PIXEL_MOVE);
        break;

      case SE:
        spaceship.move(PIXEL_MOVE, PIXEL_MOVE);
        break;

      case SW:
        spaceship.move(-PIXEL_MOVE, PIXEL_MOVE);
        break;

      case NW:
        spaceship.move(-PIXEL_MOVE, -PIXEL_MOVE);
        break;

      case STILL:
        spaceship.move(0, 0);
        break;
    }
  }

  public void run() {

    while (true) {

      if (moving) {

        if (!initial) {

          move();
        }

        else {

          setInitial();
        }
      }

      if (initial && !moving) {

        moving = true;
        setInitial();
      }

      pause(DELAY);
    }
  }
}
