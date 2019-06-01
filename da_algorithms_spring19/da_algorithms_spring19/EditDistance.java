package da_algorithms_spring19;

import java.util.Scanner;

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
		
		int insertionCost = 2;

		int[][] cost = new int[a.length() + 1][b.length() + 1];
		char[][] direction = new char[a.length() + 1][b.length() + 1];
		// Cost zero along a[0] and b[0]
		for (int i = 0; i <= a.length(); i++) {
			cost[i][0] = 2 * i;
			direction[i][0] = 'D';
		}
		for (int i = 0; i <= b.length(); i++) {
			cost[0][i] = 2 * i;
			if (i == 0)
				continue;
			direction[0][i] = 'L';
		} 

		// Compute cost of each
		for (int i = 1; i <= a.length(); i++) {
			for (int j = 1; j <= b.length(); j++) {

				int diff = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
				cost[i][j] = Math.min(Math.min(cost[i - 1][j] + insertionCost, cost[i][j - 1] + insertionCost), cost[i - 1][j - 1] + diff);

				if (cost[i][j] == cost[i - 1][j] + insertionCost)
					direction[i][j] = 'D';

				else if (cost[i][j] == cost[i][j - 1] + insertionCost)
					direction[i][j] = 'L';

				else if (cost[i][j] == cost[i - 1][j - 1] + diff)
					direction[i][j] = 'G';
				
			}
		}
		System.out.println("Edit Distance = " + cost[a.length()][b.length()]);

//		for (int i = 0; i < cost.length; i++) {
//			for (int j = 0; j < cost[i].length; j++) {
//				System.out.print(cost[i][j] + " ");
//			}
//			System.out.println();
//		}

		int i = a.length(), j = b.length();
		Path temp = new Path(i, j, cost[i][j]);
		while (i > 0 || j > 0) {
			Path n = null;

			switch (direction[i][j]) {
			case 'D': // left
				n = new Path(--i, j, cost[i][j]);
				break;
			case 'L': // down
				n = new Path(i, --j, cost[i][j]);
				break;
			case 'G': // diagonal
				n = new Path(--i, --j, cost[i][j]);
				break;
			}
			n.setNext(temp);
			temp = n;
		}
		System.out.println();
		return temp;
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		// read 2 strings from standard input
		String a = in.next();
		String b = in.next();
		// compute and print the edit distance between them and output an optimal
		// alignment and associated penalties
		Path p = match(a, b);
		Path next = p.getNext();
		in.close();

		int i = 0, j = 0, cost, up, right;
		while (next != null) {
			cost = next.getCost() - p.getCost();
			up = next.getRow() - p.getRow();
			right = next.getCol() - p.getCol();

			if (up == 1 && right == 1) {
				System.out.println(a.charAt(i++) + " " + b.charAt(j++) + " " + cost);
			} else if (up == 1) {
				System.out.println(a.charAt(i++) + " " + "-" + " " + cost);
			} else if (right == 1) {
				System.out.println("-" + " " + b.charAt(j++) + " " + cost);
			}

			p = next;
			next = next.getNext();
		}
	}

}
