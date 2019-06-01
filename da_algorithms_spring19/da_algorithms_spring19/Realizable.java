package da_algorithms_spring19;

import java.util.Arrays;
import java.util.Scanner;

/**
 * <h1>Dynamic Programming using Realizable Arrays!</h1>
 * 
 * @author Sualeh Ali
 * @version 1.0
 * @since 2019-05-03
 */

public class Realizable {

	static int ctr;
	static StringBuilder sb;

	/**
	 * Given an Array A and value T, it tells you that if you write the integers in
	 * the order they appear in A, and insert symbols + or − before each integer,
	 * you get an arithmetic expression. If the evaluation of the expression gives
	 * the value v which is same as T we say that v is realized (or realizable) by
	 * the array A
	 * 
	 * @param A array of positive integers
	 * @param T value to be 'realized'
	 * @return boolean This returns whether the array is realizable or not
	 */
	public static boolean realizable(int[] A, int T) {

		// Initialize 2d array of rows
		int S = Arrays.stream(A).sum();
//		System.out.println(S);
		// limit check
		if (T > S || T < -S)
			return false;

		boolean[][] grid = new boolean[A.length + 1][2 * S + 1];

		grid[0][S] = true;
		for (int i = 1; i < grid.length; i++) {
			for (int j = -S; j <= S; j++) {
				// j + S done for shifting
				// inequality checked to see if bounds do not cross
				// recursive implementation allows us to check if adding/subtracting on previous
				// array is valid then true
				boolean flag1 = (j - A[i - 1] >= -S && grid[i - 1][j - A[i - 1] + S]);
				boolean flag2 = (j + A[i - 1] <= S && grid[i - 1][j + A[i - 1] + S]);
				grid[i][j + S] = flag1 || flag2;
			}
		}
//		for (int i = 0; i < grid.length; i++) {
//			for (int j = 0; j < grid[i].length; j++) {
//				System.out.print(grid[i][j] == true ? 1 + " ": 0 + " ");
//			}
//			System.out.println();
//		}
		return grid[A.length][S + T];
	}

	/**
	 * Follows the same algorithm as Realizable but additionally prints one way of
	 * realizing T (in case T is realizable).
	 * 
	 * @param A
	 * @param T
	 */
	public static String showOne(int[] A, int T) {
		// Initialize 2d array of rows
		int S = Arrays.stream(A).sum();
		if (T > S || T < -S) {
			return "The value " + T + " is not realizable";
		}

		// System.out.println(S);
		// limit check

		int[][] grid = new int[A.length + 1][2 * S + 1];

		grid[0][S] = 1;
		for (int i = 1; i < grid.length; i++) {
			for (int j = -S; j <= S; j++) {
				// j + S done for shifting
				// inequality checked to see if bounds do not cross
				// recursive implementation allows us to check if adding/subtracting on previous
				// array is valid then true
				boolean flag1 = (j - A[i - 1] >= -S && grid[i - 1][j - A[i - 1] + S] != 0);
				boolean flag2 = (j + A[i - 1] <= S && grid[i - 1][j + A[i - 1] + S] != 0);
				if (flag1)
					grid[i][j + S] = 1;
				else if (flag2)
					grid[i][j + S] = -1;
			}
		}

//		for (int i = 0; i < grid.length; i++) {
//			for (int j = 0; j < grid[i].length; j++) {
//				System.out.print(grid[i][j] + " ");
//			}
//			System.out.println();
//		}

		// backtracking time !!
		int i = A.length;
		int j = T;
		int temp = grid[i][S + T];
		StringBuilder str = new StringBuilder();
		if (temp == 0) {
			return "The value " + T + " is not realizable";
		}
		do {
			if (temp == 1) {
				str.insert(0, "+" + A[i - 1] + "");
				j -= A[i - 1];
				temp = grid[i - 1][j + S];
			} else {
				str.insert(0, "-" + A[i - 1] + "");
				j += A[i - 1];

				temp = grid[i - 1][j + S];
			}
		} while (i-- != 1);
		return str.toString() + "= " + T;

	}

