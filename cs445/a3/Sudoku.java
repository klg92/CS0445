package cs445.a3;

import java.util.List;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Sudoku {
	//used to make sure we don't change the numbers that were in the board at the start
	static int[][] originalBoard = new int[9][9];
	//coordinates for space we are changing
	static int x = 0;
	static int y = 0;
    static boolean isFullSolution(int[][] board) {
        // TODO: Complete this method
		/* 
			do the stuff in reject and make sure that all spaces are filled
		*/
		for (int[] i: board){
			for (int element: i){
				if (element == 0){
					return false;
				}
			}
		}
		if (!reject(board)){
			System.out.println("\n-found a solution");
			return true;
		}
		System.out.println("not a solution");
		return false;
    }

    static boolean reject(int[][] board) {
        // TODO: Complete this method
		/*iterate through board and make sure that:
		1. no two numbers in each row (besides 0) are the same (use double for loop?)
		2. no two numbers in each column are the same board[i][j] != board[i+1][j]
		3. no two numbers in each region are the same ex. board[0][0-2] board[1][0-2] board[2][0-2] is one region
			put numbers in region into separate array and iterate through?
		*/
		
		for (int i = 0; i < 9; i++){
			for (int j = 0; j < 9; j++){
				// make sure 2 numbers in each row aren't the same excluding 0
				if (testRow(i, board)){
					System.out.println("rejected: row number " + i + " has a duplicate.");
					return true;
				}
				//make sure 2 numbers in each column aren't the same excluding 0
				if ((i != j) && (board[i][i] == board[j][i]) && (board[i][i] != 0)){
					System.out.println("rejected: in column " + i + ", " + board[i][i] + " is equal to " + board[j][i]);
					return true;
				}
				// make sure all the regions abide by sudoku rules
				
				//make sure regions don't have more than 1 number
			}
		}
		if (testRegion(0,0, board) || testRegion(0,3, board) || testRegion(0,6, board) || testRegion(3,0, board) || testRegion(3,3, board) || testRegion(3,6, board) || testRegion(6,0, board) || testRegion(6,3, board) || testRegion(6,6, board)){
			System.out.println("rejected: a region has a duplicate.");
			return true;
		}
        return false;
    }

    static int[][] extend(int[][] board) {
        // TODO: Complete this method\
		// change first zero seen to 1, then increment the coordinates of the position we are changing appropriately
		//look at attempt[x][y], if it is a zero, change to 1. If it is not, increment and try again
		int[][] attempt = new int[9][9];
		for (int i = 0; i < 9; i++){
			for (int j = 0; j < 9; j++){
				attempt[i][j] = board[i][j];
			}
		}
		while (!isChangeable(x,y,originalBoard) && (x < 8 && y < 8)){
			if (y < 8){
				y++;
			}else if (y == 8 && x < 8){
				x++;
				y = 0;
			}
		}
		if (attempt[x][y] == 0){
			System.out.println("position " + x + " " + y + "is now 1");
			attempt[x][y] = 1;
			return attempt;
		}
        return null;
    }

    static int[][] next(int[][] board) {
        // TODO: Complete this method
		// add 1 to most recent number extended, return null if it is 9 and still rejected and reset the board (to where? reset the current and next the previous?)
		// if the thing we extended last violates the rules, add 1 to it. If it doesn't, increment x and y
		if (!reject(board)){
			if (y < 8){
				y++;
			}else if (y == 8 && x < 8){
				x++;
				y = 0;
			}
			return board;
		}//else if (board[x][y] < 9){
			System.out.println("changing position " + x + " " + y + " to " + (board[x][y]+1));
			board[x][y] = board[x][y] + 1;
			return board;
		//}
		//figure out how to go backwards if we're trying to next something that's already a 9
		//return null;
		
		
    }

    static void testIsFullSolution() {
        // TODO: Complete this method
		//make boards that are: filled but wrong, right but incomplete, completely correct
		System.out.println("testing isFullSolution:");
		System.out.println("testing if actual solution is correctly identified");
		int[][] board = readBoard("cs445/testfull.txt");
		isFullSolution(board);
		printBoard(board);
		
		System.out.println("testing if wrong solution with full board is correctly identified");
		int[][] board2 = readBoard("cs445/testfull2.txt");
		isFullSolution(board2);
		printBoard(board2);
		
		System.out.println("testing if incomplete but correct board is correctly identified");
		int[][] board3 = readBoard("cs445/testfull3.txt");
		isFullSolution(board3);
		printBoard(board3);
    }

    static void testReject() {
        // TODO: Complete this method
		//make boards where: 2 in a row, 2 in a column, 2 in a region. 
		System.out.println("testing reject function");
		System.out.println("testing row detection:");
		int[][] board = new int[9][9];
		board[0][1] = 1;
		board[0][5] = 1;
		printBoard(board);
		reject(board);
		
		System.out.println("testing column detection:");
		board[0][5] = 0;
		board[1][1] = 1;
		printBoard(board);
		reject(board);
		
		System.out.println("testing region detection:");
		board[1][1] = 0;
		board[1][2] = 1;
		printBoard(board);
		reject(board);
		
		System.out.println("testing for false positives:");
		board[1][2] = 0;
		board[0][8] = 2;
		board[4][1] = 3;
		board[1][2] = 4;
		printBoard(board);
		reject(board);
    }

    static void testExtend() {
        // TODO: Complete this method
		//extend something properly, extend with number in the way, try to extend something without any open spaces
		System.out.println("testing extend function");
		System.out.println("testing if extend changes first zero");
		int[][] board =  readBoard("cs445/testextend.txt");
		originalBoard = board;
		printBoard(extend(board));
		
		System.out.println("testing if extend skips over given numbers");
		int[][] board2 = readBoard("cs445/testextend2.txt");
		originalBoard = board2;
		extend(board2);
		printBoard(extend(board2));
		
		System.out.println("testing if there is nothing to extend");
		int[][] board3 = readBoard("cs445/testfull.txt");
		originalBoard = board3;
		System.out.println(extend(board3));
    }

    static void testNext() {
        // TODO: Complete this method
		//don't increment when not necessary, increment properly, backtrack properly
    }

    static void printBoard(int[][] board) {
        if (board == null) {
            System.out.println("No assignment");
            return;
        }
        for (int i = 0; i < 9; i++) {
            if (i == 3 || i == 6) {
                System.out.println("----+-----+----");
            }
            for (int j = 0; j < 9; j++) {
                if (j == 2 || j == 5) {
                    System.out.print(board[i][j] + " | ");
                } else {
                    System.out.print(board[i][j]);
                }
            }
            System.out.print("\n");
        }
    }

    static int[][] readBoard(String filename) {
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(filename), Charset.defaultCharset());
        } catch (IOException e) {
            return null;
        }
        int[][] board = new int[9][9];
        int val = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                try {
                    val = Integer.parseInt(Character.toString(lines.get(i).charAt(j)));
                } catch (Exception e) {
                    val = 0;
                }
                board[i][j] = val;
            }
        }
        return board;
    }

    static int[][] solve(int[][] board) {
        if (reject(board)) return null;
        if (isFullSolution(board)) return board;
        int[][] attempt = extend(board);
        while (attempt != null) {
            int[][] solution = solve(attempt);
            if (solution != null) return solution;
            attempt = next(attempt);
        }
        return null;
    }

    public static void main(String[] args) {
        if (args[0].equals("-t")) {
            testIsFullSolution();
			System.out.println("\n");
            testReject();
            testExtend();
            testNext();
        } else {
            int[][] board = readBoard(args[0]);
			originalBoard = board;
            printBoard(board);
            printBoard(solve(board));
        }
    }
	//true if region violates sudoku rules. i and j are coordinates of top left spot in region
	private static boolean testRegion(int i, int j, int[][] board){
		int[] temp = {board[i][j], board[i][j+1], board[i][j+2],
					  board[i+1][j], board[i+1][j+1], board[i+1][j+2],
					  board[i+2][j], board[i+2][j+1], board[i+2][j+2]};
		for (int k=0; k < 9; k++){
			for (int l=0; l < 9; l++){
				if (k != l && temp[k] == temp[l] && temp[k] != 0){
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean testRow(int i, int[][]board){
		int[] temp = new int[9];
		for (int j = 0; j < 9; j++){
			temp[j] = board[i][j];
		}
		for (int k=0; k < 9; k++){
			for (int l=0; l < 9; l++){
				if (k != l && temp[k] == temp[l] && temp[k] != 0){
					return true;
				}
			}
		}
		return false;
	}
	//make sure we don't change numbers that we're not allowed to change
	private static boolean isChangeable(int i, int j, int[][] originalBoard){
		//System.out.println(i + " " + j);
		if (originalBoard[i][j] == 0){
			return true;
		}
		return false;
	}
	
	private static void backtrack(int x, int y, int[][]board){
		printBoard(originalBoard);
		if (y > 0){
			y--;
		}else if (y == 0 && x > 0){
			x--;
			y = 8;
		}
		while (!isChangeable(x, y, originalBoard)){
			if (y > 0){
				y--;
				System.out.println("changed y to " + y);
			}else if (y == 0 && x > 0){
				System.out.println("changed x to " + x);
				x--;
				y = 8;
			}
		}
		board[x][y] = board[x][y] + 1;	
		System.out.println("backtracked" + x + " " + y + " to " + board[x][y]);
	}
}
