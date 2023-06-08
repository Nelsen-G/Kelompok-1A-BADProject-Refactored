package main;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;
import model.Book;
import util.Connect;

public class ManageBookForm {
	Stage primaryStage;
	Scene manageBookFormScene;
	
	Window window;
	BorderPane bp;
	GridPane windowGP, leftGP, buttonGP;
	
	MenuBar menuBar;
	Menu userMenu, manageMenu;
	MenuItem logOut, bookMenu, genreMenu;
	
	Label idLbl, nameLbl, authorLbl, genreLbl, priceLbl, stockLbl;
	TextField idField, nameField, authorField, priceField;
	ComboBox<String> genreCB;
	Spinner<Integer> stockSpinner;
	Button insertButton, updateButton, deleteButton;
	
	TableView<Book> bookTable;
	
	Book book;
	Alert alertMessage;
	
	public void initialize() {
		bp = new BorderPane();
		windowGP = new GridPane();
		buttonGP = new GridPane();
		leftGP = new GridPane();
		
		window = new Window();
		
		menuBar = new MenuBar();
		
		userMenu = new Menu("User");
		manageMenu = new Menu("Manage");
		
		logOut = new MenuItem("Logout");
		bookMenu = new MenuItem("Book");
		genreMenu = new MenuItem("Genre");
		
		idLbl = new Label("ID");
		idField = new TextField();
		idField.setEditable(false);
		
		nameLbl = new Label("Name");
		nameField = new TextField();
		
		authorLbl = new Label("Author");
		authorField = new TextField();
		
		genreLbl = new Label("Genre");
		genreCB = new ComboBox<>();
		genreCB.setItems(FXCollections.observableArrayList("Fantasy", "Sci-Fi", "Mystery", "Thriller", "Romance", "Programming", "Drama"));
		genreCB.setPrefWidth(300);
		
		priceLbl = new Label("Price");
		priceField = new TextField();
		
		stockLbl = new Label("Stock");
		stockSpinner = new Spinner<>(0, 100, 1);
		stockSpinner.setPrefWidth(300);
		
		insertButton = new Button("Insert");
		insertButton.setPrefWidth(300);
		
		updateButton = new Button("Update");
		updateButton.setPrefWidth(300);
		
		deleteButton = new Button("Delete");
		deleteButton.setPrefWidth(300);
		
		bookTable = new TableView<Book>();
		
		manageBookFormScene = new Scene(bp, 1200, 800);
	}
	
	public void layout() {
	    configureMenu();
	    configureLeftGridPane();
	    configureButtonGridPane();
	    leftGP.add(buttonGP, 0, 12);
	    configureWindowGridPane();
	    configureWindow();

	    bp.setTop(menuBar);
	    bp.setCenter(window);
	}

	private void configureMenu() {
	    menuBar.getMenus().addAll(userMenu, manageMenu);
	    userMenu.getItems().addAll(logOut);
	    manageMenu.getItems().addAll(bookMenu, genreMenu);
	}

	private void configureLeftGridPane() {
	    leftGP.add(idLbl, 0, 0);
	    leftGP.add(idField, 0, 1);

	    leftGP.add(nameLbl, 0, 2);
	    leftGP.add(nameField, 0, 3);

	    leftGP.add(authorLbl, 0, 4);
	    leftGP.add(authorField, 0, 5);

	    leftGP.add(genreLbl, 0, 6);
	    leftGP.add(genreCB, 0, 7);

	    leftGP.add(priceLbl, 0, 8);
	    leftGP.add(priceField, 0, 9);

	    leftGP.add(stockLbl, 0, 10);
	    leftGP.add(stockSpinner, 0, 11);

	    leftGP.setPrefWidth(300);
	    leftGP.setVgap(5);
	    leftGP.setMargin(buttonGP, new Insets(20, 0, 0, 0));
	}

	private void configureButtonGridPane() {
	    insertButton = new Button("Insert");
	    updateButton = new Button("Update");
	    deleteButton = new Button("Delete");
	    
	    insertButton.setPrefWidth(leftGP.getPrefWidth());
	    updateButton.setPrefWidth(leftGP.getPrefWidth());
	    deleteButton.setPrefWidth(leftGP.getPrefWidth());

	    buttonGP.add(insertButton, 0, 0);
	    buttonGP.add(updateButton, 0, 1);
	    buttonGP.add(deleteButton, 0, 2);
	    
	}

	private void configureWindowGridPane() {
	    windowGP.add(leftGP, 0, 0);
	    windowGP.add(bookTable, 1, 0);
	    windowGP.setMargin(leftGP, new Insets(70, 30, 30, 30));
	}

	private void configureWindow() {
	    window.getContentPane().getChildren().add(windowGP);
	    window.setTitle("Manage Book");
	}

	
	
	public void setBookTable() {
	    configureTableColumns();
	    configureTableWidths();
	    configureTableData();
	}

