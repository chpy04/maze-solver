import java.awt.Color;
import java.util.*;
import javalib.worldimages.*;

//ACell is either a square or hexagonal cell in a maze game
abstract class ACell {
  // all Cells have distances from start and end, as well as colors.
  // because the Cell and HexCell classes need access to these fields,
  // they cannot be made private, so protected is the next best option

  // distances not final because they cannot be calculated until the maze
  // has been constructed
  private int distanceFromStart;
  private int distanceFromEnd;
  // colors not final because it changes when this cell is searched
  private Color defaultColor;
  private Color visitedPathColor;
  private final boolean end;

  ACell(boolean end) {
    this.distanceFromEnd = 0;
    this.distanceFromStart = 0;
    // if this cell is the end of the maze, draw it magenta, OW draw it white
    if (end) {
      this.defaultColor = Color.MAGENTA;
      this.visitedPathColor = Color.MAGENTA;
    }
    else {
      this.defaultColor = Color.WHITE;
      this.visitedPathColor = Color.WHITE;
    }
    this.end = end;
  }

  // given a prvious max distance from the start, return the maximum of the
  // prevMax and this cells distance from the start
  public int startMaxDist(int prevMax) {
    return Math.max(prevMax, this.distanceFromStart);
  }

  // given a prvious max distance from the end, return the maximum of the
  // prevMax and this cells distance from the end
  public int endMaxDist(int prevMax) {
    return Math.max(prevMax, this.distanceFromEnd);
  }

  // given the cell that this cell is coming from starting from the start,
  // calculate this cells distance from the start, assuming the cell it is coming
  // from
  // has already calculated its distance from the start (add 1)
  void calcStartDistances(ACell prevCell) {
    this.distanceFromStart = prevCell.distanceFromStart + 1;
  }

  // given the cell that this cell is coming from starting from the end,
  // calculate this cells distance from the end, assuming the cell it is coming
  // from
  // has already calculated its distance from the end (add 1)
  void calcEndDistances(ACell prevCell) {
    this.distanceFromEnd = prevCell.distanceFromEnd + 1;
  }

  // change this cells visitedpath color to the given color
  void changeVistedPathColor(Color color) {
    if (!this.end) {
      this.visitedPathColor = color;
    }
  }

  // is this cell the end of the maze
  // for determining when to highlight the visited path and terminating searching
  public boolean isEnd() {
    return this.end;
  }

  // determines cell color for a cell in a maze based on draw type.
  // if heatmap is selected, distance from end and start is relevant
  public Color cellColor(DrawType drawType, int maxStartDist, int maxEndDist) {
    Color cellColor;
    if (drawType == DrawType.VisitedPath) {
      cellColor = this.visitedPathColor;
    }
    else if (drawType == DrawType.DistanceFromStart) {
      cellColor = new Color(255 - (this.distanceFromStart * 255 / maxStartDist), 0,
          (this.distanceFromStart * 255 / maxStartDist));
    }
    else if (drawType == DrawType.DistanceFromEnd) {
      cellColor = new Color(255 - (this.distanceFromEnd * 255 / maxEndDist), 0,
          (this.distanceFromEnd * 255 / maxEndDist));
    }
    else {
      cellColor = this.defaultColor;
    }

    return cellColor;
  }

  // connect this cells rightt to a given IEdge
  abstract void connectRight(IEdge other);

  // connect this cells left to a given IEdge
  abstract void connectLeft(IEdge other);

  // connect this cells down to a given IEdge
  abstract void connectDown(IEdge other);

  // connect this cells up to a given IEdge
  abstract void connectUp(IEdge other);

  // connect this cells top right to a given IEdge
  abstract void connectTR(IEdge other);

  // connect this cells top left to a given IEdge
  abstract void connectTL(IEdge other);

  // connect this cells bottom right to a given IEdge
  abstract void connectBR(IEdge other);

  // connect this cells bottom left to a given IEdge
  abstract void connectBL(IEdge other);

  // draw this cells walls onto the given background, asks this cells edges to
  // draw themselves
  // onto the background
  abstract WorldImage drawWallsOntoBackground(int x, int y, int cellWidth, WorldImage bg);

  // draw the user on this background if the user is in this cell
  abstract WorldImage drawUserOnBackground(int x, int y, int cellWidth, WorldImage bg);

  // draw this cell onto the background (does not draw walls), given a drawtype
  abstract WorldImage drawCellOntoBackground(int x, int y, int cellWidth, WorldImage bg,
      DrawType drawType, int maxStartDist, int maxEndDist);

  // collect all of the edges that this cell points to
  abstract ArrayList<Edge> collectEdges();

  // assuming the user is in this cell, compute the next cell the user would be in
  // for a given key input.
  abstract ACell computeNextUser(String key);
}

