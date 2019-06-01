package da_algorithms_spring19;

import java.awt.Color;
import java.io.File;

/**
 * @author sualeh Seam Carver API
 */

public class SeamCarver {
	private Picture picture;
	private double[][] minEnergyGrid;
	private double[][] energyGrid;

	/**
	 * @return double[][] energyGrid
	 */
	public double[][] getEnergyGrid() {
		return energyGrid;
	}

	/**
	 * @return double[][] minEnergyGrid
	 */
	public double[][] getMinEnergyGrid() {
		return minEnergyGrid;
	}

	/**
	 * Creates a seam carver object
	 * 
	 * @param picture
	 *
	 **/
	public SeamCarver(Picture picture) {
		this.picture = picture;
	}

	/**
	 * Returns current picture
	 * 
	 * @return Picture
	 */
	public Picture picture() {
		return picture;
	}

	/**
	 * Returns width of current picture
	 * 
	 * @return width
	 */
	public int width() {
		return picture.width();
	}

	/**
	 * Returns height of current picture
	 * 
	 * @return height
	 */
	public int height() {
		return picture.height();
	}

	/**
	 * Computes energy of pixel at column x and row y
	 * 
	 * @param int x
	 * @param int y
	 * @return double energy
	 */
	public double energy(int x, int y) throws IndexOutOfBoundsException {
		if (x > picture.width() - 1 || y > picture.height() - 1) {
			throw new IndexOutOfBoundsException("Out of range");
		}
		double deltaXsquared = 0.0, deltaYsquared = 0.0;

		int rightX = x + 1, leftX = x - 1, rightY = y + 1, leftY = y - 1;

		// corner cases
		if (x == 0) {
			rightX = 1;
			leftX = picture.width() - 1;
		}
		if (y == 0) {
			rightY = picture.height() - 1;
			leftY = 1;
		}
		if (x == picture.width() - 1) {
			rightX = 0;
			leftX = picture.width() - 2;
		}
		if (y == picture.height() - 1) {
			rightY = picture.height() - 2;
			leftY = 0;
		}
		Color leftColorY = picture.get(x, leftY), rightColorY = picture.get(x, rightY);
		Color leftColorX = picture.get(leftX, y), rightColorX = picture.get(rightX, y);

		deltaXsquared = Math.pow(leftColorX.getRed() - rightColorX.getRed(), 2);
		deltaXsquared += Math.pow(leftColorX.getBlue() - rightColorX.getBlue(), 2);
		deltaXsquared += Math.pow(leftColorX.getGreen() - rightColorX.getGreen(), 2);

		deltaYsquared = Math.pow(leftColorY.getRed() - rightColorY.getRed(), 2);
		deltaYsquared += Math.pow(leftColorY.getBlue() - rightColorY.getBlue(), 2);
		deltaYsquared += Math.pow(leftColorY.getGreen() - rightColorY.getGreen(), 2);

		return Math.sqrt(deltaXsquared + deltaYsquared);
	}

	/**
	 * sequence of indices for horizontal seam
	 * 
	 * @return indices of horizontal seam
	 */
	public int[] findHorizontalSeam() {

		Picture transposedImage = transposeImage();

		Picture OGImage = this.picture; // the OG!

		picture = transposedImage;
		computeEnergy();
		computeMinEnergy();
		int[] seam = this.findVerticalSeam();
		picture = OGImage;
		return seam;
	}

	/**
	 * do unit testing of this class
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

//		energyTesting();

//		verticalSeamTesting();

//		horizontalSeamTesting();
		Picture picture = new Picture("HJoceanSmall.png");

		Picture p = new Picture(picture.height(), picture.width());
		Color c1;
		for (int i = 0; i < picture.width(); i++) {
			for (int j = 0; j < picture.height(); j++) {
				c1 = picture.get(i, j);
				p.set(j, i, c1);
			}
		}

		SeamCarver seamCarver = new SeamCarver(picture);

		seamCarver.picture.show();
		final int NUM_OF_VSEAMS = (int) (0.3 * seamCarver.picture.width());
		for (int i = 0; i < NUM_OF_VSEAMS; i++) {
			seamCarver.computeEnergy();
			seamCarver.computeMinEnergy();

			seamCarver.removeVerticalSeam(seamCarver.findVerticalSeam());
		}
		final int NUM_OF_HSEAMS = (int) (0.4 * seamCarver.picture.height());
		for (int i = 0; i < NUM_OF_HSEAMS; i++) {
			seamCarver.computeEnergy();
			seamCarver.computeMinEnergy();

			seamCarver.removeHorizontalSeam(seamCarver.findHorizontalSeam());
		}
		seamCarver.picture.show();
	}

	/**
	 * do unit testing of vertical seam
	 */
	private static void verticalSeamTesting() {
		SeamCarver seamCarver = new SeamCarver(new Picture("HJoceanSmall.png"));
		seamCarver.computeEnergy();
		seamCarver.computeMinEnergy();
		int[] seam = seamCarver.findVerticalSeam();
		for (int i = 0; i < seamCarver.picture.height(); i++) {
			seamCarver.picture.set(seam[i], i, Color.RED);
			System.out.println(seamCarver.getEnergyGrid()[i][seam[i]]);
		}
		seamCarver.picture.show();
	}