	/**
	 * Just like showOne but prints all the realizations of T by A.
	 * 
	 * @param A
	 * @param T
	 * @return
	 */
	public static String showAll(int[] A, int T) {
		// Initialize 2d array of rows
		int S = Arrays.stream(A).sum();

		// limit check
		if (T > S || T < -S) {
			return "\t\tThe value " + T + " is not realizable\n";
		}

		int[][] grid = new int[A.length + 1][2 * S + 1];

		grid[0][S] = 1;
		for (int i = 1; i < grid.length; i++) {
			for (int j = -S; j <= S; j++) {
				// j + S done for shifting
				// inequality checked to see if bounds do not cross
				// recursive implementation allows us to check if adding/subtracting on previous
				// array is valid then true
				boolean flag1 = (j - A[i - 1] >= -S && grid[i - 1][j - A[i - 1] + S] != 0);
				boolean flag2 = (j + A[i - 1] <= S && grid[i - 1][j + A[i - 1] + S] != 0);
				if (flag1 && flag2)
					grid[i][j + S] = 2;
				else if (flag1)
					grid[i][j + S] = 1;
				else if (flag2)
					grid[i][j + S] = -1;
			}
		}

//		for (int i = 0; i < grid.length; i++) {
//			for (int j = 0; j < grid[i].length; j++) {
//				System.out.print(grid[i][j] + " ");
//			}
//			System.out.println();
//		}

		// backtracking time !!
		int i = A.length;
		int j = T;
		int temp = grid[i][S + T];
		String str = "";
		if (temp == 0) {
			return "";
		}
		ctr = 0;
		sb = new StringBuilder();
		showAllHelper(A, T, S, grid, i, j, temp, str);

		return sb.toString();
	}

	/**
	 * 
	 * Helper function for showAll
	 * @param A
	 * @param T
	 * @param S
	 * @param grid
	 * @param i
	 * @param j
	 * @param temp
	 * @param str
	 */
	public static void showAllHelper(int[] A, int T, int S, int[][] grid, int i, int j, int temp, String str) {
		if (i == 0) {

			sb.append(String.format("\t\tSol\t%2d : %s = %d\n", ++ctr, str.toString(), T));
			return;
		}
		if (temp == 2) {

			showAllHelper(A, T, S, grid, i - 1, j - A[i - 1], grid[i - 1][j - A[i - 1] + S], "+" + A[i - 1] + "" + str);
			showAllHelper(A, T, S, grid, i - 1, j + A[i - 1], grid[i - 1][j + A[i - 1] + S], "-" + A[i - 1] + "" + str);
		}

		else if (temp == 1) {
			str = "+" + A[i - 1] + "" + str;
			j -= A[i - 1];
			temp = grid[i - 1][j + S];

			showAllHelper(A, T, S, grid, --i, j, temp, str);
		} else if (temp == -1) {
			str = "-" + A[i - 1] + "" + str;
			j += A[i - 1];
			temp = grid[i - 1][j + S];

			showAllHelper(A, T, S, grid, --i, j, temp, str);
		}
	}

	public static void main(String[] args) {
		System.out.println("Please enter the number of elements in the array");
		Scanner in = new Scanner(System.in);
		int[] A = new int[in.nextInt()];

		System.out.println("Please enter the numbers (separated by spaces) ");
		for (int i = 0; i < A.length; i++) {
			A[i] = in.nextInt();
		}
		System.out.println("Please enter the value to be realized");
		int T = in.nextInt();

		System.out.printf("n = %d\nThe input array: ", A.length);
		for (int a : A)
			System.out.print(a + " ");
		System.out.printf("\nT = %d\n", T);

		if (!realizable(A, T)) {
			System.out.println("\tPart 1: Realizability check\n\t\tThe value " + T + " is not realizable");
		} else {
			System.out.println("\tPart 1: Realizability check\n\t\tThe value " + T + " is realizable");
		}
		System.out.printf("\tPart 2: One Solution\n\t\t%s\n", showOne(A, T));

		String str = showAll(A, T);
		System.out.printf("\tPart 3: All Solutions\n\t\tNumber of Solutions = %d\n%s", ctr, str);

		for (int i = 0; i < 100; i++) {
			System.out.print("-");
		}
		System.out.println();

		in.close();
	}

}
