package da_algorithms_spring19;

import java.awt.Color;
import java.io.File;

/**
 * @author sualeh
 * Seam Carver API
 */
public class SeamCarver {
	private Picture picture;
	/**
	 * Creates a seam carver object
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
	 * @param x
	 * @param y
	 * @return energy
	 */
	public double energy(int x, int y) throws IndexOutOfBoundsException {
		System.out.println(picture.get(x, y).toString());
		if (x > picture.width() - 1 || y > picture.height() -1) {
			throw new IndexOutOfBoundsException("Out of range");
		}
		System.out.println(x + " " + y);
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
		
		deltaXsquared = Math.pow(leftColorX.getRed() - rightColorX.getRed(),2);
		deltaXsquared += Math.pow(leftColorX.getBlue() - rightColorX.getBlue(),2);
		deltaXsquared += Math.pow(leftColorX.getGreen() - rightColorX.getGreen(),2);

		deltaYsquared = Math.pow(leftColorY.getRed() - rightColorY.getRed(),2);
		deltaYsquared += Math.pow(leftColorY.getBlue() - rightColorY.getBlue(),2);
		deltaYsquared += Math.pow(leftColorY.getGreen() - rightColorY.getGreen(),2);


		return Math.sqrt(deltaXsquared + deltaYsquared);
	}

	/**
	 * sequence of indices for horizontal seam
	 * 
	 * @return indices of horizontal seam
	 */
	public int[] findHorizontalSeam() {
		return null;
	}

	/**
	 * sequence of indices for vertical seam
	 * 
	 * @return indices of vertical seam
	 */
	public int[] findVerticalSeam() {
		return null;
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
	}

	/**
	 * do unit testing of this class
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
//		Picture samplePicture = new Picture(3, 4);
//		int[][] sampleGrid = {{255,101,51,255,101,153,255,101,255},  
//				{255,153,51,255,153,153,255,153,255},  
//				{255,203,51,255,204,153,255,205,255}, 
//				{255,255,51,255,255,153,255,255,255}};
//		for (int i = 0; i < 4; i++) {
//			int ctr = 0;
//			for (int j = 0; j < 3; j++) {
//				Color c = new Color(sampleGrid[i][ctr++],sampleGrid[i][ctr++],sampleGrid[i][ctr++]);
//				samplePicture.set(j, i, c);
//			}
//		}
//		samplePicture.save("sample.jpg");
		
		Picture p = new Picture(new File("sample.jpg"));
		SeamCarver seamCarver = new SeamCarver(p);		
		float x;
		for (int i = 0; i < seamCarver.picture.height(); i++) {
			for (int j = 0; j < seamCarver.picture.width(); j++) {
				System.out.println(seamCarver.picture.get(j, i));
				x = (float) seamCarver.energy(j,i);
				System.out.print(x + " ");
			}
			System.out.println();
		}
//		p.show();
	}

}
