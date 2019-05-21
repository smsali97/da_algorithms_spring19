package da_algorithms_spring19;

import java.util.Arrays;

/**
 * <h1> Dynamic Programming using Realizable Arrays! </h1>
 * 
 * @author Sualeh Ali
 * @version 1.0
 * @since 2019-05-03
 */

public class Realizable {

	/**
	 * Given an Array A and value T, it tells you that if you write the integers in
	 * the order they appear in A, and insert symbols + or − before each integer,
	 * you get an arithmetic expression. If the evaluation of the expression gives
	 * the value v which is same as T we say that v is realized (or realizable) by
	 * the array A
	 * @param A array of positive integers
	 * @param T value to be 'realized'
	 * @return boolean This returns whether the array is realizable or not
	 */
	public static boolean realizable(int[] A, int T) {
		
		// Initialize 2d array of rows
		int S = Arrays.stream(A).sum();
		System.out.println(S);
		// limit check
		if ( T > S || T < - S) return false;
		
		boolean[][] grid = new boolean[A.length][2*S + 1];
		
		for (int i = 1; i < grid.length; i++) {
			for (int j = -S; j <= S; j++) {
				System.out.println(i + " " + j + " " + A[i]);
				// j + S done for shifting
				// inequality checked to see if bounds do not cross
				// recursive implementation allows us to check if adding/subtracting on previous array is valid then true
				boolean flag1 = (j - A[i] >= -S && grid[i][j - A[i] + S]);
				boolean flag2 = (j + A[i] <= S && grid[i][j + A[i] + S]);
				grid[i][j + S] = flag1 || flag2;
			}
		}
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				System.out.print(grid[i][j] + " ");
			}
			System.out.println();
		}
		return grid[A.length-1][T];
	}

	public static void main(String[] args) {
		System.out.println(realizable(new int[] {7, 12, 1, 9}, 8));
	}

}
