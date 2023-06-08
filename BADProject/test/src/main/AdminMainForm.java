package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminMainForm {
    private Stage primaryStage;
    private Scene mainFormScene;

    private BorderPane bp;
    private VBox vb;

    private MenuBar menuBar;
    private Menu userMenu, manageMenu;
    private MenuItem logOut, book, genre;

    private Image bgImage;
    private ImageView bgImageView;
	
	private void initialize() {
		bp = new BorderPane();
		vb = new VBox();
		
		menuBar = new MenuBar();
		
		userMenu = new Menu("User");
		manageMenu = new Menu("Manage");
		
		logOut = new MenuItem("Logout");
		book = new MenuItem("Book");
		genre = new MenuItem("Genre");
		
		FileInputStream input = null;
		try {
			input = new FileInputStream("src/assets/flat-world-book-day-illustration.jpg/5075656.jpg");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		bgImage = new Image(input);
		bgImageView = new ImageView(bgImage);
		bgImageView.setFitWidth(1000);
		bgImageView.setFitHeight(800);
		bgImageView.setPreserveRatio(true);
		
		mainFormScene = new Scene(bp);
	}
	
	private void layout() {
		menuBar.getMenus().addAll(userMenu, manageMenu);
		
		userMenu.getItems().addAll(logOut);
		manageMenu.getItems().addAll(book, genre);
		
		bp.setTop(menuBar);
		bp.setCenter(bgImageView);
	}
	
    private void eventHandler() {
        logOut.setOnAction((event) -> handleMenuAction("Logout"));
        book.setOnAction((event) -> handleMenuAction("Manage Book"));
        genre.setOnAction((event) -> handleMenuAction("Manage Genre"));
    }

    private void handleMenuAction(String action) {
        primaryStage.close();
        Stage newStage = new Stage();
        try {
            switch (action) {
                case "Logout":
                    new Login().start(newStage);
                    break;
                case "Manage Book":
                    new ManageBookForm().start(newStage);
                    break;
                case "Manage Genre":
                    new ManageGenreForm().start(newStage);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		
		initialize();
		layout();
		eventHandler();
		
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("file:src/assets/book.png "));
		primaryStage.setTitle("Bookstore");
		primaryStage.setScene(mainFormScene);
		primaryStage.show();
	}

}
