package da_algorithms_spring19;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * @author sualeh
 * Seam Carver API
 */
/**
 * @author sualeh
 *
 */
public class SeamCarverHelper {
	private Image picture;
	private PixelReader pixelReader;
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
	public SeamCarverHelper(Image picture) {
		this.picture = picture;
		this.pixelReader = picture.getPixelReader();
	}

	/**
	 * Returns current picture
	 * 
	 * @return Picture
	 */
	public Image picture() {
		return picture;
	}

	public PixelReader getPixelReader() {
		return picture.getPixelReader();
	}

	/**
	 * Returns width of current picture
	 * 
	 * @return width
	 */
	public int width() {
		return (int) picture.getWidth();
	}

	/**
	 * Returns height of current picture
	 * 
	 * @return height
	 */
	public int height() {
		return (int) picture.getHeight();
	}

	/**
	 * Computes energy of pixel at column x and row y
	 * 
	 * @param int x
	 * @param int y
	 * @return double energy
	 */
	public double energy(int x, int y) throws IndexOutOfBoundsException {

		if (x > width() - 1 || y > height() - 1) {
			throw new IndexOutOfBoundsException("Out of range");
		}
		double deltaXsquared = 0.0, deltaYsquared = 0.0;

		int rightX = x + 1, leftX = x - 1, rightY = y + 1, leftY = y - 1;

		// corner cases
		if (x == 0) {
			rightX = 1;
			leftX = width() - 1;
		}
		if (y == 0) {
			rightY = height() - 1;
			leftY = 1;
		}
		if (x == width() - 1) {
			rightX = 0;
			leftX = width() - 2;
		}
		if (y == height() - 1) {
			rightY = height() - 2;
			leftY = 0;
		}

		PixelReader pr = getPixelReader();
		
		
		
		Color leftColorY = pr.getColor(x, leftY), rightColorY = pr.getColor(x, rightY);
		Color leftColorX = pr.getColor(leftX, y), rightColorX = pr.getColor(rightX, y);
		
		deltaXsquared =  255*255* Math.pow(leftColorX.getRed() - rightColorX.getRed(), 2);
		deltaXsquared += 255*255* Math.pow(leftColorX.getBlue() - rightColorX.getBlue(), 2);
		deltaXsquared += 255*255* Math.pow(leftColorX.getGreen() - rightColorX.getGreen(), 2);
		
		deltaYsquared = 255*255* Math.pow(leftColorY.getRed() - rightColorY.getRed(), 2);
		deltaYsquared += 255*255* Math.pow(leftColorY.getBlue() - rightColorY.getBlue(), 2);
		deltaYsquared += 255*255* Math.pow(leftColorY.getGreen() - rightColorY.getGreen(), 2);
		
		
		return Math.sqrt(deltaXsquared + deltaYsquared);
	}

	/**
	 * sequence of indices for horizontal seam
	 * 
	 * @return indices of horizontal seam
	 */
	public int[] findHorizontalSeam() {

		Image transposedImage = transposeImage();

		Image OGImage = picture(); // the OG!

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

	}

	/**
	 * transposes the image by flipping x,y coordinates
	 */
	private Image transposeImage() {
		WritableImage writableImage = new WritableImage(height(), width());

		Color c1;
		PixelReader pr = getPixelReader();
		PixelWriter pw = writableImage.getPixelWriter();
		for (int i = 0; i < width(); i++) {
			for (int j = 0; j < height(); j++) {
				c1 = pr.getColor(i, j);
				pw.setColor(j, i, c1);
			}
		}
		return writableImage;
	}

	/**
	 * sequence of indices for vertical seam
	 * 
	 * @return indices of vertical seam
	 */
	public int[] findVerticalSeam() {
		computeMinEnergy(); // construct the dp matrix
		double[][] minEnergyGrid = getMinEnergyGrid();
		
		
		int[] seam = new int[height()];

		double min = Double.MAX_VALUE;
		int indexMin = -1;
		for (int i = 0; i < width(); i++) {
			if (minEnergyGrid[height() - 1][i] < min) {
				min = minEnergyGrid[height() - 1][i];
				indexMin = i;
			}
		}

		seam[height() - 1] = indexMin;
		int temp = -1;

		double a, b, c;

		for (int i = height() - 2; i >= 0; i--) {

			a = indexMin == 0 ? Double.MAX_VALUE : minEnergyGrid[i][indexMin - 1];
			b = indexMin == width() - 1 ? Double.MAX_VALUE : minEnergyGrid[i][indexMin + 1];
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
		if (height() == 1 || width() == 1) {
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
		if (height() == 1 || width() == 1) {
			throw new IllegalArgumentException("The width or height of the current picture is 1");
		}
		WritableImage writableImage = new WritableImage(width() - 1, height());
		PixelReader pixelReader = getPixelReader();
		PixelWriter pixelWriter = writableImage.getPixelWriter();

		int col = 0;
		for (int j = 0; j < height(); j++) {
			col = 0;
			for (int k = 0; k < width(); k++) {
				if (seam[j] == k) {
					continue;
				}
				pixelWriter.setColor(col++, j, pixelReader.getColor(k, j));
			}
		}
		picture = writableImage;
	}

	/**
	 * Computes energy of the picture
	 */
	public void computeEnergy() {
		energyGrid = new double[height()][width()];

		for (int i = 0; i < height(); i++) {
			for (int j = 0; j < width(); j++) {
				energyGrid[i][j] = this.energy(j, i);
			}
		}

	}

	/**
	 * Computes minimum energy dynamic programming style!
	 */
	public void computeMinEnergy() {
		minEnergyGrid = new double[height()][width()];
		for (int i = 0; i < width(); i++) {
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
