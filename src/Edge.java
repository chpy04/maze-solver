import java.awt.Color;
import java.util.*;
import javalib.worldimages.*;

//an IEdge represents a class that cells point to, either an edge from cell
//to cell or a border
interface IEdge {

  // draw this right edge onto the given background at the given coordinates if
  // applicable
  WorldImage drawRightBorder(WorldImage bg, int x, int y, int cellWidth);

  // draw this bottom edge onto the given background at the given coordinates if
  // applicable
  WorldImage drawBottomBorder(WorldImage bg, int x, int y, int cellWidth);

  // draw this left edge onto the given background at the given coordinates if
  // applicable
  WorldImage drawLeftBorder(WorldImage bg, int x, int y, int cellWidth);

  // draw this top edge onto the given background at the given coordinates if
  // applicable
  WorldImage drawTopBorder(WorldImage bg, int x, int y, int cellWidth);

  // draw the right hexagon border onto the given background at the given
  // coordinates if applicable
  WorldImage drawHexRightBorder(WorldImage bg, int x, int y, int cellWidth);

  // draw the left hexagon border onto the given background at the given
  // coordinates if applicable
  WorldImage drawHexLeftBorder(WorldImage bg, int x, int y, int cellWidth);

  // draw the top right hexagon border onto the given background at the given
  // coordinates if applicable
  WorldImage drawHexTRBorder(WorldImage bg, int x, int y, int cellWidth);

  // draw the top left hexagon border onto the given background at the given
  // coordinates if applicable
  WorldImage drawHexTLBorder(WorldImage bg, int x, int y, int cellWidth);

  // draw the bottom right hexagon border onto the given background at the given
  // coordinates if applicable
  WorldImage drawHexBRBorder(WorldImage bg, int x, int y, int cellWidth);

  // draw the bottom left hexagon border onto the given background at the given
  // coordinates if applicable
  WorldImage drawHexBLBorder(WorldImage bg, int x, int y, int cellWidth);

  // adds this edge to the arrayList if it is an edge from cell to cell
  void addIfEdge(ArrayList<Edge> edges);

  // the next cell the user will be in given that it is trying to go in
  // this direction and coming from the given cell
  ACell nextUserCell(ACell comingFrom);
}

//an Edge represents a connection between two cells
class Edge implements IEdge {
  private final ACell to;
  private final ACell from;
  private final int weight;

  // following booleans indicate which direction the edge is pointing in,
  // either left and right for hex or normal cells, botleft <-> topright for
  // hex cells, botright <-> topleft for hex cells, or if they are all false,
  // up down for normal cells
  private final boolean horizontal;
  private final boolean botLeft;
  private final boolean botRight;

  Edge(ACell from, ACell to, int weight, boolean horizontal, boolean botLeft, boolean botRight) {
    // if more than one of these booleans are true, the data definition above doesnt
    // make sense
    if ((horizontal && botLeft) || (horizontal && botRight) || (botLeft && botRight)) {
      throw new IllegalArgumentException("edges can only be in one direction");
    }
    this.to = to;
    this.from = from;
    this.weight = weight;
    this.horizontal = horizontal;
    this.botLeft = botLeft;
    this.botRight = botRight;
  }

  // connect both of this edges cell to this edge, removing the wall
  // between the two cells in the maze
  void connectTheseCells() {
    if (this.horizontal) {
      this.from.connectRight(this);
      this.to.connectLeft(this);
    }
    else if (botLeft) {
      this.from.connectBL(this);
      this.to.connectTR(this);
    }
    else if (botRight) {
      this.from.connectBR(this);
      this.to.connectTL(this);
    }
    else {
      this.from.connectDown(this);
      this.to.connectUp(this);
    }
  }

  // given a hashmap of cells to their representatives in kruskals alg,
  // if the two cells on this edge have different representatives,
  // connect the two and return true, otherwise do nothing and return false
  boolean connectNodesInHash(HashMap<ACell, ACell> nodes) {
    // to find the representative group, iterate through the hashmap of
    // representatives until it gets to a cell which is its own represetative
    ACell fromGroup = this.from;
    while (nodes.get(fromGroup) != fromGroup) {
      fromGroup = nodes.get(fromGroup);
    }
    // same process for this.to's group
    ACell toGroup = this.to;
    while (nodes.get(toGroup) != toGroup) {
      toGroup = nodes.get(toGroup);
    }

    // if the groups are different, assign the fromgroup to the
    // togroup, which also changes the representatives of all the cells
    // in the from group to be in the to group. Then return true.
    if (fromGroup != toGroup) {
      nodes.put(fromGroup, toGroup);
      return true;
    }
    // otherwise do nothing and return false
    return false;
  }

