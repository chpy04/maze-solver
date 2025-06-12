import java.util.*;
import java.awt.Color;
import javalib.worldimages.*;

//Represents a grph of cells and edges
class Maze {
  // 2 dim AL of Cells representing the cells in this maze
  private final ArrayList<ArrayList<ACell>> mazeGraph;

  // width of this maze in # of cells
  private final int width;

  // height of this maze in # of cells
  private final int height;

  // Mapping of cells to their cell representatives, for the union/find data
  // structure
  private final HashMap<ACell, ACell> nodeConnections;

  // AL of all the edges in this maze
  private final ArrayList<Edge> cellEdges;

  // AL of the edges in this maze that make up the minimum spanning tree
  private final ArrayList<Edge> minSpanningTree;

  // the current worklist for either search algorithm
  private final Deque<ACell> workList;

  // Mapping of cells to the cell one step closer to the start (start cell maps to
  // itself)
  private final HashMap<ACell, ACell> prevCells;

  // the cell that the user is currently in. This is not final because the user
  // can move,
  // and when the user moves the Cell representing the user should change
  private ACell user;

  // the largest distance from the start of any cell found so far.
  // not final because the max start distance cannot be found until the maze has
  // been
  // fully built, which does not happen in the constructor. Is also used as an
  // accumulator
  // to keep track of the largest so far as all the cells are searched through
  private int maxStartDist;

  // the largest distance from the end of any cell found so far.
  // not final for same reason as maxStartDist
  private int maxEndDist;

  Maze(int width, int height, double verticalBias, boolean hex) {
    // width and height must be positive non-zero integers
    if (width < 1 || height < 1) {
      throw new IllegalArgumentException("cannot construct mazes smaller than 1 by 1");
    }

    // vertical bias is a scalar between 0 and 1
    if (0 > verticalBias || 1 < verticalBias) {
      throw new IllegalArgumentException("vertical bias must be between 0 and 1");
    }

    this.width = width;
    this.height = height;
    this.cellEdges = new ArrayList<Edge>();
    this.nodeConnections = new HashMap<ACell, ACell>();
    this.workList = new LinkedList<ACell>();
    this.prevCells = new HashMap<ACell, ACell>();

    // start max distances as 1 to then be updated later when the maze has been
    // constructed
    this.maxStartDist = 1;
    this.maxEndDist = 1;
    if (hex) {
      this.mazeGraph = this.generateHexMaze(this.width, this.height, verticalBias, this.cellEdges);
    }
    else {
      this.mazeGraph = this.generateSquareMaze(this.width, this.height, verticalBias,
          this.cellEdges);
    }

    // start the user in the top left
    this.user = this.mazeGraph.get(0).get(0);

    // sort the edges by their weights
    Collections.sort(this.cellEdges, new EdgeComparator());

    // calculate the minimum spanning tree using the edges in this maze
    this.minSpanningTree = this.minSpanningTree();
  }