//A cell represents a coordinate in a maze, and points to edges in 
//each of its 4 directions
class Cell extends ACell {
  // because cells are initialized before the maze has been built,
  // their connections cannot be final because they change as walls
  // get knocked down
  private IEdge left;
  private IEdge right;
  private IEdge up;
  private IEdge down;

  Cell(boolean end) {
    super(end);
    this.left = new Border();
    this.right = new Border();
    this.up = new Border();
    this.down = new Border();
  }

  // connect this cells rightt to a given IEdge
  void connectRight(IEdge other) {
    this.right = other;
  }

  // connect this cells left to a given IEdge
  void connectLeft(IEdge other) {
    this.left = other;
  }

  // connect this cells down to a given IEdge
  void connectDown(IEdge other) {
    this.down = other;
  }

  // connect this cells up to a given IEdge
  void connectUp(IEdge other) {
    this.up = other;
  }

  // connect this cells top right to a given IEdge
  void connectTR(IEdge other) {
    throw new IllegalStateException("a square cell does not have a top right to connect");
  }

  // connect this cells top left to a given IEdge
  void connectTL(IEdge other) {
    throw new IllegalStateException("a square cell does not have a top left to connect");
  }

  // connect this cells bottom right to a given IEdge
  void connectBR(IEdge other) {
    throw new IllegalStateException("a square cell does not have a bottom right to connect");
  }

  // connect this cells bottom left to a given IEdge
  void connectBL(IEdge other) {
    throw new IllegalStateException("a square cell does not have a bottom left to connect");
  }

  // draw this cells walls onto the given background, asks this cells edges to
  // draw themselves
  // onto the background
  public WorldImage drawWallsOntoBackground(int x, int y, int cellWidth, WorldImage bg) {
    WorldImage newBackground = this.right.drawRightBorder(bg, x, y, cellWidth);
    newBackground = this.down.drawBottomBorder(newBackground, x, y, cellWidth);
    if (x == 1) {
      newBackground = this.left.drawLeftBorder(newBackground, x, y, cellWidth);
    }
    if (y == 1) {
      newBackground = this.up.drawTopBorder(newBackground, x, y, cellWidth);
    }
    return newBackground;
  }

  // draw the user on this background if the user is in this cell
  public WorldImage drawUserOnBackground(int x, int y, int cellWidth, WorldImage bg) {
    WorldImage circ = new CircleImage(cellWidth / 3, OutlineMode.SOLID, Color.CYAN);
    return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, circ,
        -(x * cellWidth) - cellWidth / 6, -(y * cellWidth) - cellWidth / 6, bg);
  }

  // draw this cell onto the background (does not draw walls), given a drawtype
  public WorldImage drawCellOntoBackground(int x, int y, int cellWidth, WorldImage bg,
      DrawType drawType, int maxStartDist, int maxEndDist) {
    Color cellColor = super.cellColor(drawType, maxStartDist, maxEndDist);

    WorldImage rect = new RectangleImage(cellWidth, cellWidth, OutlineMode.SOLID, cellColor);
    return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, rect, -(x * cellWidth),
        -(y * cellWidth), bg);
  }

  // collect all of the edges that this cell points to
  ArrayList<Edge> collectEdges() {
    ArrayList<Edge> ret = new ArrayList<Edge>();
    this.right.addIfEdge(ret);
    this.down.addIfEdge(ret);
    this.left.addIfEdge(ret);
    this.up.addIfEdge(ret);
    return ret;
  }

  // assuming the user is in this cell, compute the next cell the user would be in
  // for a given key input.
  ACell computeNextUser(String key) {
    if (key.equals("up")) {
      return this.up.nextUserCell(this);
    }
    else if (key.equals("left")) {
      return this.left.nextUserCell(this);
    }
    else if (key.equals("down")) {
      return this.down.nextUserCell(this);
    }
    else if (key.equals("right")) {
      return this.right.nextUserCell(this);
    }
    else {
      return this;
    }
  }
}

//A HexCell represents a coordinate in a maze, and points to edges in
//each of its 6 directions
class HexCell extends ACell {
  // because cells are initialized before the maze has been built,
  // their connections cannot be final because they change as walls
  // get knocked down
  private IEdge left;
  private IEdge right;
  private IEdge tr;
  private IEdge tl;
  private IEdge br;
  private IEdge bl;

  HexCell(boolean end) {
    super(end);
    this.left = new Border();
    this.right = new Border();
    this.tr = new Border();
    this.tl = new Border();
    this.br = new Border();
    this.bl = new Border();
  }

  // connect this cells rightt to a given IEdge
  void connectRight(IEdge other) {
    this.right = other;
  }

  // connect this cells left to a given IEdge
  void connectLeft(IEdge other) {
    this.left = other;
  }