  // given a cell on this edge, if the other cell on this edge is not the cell
  // that the next cell is coming from,
  // it is a new cell that needs to be processed. Put it in the worklist and
  // prevCells, and paint it green if
  // applicable
  void addToHashIfPossible(ACell next, HashMap<ACell, ACell> prevCells, Deque<ACell> workList,
      boolean paintGreen) {
    if (this.to == next && this.from != prevCells.get(next)) {
      prevCells.put(this.from, this.to);
      workList.addFirst(this.from);
      if (paintGreen) {
        this.from.changeVistedPathColor(Color.green);
      }
    }
    if (this.from == next && this.to != prevCells.get(next)) {
      prevCells.put(this.to, this.from);
      workList.addFirst(this.to);
      if (paintGreen) {
        this.to.changeVistedPathColor(Color.green);
      }
    }
  }

  // edges on a cell mean that there is no border there, just return the bg
  public WorldImage drawRightBorder(WorldImage bg, int x, int y, int cellWidth) {
    return bg;
  }

  // edges on a cell mean that there is no border there, just return the bg
  public WorldImage drawBottomBorder(WorldImage bg, int x, int y, int cellWidth) {
    return bg;
  }

  // edges on a cell mean that there is no border there, just return the bg
  public WorldImage drawLeftBorder(WorldImage bg, int x, int y, int cellWidth) {
    return bg;
  }

  // edges on a cell mean that there is no border there, just return the bg
  public WorldImage drawTopBorder(WorldImage bg, int x, int y, int cellWidth) {
    return bg;
  }

  // edges on a cell mean that there is no border there, just return the bg
  public WorldImage drawHexRightBorder(WorldImage bg, int x, int y, int cellWidth) {
    return bg;
  }

  // edges on a cell mean that there is no border there, just return the bg
  public WorldImage drawHexLeftBorder(WorldImage bg, int x, int y, int cellWidth) {
    return bg;
  }

  // edges on a cell mean that there is no border there, just return the bg
  public WorldImage drawHexTRBorder(WorldImage bg, int x, int y, int cellWidth) {
    return bg;
  }

  // edges on a cell mean that there is no border there, just return the bg
  public WorldImage drawHexTLBorder(WorldImage bg, int x, int y, int cellWidth) {
    return bg;
  }

  // edges on a cell mean that there is no border there, just return the bg
  public WorldImage drawHexBRBorder(WorldImage bg, int x, int y, int cellWidth) {
    return bg;
  }

  // edges on a cell mean that there is no border there, just return the bg
  public WorldImage drawHexBLBorder(WorldImage bg, int x, int y, int cellWidth) {
    return bg;
  }

  // this is an edge, so add it to the AL
  public void addIfEdge(ArrayList<Edge> edges) {
    edges.add(this);
  }

  // return the difference between the other weight and this weight
  public int weightDifference(Edge other) {
    return other.weight - this.weight;
  }

  // whichever one isnt the cell that the user is coming from.
  // 'comingFrom' is constrained to being either this.to or this.from in this
  // Edge.
  public ACell nextUserCell(ACell comingFrom) {
    if (comingFrom == this.to) {
      return this.from;
    }
    return this.to;
  }
}

//comparator used for sorting edges, returns the difference in weights between
//two edges
class EdgeComparator implements Comparator<Edge> {
  public int compare(Edge e1, Edge e2) {
    return e1.weightDifference(e2);
  }
}

//represents a border in the maze
class Border implements IEdge {
  private final Color BORDERCOLOR = Color.BLACK;
  private final int BORDERWIDTH = 2;

