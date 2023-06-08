package main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;
import model.Book;
import model.Cart;
import util.Connect;

public class BuyBookForm {
	Stage primaryStage;
	Scene buyBookScene;
	
	Window window;
	BorderPane bp;
	VBox vb, quantityVB, buttonVB, upperVB, leftlowerVB, rightlowerVB, lowerVB;
	HBox lowerHB;
	
	MenuBar menuBar;
	Menu userMenu, transactionMenu;
	MenuItem logOut, buyBook, transactionHistory;
	
	TableView<Book> bookTable;
	TableView<Cart> cartTable;
	
	Text bookTxt, cartTxt;
	Label quantityLbl;
	Spinner<Integer> quantity;
	Button addButton, updateButton, removeButton, checkoutButton;
	
	Book book;
	Alert alertMessage;
	
	private void initialize() {
		window = new Window();
		bp = new BorderPane();
		
		vb = new VBox();
		quantityVB = new VBox();
		buttonVB = new VBox();
		upperVB = new VBox();
		leftlowerVB = new VBox();
		rightlowerVB = new VBox();
		lowerVB = new VBox();
		
		lowerHB = new HBox();
		
		bookTable = new TableView<Book>();
		cartTable = new TableView<Cart>();
		
		menuBar = new MenuBar();
		
		userMenu = new Menu("User");
		transactionMenu = new Menu("Transaction");
		
		logOut = new MenuItem("Logout");
		buyBook = new MenuItem("Buy Book");
		transactionHistory = new MenuItem("View Transaction History");
		
		bookTxt = new Text("Book List");
		cartTxt = new Text("My Cart");
		
		quantityLbl = new Label("Quantity");
		quantity = new Spinner<>(1, 100, 1);
		quantity.setPrefWidth(300);
		
		addButton = new Button("Add to Cart");
		addButton.setPrefWidth(300);
		
		updateButton = new Button("Update Cart");
		updateButton.setPrefWidth(300);
		
		removeButton = new Button("Remove from Cart");
		removeButton.setPrefWidth(300);
		
		checkoutButton = new Button("Checkout");
		checkoutButton.setPrefWidth(300);
		
		buyBookScene = new Scene(bp, 1000, 800);
	}
	


	
	
	private void layout() {
		setupMenus();
		setupQuantityVB();
		setupButtonVB();
		setupLeftLowerVB();
		setupRightLowerVB();
		setupLowerHB();
		setupLowerVB();
		setupUpperVB();
		setupVB();
		setupWindow();
		setupBorderPane();
	}

	private void setupMenus() {
		menuBar.getMenus().addAll(userMenu, transactionMenu);
		userMenu.getItems().addAll(logOut);
		transactionMenu.getItems().addAll(buyBook, transactionHistory);
	}

	private void setupQuantityVB() {
		quantityVB.getChildren().addAll(quantityLbl, quantity);
		quantityVB.setMargin(quantityLbl, new Insets(5, 0, 0, 15));
		quantityVB.setMargin(quantity, new Insets(0, 0, 20, 15));
	}

	private void setupButtonVB() {
		buttonVB.getChildren().addAll(addButton, updateButton, removeButton, checkoutButton);
	}

	private void setupLeftLowerVB() {
		leftlowerVB.getChildren().addAll(cartTable);
		leftlowerVB.setPrefWidth(700);
		leftlowerVB.setPrefHeight(250);
		leftlowerVB.setMargin(cartTable, new Insets(0, 0, 0, 15));
	}

	private void setupRightLowerVB() {
		rightlowerVB.getChildren().addAll(quantityVB, buttonVB);
		rightlowerVB.setMargin(buttonVB, new Insets(20, 0, 0, 15));
		rightlowerVB.setPrefWidth(400);
		rightlowerVB.setAlignment(Pos.TOP_RIGHT);
	}

	private void setupLowerHB() {
		lowerHB.getChildren().addAll(leftlowerVB, rightlowerVB);
	}