	private void configureTableColumns() {
	    TableColumn<Book, Integer> bookID = new TableColumn<>("ID");
	    TableColumn<Book, String> bookName = new TableColumn<>("Name");
	    TableColumn<Book, String> bookAuthor = new TableColumn<>("Author");
	    TableColumn<Book, String> bookGenre = new TableColumn<>("Genre");
	    TableColumn<Book, Integer> bookStock = new TableColumn<>("Stock");
	    TableColumn<Book, Integer> bookPrice = new TableColumn<>("Price");

	    bookID.setCellValueFactory(new PropertyValueFactory<>("bookID"));
	    bookName.setCellValueFactory(new PropertyValueFactory<>("bookName"));
	    bookAuthor.setCellValueFactory(new PropertyValueFactory<>("bookAuthor"));
	    bookGenre.setCellValueFactory(new PropertyValueFactory<>("bookGenre"));
	    bookStock.setCellValueFactory(new PropertyValueFactory<>("bookStock"));
	    bookPrice.setCellValueFactory(new PropertyValueFactory<>("bookPrice"));

	    bookTable.getColumns().addAll(bookID, bookName, bookAuthor, bookGenre, bookStock, bookPrice);
	}

	private void configureTableWidths() {
	    double tableWidth = 900;
	    double columnWidth = tableWidth / 6.25;

	    for (TableColumn<Book, ?> column : bookTable.getColumns()) {
	        column.setMinWidth(columnWidth);
	    }
	}

