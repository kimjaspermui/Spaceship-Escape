import objectdraw.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.lang.InterruptedException;

public class Star extends ActiveObject {

  private static final Color STAR_COLOR = Color.YELLOW;

  private static final int CHECKPOINTS_NUM = 8;

  private static final int HALF = 2;

  private static final int DELAY = 50;

  public static boolean moving;
  private static boolean clear;

  private static final int PIXEL_MOVE = 1;

  private FilledOval star;

  private int starSize;
  private int starRadius;

  private LinkedList<Location> starCheckpoints;

  private Direction currentDir;

  private int appletWidth;
  private int appletHeight;

  private Spaceship spaceship;

  private int spaceshipSize;

  // ACCEPT SOUTHPANEL HEIGHT
  public Star(int appletWidth, int appletHeight, int starSize,
  Location initialLoc, Direction initialDir, Spaceship spaceship,
  int spaceshipSize, DrawingCanvas canvas) {

    this.appletWidth = appletWidth;
    this.appletHeight = appletHeight;

    this.starSize = starSize;
    starRadius = starSize / HALF;

    starCheckpoints = new LinkedList<Location>();

    starCheckpoints.add(new Location(starRadius, 0));
    starCheckpoints.add(new Location(starSize, starRadius));
    starCheckpoints.add(new Location(starRadius, starSize));
    starCheckpoints.add(new Location(0, starRadius));
    starCheckpoints.add(new Location(starRadius + starRadius / HALF,
    starRadius / HALF));
    starCheckpoints.add(new Location(starRadius + starRadius / HALF,
    starRadius + starRadius / HALF));
    starCheckpoints.add(new Location(starRadius / HALF,
    starRadius + starRadius / HALF));
    starCheckpoints.add(new Location(starRadius / HALF, starRadius / HALF));

    star = new FilledOval(initialLoc.getX(), initialLoc.getY(), starSize,
    starSize, canvas);
    currentDir = initialDir;
    star.setColor(STAR_COLOR);

    this.spaceship = spaceship;
    this.spaceshipSize = spaceshipSize;

    moving = true;

    clear = false;

    start();
  }

  private void move() {

    switch (currentDir) {

      case UP:
        star.move(0, -PIXEL_MOVE);
        break;

      case DOWN:
        star.move(0, PIXEL_MOVE);
        break;

      case LEFT:
        star.move(-PIXEL_MOVE, 0);
        break;

      case RIGHT:
        star.move(PIXEL_MOVE, 0);
        break;

      case NW:
        star.move(-PIXEL_MOVE, -PIXEL_MOVE);
        break;

      case NE:
        star.move(PIXEL_MOVE, -PIXEL_MOVE);
        break;

      case SE:
        star.move(PIXEL_MOVE, PIXEL_MOVE);
        break;

      case SW:
        star.move(-PIXEL_MOVE, PIXEL_MOVE);
        break;
    }
  }

  private void checkPosition() {

    if (star.getX() == 0 && star.getY() == 0) {

      if (currentDir == Direction.UP) {

        currentDir = Direction.DOWN;
      }

      else if (currentDir == Direction.NW) {

        currentDir = Direction.SE;
      }

      else if (currentDir == Direction.LEFT) {

        currentDir = Direction.RIGHT;
      }
    }

    else if (star.getX() == appletWidth - starSize && star.getY() == 0) {

      if (currentDir == Direction.RIGHT) {

        currentDir = Direction.LEFT;
      }

      else if (currentDir == Direction.NE) {

        currentDir = Direction.SW;
      }

      else if (currentDir == Direction.UP) {

        currentDir = Direction.DOWN;
      }
    }

    else if (star.getX() == appletWidth - starSize && star.getY() ==
    appletHeight - starSize) {

      if (currentDir == Direction.RIGHT) {

        currentDir = Direction.LEFT;
      }

      else if (currentDir == Direction.SE) {

        currentDir = Direction.NW;
      }

      else if (currentDir == Direction.DOWN) {

        currentDir = Direction.UP;
      }
    }

    else if (star.getX() == 0 && star.getY() ==
    appletHeight - starSize) {

      if (currentDir == Direction.LEFT) {

        currentDir = Direction.RIGHT;
      }

      else if (currentDir == Direction.SW) {

        currentDir = Direction.NE;
      }

      else if (currentDir == Direction.DOWN) {

        currentDir = Direction.UP;
      }
    }

    else if (star.getX() == 0) {

      if (currentDir == Direction.SW) {

        currentDir = Direction.SE;
      }

      else if (currentDir == Direction.LEFT) {

        currentDir = Direction.RIGHT;
      }

      else if (currentDir == Direction.NW) {

        currentDir = Direction.NE;
      }
    }

    else if (star.getX() == appletWidth - starSize) {

      if (currentDir == Direction.SE) {

        currentDir = Direction.SW;
      }

      else if (currentDir == Direction.RIGHT) {

        currentDir = Direction.LEFT;
      }

      else if (currentDir == Direction.NE) {

        currentDir = Direction.NW;
      }
    }

    else if (star.getY() == 0) {

      if (currentDir == Direction.NW) {

        currentDir = Direction.SW;
      }

      else if (currentDir == Direction.UP) {

        currentDir = Direction.DOWN;
      }

      else if (currentDir == Direction.NE) {

        currentDir = Direction.SE;
      }
    }

    else if (star.getY() == appletHeight - starSize) {

      if (currentDir == Direction.SW) {

        currentDir = Direction.NW;
      }

      else if (currentDir == Direction.DOWN) {

        currentDir = Direction.UP;
      }

      else if (currentDir == Direction.SE) {

        currentDir = Direction.NE;
      }
    }
  }

  private boolean checkHit() {

    Location spaceshipTopLeft = spaceship.getSpaceshipLocation();
    int spaceshipTopLeftX = (int) spaceshipTopLeft.getX();
    int spaceshipTopLeftY = (int) spaceshipTopLeft.getY();

    int starTopLeftX = (int) star.getLocation().getX();
    int starTopLeftY = (int) star.getLocation().getY();

    int starX = 0;
    int starY = 0;

    Color pixelColor = null;

    Location currentCheckpoint = null;

    VisibleImage spaceshipImage = spaceship.getImage();

    for (int i = 0; i < CHECKPOINTS_NUM; i++) {

      currentCheckpoint = starCheckpoints.get(i);

      starX = (int) currentCheckpoint.getX() + starTopLeftX;
      starY = (int) currentCheckpoint.getY() + starTopLeftY;

      if (spaceshipImage.contains(new Location(starX, starY))) {

        try {

          pixelColor = new Color(((BufferedImage)
          spaceshipImage.getImage()).getRGB(starX - spaceshipTopLeftX, starY -
          spaceshipTopLeftY), true);

          if (pixelColor.getAlpha() != 0) {

            System.out.println("hit!");
            return true;
          }
        }

        catch (ArrayIndexOutOfBoundsException e) {

          // do nothing
        }
      }
    }

    return false;
  }

  public static void clear() {

    clear = true;
  }

  public void run() {

    while (true) {

      if (moving) {

        checkPosition();
        move();

        if (checkHit()) {

          SpaceshipEscapeController.isHit();
        }
      }

      if (clear && star != null) {

        star.removeFromCanvas();
        break;
      }

      pause(DELAY);
    }
  }
}
