package da_algorithms_spring19;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SeamCarverGUI extends Application {

	Pane pane;

	final int MIN_WIDTH = 200;
	final int MIN_HEIGHT = 200;

	@Override
	public void start(Stage primaryStage) {
		
        FileChooser fileChooser = new FileChooser();
//        fileChooser.setInitialDirectory(new File("/home/sualeh/Pictures/pictures"));
        fileChooser.setTitle("Please select an Image for Seam Carving");
        
        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);
        
        Image image = null;
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            image = SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
         }
		
        if (image == null) {
        	System.exit(0);
        }
		
		
//		Image image = new Image(getClass().getResource("../HJoceanSmall.png").toString());

		final int MAX_WIDTH = (int) image.getWidth();
		final int MAX_HEIGHT = (int) image.getHeight();

		System.out.println(MAX_WIDTH + " x " + MAX_HEIGHT);
		SeamCarverHelper seamCarver = new SeamCarverHelper(image);
		Canvas canvas = new Canvas(seamCarver.width(), seamCarver.height());
		GraphicsContext context = canvas.getGraphicsContext2D();
		
		
		context.drawImage(seamCarver.picture(), 0, 0);

		BorderPane pane = new BorderPane(canvas);
		Scene scene = new Scene(pane);

		primaryStage.setScene(scene);
		primaryStage.setMinWidth(MIN_WIDTH);
		primaryStage.setMaxWidth(MAX_WIDTH);
		primaryStage.setMinHeight(MIN_HEIGHT);
		primaryStage.setMaxHeight(MAX_HEIGHT);
		
		primaryStage.setWidth(MAX_WIDTH);
		primaryStage.setHeight(MAX_HEIGHT);

		// create a listener
		final ChangeListener<Number> listener = new ChangeListener<Number>() {
			final Timer timer = new Timer(); // uses a timer to call your resize method
			TimerTask task = null; // task to execute after defined delay
			final long delayTime = 200; // delay that has to pass in order to consider an operation done

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldVal, final Number newVal) {
				if (task != null) { // there was already a task scheduled from the previous operation ...
					task.cancel(); // cancel it, we have a new size to consider
				}

				task = new TimerTask() // create new task that calls your resize operation
				{
					@Override
					public void run() {

							
//							canvas.setHeight(
//									resizedImages[((int) resizedImages[0].getHeight()) - newVal.intValue()].getHeight());
						canvas.setWidth(newVal.intValue());
						if (newVal.intValue() - oldVal.intValue() >= 0) return;
						SeamCarverHelper temp = seamCarver;
						while (temp.picture().getWidth() > newVal.intValue()) {
							temp.computeEnergy();
							temp.computeMinEnergy();
							temp.removeVerticalSeam(temp.findVerticalSeam());

						}
						
						
						context.setFill(Color.WHITE);

//						int stageHeight = (int) primaryStage.getHeight();
						
						context.fillRect(0, 0, MAX_WIDTH, MAX_HEIGHT);
						context.drawImage(temp.picture(), 0, 0);
						System.out.println("resize to " + primaryStage.getWidth() + " " + primaryStage.getHeight());
					}
				};

				// schedule new task
				timer.schedule(task, delayTime);
			}
		};
		final ChangeListener<Number> hlistener = new ChangeListener<Number>() {
			final Timer timer = new Timer(); // uses a timer to call your resize method
			TimerTask task = null; // task to execute after defined delay
			final long delayTime = 200; // delay that has to pass in order to consider an operation done

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldVal, final Number newVal) {
				if (task != null) { // there was already a task scheduled from the previous operation ...
					task.cancel(); // cancel it, we have a new size to consider
				}

				task = new TimerTask() // create new task that calls your resize operation
				{
					@Override
					public void run() {

						canvas.setHeight(newVal.intValue());
						if (newVal.intValue() - oldVal.intValue() >= 0) return;
						SeamCarverHelper temp = seamCarver;
						while (temp.picture().getHeight() > newVal.intValue()) {
							temp.computeEnergy();
							temp.computeMinEnergy();
							temp.removeHorizontalSeam(temp.findHorizontalSeam());

						}
						
						
						context.setFill(Color.WHITE);

//						int stageHeight = (int) primaryStage.getHeight();
						
						context.fillRect(0, 0, MAX_WIDTH, MAX_HEIGHT);
						context.drawImage(temp.picture(), 0, 0);
						System.out.println("Height resize to " + primaryStage.getHeight() + " " + primaryStage.getWidth());
					}
				};

				// schedule new task
				timer.schedule(task, delayTime);
			}
		};
//
		primaryStage.widthProperty().addListener(listener);
		primaryStage.heightProperty().addListener(hlistener);

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * copy the given image to a writeable image
	 * 
	 * @param image
	 * @return a writeable image
	 */
	public static WritableImage copyImage(Image image) {
		int height = (int) image.getHeight();
		int width = (int) image.getWidth();
		PixelReader pixelReader = image.getPixelReader();
		WritableImage writableImage = new WritableImage(width, height);
		PixelWriter pixelWriter = writableImage.getPixelWriter();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Color color = pixelReader.getColor(x, y);
				pixelWriter.setColor(x, y, color);
			}
		}
		return writableImage;
	}

}