	private void configureTableData() {
	    bookTable.setPrefHeight(800);

	    Connect cn = new Connect();
	    String query = "SELECT * FROM book b JOIN genre g ON b.GenreID = g.GenreID";
	    ResultSet resultSet = cn.executeSelect(query);

	    try {
	        while (resultSet.next()) {
	            int id = resultSet.getInt("BookID");
	            String name = resultSet.getString("BookName");
	            String author = resultSet.getString("BookAuthor");
	            String genre = resultSet.getString("GenreName");
	            int price = resultSet.getInt("BookPrice");
	            int stock = resultSet.getInt("BookStock");
	            Book book = new Book(id, name, author, genre, stock, price);
	            bookTable.getItems().add(book);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	public boolean checkPrice(String price) {
	    for (int i = 0; i < price.length(); i++) {
	        if (Character.isDigit(price.charAt(i))) {
	            return false;
	        }
	    }
	    return true;
	}

	
	private void eventHandler() {
	    logOut.setOnAction((event) -> {
	        primaryStage.close();
	        openLoginStage();
	    });

	    bookMenu.setOnAction((event) -> {
	        primaryStage.close();
	        openManageBookFormStage();
	    });

	    genreMenu.setOnAction((event) -> {
	        primaryStage.close();
	        openManageGenreFormStage();
	    });

	    insertButton.setOnAction((event) -> {
	        handleInsertButton();
	    });

	    bookTable.setOnMouseClicked((event) -> {
	        handleBookTableClick();
	    });

	    updateButton.setOnAction((event) -> {
	        handleUpdateButton();
	    });

	    deleteButton.setOnAction((event) -> {
	        handleDeleteButton();
	    });
	}

	private void openLoginStage() {
	    Stage newStage = new Stage();
	    try {
	        new Login().start(newStage);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private void openManageBookFormStage() {
	    Stage newStage = new Stage();
	    try {
	        new ManageBookForm().start(newStage);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private void openManageGenreFormStage() {
	    Stage newStage = new Stage();
	    try {
	        new ManageGenreForm().start(newStage);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private void handleInsertButton() {
	    String id = idField.getText();
	    String name = nameField.getText();
	    String author = authorField.getText();
	    boolean selectedCB = genreCB.getSelectionModel().isEmpty();
	    String price = priceField.getText();
	    Integer stock = stockSpinner.getValue();

	    if (name.length() < 5 || name.length() > 45) {
	        showErrorAlert("Book name must be between 5 and 45 characters!");
	    } else if (author.length() < 5 || author.length() > 20) {
	        showErrorAlert("Author name must be between 5 and 20 characters!");
	    } else if (selectedCB) {
	        showErrorAlert("Genre must be selected!");
	    } else if (checkPrice(price)) {
	        showErrorAlert("Price must be numerical!");
	    } else if (stock <= 0) {
	        showErrorAlert("Stock must be more than 0!");
	    } else {
	        insertBook(id, name, author, price, stock);
	        clearInputFields();
	        updateBookTable();
	        showInformationAlert("Book " + name + " added!");
	    }
	}

	private void handleBookTableClick() {
	    Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
	    if (selectedBook != null) {
	        idField.setText(selectedBook.getBookID().toString());
	        nameField.setText(selectedBook.getBookName());
	        authorField.setText(selectedBook.getBookAuthor());
	        genreCB.setValue(selectedBook.getBookGenre());
	        priceField.setText(selectedBook.getBookPrice().toString());
	        stockSpinner.getValueFactory().setValue(selectedBook.getBookStock());
	    }
	}

	private void handleUpdateButton() {
	    String id = idField.getText();
	    String name = nameField.getText();
	    String author = authorField.getText();
	    boolean selectedCB = genreCB.getSelectionModel().isEmpty();
	    String price = priceField.getText();
	    Integer stock = stockSpinner.getValue();

	    if (id.isEmpty()) {
	        showErrorAlert("Please select a book first!");
	    } else if (name.length() < 5 || name.length() > 45) {
	        showErrorAlert("Book name must be between 5 and 45 characters!");
	    } else if (author.length() < 5 || author.length() > 20) {
	        showErrorAlert("Author name must be between 5 and 20 characters!");
	    } else if (selectedCB) {
	        showErrorAlert("Genre must be selected!");
	    } else if (checkPrice(price)) {
	        showErrorAlert("Price must be numerical!");
	    } else if (stock <= 0) {
	        showErrorAlert("Stock must be more than 0!");
	    } else {
	        updateBook(id, name, author, price, stock);
	        clearInputFields();
	        updateBookTable();
	        showInformationAlert("Book updated!");
	    }
	}

	private void handleDeleteButton() {
	    String id = idField.getText();
	    if (id.isEmpty()) {
	        showErrorAlert("Please select a book first!");
	    } else {
	        deleteBook(id);
	        clearInputFields();
	        updateBookTable();
	        showInformationAlert("Book deleted!");
	    }
	}

	private void showErrorAlert(String message) {
	    Alert alertMessage = new Alert(AlertType.ERROR);
	    alertMessage.setTitle("Error");
	    alertMessage.setHeaderText(message);
	    alertMessage.show();
	}

	private void showInformationAlert(String message) {
	    Alert alertMessage = new Alert(AlertType.INFORMATION);
	    alertMessage.setTitle("Notification");
	    alertMessage.setHeaderText(message);
	    alertMessage.show();
	}

	private void insertBook(String id, String name, String author, String price, Integer stock) {
	    String cb = genreCB.getValue();
	    Connect c = new Connect();
	    String genreIdQuery = "SELECT GenreID FROM genre WHERE GenreName = \"" + cb + "\"";
	    ResultSet r = c.executeSelect(genreIdQuery);
	    int genreId = 0;
	    try {
	        while (r.next()) {
	            genreId = r.getInt("GenreID");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    String insertQuery = "INSERT INTO book VALUES (null," + genreId + ",\"" + name + "\",\"" + author + "\","
	            + Integer.parseInt(price) + "," + stock + ")";
	    c.execute(insertQuery);
	}

	private void clearInputFields() {
	    nameField.setText("");
	    authorField.setText("");
	    genreCB.getSelectionModel().clearSelection();
	    priceField.setText("");
	    stockSpinner.getValueFactory().setValue(1);
	}

	private void updateBookTable() {
	    bookTable.getItems().clear();
	    Connect c = new Connect();
	    String selectQuery = "SELECT * FROM book b JOIN genre g ON b.GenreID = g.GenreID";
	    ResultSet re = c.executeSelect(selectQuery);
	    try {
	        while (re.next()) {
	            int ids = re.getInt("BookID");
	            String nm = re.getString("BookName");
	            String auth = re.getString("BookAuthor");
	            String gen = re.getString("GenreName");
	            int harga = re.getInt("BookPrice");
	            int setok = re.getInt("BookStock");
	            Book bk = new Book(ids, nm, auth, gen, setok, harga);
	            bookTable.getItems().add(bk);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	private void updateBook(String id, String name, String author, String price, Integer stock) {
	    String cb = genreCB.getValue();
	    Connect c = new Connect();
	    String genreIdQuery = "SELECT GenreID FROM genre WHERE GenreName = \"" + cb + "\"";
	    ResultSet r = c.executeSelect(genreIdQuery);
	    int genreId = 0;
	    try {
	        while (r.next()) {
	            genreId = r.getInt("GenreID");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    String updateQuery = "UPDATE book SET BookName=\"" + name + "\",BookAuthor=\"" + author + "\",BookPrice=" + price
	            + ",BookStock=" + stock + ",GenreID=" + genreId + " WHERE BookID=" + id;
	    c.execute(updateQuery);
	}

	private void deleteBook(String id) {
	    Connect c = new Connect();
	    String deleteQuery = "DELETE FROM book WHERE BookID=" + id;
	    c.execute(deleteQuery);
	}
	
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		
		initialize();
		layout();
		setBookTable();
		eventHandler();
		
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("file:src/assets/book.png "));
		primaryStage.setTitle("Bookstore");
		primaryStage.setScene(manageBookFormScene);
		primaryStage.show();
	}

}
