package application;

import controller.RootController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		Parent root = fxmlLoader.load();
		RootController rootController= fxmlLoader.getController();
		rootController.stage=primaryStage;
		Scene s= new Scene(root);
		primaryStage.setScene(s);
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image(getClass().getResource("/image/logotest.png").toString()));
		
		System.setProperty("prism.lcdtext", "false"); // 폰트파일 로드전에 실행

		Font.loadFont(getClass().getResourceAsStream("/resources/NanumSquareR.ttf"), 10);
		Font.loadFont(getClass().getResourceAsStream("/resources/NanumGothic.ttf"), 10);
		s.getStylesheets().add(getClass().getResource("main.css").toString());
		
		primaryStage.setTitle("KD Library");
		primaryStage.show();
		
		
	}

}
