
import java.util.Random;
import java.util.Scanner;

/**
 * Created by trevor1 on 3/6/17.
 */
public class Game {

  public Game() {
    scanner = new Scanner(System.in);
  }

  public void run() {
    boolean playAgain = true;

    while (playAgain) {
      System.out.println("Welcome to TicTacNole!\n");
      System.out.println("Instructions:");
      System.out.println("\t1. Game can be played with 1 player (player v. AI) or 2 players (player v. player).");
      System.out.println("\t2. Input moves by selecting row letter/column number.\n\t\tFor example, `B2` corresponds to the square in row B, column 2 (the center square).");
      System.out.println("\t3. If playing against a computer, the player to start is randomly selected.\n");

      System.out.print("Enter grid size (must be odd): (N x N) ");
      int gs = scanner.nextInt();
      scanner.nextLine();

      while (gs % 2 == 0) {
        System.out.print("Grid size must be odd. Try again: ");
        gs = scanner.nextInt();
        scanner.nextLine();
      }

      boolean useAi = false;
      System.out.print("Would you like to play against a computer? (y/n) ");
      String ans = scanner.nextLine().toLowerCase();

      while (!ans.equals("y") && !ans.equals("n")) {
        System.out.print("Oops, that's an invalid answer. Try again: (y/n) ");
        ans = scanner.nextLine().toLowerCase();
      }
      useAi = ans.equals("y");

      play(useAi, gs);

      System.out.print("Would you like to play again? (y/n) ");
      ans = scanner.nextLine().toLowerCase();

      while (!ans.equals("y") && !ans.equals("n")) {
        System.out.print("Oops, that's an invalid answer. Try again: (y/n) ");
        ans = scanner.nextLine().toLowerCase();
      }

      playAgain = ans.equals("y");
      System.out.println("");
    }

    System.out.println("\nThanks for playing. Goodbye!");

  }

  /**
   * Default play function without using AI.
   */
  public void play() {
    play(false, 3);
  }

  /**
   * Plays one game of tic tac toe.
   * @param ai If true, the user will play against an AI.
   */
  public void play(boolean ai, int gs) {
    board = new TicTacToe(gs);

    // Set X to start, by default, unless AI is enabled (in which case it is random).
    TicTacToe.Marker player;

    if (ai) {
      Random rand = new Random();
      switch (rand.nextInt(2)) {
        case 0:
          player = TicTacToe.Marker.X;
          break;
        case 1:
          player = TicTacToe.Marker.O;
          break;
        default:
          player = TicTacToe.Marker.X;
      }
    }
    else {
      player = TicTacToe.Marker.X;
    }

    for (int turn = 0; turn < board.NUM_TURNS; turn++) {
      System.out.println("");
      board.printBoard();

      // Handle AI player.
      if (player == TicTacToe.Marker.O && ai) {
        System.out.printf("\nThe AI is calculating its next move...\n", player.mark);
        try {
          Thread.sleep(1500);
        }
        catch (InterruptedException e) {

        }
        aiMarkBoard();
      }
      else {
        System.out.printf("\nIt is %c's turn. Enter your move (q to quit): ", player.mark);
        String line = scanner.nextLine();

        if (line.toLowerCase().equals("q")) {
          System.out.println("Quitting current game!\n");
          return;
        }

        markBoard(player, line);
      }

      TicTacToe.Marker winner = board.gameWon();
      if (winner != TicTacToe.Marker.EMPTY) {
        System.out.println("");
        board.printBoard();
        System.out.printf("\n%c wins!!!\n", winner.mark);
        return;
      }

      // Switch player.
      player = TicTacToe.Marker.switchPlayer(player);

    }

    // If we're here, there was a draw.
    System.out.println("");
    board.printBoard();
    System.out.println("It's a draw!!!\n");
  }

