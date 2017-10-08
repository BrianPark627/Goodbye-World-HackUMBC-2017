package goodbye;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GUI extends Application {
	private final static String EDIT_DELIMITER = "*edit*";
	
	private final static int WIDTH = 700;
	private final static int HEIGHT = 300;
	
    @Override
    public void start(Stage stage) throws Exception {
    	// desktop image stuff
    	File desktopFile = new File("INSERT URL HERE");
    	if (!desktopFile.exists()) {
    		// throw new FileNotFoundException(desktopFile.toString());
		}
    	String desktopURL = desktopFile.toURI().toURL().toString();
    	System.out.println(desktopURL);
    	Image desktopImg = new Image(desktopURL);
		ImageView desktop = new ImageView(desktopImg);
		
		// root stuff
		StackPane root = new StackPane();
		root.setAlignment(Pos.TOP_LEFT);
		
		// error box
		ListedLabelBox errorBox = new ListedLabelBox();
		readControls(errorBox, "errs/err1.txt", EDIT_DELIMITER);
		
		// code editor box
		ListedLabelBox codeBox = new ListedLabelBox(Color.BEIGE, Color.BLACK);
		readControls(codeBox, "bugs/bug1.txt", EDIT_DELIMITER);
	
		HBox boxes = new HBox();
		VBox eb = errorBox.getBox();
		VBox cb = codeBox.getBox();
		eb.setPrefWidth(WIDTH / 2);
		cb.setPrefWidth(WIDTH / 2);
		boxes.getChildren().addAll(eb, cb);
		
		// add the nodes to the root
		root.getChildren().addAll(desktop, boxes);
		
		// show stage
		Scene scene = new Scene(root, WIDTH, HEIGHT);
		stage.setScene(scene);
		stage.setTitle("Goodbye World");
		stage.show();
    }

    public void readControls(ListedLabelBox llb, String file, String editDelimiter) throws FileNotFoundException {
    	Scanner scanner = new Scanner(new File(file));
    	
    	while (scanner.hasNextLine()) {
    		String line = scanner.nextLine();
			
    		if (line.startsWith(editDelimiter)) {
    			line = line.substring(editDelimiter.length());
    			TextField field = new TextField(line);
    			llb.add(field);
			} else {
    			Text text = new Text(line);
    			llb.add(text);
			}
		}
	}

    public static void main(String[] args) {
        launch(args);
    }
}