  // connect this cells down to a given IEdge
  void connectDown(IEdge other) {
    throw new IllegalStateException("cannont connect hexagon cells down");
  }

  // connect this cells up to a given IEdge
  void connectUp(IEdge other) {
    throw new IllegalStateException("cannont connect hexagon cells up");
  }

  // connect the top right of this cell to a given edge
  void connectTR(IEdge other) {
    this.tr = other;
  }

  // connect the top left of this cell to a given edge
  void connectTL(IEdge other) {
    this.tl = other;
  }

  // connect the bottom right of this cell to a given edge
  void connectBR(IEdge other) {
    this.br = other;
  }

  // connect the bottom left of this cell to a given edge
  void connectBL(IEdge other) {
    this.bl = other;
  }

  // draw this cells walls onto the given background, asks this cells edges to
  // draw themselves
  // onto the background
  public WorldImage drawWallsOntoBackground(int x, int y, int cellWidth, WorldImage bg) {
    WorldImage newBackground = this.right.drawHexRightBorder(bg, x, y, cellWidth);
    // if the cell is on the edge of the image and in certain rows, certain edges
    // may or
    // may not need to be drawn
    newBackground = this.br.drawHexBRBorder(newBackground, x, y, cellWidth);
    newBackground = this.bl.drawHexBLBorder(newBackground, x, y, cellWidth);
    if (x == 1) {
      newBackground = this.left.drawHexLeftBorder(newBackground, x, y, cellWidth);
      if (y % 2 == 1) {
        newBackground = this.tl.drawHexTLBorder(newBackground, x, y, cellWidth);
      }
    }
    if (y == 1) {
      newBackground = this.tr.drawHexTRBorder(newBackground, x, y, cellWidth);
      newBackground = this.tl.drawHexTLBorder(newBackground, x, y, cellWidth);
    }
    return newBackground;
  }

  // draw the user on this background if the user is in this cell
  public WorldImage drawUserOnBackground(int x, int y, int cellWidth, WorldImage bg) {
    int xOffSet = 0;
    if (y % 2 == 0) {
      xOffSet = cellWidth / 2;
    }
    WorldImage circ = new CircleImage(cellWidth / 3, OutlineMode.SOLID, Color.CYAN);
    return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, circ,
        -(x * cellWidth) - cellWidth / 6 - xOffSet, -(y * cellWidth) - cellWidth / 6, bg);
  }

  // draw this cell onto the background (does not draw walls), given a drawtype
  public WorldImage drawCellOntoBackground(int x, int y, int cellWidth, WorldImage bg,
      DrawType drawType, int maxStartDist, int maxEndDist) {
    int xOffSet = 0;
    if (y % 2 == 0) {
      xOffSet = cellWidth / 2;
    }

    Color cellColor = super.cellColor(drawType, maxStartDist, maxEndDist);

    // hexagons are represented as two triangles and a rectangle because this is
    // much easier to
    // work with than the image library hexagons
    WorldImage upperTri = new TriangleImage(new Posn(cellWidth / 2, 0), new Posn(0, cellWidth / 2),
        new Posn(cellWidth, cellWidth / 2), OutlineMode.SOLID, cellColor);
    WorldImage rect = new RectangleImage(cellWidth, cellWidth / 2, OutlineMode.SOLID, cellColor);
    WorldImage lowerTri = new TriangleImage(new Posn(0, 0), new Posn(cellWidth / 2, cellWidth / 2),
        new Posn(cellWidth, 0), OutlineMode.SOLID, cellColor);

    WorldImage hex = new AboveAlignImage(AlignModeX.RIGHT, upperTri, rect, lowerTri);

    return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, hex,
        -((x * cellWidth) + xOffSet), -(y * cellWidth) + cellWidth / 4, bg);
  }

  // collect all of the edges that this cell points to
  ArrayList<Edge> collectEdges() {
    ArrayList<Edge> ret = new ArrayList<Edge>();
    this.right.addIfEdge(ret);
    this.tr.addIfEdge(ret);
    this.tl.addIfEdge(ret);
    this.left.addIfEdge(ret);
    this.br.addIfEdge(ret);
    this.bl.addIfEdge(ret);
    return ret;
  }

  // assuming the user is in this cell, compute the next cell the user would be in
  // for a given key input.
  ACell computeNextUser(String key) {
    if (key.equals("w")) {
      return this.tl.nextUserCell(this);
    }
    else if (key.equals("e")) {
      return this.tr.nextUserCell(this);
    }
    else if (key.equals("d")) {
      return this.right.nextUserCell(this);
    }
    else if (key.equals("a")) {
      return this.left.nextUserCell(this);
    }
    else if (key.equals("z")) {
      return this.bl.nextUserCell(this);
    }
    else if (key.equals("x")) {
      return this.br.nextUserCell(this);
    }
    else {
      return this;
    }
  }
}