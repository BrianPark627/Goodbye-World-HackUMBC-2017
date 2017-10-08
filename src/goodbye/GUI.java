package goodbye;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GUI extends Application {
	private final static String EDIT_DELIMITER = "*edit*";
	
	private final static String BUG_TEMPLATE = "bugs/bugINDEX.txt";
	private final static String FIX_TEMPLATE = "fixes/fixINDEX.txt";
	private final static String ERR_TEMPLATE = "errs/errINDEX.txt";
	private final static String MSG_TEMPLATE = "msgs/msgINDEX.txt";
	
	private final static int WIDTH = 700;
	private final static int HEIGHT = 300;
	
	ListedLabelBox errorBox = new ListedLabelBox(this);
	ListedLabelBox codeBox = new ListedLabelBox(this, Color.BEIGE, Color.BLACK);
	private int index = 1;
	private final static int MAX_INDEX = 10;
	
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
		root.setAlignment(Pos.CENTER);
		
		this.loadSet();
	
		HBox boxes = new HBox();
		VBox eb = this.errorBox.getBox();
		VBox cb = this.codeBox.getBox();
		eb.prefWidthProperty().bind(root.widthProperty().divide(2));
		cb.prefWidthProperty().bind(root.widthProperty().divide(2));
		boxes.getChildren().addAll(eb, cb);
		
		// add the nodes to the root
		root.getChildren().addAll(desktop, boxes);
		
		// show stage
		Scene scene = new Scene(root, WIDTH, HEIGHT);
		
		stage.setScene(scene);
		stage.setTitle("Goodbye World");
		stage.setFullScreen(true);
		stage.show();
	}

	private void readControls(ListedLabelBox llb, String file, String editDelimiter, boolean isConsole) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(file));
		
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			
			if (line.startsWith(editDelimiter)) {
				line = line.substring(editDelimiter.length());
				TextField field = new TextField(line);
				llb.add(field);
			} else {
				if (isConsole) {
					if (!line.startsWith("    ")) {
						line = "> " + line;
					} else {
						line = "  " + line;
					}
				}
				
				Text text = new Text(line);
				llb.add(text);
			}
		}
		
		scanner.close();
	}
	
	private String readLine(String file) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(file));
		String line = scanner.nextLine();
		scanner.close();
		return line;
	}
	
	private List<String> readLines(String file) throws FileNotFoundException {
		List<String> list = new ArrayList<>();
		
		Scanner scanner = new Scanner(new File(file));
		
		while (scanner.hasNextLine()) {
			list.add(scanner.nextLine());
		}
		
		scanner.close();
		
		return list;
	}
	
	public void loadSet() throws FileNotFoundException {
		if (this.index >= MAX_INDEX) {
			return;
		} else if (this.index == MAX_INDEX - 1) {
			this.loadFinalSet();
			return;
		}
		
		this.errorBox.reset();
		this.codeBox.reset();
		
		readControls(this.errorBox, ERR_TEMPLATE.replace("INDEX", String.valueOf(this.index)), EDIT_DELIMITER, true);
		readControls(this.codeBox, BUG_TEMPLATE.replace("INDEX", String.valueOf(this.index)), EDIT_DELIMITER, false);
		this.codeBox.setCorrectAnswer(readLine(FIX_TEMPLATE.replace("INDEX", String.valueOf(this.index))));
		
		String transitionText = this.index + 1 == 1 ? "_welcome operator_" : "_welcome back operator_";
		
		List<String> msgs = new ArrayList<>();
		msgs.add(transitionText);
		
		try {
			msgs.addAll(readLines(MSG_TEMPLATE.replace("INDEX", String.valueOf(this.index++))));
		} catch (FileNotFoundException fnfe) {
		}
		
		this.codeBox.addMsgs(msgs);
	}
	
	public void loadFinalSet() throws FileNotFoundException {
		this.codeBox.reset();
		Parent p = this.codeBox.getBox().getParent();
		((HBox) p).getChildren().remove(this.codeBox.getBox());
		
		this.errorBox.reset();
		this.errorBox.add(new Text("Goodbye World"));
		this.errorBox.getBox().setOnKeyTyped(e -> {
			this.errorBox.getBox().setOnKeyTyped(e2 -> {});
			
			try {
				List<String> msgs = readLines(MSG_TEMPLATE.replace("INDEX", "Final"));
				this.errorBox.reset();
				this.errorBox.getBox().setFillWidth(true);
				
				for (String m : msgs) {
					this.errorBox.add(new Text(m));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
