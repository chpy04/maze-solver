import java.awt.Color;
import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.*;

//a Game represents the handling of key inputs and displaying of a maze
class Game extends World {
  // the maze being worked with
  // not final bc when this game is reset, it reasigns a new maze
  private Maze mg;
  // the window wieth in pixels
  private final int windowWidth;
  // the window height in pixels
  private final int windowHeight;
  // the maze width in cells
  private final int mazeWidth;
  // the maze height in cells
  private final int mazeHeight;
  // is this maze made of square (false) or hex (true) cells
  private final boolean squareOrHex;
  // the bias for vertical corridoors in the maze (between 0 and 1)
  private final double verticalBias;
  // is this maze currently automatically removing edges
  // not final because it could be changed with user input
  private boolean autoRemoveEdges;
  // is this maze currently automatically solving itself
  // not final because it could be changed with user input
  private boolean autoSolve;
  // is the solution being calculated with depth first (true) or breath first
  // (false)
  // not final because it could be changed with user input
  private boolean depthFirst;
  // what is the maze currently displaying
  // not final because it could be changed with user input
  private DrawType drawType;
  // whether or not to show the control panel of keys
  // not final because it could be changed with user input
  private boolean showControlPanel;
  // number of moves the player has made
  // not final because it should change when the user moves
  private int moveCount;
  // whether or not the user has reached the end
  // not final because it changes when the user wins
  private boolean userHasWon;
  // no tfinal because it could change with user input
  // how fast should the search be calculated (how many steps per frame)
  private double speed;

  Game(int mazeWidth, int mazeHeight, double verticalBias, boolean squareOrHex, int windowWidth,
      int windowHeight) {
    // construct the maze and save the parameters for restarting later
    this.mg = new Maze(mazeWidth, mazeHeight, verticalBias, squareOrHex);
    this.windowWidth = windowWidth;
    this.windowHeight = windowHeight;
    this.mazeWidth = mazeWidth;
    this.mazeHeight = mazeHeight;
    this.squareOrHex = squareOrHex;
    this.autoRemoveEdges = true;
    this.autoSolve = false;
    this.depthFirst = true;
    this.drawType = DrawType.VisitedPath;
    this.showControlPanel = false;
    this.moveCount = 0;
    this.userHasWon = false;
    this.verticalBias = verticalBias;
    this.speed = 1.0;
  }

  // draws this maze on the game
  public WorldScene makeScene() {
    if (!this.showControlPanel) {
      // if showing the maze, draw the score count and if the user has won
      WorldScene canvas = new WorldScene(this.windowWidth, this.windowHeight);

      WorldImage scoreboard = new TextImage("Move Count: " + this.moveCount
          + "        Press = to see user guide." + " PRESS 'g' TO AUTO REMOVE ALL WALLS!",
          Color.RED);

      if (userHasWon) {
        scoreboard = new TextImage("Move Count: " + this.moveCount + ", Player has won!"
            + "        Press = to see user guide", Color.RED);
      }

      // have the maze draw itself onto the canvas
      canvas.placeImageXY(
          new AboveImage(scoreboard,
              this.mg.draw(this.windowWidth - 30, this.windowHeight - 30, this.drawType)),
          this.windowWidth / 2, this.windowHeight / 2);
      return canvas;
    }
    else {
      // draw the instructions key
      WorldScene canvas = new WorldScene(this.windowWidth, this.windowHeight);
      canvas.placeImageXY(new AboveImage(new TextImage("g - remove all edges at once", Color.RED),
          new TextImage("f - initiate depth first search", Color.RED),
          new TextImage("b - initiate breadth first search", Color.RED),
          new TextImage("spacebar - toggles auto solve (on by default)", Color.RED),
          new TextImage("c - searches one step in the current search type", Color.RED),
          new TextImage("s - toggles depth first / breadth first (dfs by default)", Color.RED),
          new TextImage("r - resets the maze (autoremove on, autosolve off, dfs on)", Color.RED),
          new TextImage("v - cycles draw types (Nothing, "
              + "distance from start, distance from end, visited path)", Color.RED),
          new TextImage("= - show control panel", Color.RED),
          new TextImage(". - multiplies 'speed' factor by 1.5", Color.RED),
          new TextImage(", - mulitplies 'speed' factor by 0.66", Color.RED),
          new TextImage("arrow keys - move player ", Color.RED),
          new TextImage("Hexagon Movement: ", Color.RED),
          new TextImage("w - northwest ↖", Color.RED), new TextImage("e - northeast ↗", Color.RED),
          new TextImage("d - east →", Color.RED), new TextImage("a - west ←", Color.RED),
          new TextImage("x - southeast ↘", Color.RED), new TextImage("z - southwest ↙", Color.RED)),
          this.windowWidth / 2, this.windowHeight / 2);
      return canvas;
    }
  }