  // Helper: given a width, height, verticalbias, and an AL to add all edges into,
  // create a 2d arraylist of
  // hexcells to represent this hexagon maze, while adding all the edges to the
  // edges AL
  ArrayList<ArrayList<ACell>> generateHexMaze(int width, int height, double verticalBias,
      ArrayList<Edge> edges) {
    ArrayList<ArrayList<ACell>> ret = new ArrayList<ArrayList<ACell>>();
    for (int yPos = 0; yPos < height; yPos += 1) {
      ArrayList<ACell> row = new ArrayList<ACell>();
      // loop through ever cell in a given row
      for (int xPos = 0; xPos < width; xPos += 1) {
        // the cell to add, not the end for every cell but the bottom right corner
        ACell cell;
        cell = new HexCell(xPos == this.width - 1 && yPos == this.height - 1);

        row.add(cell);

        // set the cells representative to itself
        this.nodeConnections.put(cell, cell);
        // if the x pos isn't 0, there is a cell to the left of it. create a new edge
        // with
        // these two cells and add it to this.cellEdges
        if (xPos != 0) {
          edges.add(new Edge(row.get(xPos - 1), cell,
              // randomized weights * the inverse of the vertical bias for horizontal edges
              (int) (Math.random() * 1000 * (1 - verticalBias)), true, false, false));
        }
        if (yPos % 2 == 0) {
          // if the y pos isn't 0, there is a cell to the above it. create a new edge with
          // these two cells and add it to this.cellEdges
          if (yPos != 0 && xPos != 0) {
            edges.add(new Edge(ret.get(yPos - 1).get(xPos - 1), cell,
                // randomized weights * the the vertical bias for vertical edges
                (int) (Math.random() * 1000 * (verticalBias)), false, false, true));
          }

          if (yPos != 0) {
            edges.add(new Edge(ret.get(yPos - 1).get(xPos), cell,
                // randomized weights * the the vertical bias for vertical edges
                (int) (Math.random() * 1000 * (verticalBias)), false, true, false));
          }
        }
        else {
          // if the y pos isn't 0, there is a cell to the above it. create a new edge with
          // these two cells and add it to this.cellEdges
          if (yPos != 0) {
            edges.add(new Edge(ret.get(yPos - 1).get(xPos), cell,
                // randomized weights * the the vertical bias for vertical edges
                (int) (Math.random() * 1000 * (verticalBias)), false, false, true));
          }

          if (yPos != 0 && xPos != this.width - 1) {
            edges.add(new Edge(ret.get(yPos - 1).get(xPos + 1), cell,
                // randomized weights * the the vertical bias for vertical edges
                (int) (Math.random() * 1000 * (verticalBias)), false, true, false));
          }
        }
      }
      ret.add(row);
    }
    return ret;
  }

  // Helper: given a width, height, verticalbias, and an AL to add all edges into,
  // create a 2d arraylist of
  // cells to represent this square maze, while adding all the edges to the edges
  // AL
  ArrayList<ArrayList<ACell>> generateSquareMaze(int width, int height, double verticalBias,
      ArrayList<Edge> edges) {
    ArrayList<ArrayList<ACell>> ret = new ArrayList<ArrayList<ACell>>();
    // loop through ever cell, making the rows and adding to this.mazegraph
    for (int yPos = 0; yPos < height; yPos += 1) {
      ArrayList<ACell> row = new ArrayList<ACell>();
      // loop through ever cell in a given row
      for (int xPos = 0; xPos < width; xPos += 1) {
        // the cell to add, not the end for every cell but the bottom right corner
        ACell cell;
        cell = new Cell(xPos == this.width - 1 && yPos == this.height - 1);

        row.add(cell);

        // set the cells representative to itself
        this.nodeConnections.put(cell, cell);
        // if the x pos isn't 0, there is a cell to the left of it. create a new edge
        // with
        // these two cells and add it to this.cellEdges
        if (xPos != 0) {
          edges.add(new Edge(row.get(xPos - 1), cell,
              // randomized weights * the inverse of the vertical bias for horizontal edges
              (int) (Math.random() * 1000 * (1 - verticalBias)), true, false, false));
        }

        // if the y pos isn't 0, there is a cell to the above it. create a new edge with
        // these two cells and add it to this.cellEdges
        if (yPos != 0) {
          edges.add(new Edge(ret.get(yPos - 1).get(xPos), cell,
              // randomized weights * the the vertical bias for vertical edges
              (int) (Math.random() * 1000 * (verticalBias)), false, false, false));
        }
      }
      ret.add(row);
    }
    return ret;
  }

  // convenience constructor that takes in width and height, assumes there is
  // no bias in either direction for vertical/horizontal edges
  Maze(int width, int height) {
    this(width, height, .5, false);
  }

  // produce an image of all the maze walls in this maze
  WorldImage calcWallsImage(int imageWidth, int imageHeight, int cellWidth) {
    WorldImage bg = new RectangleImage(imageWidth, imageHeight, OutlineMode.OUTLINE, Color.WHITE);

    // loop through every cell, and have it draw its walls onto the background,
    // passing through the coordinates and cellWidth
    for (int row = 0; row < this.height; row += 1) {
      for (int col = 0; col < this.width; col += 1) {
        bg = this.mazeGraph.get(row).get(col).drawWallsOntoBackground(col + 1, row + 1, cellWidth,
            bg);
      }
    }
    return bg;
  }

