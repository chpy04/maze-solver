import java.awt.Color;
import javalib.worldimages.*;
import tester.*;
import java.util.*;

class ExamplesMaze {
  // vertical cells
  Cell topCell;
  Cell botCell;
  Edge vertEdge;
  Cell leftCell;
  Cell rightCell;
  Edge horizEdge;
  HexCell TRCell;
  HexCell TLCell;
  HexCell BRCell;
  HexCell BLCell;
  Edge botLeftEdge;
  Edge botRightEdge;
  Border border;
  Cell endCell;
  EdgeComparator ec;
  WorldImage bg;
  WorldImage bg2;
  Maze maze;
  Maze twoByOne;
  HexCell leftHexCell;
  HexCell rightHexCell;

  Maze twoByOneHex;
  private final int BORDERWIDTH = 2;
  private final Color BORDERCOLOR = Color.BLACK;

  // testing very large mazes may be slow to draw removing the edges initially.
  // If so, press 'g' to auto remove the walls.
  // Otherwise, you can wait for it to draw. The actual solving of the maze is
  // very quick even in
  // large sizes.
  // Solving the maze can be sped up with the speed modifier keys, which are
  // listed in the
  // control menu
  //void testBigBang(Tester t) {
  //  Game g = new Game(100, 60, .5, false, 1000, 600);
  //  g.bigBang(1000, 600, .01);
  //}

  // Additionally, Solving the maze can be sped
  // up with the speed modifier keys, which are listed in the control menu

  // Small Hexagon
  //void testBigBangSmallHexagon(Tester t) {
  //  Game g = new Game(15, 25, .5, true, 1000, 600);
  //  g.bigBang(1000, 600, .000001);
  //}

  // Small Square
  //void testBigBangSmallSquare(Tester t) {
  //  Game g = new Game(15, 25, .5, false, 1000, 600);
  //  g.bigBang(1000, 600, .000001);
  //}

  // Medium Hexagon
  // If running slow, press 'g' to remove all walls, press '.' to increase speed
  // 1.5x each press.
  // Note: we believe that the slowness is due to drawing issues, not calculation
  // issues.
  // When we cycle between heatmaps, the entire maze is searched and that switch
  // is very quick,
  // meaning the searching is quick and drawing is what is slow.
  //void testBigBangMediumHexagon(Tester t) {
  //  Game g = new Game(45, 35, .5, true, 1000, 600);
  //  g.bigBang(1000, 600, .000001);
  //}

  // Medium Square
  //void testBigBangMediumSquare(Tester t) {
  //  Game g = new Game(45, 35, .5, false, 1000, 600);
  //  g.bigBang(1000, 600, .000001);
  //}

  // Large maze (be patient with drawing, or press g to speed up initial removal).
  // Also,
  // speed up actual searching by 1.5x each time you press '.'
  //void testBigBangLarge(Tester t) {
  //  Game g = new Game(80, 50, .5, false, 1000, 600);
  //  g.bigBang(1000, 600, .000001);
  //}

  public void resetVars() {
    topCell = new Cell(false);
    botCell = new Cell(false);
    vertEdge = new Edge(topCell, botCell, 3, false, false, false);

    leftCell = new Cell(false);
    rightCell = new Cell(true);
    horizEdge = new Edge(leftCell, rightCell, 6, true, false, false);

    leftHexCell = new HexCell(false);
    rightHexCell = new HexCell(true);

    TLCell = new HexCell(false);
    TRCell = new HexCell(true);
    BLCell = new HexCell(false);
    BRCell = new HexCell(true);
    botLeftEdge = new Edge(TRCell, BLCell, 2, false, true, false);
    botRightEdge = new Edge(TLCell, BRCell, 10, false, false, true);

    horizEdge = new Edge(leftCell, rightCell, 6, true, false, false);
    border = new Border();

    ec = new EdgeComparator();

    bg = new RectangleImage(970, 570, OutlineMode.OUTLINE, Color.WHITE);

    bg2 = new RectangleImage(970, 570, OutlineMode.OUTLINE, Color.WHITE);

    endCell = new Cell(true);

    maze = new Maze(2, 2, 0.5, false);

    twoByOne = new Maze(2, 1, 0.5, false);

    twoByOneHex = new Maze(2, 1, 0.5, true);
  }

  // Edge Class
  public void testConnectTheseCells(Tester t) {
    resetVars();

    // vertical cells
    // initially, there are no visitable edges
    t.checkExpect(topCell.collectEdges(), new ArrayList<Edge>());
    t.checkExpect(botCell.collectEdges(), new ArrayList<Edge>());
    ArrayList<Edge> edges = new ArrayList<Edge>(List.of(vertEdge));
    vertEdge.connectTheseCells();
    t.checkExpect(topCell.collectEdges(), edges);
    t.checkExpect(botCell.collectEdges(), edges);

    // horizontal cells
    // initially, there are no visitable edges
    t.checkExpect(leftCell.collectEdges(), new ArrayList<Edge>());
    t.checkExpect(rightCell.collectEdges(), new ArrayList<Edge>());
    ArrayList<Edge> horizEdges = new ArrayList<Edge>(List.of(horizEdge));
    horizEdge.connectTheseCells();

    t.checkExpect(leftCell.collectEdges(), horizEdges);
    t.checkExpect(rightCell.collectEdges(), horizEdges);

    t.checkExpect(TRCell.collectEdges(), new ArrayList<Edge>());
    t.checkExpect(BLCell.collectEdges(), new ArrayList<Edge>());
    ArrayList<Edge> BLedges = new ArrayList<Edge>(List.of(botLeftEdge));
    botLeftEdge.connectTheseCells();
    t.checkExpect(TRCell.collectEdges(), BLedges);
    t.checkExpect(BLCell.collectEdges(), BLedges);

    t.checkExpect(TLCell.collectEdges(), new ArrayList<Edge>());
    t.checkExpect(BRCell.collectEdges(), new ArrayList<Edge>());
    ArrayList<Edge> BRedges = new ArrayList<Edge>(List.of(botRightEdge));
    botRightEdge.connectTheseCells();
    t.checkExpect(TLCell.collectEdges(), BRedges);
    t.checkExpect(BRCell.collectEdges(), BRedges);
  }

