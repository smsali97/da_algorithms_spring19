package da_algorithms_spring19;

import java.util.Scanner;
import da_algorithms_spring19.Path;

public class EditDistance {
	/**
	 * Returns the optimal match between the strings a and b. It will return null if
	 * either string is null or if either string is length 0
	 * 
	 * @param String a
	 * @param String b
	 * @return Path path
	 */
	static Path match(String a, String b) {
		if (a == null || b == null || a.length() == 0 || b.length() == 0)
			return null;

		int[][] cost = new int[a.length() + 1][b.length() + 1];
		Path[][] grid = new Path[a.length() + 1][b.length() + 1];

		// Cost zero along a[0] and b[0]
		for (int i = 0; i <= a.length(); i++) {
			cost[i][0] = i;
			grid[i][0] = new Path(i, 0, i);
		}
		for (int i = 0; i <= b.length(); i++) {
			cost[0][i] = i;
			grid[0][i] = new Path(0, i, i);
		}

		// Compute cost of each
		Path temp = null;
		for (int i = 1; i <= a.length(); i++) {
			for (int j = 1; j <= b.length(); j++) {
				
				int diff = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
				cost[i][j] = Math.min(Math.min(cost[i - 1][j] + 2, cost[i][j - 1] + 2), cost[i - 1][j - 1] + diff);

				if (cost[i][j] == cost[i - 1][j] + 2) {

					grid[i][j] = new Path(i - 1, j, 2);
					grid[i][j].firstChar = a.charAt(i - 1);

					grid[i - 1][j].setNext(grid[i][j]);
					
					temp = grid[i-1][j];
				} else if (cost[i][j] == cost[i][j - 1] + 2) {
					grid[i][j] = new Path(i, j - 1, 2);
					grid[i][j].secondChar = b.charAt(j - 1);

					grid[i][j - 1].setNext(grid[i][j]);
					
					
					temp = grid[i][j - 1];
				} else {
					grid[i][j] = new Path(i - 1, j - 1, diff);
					grid[i][j].firstChar = a.charAt(i - 1);
					grid[i][j].secondChar = b.charAt(j - 1);

					grid[i - 1][j - 1].setNext(grid[i][j]);
					temp = grid[i - 1][j - 1];
				}
			}
		}
		System.out.println("Edit Distance: " + cost[a.length()][b.length()]);

		System.out.println();
		int n = 0;
		for (int j2 = 0; j2 <= a.length(); j2++) {
			for (int k = 0; k <= b.length(); k++) {
				System.out.print(grid[j2][k] + "\t");
			}
			System.out.println();
		}
		if (temp != null) temp.setNext(grid[a.length()][b.length()]);
		return grid[1][1];
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		// read 2 strings from standard input
		String a = in.next();
		String b = in.next();
		// compute and print the edit distance between them and output an optimal
		// alignment and associated penalties
		Path p = match(a, b);
		in.close();

		while (p != null) {
			System.out.println(p.firstChar + " " + p.secondChar + " " + p.getCost());
			p = p.getNext();
		}
	}

}
