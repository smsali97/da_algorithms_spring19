package da_algorithms_spring19;

public class Path {
	private int row, col; // the row and column this node represents
	private int cost; // the matching cost upto this point
	private Path next; // the next node in the optimal path
	char firstChar = '-';
	char secondChar = '-';
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "(" + row + "," + col + "," + cost + ")";
	}
	
	public Path(int row, int col, int cost) {
		this.setCol(col);
		this.setRow(row);
		this.setCost(cost);
	}
	public void setRow(int row) {
		this.row = row;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public void setNext(Path next) {
		this.next = next;
	}
	public int getCol() {
		return col;
	}
	public int getCost() {
		return cost;
	}
	public Path getNext() {
		return next;
	}
	public int getRow() {
		return row;
	}
	
}