  // draw this maze on an image using a given width and height. Also takes in the
  // state being drawn
  public WorldImage draw(int imageWidth, int imageHeight, DrawType drawType) {
    // cell width makes sure that all cells can fit in the given image, cells are
    // always square
    int cellWidth = Math.min(imageWidth / (this.width + 2), imageHeight / (this.height + 2));

    WorldImage bg = new RectangleImage(imageWidth, imageHeight, OutlineMode.OUTLINE, Color.WHITE);

    // if the minSpanningTree is empty, the maze has been built and everything needs
    // to be drawn
    if (this.minSpanningTree.size() == 0) {
      // for every cell in the maze, draw itself in the appropriate drawType on the
      // background
      for (int row = 0; row < this.height; row += 1) {
        for (int col = 0; col < this.width; col += 1) {
          bg = this.mazeGraph.get(row).get(col).drawCellOntoBackground(col + 1, row + 1, cellWidth,
              bg, drawType, this.maxStartDist, this.maxEndDist);

          // if this is the user, draw the user onto the background
          if (this.mazeGraph.get(row).get(col) == this.user) {
            bg = this.user.drawUserOnBackground(col + 1, row + 1, cellWidth, bg);
          }
        }
      }
      return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP,
          this.calcWallsImage(imageWidth, imageHeight, cellWidth), 0, 0, bg);
    }
    else {
      // if the minSpanningTree is not empty, edges are still being removed, so
      // nothing but the
      // edges needs to be displayed
      bg = this.calcWallsImage(imageWidth, imageHeight, cellWidth);
    }