	private void setupLowerVB() {
		lowerVB.getChildren().addAll(cartTxt, lowerHB);
		lowerVB.setPrefWidth(1000);
		lowerVB.setMargin(cartTxt, new Insets(10, 0, 10, 15));
	}

	private void setupUpperVB() {
		upperVB.getChildren().addAll(bookTxt, bookTable);
		upperVB.setMargin(bookTxt, new Insets(15, 0, 0, 15));
		upperVB.setMargin(bookTable, new Insets(0, 15, 0, 15));
	}

	private void setupVB() {
		vb.getChildren().addAll(upperVB, lowerVB);
	}

	private void setupWindow() {
		window.getContentPane().getChildren().add(vb);
		window.setTitle("Buy Book");
		window.setPrefHeight(700);
	}

	private void setupBorderPane() {
		bp.setTop(menuBar);
		bp.setCenter(window);
	}
	
	
	
	private void setBookTable() {
	    TableColumn<Book, Integer> bookID = createTableColumn("ID", "bookID");
	    TableColumn<Book, String> bookName = createTableColumn("Name", "bookName");
	    TableColumn<Book, String> bookAuthor = createTableColumn("Author", "bookAuthor");
	    TableColumn<Book, String> bookGenre = createTableColumn("Genre", "bookGenre");
	    TableColumn<Book, Integer> bookStock = createTableColumn("Stock", "bookStock");
	    TableColumn<Book, Integer> bookPrice = createTableColumn("Price", "bookPrice");

	    bookTable.getColumns().addAll(bookID, bookName, bookAuthor, bookGenre, bookStock, bookPrice);
	    bookTable.setPrefHeight(365);

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

	private <S, T> TableColumn<S, T> createTableColumn(String columnName, String propertyValue) {
	    TableColumn<S, T> column = new TableColumn<>(columnName);
	    column.setCellValueFactory(new PropertyValueFactory<>(propertyValue));
	    column.setMinWidth(1000 / 6.2);
	    return column;
	}
	
	
	
	private void setCartTable() {
	    configureTableColumns();
	    populateCartTable();
	}

	private void configureTableColumns() {
	    TableColumn<Cart, Integer> cartBookID = new TableColumn<>("Book ID");
	    TableColumn<Cart, String> cartBookName = new TableColumn<>("Name");
	    TableColumn<Cart, String> cartBookAuthor = new TableColumn<>("Author");
	    TableColumn<Cart, Integer> cartBookPrice = new TableColumn<>("Price");
	    TableColumn<Cart, Integer> cartBookQuantity = new TableColumn<>("Qty");

	    configureColumnCellValueFactories(cartBookID, cartBookName, cartBookAuthor, cartBookPrice, cartBookQuantity);
	    configureColumnMinWidths(cartBookID, cartBookName, cartBookAuthor, cartBookPrice, cartBookQuantity);

	    cartTable.getColumns().addAll(cartBookID, cartBookName, cartBookAuthor, cartBookPrice, cartBookQuantity);
	}

	private void configureColumnCellValueFactories(TableColumn<Cart, Integer> cartBookID, TableColumn<Cart, String> cartBookName,
	                                               TableColumn<Cart, String> cartBookAuthor, TableColumn<Cart, Integer> cartBookPrice,
	                                               TableColumn<Cart, Integer> cartBookQuantity) {
	    cartBookID.setCellValueFactory(new PropertyValueFactory<>("cartBookID"));
	    cartBookName.setCellValueFactory(new PropertyValueFactory<>("cartBookName"));
	    cartBookAuthor.setCellValueFactory(new PropertyValueFactory<>("cartBookAuthor"));
	    cartBookPrice.setCellValueFactory(new PropertyValueFactory<>("cartBookPrice"));
	    cartBookQuantity.setCellValueFactory(new PropertyValueFactory<>("cartBookQuantity"));
	}

	private void configureColumnMinWidths(TableColumn<?, ?>... columns) {
	    int numColumns = columns.length;
	    double columnMinWidth = 700.0 / (5.5 * numColumns);

	    for (TableColumn<?, ?> column : columns) {
	        column.setMinWidth(columnMinWidth);
	    }
	}

	private void populateCartTable() {
	    Connect c = new Connect();
	    String query = "SELECT c.BookID, BookName, BookAuthor, BookPrice, CartQty FROM cart c JOIN book b ON c.BookID = b.BookID";
	    ResultSet resultSet = c.executeSelect(query);

	    try {
	        while (resultSet.next()) {
	            int bookID = resultSet.getInt("BookID");
	            String bookName = resultSet.getString("BookName");
	            String bookAuthor = resultSet.getString("BookAuthor");
	            int bookPrice = resultSet.getInt("BookPrice");
	            int cartQuantity = resultSet.getInt("CartQty");

	            Cart cart = new Cart(bookID, bookName, bookAuthor, bookPrice, cartQuantity);
	            cartTable.getItems().add(cart);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	private void eventHandler() {
	    logOut.setOnAction(event -> handleLogOut());
	    buyBook.setOnAction(event -> handleBuyBook());
	    transactionHistory.setOnAction(event -> handleTransactionHistory());
	    addButton.setOnAction(event -> handleAddButton());
	    updateButton.setOnAction(event -> handleUpdateButton());
	    removeButton.setOnAction(event -> handleRemoveButton());
	    checkoutButton.setOnAction(event -> handleCheckoutButton());
	}

	private void handleLogOut() {
	    primaryStage.close();
	    openLoginStage();
	}

	private void handleBuyBook() {
	    primaryStage.close();
	    openBuyBookFormStage();
	}

	private void handleTransactionHistory() {
	    primaryStage.close();
	    openTransactionHistoryStage();
	}


	private void handleAddButton() {
	    int bookID = getSelectedBookID();
	    int bookStock = getSelectedBookStock();
	    int quantityValue = quantity.getValue();

	    if (bookID == 0) {
	        showAlertError("Please select the book first!");
	    } else if (quantityValue > bookStock) {
	        showAlertError("Quantity must not be higher than stock!");
	    } else {
	        Connect c = new Connect();
	        String query = "INSERT INTO cart VALUES(" + Login.lid + "," + bookID + "," + quantityValue + ")";
	        try {
	            c.execute(query);
	        } catch (Exception e) {
	            handleException(e);
	        }
	        refreshCartTable();
	    }
	}

	private void handleUpdateButton() {
	    int cartBookID = getSelectedCartBookID();
	    int quantityValue = quantity.getValue();

	    if (cartBookID == 0) {
	        showAlertError("Please select the book first!");
	    } else {
	        Connect c = new Connect();
	        String stockQuery = "SELECT BookStock FROM book WHERE BookID=" + cartBookID;
	        ResultSet resultSet = c.executeSelect(stockQuery);
	        try {
	            if (resultSet.next()) {
	                int stock = resultSet.getInt("BookStock");
	                if (quantityValue > stock) {
	                    showAlertError("Quantity must not be higher than stock!");
	                } else {
	                    String updateQuery = "UPDATE cart SET CartQty=" + quantityValue +
	                                         " WHERE UserID=" + Login.lid + " AND BookID=" + cartBookID;
	                    c.execute(updateQuery);
	                    clearCartSelection();
	                    refreshCartTable();
	                }
	            }
	        } catch (SQLException e) {
	            handleException(e);
	        }
	    }
	}


	private void handleRemoveButton() {
	    int cartBookID = getSelectedCartBookID();

	    if (cartBookID == 0) {
	        showAlertError("Please select the book first!");
	    } else {
	        Connect c = new Connect();
	        String query = "DELETE FROM cart WHERE BookID=" + cartBookID + " AND UserID=" + Login.lid;
	        try {
	            c.execute(query);
	        } catch (Exception e) {
	            handleException(e);
	        }
	        clearCartSelection();
	        refreshCartTable();
	    }
	}

	private void handleCheckoutButton() {
	    if (cartTable.getItems().isEmpty()) {
	        showAlertError("Cart is empty!");
	    } else {
	        showAlertInformation("Checkout success!");
	        Connect c = new Connect();
	        String insertQuery = "INSERT INTO headertransaction VALUES(null, " + Login.lid + ", \"" + LocalDate.now() + "\")";
	        c.execute(insertQuery);
	        int transactionID = getLatestTransactionID(c);
	        insertDetailTransactions(c, transactionID);
	        deleteCartItems(c);
	        refreshCartTable();
	    }
	}

	private int getSelectedBookID() {
	    Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
	    return (selectedBook != null) ? selectedBook.getBookID() : 0;
	}

	private int getSelectedBookStock() {
	    Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
	    return (selectedBook != null) ? selectedBook.getBookStock() : 0;
	}

	private int getSelectedCartBookID() {
	    Cart selectedCart = cartTable.getSelectionModel().getSelectedItem();
	    return (selectedCart != null) ? selectedCart.getCartBookID() : 0;
	}

	private void clearCartSelection() {
	    cartTable.getSelectionModel().clearSelection();
	}

	private void refreshCartTable() {
	    Connect c = new Connect();
	    cartTable.getItems().clear();
	    String query = "SELECT c.BookID, BookName, BookAuthor, BookPrice, CartQty FROM cart c JOIN book b ON c.BookID = b.BookID";
	    ResultSet resultSet = c.executeSelect(query);
	    try {
	        while (resultSet.next()) {
	            int bookID = resultSet.getInt("BookID");
	            String bookName = resultSet.getString("BookName");
	            String bookAuthor = resultSet.getString("BookAuthor");
	            int bookPrice = resultSet.getInt("BookPrice");
	            int cartQty = resultSet.getInt("CartQty");
	            Cart cartItem = new Cart(bookID, bookName, bookAuthor, bookPrice, cartQty);
	            cartTable.getItems().add(cartItem);
	        }
	    } catch (SQLException e) {
	        handleException(e);
	    }
	}

	private void openLoginStage() {
	    Stage newStage = new Stage();
	    try {
	        new Login().start(newStage);
	    } catch (Exception e) {
	        handleException(e);
	    }
	}

	private void openBuyBookFormStage() {
	    Stage newStage = new Stage();
	    try {
	        new BuyBookForm().start(newStage);
	    } catch (Exception e) {
	        handleException(e);
	    }
	}

	private void openTransactionHistoryStage() {
	    Stage newStage = new Stage();
	    try {
	        new TransactionHistory().start(newStage);
	    } catch (Exception e) {
	        handleException(e);
	    }
	}

	private void handleException(Exception e) {
	    e.printStackTrace();
	}

	private void showAlertError(String message) {
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle("Error");
	    alert.setHeaderText(message);
	    alert.show();
	}

	private void showAlertInformation(String message) {
	    Alert alert = new Alert(AlertType.INFORMATION);
	    alert.setTitle("Notification");
	    alert.setHeaderText(message);
	    alert.show();
	}

	private int getLatestTransactionID(Connect c) {
	    int transactionID = 0;
	    String query = "SELECT * FROM headertransaction";
	    ResultSet resultSet = c.executeSelect(query);
	    try {
	        while (resultSet.next()) {
	            transactionID = resultSet.getInt("TransactionID");
	        }
	    } catch (SQLException e) {
	        handleException(e);
	    }
	    return transactionID;
	}

	private void insertDetailTransactions(Connect c, int transactionID) {
	    for (Cart cartItem : cartTable.getItems()) {
	        String query = "INSERT INTO detailtransaction VALUES(" +
	                       transactionID + "," +
	                       cartItem.getCartBookID() + "," +
	                       cartItem.getCartBookQuantity() + ")";
	        c.execute(query);
	    }
	}

	private void deleteCartItems(Connect c) {
	    String query = "DELETE FROM cart WHERE UserID=" + Login.lid;
	    c.execute(query);
	}


	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		
		initialize();
		layout();
		setBookTable();
		setCartTable();
		eventHandler();
		
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("file:src/assets/book.png"));
		primaryStage.setTitle("Bookstore");
		primaryStage.setScene(buyBookScene);
		primaryStage.show();
	}

}
