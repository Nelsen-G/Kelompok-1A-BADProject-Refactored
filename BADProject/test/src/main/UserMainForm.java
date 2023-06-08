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
import javafx.stage.Stage;


public class UserMainForm {
    private Stage primaryStage;
    private Scene mainFormScene;
    private BorderPane bp;
    private MenuBar menuBar;
    private Menu userMenu, transactionMenu;
    private MenuItem logOut, buyBook, transactionHistory;
    private Image bgImage;
    private ImageView bgImageView;

    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        initialize();
        setupLayout();
        setupEventHandlers();
        configureStage();
        primaryStage.show();
    }

    private void initialize() throws FileNotFoundException {
        bp = new BorderPane();
        menuBar = new MenuBar();
        userMenu = new Menu("User");
        transactionMenu = new Menu("Transaction");
        logOut = new MenuItem("Logout");
        buyBook = new MenuItem("Buy Book");
        transactionHistory = new MenuItem("View Transaction History");
        bgImage = new Image(new FileInputStream("src/assets/flat-world-book-day-illustration.jpg/5075656.jpg"));
        bgImageView = new ImageView(bgImage);
        bgImageView.setFitWidth(1000);
        bgImageView.setFitHeight(800);
        bgImageView.setPreserveRatio(true);
        mainFormScene = new Scene(bp);
    }

    private void setupLayout() {
        menuBar.getMenus().addAll(userMenu, transactionMenu);
        userMenu.getItems().addAll(logOut);
        transactionMenu.getItems().addAll(buyBook, transactionHistory);
        bp.setTop(menuBar);
        bp.setCenter(bgImageView);
    }

    private void setupEventHandlers() {
        logOut.setOnAction(event -> handleMenuAction("Logout"));
        buyBook.setOnAction(event -> handleMenuAction("Buy Book"));
        transactionHistory.setOnAction(event -> handleMenuAction("View Transaction History"));
    }

    private void configureStage() {
        primaryStage.setResizable(false);
        primaryStage.setTitle("Bookstore");
        primaryStage.getIcons().add(new Image("file:src/assets/book.png"));
        primaryStage.setScene(mainFormScene);
    }

    private void handleMenuAction(String action) {
        ActionHandler actionHandler = new ActionHandler();
        actionHandler.handleAction(action, primaryStage);
    }

    private class ActionHandler {
        public void handleAction(String action, Stage primaryStage) {
            primaryStage.close();
            try {
                Stage newStage = new Stage();
                switch (action) {
                    case "Logout":
                        new Login().start(newStage);
                        break;
                    case "Buy Book":
                        new BuyBookForm().start(newStage);
                        break;
                    case "View Transaction History":
                        new TransactionHistory().start(newStage);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}