    return bg;
  }

  // compute the minimum spanning tree of edges to connect this maze of cells
  private ArrayList<Edge> minSpanningTree() {
    // AL of all edges that will be returned
    ArrayList<Edge> edgesInTree = new ArrayList<Edge>();
    // worklist of edges that still need to be processed, starts as every edge
    ArrayList<Edge> worklist = new ArrayList<Edge>(this.cellEdges);

    // while there are still edges to process, add the cheapest one to the tree if
    // it's cells have difference representatives
    while (worklist.size() > 0) {
      Edge cheapestEdge = worklist.remove(0);
      // if the cells on this edge were not in the same representative group, add this
      // edge
      // to the tree, otherwise don't add the edge
      if (cheapestEdge.connectNodesInHash(this.nodeConnections)) {
        edgesInTree.add(cheapestEdge);
      }
    }
    return edgesInTree;
  }

  // remove all of the remaining edges in the minSpanning Tree, and calculate
  // the start and end distances
  void computeMaze() {
    // for each edge in the minSpanningTree, connect its cells together, and then
    // clear
    // this.minSpanningTree to show that all the edges have been processed
    for (Edge edge : this.minSpanningTree) {
      edge.connectTheseCells();
    }
    this.minSpanningTree.clear();

    // calculate the distances from start and end
    this.calcDistFromStartAndEnd();
  }

  // remove the next edge from this.minSpanningTree and connects its cells.
  // Returns true if all the edges have been removed, false ow
  boolean removeNextEdge() {
    if (this.minSpanningTree.size() > 0) {
      this.minSpanningTree.remove(0).connectTheseCells();
      return false;
    }
    // calc distances from start and end when maze has been made
    this.calcDistFromStartAndEnd();
    return true;
  }

  // highlight all the cells from a given cell to the start of the maze red
  private void highLightPathFrom(ACell last) {
    // keeps track of two cells and works through this.prevCells backtracking
    // to the start and painting cells red as it goes along. because this.prevcells
    // has the starting cell pointing to itself, continue back tracking until the
    // two
    // cells are equal to each other and therefore are the start
    ACell thisCell = last;
    ACell prevCell = this.prevCells.get(last);
    thisCell.changeVistedPathColor(Color.red);
    while (thisCell != prevCell) {
      thisCell = prevCell;
      prevCell = this.prevCells.get(prevCell);
      thisCell.changeVistedPathColor(Color.red);
    }
  }

  // paint all the cells back to white to stop showing a solution
  void resetSolution() {
    // for every cell in the maze, paint it white
    for (ArrayList<ACell> row : this.mazeGraph) {
      for (ACell cell : row) {
        cell.changeVistedPathColor(Color.WHITE);
      }
    }
    // reset the prevCells and WorkList
    this.prevCells.clear();
    this.workList.clear();

  }

  // calculate each cells distance from the start and end of the maze, as well as
  // update the max start and end distances that have been seen
  private void calcDistFromStartAndEnd() {
    // to calculate distances, work through the entire maze using breadth first
    // search,
    // and store the cell that each cell is coming from in the distPrevCells
    // hashMap. Then,
    // every cells distance from the start will be the previous cells distance plus
    // 1. Also
    // keep track of the largest distance seen so far for color scaling
    Deque<ACell> distWorkList = new LinkedList<ACell>();
    HashMap<ACell, ACell> distPrevCells = new HashMap<ACell, ACell>();
    distPrevCells.put(this.mazeGraph.get(0).get(0), this.mazeGraph.get(0).get(0));
    distWorkList.add(this.mazeGraph.get(0).get(0));

    // while there are cells in the worklist, calculate their distance and add any
    // next cells to the worklist/prevcells (very similar to maze step algorithm)
    while (distWorkList.size() > 0) {
      ACell next = distWorkList.removeLast();
      next.calcStartDistances(distPrevCells.get(next));
      this.maxStartDist = next.startMaxDist(this.maxStartDist);
      ArrayList<Edge> nextEdges = next.collectEdges();
      for (Edge edge : nextEdges) {
        edge.addToHashIfPossible(next, distPrevCells, distWorkList, false);
      }
    }

    // do the same thing, but start from the end and calculate end distances.
    // Note that end distances are not just the inverse of start distances.
    distWorkList.clear();
    distPrevCells.clear();
    distPrevCells.put(this.mazeGraph.get(this.height - 1).get(this.width - 1),
        this.mazeGraph.get(this.height - 1).get(this.width - 1));
    distWorkList.add(this.mazeGraph.get(this.height - 1).get(this.width - 1));
    // same while loop for searching through maze, this time getting distance from
    // end
    while (distWorkList.size() > 0) {
      ACell next = distWorkList.removeLast();
      next.calcEndDistances(distPrevCells.get(next));
      this.maxEndDist = next.endMaxDist(this.maxEndDist);
      ArrayList<Edge> nextEdges = next.collectEdges();
      for (Edge edge : nextEdges) {
        edge.addToHashIfPossible(next, distPrevCells, distWorkList, false);
      }
    }
  }

  // using this.worklist, compute the next step in in finding a solution to the
  // maze
  // by either breath first or depth first search
  void nextMazeStep(boolean depthFirst) {
    // if the prevCells hashmap is empty, the search has not been started yet. Add
    // the
    // start cell to the hashmap and the worklist to begin the search
    if (this.prevCells.size() == 0) {
      this.prevCells.put(this.mazeGraph.get(0).get(0), this.mazeGraph.get(0).get(0));
      this.workList.addFirst(this.mazeGraph.get(0).get(0));
    }
    // if there are cells in the worklist, there is a next step to be looked at
    else if (this.workList.size() > 0) {
      ACell next;
      // cells are always added to the front of the worklist deque, so depth first
      // is removing from the front, and breath first is removing from the back
      if (depthFirst) {
        next = workList.removeFirst();
      }
      else {
        next = workList.removeLast();
      }

      // if the cell being looked at is the end of the maze, highlight the path
      // from the end back to the start and clear the worklist to stop the search
      if (next.isEnd()) {
        this.highLightPathFrom(next);
        this.workList.clear();
      }

      // collect all the edges adjacent to the next cell
      ArrayList<Edge> nextEdges = next.collectEdges();

      // for every edge, add the other corresponding cell to the worklist and
      // prevCells
      // if applicable
      for (Edge edge : nextEdges) {
        edge.addToHashIfPossible(next, this.prevCells, this.workList, true);
      }
    }
  }

  // given a key input, move the user if possible. Return true if the
  // user was moved and false if not
  boolean moveUser(String key) {
    ACell currCell = this.user;
    this.user = this.user.computeNextUser(key);

    // No movement happened, don't increment move count
    return (this.user != currCell);
  }

  // returns if the user is currently on the destination cell
  // which would display "User has won" and disable user movement
  boolean userHasWon() {
    return this.user == this.mazeGraph.get(this.mazeGraph.size() - 1)
        .get(this.mazeGraph.get(0).size() - 1);
  }
}