  /**
   * Determines where the AI will mark the board for a turn.
   * By default, tries to prevent a user winning over winning itself.
   */
  private void aiMarkBoard() {
    // For the sake of making the game easier, if O starts, it will select a random position.
    if (board.empty()) {
      Random rand = new Random();
      try {
        board.setMark(rand.nextInt(board.BOARD_SIZE), rand.nextInt(board.BOARD_SIZE), TicTacToe.Marker.O);
        return;
      }
      catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

    // Favor the middle slot if it is not taken yet.
    if (board.getBoard()[1][1] == TicTacToe.Marker.EMPTY) {
      try {
        board.setMark(1, 1, TicTacToe.Marker.O);
        return;
      }
      catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

    int largest = 0;
    for (int i = 0; i < board.getSums().length; i++) {
      if (board.getSums()[i] > board.getSums()[largest])
        largest = i;
    }


    // If AI is "winning", let's be greedy.
    int smallest = 0;
    for (int i = 0; i < board.getSums().length; i++) {
      if (board.getSums()[i] < board.getSums()[smallest])
        smallest = i;
    }

    Random rand = new Random();
    if (board.getSums()[smallest] < -(board.BOARD_SIZE / 2 + rand.nextInt(1)) && -(board.getSums()[smallest]) > board.getSums()[largest]) {
      largest = smallest;
    }
    // Choose a random slot if sum of row/col/diag is not more than half the board size.
    else if (board.getSums()[largest] < board.BOARD_SIZE / 2 + 1) {
      rand = new Random();
      try {
        board.setMark(rand.nextInt(board.BOARD_SIZE), rand.nextInt(board.BOARD_SIZE), TicTacToe.Marker.O);
        return;
      }
      catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }

    if (largest < board.BOARD_SIZE) {
      // It's a row.
      TicTacToe.Marker[] row = board.getBoard()[largest];

      for (int pos = 0; pos < row.length; pos++) {
        if (row[pos] == TicTacToe.Marker.EMPTY) {
          try {
            board.setMark(largest, pos, TicTacToe.Marker.O);
            return;
          }
          catch (Exception e) {
            System.out.println(e.getMessage());
          }
        }
      }
    }
    else if (largest >= board.BOARD_SIZE && largest < 2 * board.BOARD_SIZE) {
      int col = largest - board.BOARD_SIZE;

      for (int r = 0; r < board.BOARD_SIZE; r++) {
        try {
          board.setMark(r, col, TicTacToe.Marker.O);
          return;
        }
        catch (Exception e) {
          // Do nothing.
        }
      }
    }
    else {
      if (largest == board.BOARD_SIZE * 2) {
        for (int i = 0; i < board.BOARD_SIZE; i++) {
          try {
            board.setMark(i, i, TicTacToe.Marker.O);
            return;
          }
          catch (Exception e) {
            // Do nothing.
          }
        }
      }
      else if (largest == board.BOARD_SIZE * 2 + 1) {
        for (int r = 0, c = board.BOARD_SIZE - 1; r < board.BOARD_SIZE && c >= 0; r++, c--) {
          try {
            board.setMark(r, c, TicTacToe.Marker.O);
            return;
          }
          catch (Exception e) {
            // Do nothing.
          }
        }
      }
    }

    for (int i = 0; i < board.BOARD_SIZE; i++) {
      for (int j = 0; j < board.BOARD_SIZE; j++) {
        try {
          board.setMark(i, j, TicTacToe.Marker.O);
          return;
        }
        catch (Exception e) {
          // do nothing on purpose
        }
      }
    }


  }


  private void markBoard(TicTacToe.Marker player, String mv) {
    int[] move = getMove(mv);

    boolean markSet = false;

    while (!markSet) {
      try {
        board.setMark(move[0], move[1], player);
        markSet = true;
      }
      catch (Exception e) {
        System.out.println("\n" + e.getMessage());
        System.out.print("Enter valid move: ");
        move = getMove(scanner.nextLine());
      }
    }
  }

  private int[] getMove(String mv) {
    int[] move = null;

    while (move == null) {
      try {
        move = board.parseMove(mv);
      }
      catch (Exception e) {
        System.out.println("\n" + e.getMessage());
        System.out.print("Enter valid move: ");
        mv = scanner.nextLine();
      }
    }

    return move;
  }

  private TicTacToe board;
  private Scanner scanner;
}