  // draw a border on the right side of the cell at the given x y coordinate
  public WorldImage drawRightBorder(WorldImage bg, int x, int y, int cellWidth) {
    WorldImage rect = new RectangleImage(BORDERWIDTH, cellWidth + BORDERWIDTH, OutlineMode.SOLID,
        BORDERCOLOR);

    return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, rect,
        -((x + 1) * cellWidth) + (BORDERWIDTH / 2), -((y * cellWidth) - (BORDERWIDTH / 2)), bg);
  }

  // draw a border on the bottom side of the cell at the given x y coordinate
  public WorldImage drawBottomBorder(WorldImage bg, int x, int y, int cellWidth) {
    WorldImage rect = new RectangleImage(cellWidth + BORDERWIDTH, BORDERWIDTH, OutlineMode.SOLID,
        BORDERCOLOR);

    return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, rect,
        -((x * cellWidth) - (BORDERWIDTH / 2)), -(((y + 1) * cellWidth) - (BORDERWIDTH / 2)), bg);
  }

  // draw a border on the left side of the cell at the given x y coordinate
  public WorldImage drawLeftBorder(WorldImage bg, int x, int y, int cellWidth) {
    WorldImage rect = new RectangleImage(BORDERWIDTH, cellWidth + BORDERWIDTH, OutlineMode.SOLID,
        BORDERCOLOR);

    return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, rect,
        -((x) * cellWidth) + (BORDERWIDTH / 2), -((y * cellWidth) - (BORDERWIDTH / 2)), bg);
  }

  // draw a border on the top side of the cell at the given x y coordinate
  public WorldImage drawTopBorder(WorldImage bg, int x, int y, int cellWidth) {
    WorldImage rect = new RectangleImage(cellWidth + BORDERWIDTH, BORDERWIDTH, OutlineMode.SOLID,
        BORDERCOLOR);

    return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, rect,
        -((x * cellWidth) - (BORDERWIDTH / 2)), -(((y) * cellWidth) - (BORDERWIDTH / 2)), bg);
  }

  // there is no adjacent cell for the user to move into, just return the
  // comingFrom cell
  public ACell nextUserCell(ACell comingFrom) {
    return comingFrom;
  }

  // borders do not connect two cells, do nothing to the AL
  public void addIfEdge(ArrayList<Edge> edges) {
    return;
  }

  // draw a hexagon border right of the cell at the given x y coordinates on the
  // bg
  public WorldImage drawHexRightBorder(WorldImage bg, int x, int y, int cellWidth) {
    int xOffSet = 0;
    if (y % 2 == 0) {
      xOffSet = cellWidth / 2;
    }

    WorldImage line = new LineImage(new Posn(0, cellWidth / 2), Color.BLACK);

    return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, line,
        -(((x + 1) * cellWidth) + xOffSet), -(((y) * cellWidth) + (cellWidth / 4)), bg);
  }

  // draw a hexagon border left of the cell at the given x y coordinates on the bg
  public WorldImage drawHexLeftBorder(WorldImage bg, int x, int y, int cellWidth) {
    int xOffSet = 0;
    if (y % 2 == 0) {
      xOffSet = cellWidth / 2;
    }

    WorldImage line = new LineImage(new Posn(0, cellWidth / 2), Color.BLACK);

    return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, line,
        -(((x) * cellWidth) + xOffSet), -(((y) * cellWidth) + (cellWidth / 4)), bg);
  }

  // draw a hexagon border top-right of the cell at the given x y coordinates on
  // the bg
  public WorldImage drawHexTRBorder(WorldImage bg, int x, int y, int cellWidth) {
    int xOffSet = 0;
    if (y % 2 == 0) {
      xOffSet = cellWidth / 2;
    }

    WorldImage line = new LineImage(new Posn(cellWidth / 2, cellWidth / 2), Color.BLACK);

    return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, line,
        -(((x) * cellWidth) + (cellWidth / 2) + xOffSet),
        -(((y) * cellWidth) - (cellWidth / 4) - 2), bg);
  }

  // draw a hexagon border top-left of the cell at the given x y coordinates on
  // the bg
  public WorldImage drawHexTLBorder(WorldImage bg, int x, int y, int cellWidth) {
    int xOffSet = 0;
    if (y % 2 == 0) {
      xOffSet = cellWidth / 2;
    }

    WorldImage line = new LineImage(new Posn(-cellWidth / 2, cellWidth / 2), Color.BLACK);

    return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, line,
        -(((x) * cellWidth) + xOffSet), -(((y) * cellWidth) - (cellWidth / 4) - 2), bg);
  }

  // draw a hexagon border bottom-right of the cell at the given x y coordinates
  // on the bg
  public WorldImage drawHexBRBorder(WorldImage bg, int x, int y, int cellWidth) {
    // TODO Auto-generated method stub
    int xOffSet = 0;
    if (y % 2 == 0) {
      xOffSet = cellWidth / 2;
    }

    WorldImage line = new LineImage(new Posn(-cellWidth / 2, cellWidth / 2), Color.BLACK);

    return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, line,
        -(((x) * cellWidth) + (cellWidth / 2) + xOffSet),
        -(((y) * cellWidth) + (3 * cellWidth / 4)), bg);
  }

  // draw a hexagon border bottom-left of the cell at the given x y coordinates on
  // the bg
  public WorldImage drawHexBLBorder(WorldImage bg, int x, int y, int cellWidth) {
    int xOffSet = 0;
    if (y % 2 == 0) {
      xOffSet = cellWidth / 2;
    }

    WorldImage line = new LineImage(new Posn(cellWidth / 2, cellWidth / 2), Color.BLACK);

    return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, line,
        -(((x) * cellWidth) + xOffSet), -(((y) * cellWidth) + (3 * cellWidth / 4)), bg);
  }

}