	/**
	 * do unit testing of horizontal seam
	 */
	private static void horizontalSeamTesting() {
		SeamCarver seamCarver = new SeamCarver(new Picture("HJoceanSmall.png"));
		int[] seam = seamCarver.findHorizontalSeam();
		for (int i = 0; i < seamCarver.picture.width(); i++) {
			seamCarver.picture.set(i, seam[i], Color.RED);
			System.out.println(seamCarver.getEnergyGrid()[i][seam[i]]);
		}
		seamCarver.picture.show();
	}

	/**
	 * do unit testing of energy function
	 */
	private static void energyTesting() {
		Picture p = new Picture(new File("6x5.png"));

		SeamCarver seamCarver = new SeamCarver(p);
		seamCarver.computeEnergy();
		float x;
		for (int i = 0; i < seamCarver.picture.height(); i++) {
			for (int j = 0; j < seamCarver.picture.width(); j++) {
				x = (float) Math.pow(seamCarver.getEnergyGrid()[i][j], 2);
				System.out.print("√" + x + " ");
			}
			System.out.println();
		}
	}

	/**
	 * transposes the image by flipping x,y coordinates
	 */
	private Picture transposeImage() {
		Picture p = new Picture(picture.height(), picture.width());
		Color c1;
		for (int i = 0; i < this.picture.width(); i++) {
			for (int j = 0; j < picture.height(); j++) {
				c1 = picture.get(i, j);
				p.set(j, i, c1);
			}
		}
		return p;
	}

	/**
	 * sequence of indices for vertical seam
	 * 
	 * @return indices of vertical seam
	 */
	public int[] findVerticalSeam() {
		computeMinEnergy(); // construct the dp matrix
		double[][] minEnergyGrid = getMinEnergyGrid();

		int[] seam = new int[picture.height()];

		double min = Double.MAX_VALUE;
		int indexMin = -1;
		for (int i = 0; i < picture.width(); i++) {
			if (minEnergyGrid[picture.height() - 1][i] < min) {
				min = minEnergyGrid[picture.height() - 1][i];
				indexMin = i;
			}
		}

		seam[picture.height() - 1] = indexMin;
		int temp = -1;

		double a, b, c;

		for (int i = picture.height() - 2; i >= 0; i--) {

			a = indexMin == 0 ? Double.MAX_VALUE : minEnergyGrid[i][indexMin - 1];
			b = indexMin == picture.width() - 1 ? Double.MAX_VALUE : minEnergyGrid[i][indexMin + 1];
			c = minEnergyGrid[i][indexMin];

			min = Math.min(Math.min(a, b), c);
			if (min == a)
				indexMin--;
			else if (min == b)
				indexMin++;

			seam[i] = indexMin;
		}
		return seam;
	}

	/**
	 * remove horizontal seam from current picture
	 * 
	 * @param seam
	 */
	public void removeHorizontalSeam(int[] seam) throws NullPointerException, IllegalArgumentException {
		if (seam == null) {
			throw new NullPointerException("No argument given!");
		}
		if (picture.height() == 1 || picture.width() == 1) {
			throw new IllegalArgumentException("The width or height of the current picture is 1");
		}

		picture = transposeImage();
		removeVerticalSeam(seam);
		picture = transposeImage();
	}

	/**
	 * remove vertical seam from current picture
	 * 
	 * @param seam
	 */
	public void removeVerticalSeam(int[] seam) throws NullPointerException, IllegalArgumentException {
		if (seam == null) {
			throw new NullPointerException("No argument given!");
		}
		if (picture.height() == 1 || picture.width() == 1) {
			throw new IllegalArgumentException("The width or height of the current picture is 1");
		}

		Picture temp = new Picture(picture.width() - 1, picture.height());
		int col = 0;
		for (int j = 0; j < picture.height(); j++) {
			col = 0;
			for (int k = 0; k < picture.width(); k++) {
				if (seam[j] == k) {
					continue;
				}
				temp.set(col++, j, picture.get(k, j));
			}
		}
		picture = temp;

	}

	/**
	 * Computes energy of the picture
	 */
	private void computeEnergy() {
		energyGrid = new double[picture.height()][picture.width()];

		for (int i = 0; i < this.picture.height(); i++) {
			for (int j = 0; j < this.picture.width(); j++) {
				energyGrid[i][j] = this.energy(j, i);
			}
		}

	}

	/**
	 * Computes minimum energy dynamic programming style!
	 */
	private void computeMinEnergy() {
		minEnergyGrid = new double[picture.height()][picture.width()];
		for (int i = 0; i < picture.width(); i++) {
			minEnergyGrid[0][i] = energyGrid[0][i];
		}
		// dynamic programming time!
		for (int i = 1; i < energyGrid.length; i++) {
			for (int j = 0; j < energyGrid[i].length; j++) {
				double a = j == 0 ? Double.MAX_VALUE : minEnergyGrid[i - 1][j - 1];
				double b = minEnergyGrid[i - 1][j];
				double c = j == energyGrid[i].length - 1 ? Double.MAX_VALUE : minEnergyGrid[i - 1][j + 1];

				minEnergyGrid[i][j] = energyGrid[i][j] + Math.min(Math.min(a, b), c);
			}
		}

	}

}
