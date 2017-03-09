import java.util.Arrays;

/**
 * Contains code to create a tic tac toe board and interact with it via Players.
 */
public class TicTacToe {

  // public static final int BOARD_SIZE = 5;
  // public static final int NUM_TURNS = BOARD_SIZE * BOARD_SIZE;

  public int BOARD_SIZE;
  public int NUM_TURNS;

  /**
   * Game board markers.
   */
  public enum Marker {
    EMPTY(0, ' '), X(1, 'X'), O(-1, 'O');

    public int val;
    public char mark;

    Marker(int val, char mark) {
      this.val = val;
      this.mark = mark;
    }

    public static Marker switchPlayer(Marker m) {
      if (m == X)
        return O;
      else if (m == O)
        return X;
      return EMPTY;
    }
  };


  /**
   * Initialize default tic tac toe board.
   */
  public TicTacToe(int size) {
    BOARD_SIZE = size;
    NUM_TURNS = BOARD_SIZE * BOARD_SIZE;

    // Create board and initialize positions to be empty.
    board = new Marker[BOARD_SIZE][BOARD_SIZE];

    for (Marker[] row : board) {
      Arrays.fill(row, Marker.EMPTY);
    }

    // Initialize sums of horizontals/verticals and diagonals.
    // (Board size rows + board size columns + 2 diagonals.
    sums = new int[2 * BOARD_SIZE + 2];
  }

  public Marker getMark(int row, int col) {
    return board[row][col];
  }

  /**
   * Set a marker at specified location.
   * @param row Which row the location is in.
   * @param col Which column the location is in.
   * @param mark Which marker to set.
   * @return False, if the location is already not Empty.
   *         True, if a new marker is set in the specified location.
   */
  public void setMark(int row, int col, Marker mark) throws Exception {
    if (row >= BOARD_SIZE || row < 0) {
      throw new Exception("Invalid move: row outside of board range.");
    }

    if (col >= BOARD_SIZE || col < 0) {
      throw new Exception("Invalid move: column outside of board range.");
    }

    if (board[row][col] != Marker.EMPTY) {
      throw new Exception("Invalid move: selected location is already marked.");
    }

    board[row][col] = mark;

    sums[row] += mark.val;
    sums[BOARD_SIZE + col] += mark.val;

    // Main diagonal.
    if (row == col)
      sums[2 * BOARD_SIZE] += mark.val;

    if (BOARD_SIZE - col - 1 == row)
      sums[2 * BOARD_SIZE + 1] += mark.val;
  }

  /**
   * Parses a move into a row #/column # pair.
   * @param move The 2-character string representing the move.
   *             1st char: [A-Z] representing row.
   *             2nd char: [0-9] representing column.
   * @return Array of the row/column pair.
   *         Index 0: row
   *         Index 1: column
   */
  public int[] parseMove(String move) throws Exception {
    if (move.length() != 2)
      throw new Exception("Invalid move format.");

    int row = move.charAt(0) - 'A';
    int column = move.charAt(1) - '1';
    int[] res = { row, column };
    return res;
  }

  /**
   * Checks to see if any player has won the game.
   * @return The Marker of the player who won, or null if there is no winner yet.
   */
  public Marker gameWon() {
    for (int s : sums) {
      if (s == BOARD_SIZE) {
        return Marker.X;
      }
      else if (s == -BOARD_SIZE) {
        return Marker.O;
      }
    }

    return Marker.EMPTY;
  }

  /**
   * Gets the raw array representing the current game board.
   * @return Raw Marker array of BOARD_SIZE x BOARD_SIZE size.
   */
  public Marker[][] getBoard() { return board; }

  public boolean empty() {
    for (Marker[] row : board) {
      for (Marker m : row) {
        if (m != Marker.EMPTY) {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Print a human-readable version of the current game board.
   */
  public void printBoard() {
    // Print column numbers.
    System.out.print("    ");
    for (int i = 0; i < BOARD_SIZE; i++) {
      System.out.printf(" %d  ", i+1);
    }
    System.out.println("\n");


    for (int i = 0; i < BOARD_SIZE; i++) {
      System.out.printf("%c    %c ", 'A' + i, board[i][0].mark);

      for (int j = 1; j < BOARD_SIZE; j++) {
        System.out.printf("| %c ", board[i][j].mark);
      }

      System.out.println("");

      if (i < BOARD_SIZE - 1) {
        System.out.print("    ");
        for (int k = 1; k < BOARD_SIZE * 3 + BOARD_SIZE; k++) {
          System.out.print("-");
        }
        System.out.println("");
      }
    }
    System.out.println("");
  }

  public int[] getSums() { return sums; }


  // 2D array representing game board. Valid values are 0 (empty), 1 (X), -1 (Y).
  private Marker[][] board;

  // Array representing current sums on the horizontals, verticals, and diagonals of the game board.
  private int[] sums;
}