  public void testConnectNodesInHash(Tester t) {
    resetVars();
    Cell a = new Cell(false);
    Cell b = new Cell(false);
    Cell c = new Cell(false);
    Cell d = new Cell(false);
    Cell e = new Cell(false);

    HashMap<ACell, ACell> nodes = new HashMap<ACell, ACell>();
    nodes.put(a, a);
    nodes.put(b, b);
    nodes.put(c, c);
    nodes.put(d, d);
    nodes.put(e, e);

    Edge edge = new Edge(a, b, 0, true, false, false);
    t.checkExpect(edge.connectNodesInHash(nodes), true);
    t.checkExpect(nodes.get(a), b);
    t.checkExpect(nodes.get(b), b);

    Edge edge1 = new Edge(c, d, 0, true, false, false);
    t.checkExpect(edge1.connectNodesInHash(nodes), true);
    t.checkExpect(nodes.get(c), d);
    t.checkExpect(nodes.get(d), d);

    Edge edge2 = new Edge(c, a, 0, true, false, false);
    t.checkExpect(edge2.connectNodesInHash(nodes), true);
    t.checkExpect(nodes.get(d), b);
    t.checkExpect(nodes.get(c), d);
    t.checkExpect(nodes.get(a), b);

    Edge edge3 = new Edge(b, d, 0, true, false, false);
    t.checkExpect(edge3.connectNodesInHash(nodes), false);
    t.checkExpect(nodes.get(d), b);
    t.checkExpect(nodes.get(c), d);
    t.checkExpect(nodes.get(b), b);
  }

  public void testAddToHashIfPossible(Tester t) {
    resetVars();
    Cell a = new Cell(false);
    Cell b = new Cell(false);
    Cell c = new Cell(false);
    Cell d = new Cell(false);
    Cell e = new Cell(false);

    HashMap<ACell, ACell> nodes = new HashMap<ACell, ACell>();
    nodes.put(a, a);
    nodes.put(b, a);
    nodes.put(c, c);
    nodes.put(d, d);
    nodes.put(e, e);

    Edge edge1 = new Edge(a, b, 0, true, false, false);
    Edge edge2 = new Edge(c, b, 0, true, false, false);
    Edge edge3 = new Edge(d, a, 0, true, false, false);
    Edge edge4 = new Edge(b, e, 0, true, false, false);

    Deque<ACell> deque = new LinkedList<ACell>();

    edge1.addToHashIfPossible(a, nodes, deque, false);
    t.checkExpect(nodes.get(a), a);
    edge2.addToHashIfPossible(b, nodes, deque, false);
    t.checkExpect(nodes.get(c), a);
    edge3.addToHashIfPossible(d, nodes, deque, false);
    t.checkExpect(nodes.get(d), a);
    edge4.addToHashIfPossible(e, nodes, deque, false);
    t.checkExpect(nodes.get(e), b);
  }