  // runs ever tick of the game
  public void onTick() {
    if (this.autoRemoveEdges) {
      // if autoremoveEdges is on, remove edges until the last one is removed, then
      // turn it off
      this.autoRemoveEdges = !this.mg.removeNextEdge();
    }
    else if (this.autoSolve) {
      // if autoSolve is on, run this.speed many steps on the maze
      for (int iterations = 0; iterations < (int) this.speed; iterations += 1) {
        this.mg.nextMazeStep(this.depthFirst);
      }
    }
  }

  // key handling
  public void onKeyEvent(String key) {
    // finish computing the maze using the min spanning tree
    if (key.equals("g")) {
      this.autoRemoveEdges = false;
      this.mg.computeMaze();
    }

    // switch in or out of the control panel
    else if (key.equals("=")) {
      this.showControlPanel = !this.showControlPanel;
    }

    // if the maze has not finished being made, do not do anything for
    // other keys
    if (this.autoRemoveEdges) {
      return;
    }

    // reset the maze and initiate a depth first search
    else if (key.equals("f")) {
      this.mg.resetSolution();
      this.depthFirst = true;
      this.autoSolve = true;
    }

    // reset the maze and initiate a breath first search
    else if (key.equals("b")) {
      this.mg.resetSolution();
      this.depthFirst = false;
      this.autoSolve = true;
    }

    // stop/start the autosolver
    else if (key.equals(" ")) {
      this.autoSolve = !this.autoSolve;
    }

    // compute the next step in the maze
    else if (key.equals("c")) {
      this.mg.nextMazeStep(this.depthFirst);
    }

    // toggle between breadth and depth first search
    else if (key.equals("s")) {
      this.depthFirst = !this.depthFirst;
    }

    // reset the maze, by constructing a new maze and resetting fields of this game
    else if (key.equals("r")) {
      this.mg = new Maze(this.mazeWidth, this.mazeHeight, this.verticalBias, this.squareOrHex);
      this.autoRemoveEdges = true;
      this.autoSolve = false;
      this.depthFirst = true;
      this.moveCount = 0;
      this.userHasWon = false;
    }

    // change the drawType, cycling through the differenct options
    else if (key.equals("v")) {
      if (this.drawType == DrawType.Nothing) {
        this.drawType = DrawType.DistanceFromStart;
      }
      else if (this.drawType == DrawType.DistanceFromStart) {
        this.drawType = DrawType.DistanceFromEnd;
      }
      else if (this.drawType == DrawType.DistanceFromEnd) {
        this.drawType = DrawType.VisitedPath;
      }
      else {
        this.drawType = DrawType.Nothing;
      }
    }

    // speed up the number of steps being shown on each tick by a factor of 1.5
    else if (key.equals(".")) {
      this.speed = this.speed * 1.5;
    }

    // slow down the number of steps being shown on each tick by a factor of .66
    else if (key.equals(",")) {
      this.speed = this.speed * .66;
    }

    // handle moving keys
    else {
      if (!mg.userHasWon()) {
        boolean didUserMove = this.mg.moveUser(key);
        // increment move count if user moved
        if (didUserMove) {
          this.moveCount += 1;
        }
        // recalculate if the user has won
        this.userHasWon = mg.userHasWon();
      }
    }
  }
}

//the type of maze being displayed
enum DrawType {
  // standard maze, no colors, no search algorithms
  Nothing,
  // each cell colored as a red-blue shade representing its distance from the
  // start
  DistanceFromStart,
  // each cell colored as a red-blue shade representing its distance from the end
  DistanceFromEnd,
  // cells being draw to show a search algoithm, green for having been visited,
  // red
  // for being a part of the solution path
  VisitedPath
}