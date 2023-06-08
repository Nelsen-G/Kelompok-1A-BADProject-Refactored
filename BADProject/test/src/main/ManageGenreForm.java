package main;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;

import model.Genre;
import util.Connect;

public class ManageGenreForm {
    private Stage primaryStage;
    private Scene manageGenreFormScene;
    private GenreFormWindow genreFormWindow;
    private Window window;
    private BorderPane bp;
    private GridPane windowGP, leftGP;
    private MenuBar menuBar;
    private Menu userMenu, manageMenu;
    private MenuItem logOut, bookMenu, genreMenu;
    private TableView<Genre> genreTable;
    private Genre genre;
    private Alert alertMessage;

    public void initialize() {
        genreFormWindow = new GenreFormWindow(primaryStage);
        manageGenreFormScene = new Scene(genreFormWindow.getLayout(), 1200, 800);
    }

    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        initialize();
        setupUI();
        setEventHandlers();

        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("file:src/assets/book.png "));
        primaryStage.setTitle("Bookstore");
        primaryStage.setScene(manageGenreFormScene);
        primaryStage.show();
    }

    private void setupUI() {
        genreFormWindow.setBookTable();
    }

    private void setEventHandlers() {
        genreFormWindow.setEventHandlers(this);
    }
}

class GenreFormWindow {
    private ManageGenreForm manageGenreForm;
    private Stage primaryStage;
    private BorderPane bp;
    private GridPane windowGP, leftGP;
    private Window window;
    private MenuBar menuBar;
    private Menu userMenu, manageMenu;
    private MenuItem logOut, bookMenu, genreMenu;
    private TableView<Genre> genreTable;
    private Alert alertMessage;
    private Label nameLbl;
    private TextField nameField;
    private Button insertButton;

    public TableView<Genre> getGenreTable() {
        return genreTable;
    }

    public TextField getNameField() {
        return nameField;
    }

    public Button getInsertButton() {
        return insertButton;
    }

    public GenreFormWindow(Stage primaryStage) {
        this.primaryStage = primaryStage;
        bp = new BorderPane();
        windowGP = new GridPane();
        leftGP = new GridPane();

        window = new Window();

        menuBar = new MenuBar();

        userMenu = new Menu("User");
        manageMenu = new Menu("Manage");

        logOut = new MenuItem("Logout");
        bookMenu = new MenuItem("Book");
        genreMenu = new MenuItem("Genre");

        nameLbl = new Label("Name");
        nameField = new TextField();

        insertButton = new Button("Insert");
        insertButton.setPrefWidth(400);

        genreTable = new TableView<Genre>();
    }

    public BorderPane getLayout() {
        menuBar.getMenus().addAll(userMenu, manageMenu);
        userMenu.getItems().addAll(logOut);
        manageMenu.getItems().addAll(bookMenu, genreMenu);

        leftGP.add(nameLbl, 0, 0);
        leftGP.add(nameField, 0, 1);
        leftGP.add(insertButton, 0, 2);

        leftGP.setPrefWidth(300);
        leftGP.setAlignment(Pos.CENTER);
        leftGP.setVgap(0);

        windowGP.add(leftGP, 0, 0);
        windowGP.add(genreTable, 1, 0);
        windowGP.setMargin(leftGP, new Insets(70, 30, 30, 30));
        windowGP.setMargin(genreTable, new Insets(0));

        window.getContentPane().getChildren().add(windowGP);
        window.setTitle("Manage Genre");

        bp.setTop(menuBar);
        bp.setCenter(window);

        return bp;
    }

    public void setBookTable() {
        TableColumn<Genre, Integer> genreID = new TableColumn<Genre, Integer>("ID");
        TableColumn<Genre, String> genreName = new TableColumn<Genre, String>("Name");

        genreID.setCellValueFactory(new PropertyValueFactory<>("genreID"));
        genreName.setCellValueFactory(new PropertyValueFactory<>("genreName"));

        genreID.setMinWidth(850 / 2);
        genreName.setMinWidth(850 / 2);

        genreTable.getColumns().addAll(genreID, genreName);
        genreTable.setPrefHeight(800);

        populateGenreTable();
    }

    public void populateGenreTable() {
        Connect cn = new Connect();
        String q = "SELECT * FROM genre";
        ResultSet r = cn.executeSelect(q);
        try {
            while (r.next()) {
                int id = r.getInt("GenreID");
                String gen = r.getString("GenreName");
                Genre g = new Genre(id, gen);
                genreTable.getItems().add(g);
            }
        } catch (SQLException e) {
            
            e.printStackTrace();
        }
    }

    public void setEventHandlers(ManageGenreForm manageGenreForm) {
        logOut.setOnAction(event -> handleLogout());
        bookMenu.setOnAction(event -> handleBookMenu());
        genreMenu.setOnAction(event -> handleGenreMenu());
        insertButton.setOnAction(event -> handleInsertButton());
    }

    private void handleLogout() {
        primaryStage.close();
        Stage newStage = new Stage();
        try {
            new Login().start(newStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleBookMenu() {
        primaryStage.close();
        Stage newStage = new Stage();
        try {
            new ManageBookForm().start(newStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGenreMenu() {
        primaryStage.close();
        Stage newStage = new Stage();
        try {
            manageGenreForm.start(newStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        alertMessage = new Alert(AlertType.ERROR);
        alertMessage.setTitle(title);
        alertMessage.setHeaderText(message);
        alertMessage.show();
    }

    private void handleInsertButton() {
        String name = nameField.getText();

        if (name.length() < 5 || name.length() > 12) {
            showAlert("Error", "Genre name must be between 5 - 12 characters");
        } else {
            Connect cn = new Connect();
            String q = "INSERT INTO genre VALUES(null, \"" + name + "\")";
            cn.execute(q);
            showAlert("Notification", "Genre " + name + " added!");
            genreTable.getItems().clear();
            populateGenreTable();
        }
    }
}