  public void testDrawRightBorder(Tester t) {
    resetVars();
    // Edge
    // attempting to draw borders on edges returns the background unchanged
    t.checkExpect(vertEdge.drawRightBorder(bg, 30, 40, 10), bg);
    t.checkExpect(horizEdge.drawRightBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botLeftEdge.drawRightBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botRightEdge.drawRightBorder(bg, 30, 40, 10), bg);
    // Border
    WorldImage rect = new RectangleImage(BORDERWIDTH, 10 + BORDERWIDTH, OutlineMode.SOLID,
        BORDERCOLOR);
    t.checkExpect(border.drawRightBorder(bg, 30, 40, 10),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, rect, -309, -399, bg));
  }

  public void testDrawBottomBorder(Tester t) {
    resetVars();
    // Edge
    // attempting to draw borders on edges returns the background unchanged
    t.checkExpect(vertEdge.drawBottomBorder(bg, 30, 40, 10), bg);
    t.checkExpect(horizEdge.drawBottomBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botLeftEdge.drawRightBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botRightEdge.drawRightBorder(bg, 30, 40, 10), bg);
    // Border

    WorldImage rect = new RectangleImage(10 + BORDERWIDTH, BORDERWIDTH, OutlineMode.SOLID,
        BORDERCOLOR);
    t.checkExpect(border.drawBottomBorder(bg, 30, 40, 10),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, rect, -299, -409, bg));
  }

  public void testDrawLeftBorder(Tester t) {
    resetVars();
    // Edge
    // attempting to draw borders on edges returns the background unchanged
    t.checkExpect(vertEdge.drawLeftBorder(bg, 30, 40, 10), bg);
    t.checkExpect(horizEdge.drawLeftBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botLeftEdge.drawRightBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botRightEdge.drawRightBorder(bg, 30, 40, 10), bg);
    // Border
    WorldImage rect = new RectangleImage(BORDERWIDTH, 10 + BORDERWIDTH, OutlineMode.SOLID,
        BORDERCOLOR);
    t.checkExpect(border.drawLeftBorder(bg, 30, 40, 10),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, rect, -299, -399, bg));
  }

  public void testDrawTopBorder(Tester t) {
    resetVars();
    // Edge
    // attempting to draw borders on edges returns the background unchanged
    t.checkExpect(vertEdge.drawTopBorder(bg, 30, 40, 10), bg);
    t.checkExpect(horizEdge.drawTopBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botLeftEdge.drawRightBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botRightEdge.drawRightBorder(bg, 30, 40, 10), bg);
    // Border
    WorldImage rect = new RectangleImage(10 + BORDERWIDTH, BORDERWIDTH, OutlineMode.SOLID,
        BORDERCOLOR);
    t.checkExpect(border.drawTopBorder(bg, 30, 40, 10),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, rect, -299, -399, bg));
  }

  public void testDrawRighHexBorder(Tester t) {
    resetVars();
    // Edge
    // attempting to draw borders on edges returns the background unchanged
    t.checkExpect(vertEdge.drawTopBorder(bg, 30, 40, 10), bg);
    t.checkExpect(horizEdge.drawTopBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botLeftEdge.drawRightBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botRightEdge.drawRightBorder(bg, 30, 40, 10), bg);

    WorldImage line = new LineImage(new Posn(0, 5), Color.BLACK);
    t.checkExpect(border.drawHexRightBorder(bg, 30, 40, 10),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, line, -315, -402, bg));
    WorldImage line2 = new LineImage(new Posn(0, 5), Color.BLACK);
    t.checkExpect(border.drawHexRightBorder(bg, 30, 41, 10),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, line2, -310, -412, bg));
  }

  public void testdrawHexLeftBorder(Tester t) {
    resetVars();
    // Edge
    // attempting to draw borders on edges returns the background unchanged
    t.checkExpect(vertEdge.drawTopBorder(bg, 30, 40, 10), bg);
    t.checkExpect(horizEdge.drawTopBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botLeftEdge.drawRightBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botRightEdge.drawRightBorder(bg, 30, 40, 10), bg);

    WorldImage line = new LineImage(new Posn(0, 5), Color.BLACK);
    t.checkExpect(border.drawHexLeftBorder(bg, 30, 40, 10),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, line, -305, -402, bg));
    WorldImage line2 = new LineImage(new Posn(0, 5), Color.BLACK);
    t.checkExpect(border.drawHexLeftBorder(bg, 30, 41, 10),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, line2, -300, -412, bg));
  }

  public void testdrawHexTRBorder(Tester t) {
    resetVars();
    // Edge
    // attempting to draw borders on edges returns the background unchanged
    t.checkExpect(vertEdge.drawTopBorder(bg, 30, 40, 10), bg);
    t.checkExpect(horizEdge.drawTopBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botLeftEdge.drawRightBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botRightEdge.drawRightBorder(bg, 30, 40, 10), bg);

    WorldImage line = new LineImage(new Posn(5, 5), Color.BLACK);
    t.checkExpect(border.drawHexTRBorder(bg, 30, 40, 10),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, line, -310, -396, bg));
    WorldImage line2 = new LineImage(new Posn(5, 5), Color.BLACK);
    t.checkExpect(border.drawHexTRBorder(bg, 30, 41, 10),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, line2, -305, -406, bg));
  }

  public void testdrawHexTLBorder(Tester t) {
    resetVars();
    // Edge
    // attempting to draw borders on edges returns the background unchanged
    t.checkExpect(vertEdge.drawTopBorder(bg, 30, 40, 10), bg);
    t.checkExpect(horizEdge.drawTopBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botLeftEdge.drawRightBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botRightEdge.drawRightBorder(bg, 30, 40, 10), bg);

    WorldImage line = new LineImage(new Posn(-5, 5), Color.BLACK);
    t.checkExpect(border.drawHexTLBorder(bg, 30, 40, 10),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, line, -305, -396, bg));
    WorldImage line2 = new LineImage(new Posn(-5, 5), Color.BLACK);
    t.checkExpect(border.drawHexTLBorder(bg, 30, 41, 10),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, line2, -300, -406, bg));
  }

  public void testdrawHexBRBorder(Tester t) {
    resetVars();
    // Edge
    // attempting to draw borders on edges returns the background unchanged
    t.checkExpect(vertEdge.drawTopBorder(bg, 30, 40, 10), bg);
    t.checkExpect(horizEdge.drawTopBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botLeftEdge.drawRightBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botRightEdge.drawRightBorder(bg, 30, 40, 10), bg);

    WorldImage line = new LineImage(new Posn(-5, 5), Color.BLACK);
    t.checkExpect(border.drawHexBRBorder(bg, 30, 40, 10),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, line, -310, -407, bg));
    WorldImage line2 = new LineImage(new Posn(-5, 5), Color.BLACK);
    t.checkExpect(border.drawHexBRBorder(bg, 30, 41, 10),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, line2, -305, -417, bg));
  }

  public void testdrawHexBLBorder(Tester t) {
    resetVars();
    // Edge
    // attempting to draw borders on edges returns the background unchanged
    t.checkExpect(vertEdge.drawTopBorder(bg, 30, 40, 10), bg);
    t.checkExpect(horizEdge.drawTopBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botLeftEdge.drawRightBorder(bg, 30, 40, 10), bg);
    t.checkExpect(botRightEdge.drawRightBorder(bg, 30, 40, 10), bg);

    WorldImage line = new LineImage(new Posn(5, 5), Color.BLACK);
    t.checkExpect(border.drawHexBLBorder(bg, 30, 40, 10),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, line, -305, -407, bg));
    WorldImage line2 = new LineImage(new Posn(5, 5), Color.BLACK);
    t.checkExpect(border.drawHexBLBorder(bg, 30, 41, 10),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, line2, -300, -417, bg));
  }

  public void testAddIfEdge(Tester t) {
    resetVars();
    // Edge
    ArrayList<Edge> edges = new ArrayList<Edge>();
    // adds this IEdge piece, since it is an edge
    vertEdge.addIfEdge(edges);
    t.checkExpect(edges, new ArrayList<Edge>(List.of(vertEdge)));
    // can add duplicates
    vertEdge.addIfEdge(edges);
    t.checkExpect(edges, new ArrayList<Edge>(List.of(vertEdge, vertEdge)));
    // Border
    ArrayList<Edge> borderEdges = new ArrayList<Edge>();
    // fails to add this IEdge piece, since it is a border
    t.checkExpect(borderEdges, new ArrayList<Edge>());
    border.addIfEdge(borderEdges);
    t.checkExpect(borderEdges, new ArrayList<Edge>());
  }

  public void testWeightDifference(Tester t) {
    resetVars();
    // Edge
    // order matters, vertEdge = 3, horizEdge = 6, that - this = 3
    t.checkExpect(vertEdge.weightDifference(horizEdge), 3);
    // order matters, vertEdge = 3, horizEdge = 6, that - this = -3
    t.checkExpect(horizEdge.weightDifference(vertEdge), -3);

    t.checkExpect(botRightEdge.weightDifference(vertEdge), -7);
    t.checkExpect(botLeftEdge.weightDifference(botRightEdge), 8);

    // any edge with itself is 0
    t.checkExpect(vertEdge.weightDifference(vertEdge), 0);
    t.checkExpect(horizEdge.weightDifference(horizEdge), 0);
  }

  public void testNextUserCell(Tester t) {
    resetVars();
    // Edge
    // returns whichever cell on the edge that the passed cell is not,
    // following the constraint that the passed cell is one of the two cells
    // in this edge.
    // vertical edge
    t.checkExpect(vertEdge.nextUserCell(topCell), botCell);
    t.checkExpect(vertEdge.nextUserCell(botCell), topCell);
    // horizontal edge
    t.checkExpect(horizEdge.nextUserCell(leftCell), rightCell);
    t.checkExpect(horizEdge.nextUserCell(rightCell), leftCell);

    t.checkExpect(botLeftEdge.nextUserCell(TRCell), BLCell);
    t.checkExpect(botLeftEdge.nextUserCell(BLCell), TRCell);

    t.checkExpect(botRightEdge.nextUserCell(TLCell), BRCell);
    t.checkExpect(botRightEdge.nextUserCell(BRCell), TLCell);

    // Border
    // since borders cannot be moved on to, they always return the 'coming from'
    // cell
    t.checkExpect(border.nextUserCell(topCell), topCell);
    t.checkExpect(border.nextUserCell(rightCell), rightCell);
  }

  public void testEdgeComparator(Tester t) {
    resetVars();
    // result of comparing the two edges is always the same as invoking
    // weightDifference
    t.checkExpect(ec.compare(vertEdge, horizEdge), vertEdge.weightDifference(horizEdge));
    t.checkExpect(ec.compare(horizEdge, vertEdge), horizEdge.weightDifference(vertEdge));
  }

  // Cell Tests
  public void testStartMaxDist(Tester t) {
    resetVars();
    // topCell has an initial distanceFromStart of 0, so anything > 0 will be
    // returned
    t.checkExpect(topCell.startMaxDist(2), 2);
    // while a distance of -1 doesn't make sense, it displays the functionality
    t.checkExpect(topCell.startMaxDist(-1), 0);

    // incrementing distanceFromStart
    topCell.calcStartDistances(topCell);
    topCell.calcStartDistances(topCell);
    topCell.calcStartDistances(topCell);

    // now that it is incremented, it is greater than 2 and is returned
    t.checkExpect(topCell.startMaxDist(2), 3);
  }

  public void testEndMaxDist(Tester t) {
    resetVars();
    // topCell has an initial distanceFromEnd of 0, so anything > 0 will be returned
    t.checkExpect(topCell.endMaxDist(2), 2);
    // while a distance of -1 doesn't make sense, it displays the functionality
    t.checkExpect(topCell.endMaxDist(-1), 0);

    // incrementing distanceFromStart
    topCell.calcEndDistances(topCell);
    topCell.calcEndDistances(topCell);
    topCell.calcEndDistances(topCell);

    // now that it is incremented, it is greater than 2 and is returned
    t.checkExpect(topCell.endMaxDist(2), 3);
  }

  public void testCalcStartDistances(Tester t) {
    resetVars();
    // topCell initially has a distanceFromStart of 0, so 2 is returned
    t.checkExpect(topCell.startMaxDist(2), 2);
    topCell.calcStartDistances(topCell);
    topCell.calcStartDistances(topCell);
    topCell.calcStartDistances(topCell);
    // calcStartDistances sets this distanceFromStart to the distanceFromStart field
    // of the passed cell + 1,
    // so if this is the same as the passed cell, it effectively increments its by
    // 1.
    t.checkExpect(topCell.startMaxDist(2), 3);

    // Note that the passed cell does not need to be the same cell as this
    t.checkExpect(botCell.startMaxDist(2), 2);
    botCell.calcStartDistances(topCell);
    t.checkExpect(botCell.startMaxDist(2), 4);
  }

  public void testChangeVistedPathColor(Tester t) {
    resetVars();

    WorldImage rect = new RectangleImage(10, 10, OutlineMode.SOLID, Color.WHITE);

    // the image drawn of this cell
    // should be White by default, when drawn with VisitedPath DrawType enabled
    t.checkExpect(topCell.drawCellOntoBackground(0, 0, 10, bg, DrawType.VisitedPath, 3, 3),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, rect, 0, 0, bg));

    // after changing the visited path color to Red, the image drawn of this cell
    // should be Red, when drawn with VisitedPath DrawType enabled
    topCell.changeVistedPathColor(Color.RED);
    rect = new RectangleImage(10, 10, OutlineMode.SOLID, Color.RED);

    t.checkExpect(topCell.drawCellOntoBackground(0, 0, 10, bg, DrawType.VisitedPath, 3, 3),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, rect, 0, 0, bg));
  }

  public void testCalcEndDistances(Tester t) {
    resetVars();
    // topCell initially has a distanceFromEnd of 0, so 2 is returned
    t.checkExpect(topCell.endMaxDist(2), 2);
    topCell.calcEndDistances(topCell);
    topCell.calcEndDistances(topCell);
    topCell.calcEndDistances(topCell);
    // calcEndDistances sets this distanceFromEnd to the distanceFromEnd field of
    // the passed cell + 1,
    // so if this is the same as the passed cell, it effectively increments its by
    // 1.
    t.checkExpect(topCell.endMaxDist(2), 3);

    // Note that the passed cell does not need to be the same cell as this
    t.checkExpect(botCell.endMaxDist(2), 2);
    botCell.calcEndDistances(topCell);
    t.checkExpect(botCell.endMaxDist(2), 4);
  }
  
  public void testIsEnd(Tester t) {
    resetVars();
    // topCell.end == false, so isEnd() returns false
    t.checkExpect(topCell.isEnd(), false);
    // endCell.end == true, so isEnd() returns true
    t.checkExpect(endCell.isEnd(), true);
  }

  public void testConnectRight(Tester t) {
    resetVars();
    t.checkExpect(leftCell.collectEdges(), new ArrayList<Edge>());
    // connecting right changes the right field of a cell from its current edge to
    // the passed edge,
    // in this case replacing the border
    leftCell.connectRight(horizEdge);
    t.checkExpect(leftCell.collectEdges(), new ArrayList<Edge>(List.of(horizEdge)));
    // connecting it to a different edge replaces the previous one
    leftCell.connectRight(vertEdge);
    t.checkExpect(leftCell.collectEdges(), new ArrayList<Edge>(List.of(vertEdge)));

    TRCell.connectRight(botLeftEdge);
    t.checkExpect(TRCell.collectEdges(), new ArrayList<Edge>(List.of(botLeftEdge)));

    TLCell.connectRight(botRightEdge);
    t.checkExpect(TLCell.collectEdges(), new ArrayList<Edge>(List.of(botRightEdge)));
  }

  public void testConnectLeft(Tester t) {
    resetVars();
    t.checkExpect(rightCell.collectEdges(), new ArrayList<Edge>());
    // connecting left changes the left field of a cell from its current edge to the
    // passed edge,
    // in this case replacing the border
    rightCell.connectLeft(horizEdge);
    t.checkExpect(rightCell.collectEdges(), new ArrayList<Edge>(List.of(horizEdge)));
    // connecting it to a different edge replaces the previous one
    rightCell.connectLeft(vertEdge);
    t.checkExpect(rightCell.collectEdges(), new ArrayList<Edge>(List.of(vertEdge)));

    TRCell.connectLeft(botLeftEdge);
    t.checkExpect(TRCell.collectEdges(), new ArrayList<Edge>(List.of(botLeftEdge)));

    TLCell.connectLeft(botRightEdge);
    t.checkExpect(TLCell.collectEdges(), new ArrayList<Edge>(List.of(botRightEdge)));
  }

  public void testConnectDown(Tester t) {
    resetVars();
    t.checkExpect(topCell.collectEdges(), new ArrayList<Edge>());
    // connecting down changes the down field of a cell from its current edge to the
    // passed edge,
    // in this case replacing the border
    topCell.connectDown(horizEdge);
    t.checkExpect(topCell.collectEdges(), new ArrayList<Edge>(List.of(horizEdge)));
    // connecting it to a different edge replaces the previous one
    topCell.connectDown(vertEdge);
    t.checkExpect(topCell.collectEdges(), new ArrayList<Edge>(List.of(vertEdge)));

    t.checkException(new IllegalStateException("cannont connect hexagon cells down"), TRCell,
        "connectDown", botRightEdge);
  }

  public void testConnectUp(Tester t) {
    resetVars();
    t.checkExpect(botCell.collectEdges(), new ArrayList<Edge>());
    // connecting up changes the up field of a cell from its current edge to the
    // passed edge,
    // in this case replacing the border
    botCell.connectUp(horizEdge);
    t.checkExpect(botCell.collectEdges(), new ArrayList<Edge>(List.of(horizEdge)));
    // connecting it to a different edge replaces the previous one
    botCell.connectUp(vertEdge);
    t.checkExpect(botCell.collectEdges(), new ArrayList<Edge>(List.of(vertEdge)));

    t.checkException(new IllegalStateException("cannont connect hexagon cells up"), TRCell,
        "connectUp", botRightEdge);
  }

  public void testConnectTR(Tester t) {
    resetVars();
    TRCell.connectTR(botLeftEdge);
    t.checkExpect(TRCell.collectEdges(), new ArrayList<Edge>(List.of(botLeftEdge)));

    TLCell.connectTR(botRightEdge);
    t.checkExpect(TLCell.collectEdges(), new ArrayList<Edge>(List.of(botRightEdge)));

    // attempting to connect a top right of a square cell results in an exception
    t.checkException(
        new IllegalStateException("a square cell does not have a top right to connect"), topCell,
        "connectTR", vertEdge);
  }

  public void testConnectTL(Tester t) {
    resetVars();
    TRCell.connectTL(botLeftEdge);
    t.checkExpect(TRCell.collectEdges(), new ArrayList<Edge>(List.of(botLeftEdge)));

    TLCell.connectTL(botRightEdge);
    t.checkExpect(TLCell.collectEdges(), new ArrayList<Edge>(List.of(botRightEdge)));

    // attempting to connect a top left of a square cell results in an exception
    t.checkException(new IllegalStateException("a square cell does not have a top left to connect"),
        topCell, "connectTL", vertEdge);
  }

  public void testConnectBR(Tester t) {
    resetVars();
    TRCell.connectBR(botLeftEdge);
    t.checkExpect(TRCell.collectEdges(), new ArrayList<Edge>(List.of(botLeftEdge)));

    TLCell.connectBR(botRightEdge);
    t.checkExpect(TLCell.collectEdges(), new ArrayList<Edge>(List.of(botRightEdge)));

    // attempting to connect a bottom right of a square cell results in an exception
    t.checkException(
        new IllegalStateException("a square cell does not have a bottom right to connect"), topCell,
        "connectBR", vertEdge);
  }

  public void testConnectBL(Tester t) {
    resetVars();
    TRCell.connectBL(botLeftEdge);
    t.checkExpect(TRCell.collectEdges(), new ArrayList<Edge>(List.of(botLeftEdge)));

    TLCell.connectBL(botRightEdge);
    t.checkExpect(TLCell.collectEdges(), new ArrayList<Edge>(List.of(botRightEdge)));

    // attempting to connect a bottom left of a square cell results in an exception
    t.checkException(
        new IllegalStateException("a square cell does not have a bottom left to connect"), topCell,
        "connectBL", vertEdge);
  }

  public void testDrawWallsOntoBackground(Tester t) {
    resetVars();
    // x == 1 & y == 1
    WorldImage newBackground = border.drawRightBorder(bg2, 1, 1, 10);
    newBackground = border.drawBottomBorder(newBackground, 1, 1, 10);
    newBackground = border.drawLeftBorder(newBackground, 1, 1, 10);
    newBackground = border.drawTopBorder(newBackground, 1, 1, 10);
    t.checkExpect(topCell.drawWallsOntoBackground(1, 1, 10, bg), newBackground);
    // x == 1 & y != 1
    resetVars();
    WorldImage newBackground2 = border.drawRightBorder(bg2, 1, 2, 10);
    newBackground2 = border.drawBottomBorder(newBackground2, 1, 2, 10);
    newBackground2 = border.drawLeftBorder(newBackground2, 1, 2, 10);
    t.checkExpect(topCell.drawWallsOntoBackground(1, 2, 10, bg), newBackground2);
    // x != 1 & y == 1
    resetVars();
    WorldImage newBackground3 = border.drawRightBorder(bg2, 2, 1, 10);
    newBackground3 = border.drawBottomBorder(newBackground3, 2, 1, 10);
    newBackground3 = border.drawTopBorder(newBackground3, 2, 1, 10);
    t.checkExpect(topCell.drawWallsOntoBackground(2, 1, 10, bg), newBackground3);
    // x != 1 & y != 1
    resetVars();
    WorldImage newBackground4 = border.drawRightBorder(bg2, 2, 2, 10);
    newBackground4 = border.drawBottomBorder(newBackground4, 2, 2, 10);
    t.checkExpect(topCell.drawWallsOntoBackground(2, 2, 10, bg), newBackground4);

    WorldImage newBackground5 = border.drawHexRightBorder(bg2, 1, 1, 10);
    newBackground5 = border.drawHexBRBorder(newBackground5, 1, 1, 10);
    newBackground5 = border.drawHexBLBorder(newBackground5, 1, 1, 10);
    newBackground5 = border.drawHexLeftBorder(newBackground5, 1, 1, 10);
    newBackground5 = border.drawHexTLBorder(newBackground5, 1, 1, 10);
    newBackground5 = border.drawHexTRBorder(newBackground5, 1, 1, 10);
    newBackground5 = border.drawHexTLBorder(newBackground5, 1, 1, 10);

    t.checkExpect(BRCell.drawWallsOntoBackground(1, 1, 10, bg), newBackground5);
    // x == 1 & y != 1
    resetVars();
    WorldImage newBackground6 = border.drawHexRightBorder(bg2, 1, 2, 10);
    newBackground6 = border.drawHexBRBorder(newBackground6, 1, 2, 10);
    newBackground6 = border.drawHexBLBorder(newBackground6, 1, 2, 10);
    newBackground6 = border.drawHexLeftBorder(newBackground6, 1, 2, 10);
    t.checkExpect(BRCell.drawWallsOntoBackground(1, 2, 10, bg), newBackground6);
    // x != 1 & y == 1
    resetVars();
    WorldImage newBackground7 = border.drawHexRightBorder(bg2, 2, 1, 10);
    newBackground7 = border.drawHexBRBorder(newBackground7, 2, 1, 10);
    newBackground7 = border.drawHexBLBorder(newBackground7, 2, 1, 10);
    newBackground7 = border.drawHexTRBorder(newBackground7, 2, 1, 10);
    newBackground7 = border.drawHexTLBorder(newBackground7, 2, 1, 10);
    t.checkExpect(BRCell.drawWallsOntoBackground(2, 1, 10, bg), newBackground7);
    // x != 1 & y != 1
    resetVars();
    WorldImage newBackground8 = border.drawHexRightBorder(bg2, 2, 2, 10);
    newBackground8 = border.drawHexBRBorder(newBackground8, 2, 2, 10);
    newBackground8 = border.drawHexBLBorder(newBackground8, 2, 2, 10);
    t.checkExpect(BRCell.drawWallsOntoBackground(2, 2, 10, bg), newBackground8);
  }

  public void testDrawUserOnBackground(Tester t) {
    resetVars();
    WorldImage circ = new CircleImage(3, OutlineMode.SOLID, Color.CYAN);
    t.checkExpect(topCell.drawUserOnBackground(15, 20, 10, bg),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, circ, -151, -201, bg));

    WorldImage circ2 = new CircleImage(3, OutlineMode.SOLID, Color.CYAN);
    t.checkExpect(BRCell.drawUserOnBackground(15, 20, 10, bg),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, circ2, -156, -201, bg));
  }

  public void testDrawCellOntoBackground(Tester t) {
    resetVars();
    // DrawType == visited path, sets color to current this.visitedPathColor,
    // which is white in this case
    Color cellColor = Color.WHITE;
    WorldImage rect = new RectangleImage(10, 10, OutlineMode.SOLID, cellColor);
    t.checkExpect(topCell.drawCellOntoBackground(15, 20, 10, bg, DrawType.VisitedPath, 3, 4),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, rect, -150, -200, bg));

    Color cellColor5 = new Color(255, 0, 255);
    WorldImage upperTri5 = new TriangleImage(new Posn(10 / 2, 0), new Posn(0, 10 / 2),
        new Posn(10, 10 / 2), OutlineMode.SOLID, cellColor5);
    WorldImage rect5 = new RectangleImage(10, 10 / 2, OutlineMode.SOLID, cellColor5);
    WorldImage lowerTri5 = new TriangleImage(new Posn(0, 0), new Posn(10 / 2, 10 / 2),
        new Posn(10, 0), OutlineMode.SOLID, cellColor5);
    WorldImage hex5 = new AboveAlignImage(AlignModeX.RIGHT, upperTri5, rect5, lowerTri5);

    t.checkExpect(BRCell.drawCellOntoBackground(15, 20, 10, bg, DrawType.VisitedPath, 3, 4),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, hex5, -155, -198, bg));
    // DrawType == distance from start, calculates color based on heatmap
    // in which the closer to start the more red
    resetVars();
    Color cellColor2 = new Color(255, 0, 0);
    WorldImage rect2 = new RectangleImage(10, 10, OutlineMode.SOLID, cellColor2);
    t.checkExpect(topCell.drawCellOntoBackground(15, 20, 10, bg, DrawType.DistanceFromStart, 3, 4),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, rect2, -150, -200, bg));

    Color cellColor6 = new Color(255, 0, 0);
    WorldImage upperTri6 = new TriangleImage(new Posn(10 / 2, 0), new Posn(0, 10 / 2),
        new Posn(10, 10 / 2), OutlineMode.SOLID, cellColor6);
    WorldImage rect6 = new RectangleImage(10, 10 / 2, OutlineMode.SOLID, cellColor6);
    WorldImage lowerTri6 = new TriangleImage(new Posn(0, 0), new Posn(10 / 2, 10 / 2),
        new Posn(10, 0), OutlineMode.SOLID, cellColor6);
    WorldImage hex6 = new AboveAlignImage(AlignModeX.RIGHT, upperTri6, rect6, lowerTri6);

    t.checkExpect(BRCell.drawCellOntoBackground(15, 20, 10, bg, DrawType.DistanceFromStart, 3, 4),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, hex6, -155, -198, bg));
    // DrawType == distance from end, calculates color based on heatmap
    // in which the closer end the more red
    resetVars();
    Color cellColor3 = new Color(255, 0, 0);
    WorldImage rect3 = new RectangleImage(10, 10, OutlineMode.SOLID, cellColor3);
    t.checkExpect(botCell.drawCellOntoBackground(15, 20, 10, bg, DrawType.DistanceFromEnd, 3, 4),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, rect3, -150, -200, bg));

    Color cellColor7 = new Color(255, 0, 0);
    WorldImage upperTri7 = new TriangleImage(new Posn(10 / 2, 0), new Posn(0, 10 / 2),
        new Posn(10, 10 / 2), OutlineMode.SOLID, cellColor7);
    WorldImage rect7 = new RectangleImage(10, 10 / 2, OutlineMode.SOLID, cellColor7);
    WorldImage lowerTri7 = new TriangleImage(new Posn(0, 0), new Posn(10 / 2, 10 / 2),
        new Posn(10, 0), OutlineMode.SOLID, cellColor7);
    WorldImage hex7 = new AboveAlignImage(AlignModeX.RIGHT, upperTri7, rect7, lowerTri7);

    t.checkExpect(BRCell.drawCellOntoBackground(15, 20, 10, bg, DrawType.DistanceFromEnd, 3, 4),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, hex7, -155, -198, bg));
    // else DrawType == Nothing, sets color to this.defaultColor, which in this case
    // is white
    resetVars();
    Color cellColor4 = Color.WHITE;
    WorldImage rect4 = new RectangleImage(10, 10, OutlineMode.SOLID, cellColor4);
    t.checkExpect(botCell.drawCellOntoBackground(15, 20, 10, bg, DrawType.Nothing, 3, 4),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, rect4, -150, -200, bg));

    Color cellColor8 = new Color(255, 0, 255);
    WorldImage upperTri8 = new TriangleImage(new Posn(10 / 2, 0), new Posn(0, 10 / 2),
        new Posn(10, 10 / 2), OutlineMode.SOLID, cellColor8);
    WorldImage rect8 = new RectangleImage(10, 10 / 2, OutlineMode.SOLID, cellColor8);
    WorldImage lowerTri8 = new TriangleImage(new Posn(0, 0), new Posn(10 / 2, 10 / 2),
        new Posn(10, 0), OutlineMode.SOLID, cellColor8);
    WorldImage hex8 = new AboveAlignImage(AlignModeX.RIGHT, upperTri8, rect8, lowerTri8);

    t.checkExpect(BRCell.drawCellOntoBackground(15, 20, 10, bg, DrawType.Nothing, 3, 4),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, hex8, -155, -198, bg));
  }

  public void testCollectEdges(Tester t) {
    resetVars();
    // initially, all edges are borders and are not collected
    t.checkExpect(topCell.collectEdges(), new ArrayList<Edge>());
    // connecting bottom edge makes it such that now that edge is collected
    vertEdge.connectTheseCells();
    t.checkExpect(topCell.collectEdges(), new ArrayList<Edge>(List.of(vertEdge)));
    // attempting to reconnect changes nothing
    vertEdge.connectTheseCells();
    t.checkExpect(topCell.collectEdges(), new ArrayList<Edge>(List.of(vertEdge)));
    // adding another edge will also collect that
    Cell topCell2 = new Cell(false);
    Edge vertEdge2 = new Edge(topCell2, topCell, 4, false, false, false);
    vertEdge2.connectTheseCells();
    t.checkExpect(topCell.collectEdges(), new ArrayList<Edge>(List.of(vertEdge, vertEdge2)));
  }

  public void testComputeNextUser(Tester t) {
    resetVars();
    // Everything except moving down from the topCell will return itself,
    // because it can only acess the botCell, by going down.
    t.checkExpect(topCell.computeNextUser("right"), topCell);
    t.checkExpect(topCell.computeNextUser("up"), topCell);
    t.checkExpect(topCell.computeNextUser("left"), topCell);
    t.checkExpect(topCell.computeNextUser("down"), botCell);

    TRCell.connectBL(botLeftEdge);
    BLCell.connectTR(botLeftEdge);

    t.checkExpect(TRCell.computeNextUser("a"), TRCell);
    t.checkExpect(TRCell.computeNextUser("w"), TRCell);
    t.checkExpect(TRCell.computeNextUser("e"), TRCell);
    t.checkExpect(TRCell.computeNextUser("d"), TRCell);
    t.checkExpect(TRCell.computeNextUser("x"), TRCell);
    t.checkExpect(TRCell.computeNextUser("z"), BLCell);

  }

  // Maze tests
  public void testCalcWallsImage(Tester t) {
    resetVars();

    WorldImage bg = new RectangleImage(970, 570, OutlineMode.OUTLINE, Color.WHITE);

    bg = leftCell.drawWallsOntoBackground(1, 1, 10, bg);
    bg = rightCell.drawWallsOntoBackground(2, 1, 10, bg);
    t.checkExpect(twoByOne.calcWallsImage(970, 570, 10), bg);

    WorldImage bg1 = new RectangleImage(970, 570, OutlineMode.OUTLINE, Color.WHITE);

    bg1 = leftHexCell.drawWallsOntoBackground(1, 1, 10, bg1);
    bg1 = rightHexCell.drawWallsOntoBackground(2, 1, 10, bg1);
    t.checkExpect(twoByOneHex.calcWallsImage(970, 570, 10), bg1);
  }

  public void testDraw(Tester t) {
    resetVars();

    int cellWidth = 190;

    WorldImage bg = new RectangleImage(970, 570, OutlineMode.OUTLINE, Color.WHITE);

    bg = twoByOne.calcWallsImage(970, 570, cellWidth);
    t.checkExpect(twoByOne.draw(970, 570, DrawType.Nothing), bg);

    WorldImage bg1 = new RectangleImage(970, 570, OutlineMode.OUTLINE, Color.WHITE);

    bg1 = twoByOneHex.calcWallsImage(970, 570, cellWidth);
    t.checkExpect(twoByOneHex.draw(970, 570, DrawType.Nothing), bg1);

    resetVars();
    twoByOne.computeMaze();
    // After computing a maze, this.minSpanningTree is empty, so a different branch
    // is met
    WorldImage bg2 = new RectangleImage(970, 570, OutlineMode.OUTLINE, Color.WHITE);

    bg2 = leftCell.drawCellOntoBackground(1, 1, cellWidth, bg2, DrawType.Nothing, 0, 0);
    bg2 = leftCell.drawUserOnBackground(1, 1, cellWidth, bg2);
    bg2 = rightCell.drawCellOntoBackground(2, 1, cellWidth, bg2, DrawType.Nothing, 0, 0);
    t.checkExpect(twoByOne.draw(970, 570, DrawType.Nothing), new OverlayOffsetAlign(AlignModeX.LEFT,
        AlignModeY.TOP, twoByOne.calcWallsImage(970, 570, cellWidth), 0, 0, bg2));

    twoByOneHex.computeMaze();
    // After computing a maze, this.minSpanningTree is empty, so a different branch
    // is met
    WorldImage bg3 = new RectangleImage(970, 570, OutlineMode.OUTLINE, Color.WHITE);

    bg3 = leftHexCell.drawCellOntoBackground(1, 1, cellWidth, bg3, DrawType.Nothing, 0, 0);
    bg3 = leftHexCell.drawUserOnBackground(1, 1, cellWidth, bg3);
    bg3 = rightHexCell.drawCellOntoBackground(2, 1, cellWidth, bg3, DrawType.Nothing, 0, 0);
    t.checkExpect(twoByOneHex.draw(970, 570, DrawType.Nothing),
        new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP,
            twoByOneHex.calcWallsImage(970, 570, cellWidth), 0, 0, bg3));
  }

  public void testComputeMaze(Tester t) {
    resetVars();

    // because walls are randomly generated, a 2x1 is the only maze that a wall
    // is guaranteed to be removed to the right, everytime, so we can write a unit
    // test

    t.checkExpect(twoByOne.moveUser("right"), false);
    // the user should now be able to move rightward since the cells have been
    // connected
    twoByOne.computeMaze();
    t.checkExpect(twoByOne.moveUser("right"), true);

    t.checkExpect(twoByOneHex.moveUser("d"), false);
    twoByOneHex.computeMaze();
    t.checkExpect(twoByOneHex.moveUser("d"), true);
  }

  public void testRemoveNextEdge(Tester t) {
    resetVars();
    // removing 3 of the edges in a 2x2 maze creates a minimum spanning tree
    t.checkExpect(maze.removeNextEdge(), false);
    t.checkExpect(maze.removeNextEdge(), false);
    t.checkExpect(maze.removeNextEdge(), false);
    // returns true when the spanning tree is empty, and calculates start distances
    t.checkExpect(maze.removeNextEdge(), true);

    t.checkExpect(twoByOne.moveUser("right"), false);
    // after removing an edge between two cells, one can now move between the two
    twoByOne.removeNextEdge();
    t.checkExpect(twoByOne.moveUser("right"), true);

    t.checkExpect(twoByOneHex.moveUser("d"), false);
    twoByOneHex.removeNextEdge();
    t.checkExpect(twoByOneHex.moveUser("d"), true);
  }

  public void testResetSolution(Tester t) {
    resetVars();
    // turns all cells white, clears prevCells and worklist

    // remove all edges in minimum spanning tree from this 2x2 maze
    maze.removeNextEdge();
    maze.removeNextEdge();
    maze.removeNextEdge();
    WorldImage mazeImage = maze.draw(970, 570, DrawType.Nothing);

    // goes one step further into solving maze, painting one cell red
    maze.nextMazeStep(true);
    maze.resetSolution();
    WorldImage resetMazeImage = maze.draw(970, 570, DrawType.Nothing);

    // the maze image when the maze just has all the walls broken down
    // is the same as the maze image being reset afterward
    t.checkExpect(mazeImage, resetMazeImage);
  }

  public void testNextMazeStep(Tester t) {
    resetVars();
    t.checkExpect(twoByOne.moveUser("right"), false);

    // after reaching the end cell, the previously visited path is turned red
    twoByOne.nextMazeStep(true);
    twoByOne.nextMazeStep(true);
    twoByOne.nextMazeStep(true);
    twoByOne.nextMazeStep(true);
    WorldImage bg2 = new RectangleImage(970, 570, OutlineMode.OUTLINE, Color.WHITE);
    leftCell.changeVistedPathColor(Color.RED);
    bg2 = leftCell.drawCellOntoBackground(1, 1, 190, bg2, DrawType.VisitedPath, 0, 0);
    bg2 = rightCell.drawCellOntoBackground(2, 1, 190, bg2, DrawType.VisitedPath, 0, 0);

    // t.checkExpect(twoByOne.draw(970, 570, DrawType.VisitedPath), bg2);
  }

  public void testMoveUser(Tester t) {
    resetVars();

    t.checkExpect(twoByOne.moveUser("right"), false);
    t.checkExpect(twoByOne.moveUser("left"), false);
    t.checkExpect(twoByOne.moveUser("up"), false);
    t.checkExpect(twoByOne.moveUser("down"), false);

    twoByOne.removeNextEdge();

    // t.checkExpect(twoByOne.userHasWon(), true);

    // after removing the middle edge, the user can only move rightward
    t.checkExpect(twoByOne.moveUser("left"), false);
    t.checkExpect(twoByOne.moveUser("up"), false);
    t.checkExpect(twoByOne.moveUser("down"), false);
    t.checkExpect(twoByOne.moveUser("right"), true);

    // one can see that the user has now won, indicating the user must have moved
    t.checkExpect(twoByOne.userHasWon(), true);

    // note that moveUser does not care if the user has won, the user can still move
    // through direct invocations of userHasWon(). Disabling this is through a
    // different
    // method.
    t.checkExpect(twoByOne.moveUser("left"), true);
  }

  public void testUserHasWon(Tester t) {
    resetVars();
    t.checkExpect(maze.userHasWon(), false);

    // creating a 2x1 maze, we can ensure that the first removed edge in the min
    // spanning tree
    // will be to the between the start and the end, so then we can move the user
    // rightward
    // onto the goal.
    twoByOne.removeNextEdge();
    twoByOne.moveUser("right");
    t.checkExpect(twoByOne.userHasWon(), true);
  }

  public void testSquareMazeCellGrid(Tester t) {
    resetVars();
    // need maze to call methods, but these parameters dont matter
    Maze sampleMaze = new Maze(5, 4, .5, false);
    ArrayList<Edge> edges = new ArrayList<Edge>();

    ArrayList<ArrayList<ACell>> sampleSquareGrid = sampleMaze.generateSquareMaze(5, 6, .5, edges);
    t.checkExpect(sampleSquareGrid.size(), 6);
    t.checkExpect(sampleSquareGrid.get(0).size(), 5);
    t.checkExpect(edges.size(), 49); // num of edges is 4 * 6 + 5 * 5 = 49
  }

  public void testHexMazeCellGrid(Tester t) {
    resetVars();
    // need maze to call methods, but these parameters dont matter
    Maze sampleMaze = new Maze(5, 4, .5, false);
    ArrayList<Edge> edges = new ArrayList<Edge>();

    ArrayList<ArrayList<ACell>> sampleSquareGrid = sampleMaze.generateHexMaze(5, 6, .5, edges);
    t.checkExpect(sampleSquareGrid.size(), 6);
    t.checkExpect(sampleSquareGrid.get(0).size(), 5);
    t.checkExpect(edges.size(), 69); // num of edges is 4 * 6 + 9 * 5 = 69
  }

}