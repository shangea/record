package com.mays.record.view;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ViewBuilder {

	public static Text getText(String text) {
		Text t = new Text(text);
		t.setFont(Font.font("System Regular", FontWeight.NORMAL, 20));
		t.setTextAlignment(TextAlignment.CENTER);
		return t;
	}

	public static Label getLabel(String text) {
		Label label = new Label(text);
		label.setFont(Font.font("System Regular", FontWeight.NORMAL, 16));
		// label.setWrapText(true);
		return label;
	}

	public static Label getLabelRight(String text) {
		Label label = new Label(text);
		label.setFont(Font.font("System Regular", FontWeight.NORMAL, 16));
		GridPane.setHalignment(label, HPos.RIGHT);
		return label;
	}

	public static Button getBigButton(String text) {
		Button button = new Button(text);
		button.setPrefSize(120, 40);
		return button;
	}

	public static Button getButton(String text) {
		Button button = new Button(text);
		button.setPrefSize(100, 30);
		return button;
	}

	public static TextField getTextFieldNumber() {
		TextField tf = new TextField() {
			@Override
			public void replaceText(int start, int end, String text) {
				if (!text.matches("[a-z]")) {
					super.replaceText(start, end, text);
				}
			}

			@Override
			public void replaceSelection(String replacement) {
				if (!replacement.matches("[a-z]")) {
					super.replaceSelection(replacement);
				}
			}
		};
//		tf.setFont(Font.font("System Regular", FontWeight.NORMAL, 16));
		return tf;
	}

	public static void showAlert(String text) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("提示");
		alert.setHeaderText("");
		alert.setContentText(text);
		alert.show();
	}

	public static Label getTableLabel(String text) {
		Label label = new Label(text);
		label.setFont(Font.font("System Regular", FontWeight.NORMAL, 16));
		GridPane.setHalignment(label, HPos.CENTER);
		label.setPadding(new Insets(5));
		return label;
	}

}
