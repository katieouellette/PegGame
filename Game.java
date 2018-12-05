/*
Author:			Katie Ouellette
Date:			December 4, 2018
Professor:		Dr. Tarau
Description:	Java Implementation of the Cracker Barrel Peg Game

Execute:		javac Game.java
				java Game
*/

import java.util.*;

public class Game {

     //all possible moves
     private final static int[][] MOVES = new int[][]
     { 
 	new int[] { 0, 1, 3 },
 	new int[] { 0, 2, 5 },
 	new int[] { 1, 3, 6 },
 	new int[] { 1, 4, 8 },
 	new int[] { 2, 4, 7 },
 	new int[] { 2, 5, 9 },
 	new int[] { 3, 4, 5 },
 	new int[] { 3, 6, 10 },
 	new int[] { 3, 7, 12 },
 	new int[] { 4, 7, 11 },
 	new int[] { 4, 8, 13 },
 	new int[] { 5, 8, 12 },
	new int[] { 5, 9, 14 },
 	new int[] { 6, 7, 8 },
 	new int[] { 7, 8, 9 },
 	new int[] { 10, 11, 12 },
 	new int[] { 11, 12, 13 },
        new int[] { 12, 13, 14 }
    };
    
    private static final HashSet<Long> completed = new HashSet<Long>(); //failed boards
    private static final ArrayList<Long> solution = new ArrayList<Long>(); //solution board
    private static final ArrayList<long[]> result = new ArrayList<long[]>(); //final
    private static long SOLUTION = 1048576;
    private static long START = 105983;
    private static final long VALID_BOARD_CELLS = 1154559;  
    private static final long[][] moves = new long[36][];
    

	
	//--------------------------------------------------------------------------------
    //Main Function
    public static void main(String[] args) 
    {
       	for(int i = 0; i < 4; i++)
       	{
       		System.out.println(" ");
        	System.out.println(" ");
        	System.out.println("Starting Position: " + i);
        	System.out.println(" ");
       		terse(i);
       	}
    }
    
    //--------------------------------------------------------------------------------
	private static void terse(int start)
	{
		init(start);
        solution.add(START); //use initialized board
        seenBoards.clear();
        solution.clear();
        result.clear();
            	
        ArrayList<long[]> moves = new ArrayList<long[]>();
        int[] startsX = new int[]{4, 3, 2, 8, 7, 12};
     	for (int x : startsX) 
     	{
        	checkMoves(x, x - 1, x - 2, moves);
        }
            	
        int[] startsY = new int[]{20, 15, 10, 16, 11, 12};
        for (int y : startsY) 
        {
        	checkMoves(y, y - 5, y - 10, moves);
        }
        int[] startsZ = new int[]{4, 8, 12, 3, 7, 2};
        for (int z : startsZ) 
        {
        	checkMoves(z, z + 4, z + 8, moves);
    	}
    	
        Collections.shuffle(moves);
        moves.toArray(Game.moves);
        puzzle(SOLUTION); //recursively solve

        int i = 0;
        for (long step : solution) //print solutions
        {
            Show(step);
            System.out.println(" ");
        }
   	    System.out.println("You Win!!");
        System.out.println("------------------------");
	}


	//--------------------------------------------------------------------------------------
	//Initialize board with first peg position empty
    static void init(int start) 
    {
        int boardBits = 0;
        String boardS = "";
        int counter = 0;

        //print
        for (int i = 0; i < 5; i++) {
            System.out.print("  ");
            for (int k = 0; k < 4 - i; k++) {
                System.out.print("  ");
                boardS += "0";
            }
            for (int j = 0; j < 5; j++) {
                if (j <= i) {
                    if (counter == start) {
                        System.out.print(".   ");
                        boardS += "1";
                    } else {
                        System.out.print("x   ");
                        boardS += "0";
                    }
                    counter += 1;
                }
            }
            System.out.println();
        }

        System.out.println("");
        System.out.println("---------------------");
        System.out.println("");
        boardBits = Integer.parseInt(boardS, 2) ^ 1154559;
        START = boardBits;
        SOLUTION = Integer.parseInt(boardS, 2);

    }

	
	
	//----------------------------------------------------------------------------------
    //Show Board
    private static void Show(long board) 
    {
        for (int i = 24; i >= 0; i--) 
        {
            boolean validCell = ((1L << i) & VALID_BOARD_CELLS) != 0L;
            System.out.print(validCell ? (((1L << i) & board) != 0L ? "x   " : ".   ") : "  ");
            if (i % 5 == 0) System.out.println();
        }
    }

    //--------------------------------------------------------------------------------
    //Moves - forward and backward
    private static void checkMoves(int bit1, int bit2, int bit3, ArrayList<long[]> moves) 
    {
        moves.add(new long[]{(1L << bit1), (1L << bit2) | (1L << bit3), (1L << bit1) | (1L << bit2) | (1L << bit3)});
        moves.add(new long[]{(1L << bit3), (1L << bit2) | (1L << bit1), (1L << bit1) | (1L << bit2) | (1L << bit3)});
    }
    

    //-------------------------------------------------------------------------------
    //recursively check for solution
    private static boolean puzzle(long board) {
    	//Check Moves
        for (long[] move : moves) {
            if ((move[1] & board) == 0L && (move[0] & board) != 0L) //if it works
            {
                long newBoard = board ^ move[2];  //adjust board
                if (!seenBoards.contains(newBoard)) //make sure it hasn't been done before
                {
                    seenBoards.add(newBoard);
                    if (newBoard == START || puzzle(newBoard))  //does it work 
                    {
                        solution.add(board);
                        result.add(move);
                        return true;
                    }
                }
            }
        }
        return false;  //if failed to win
    }

}
   
