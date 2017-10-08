package goodbye;

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

import java.io.FileInputStream;
import java.util.ArrayList;
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
	
	private List<Node> labels = new ArrayList<>();
	private SimpleObjectProperty<Paint> bgColorProperty;
	private SimpleObjectProperty<Paint> textColorProperty;
	private BackgroundFill bgFill;
	
	public ListedLabelBox() {
		this(Color.BLACK, Color.WHITE);
	}
	
	public ListedLabelBox(Color bgColor, Color textColor) {
		bgColorProperty = new SimpleObjectProperty<>(bgColor);
		textColorProperty = new SimpleObjectProperty<>(textColor);
		bgFill = new BackgroundFill(bgColorProperty.get(), CornerRadii.EMPTY, Insets.EMPTY);
	}
	
	public void add(TextField field) {
		this.labels.add(field);
		String color = textColorProperty.get().toString();
		color = color.substring(2);
		field.setStyle("-fx-background-color: rgba(0, 0, 0, 0);" +
				"-fx-text-fill: #f00;");
		field.setFont(Font.font(displayFont.getFamily(), FontWeight.BOLD, FONT_SIZE));
	}
	
	public void add(Text text) {
		this.labels.add(text);
		text.setStroke(textColorProperty.get());
		text.setFont(displayFont);
	}
	
	public VBox getBox() {
		VBox box = new VBox();
		
		box.getChildren().addAll(this.labels);
		box.setBackground(new Background(bgFill));
		box.setPadding(new Insets(10));
		
		return box;
	}
}
