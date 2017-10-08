package goodbye;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListedLabelBox {
	private static Font displayFont;
	private final static int FONT_SIZE = 16;
	
	static {
		try {
			displayFont = Font.loadFont(new FileInputStream("fonts/apercu_mono.ttf"), FONT_SIZE);
		} catch (Exception e) {
			displayFont = Font.font("monospaced", FONT_SIZE);
		}
	}
	
	private GUI gui;
	private VBox box = new VBox();
	private List<Node> children = box.getChildren();
	private StringBuilder msg = new StringBuilder();
	private SimpleObjectProperty<Paint> bgColorProperty;
	private SimpleObjectProperty<Paint> textColorProperty;
	private BackgroundFill bgFill;
	private List<String> correctAnswers = new ArrayList<>();
	
	public ListedLabelBox(GUI gui) {
		this(gui, Color.BLACK, Color.WHITE);
	}
	
	public ListedLabelBox(GUI gui, Color bgColor, Color textColor) {
		this.gui = gui;
		this.bgColorProperty = new SimpleObjectProperty<>(bgColor);
		this.textColorProperty = new SimpleObjectProperty<>(textColor);
		this.bgFill = new BackgroundFill(this.bgColorProperty.get(), CornerRadii.EMPTY, Insets.EMPTY);
		
		this.box.setBackground(new Background(this.bgFill));
		this.box.setPadding(new Insets(10));
	}
	
	public void addMsgs(List<String> msgs) {
		this.msg = new StringBuilder();
		msgs.forEach(m -> ListedLabelBox.this.msg.append(m).append('\n'));
	}
	
	public void add(TextField field) {
		this.children.add(field);
		String style = "-fx-background-color: rgba(0, 0, 0, 0);" +
				"-fx-text-fill: #e30000;";
		field.setStyle(style);
		field.setFont(Font.font(displayFont.getFamily(), FontWeight.BOLD, FONT_SIZE));
		field.selectedTextProperty().addListener(((observable, oldValue, newValue) -> field.deselect()));
		field.textProperty().addListener(((observable, oldValue, newValue) -> {
			for (String correctAnswer : ListedLabelBox.this.correctAnswers) {
				if (equalsWithoutWS(correctAnswer, newValue)) {
					field.setEditable(false);
					field.setStyle("-fx-background-color: rgba(0, 0, 0, 0);" +
							"-fx-text-fill: #009000");
					Platform.runLater(() -> {
						Text msgText = new Text(this.msg.toString());
						msgText.setFont(displayFont);
						
						ListedLabelBox.this.children.clear();
						gui.errorBox.children.clear();
						gui.errorBox.add(msgText);
						
						gui.errorBox.box.requestFocus();
						gui.errorBox.box.setOnKeyTyped(e -> {
							try {
								gui.errorBox.box.setOnKeyTyped(e2 -> {});
								ListedLabelBox.this.gui.loadSet();
							} catch (FileNotFoundException fnfe) {
								fnfe.printStackTrace();
								System.exit(1);
							}
						});
					});
				}
			}
		}));
	}
	
	public void reset() {
		this.children.clear();
	}
	
	public void add(Text text) {
		this.children.add(text);
		text.setStroke(textColorProperty.get());
		text.setFont(displayFont);
	}
	
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswers.clear();
		for (String ca : correctAnswer.split("~")) {
			this.correctAnswers.add(ca);
		}
//		this.correctAnswers.addAll(Arrays.asList(correctAnswer.split("~")));
	}
	
	public VBox getBox() {
		return box;
	}
	
	private static boolean equalsWithoutWS(String a, String b) {
		StringBuilder at = new StringBuilder();
		StringBuilder bt = new StringBuilder();
		
		for (int i = 0; i < a.length(); i++) {
			if (a.charAt(i) != ' ' && a.charAt(i) != '\t') {
				at.append(a.charAt(i));
			}
		}
		
		for (int i = 0; i < b.length(); i++) {
			if (b.charAt(i) != ' ' && b.charAt(i) != '\t') {
				bt.append(b.charAt(i));
			}
		}
		
		return at.toString().equals(bt.toString());
	}
}
