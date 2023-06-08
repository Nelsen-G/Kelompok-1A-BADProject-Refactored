package main;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;
import model.Transaction;
import util.Connect;
import model.Cart;

public class TransactionHistory {
	Stage primaryStage;
	Scene transactionHistoryScene;
	
	BorderPane bp;
	GridPane gp;
	
	MenuBar menuBar;
	Menu userMenu, transactionMenu;
	MenuItem logOut, buyBook, transactionHistory;
	
	Window window;
	TableView<Transaction> transactionTable;
	TableView<Cart> detailTransactionTable;
	
	private void initialize() {
		window = new Window();
		bp = new BorderPane();
		gp = new GridPane();
		
		menuBar = new MenuBar();
		
		userMenu = new Menu("User");
		transactionMenu = new Menu("Transaction");
		
		logOut = new MenuItem("Logout");
		buyBook = new MenuItem("Buy Book");
		transactionHistory = new MenuItem("View Transaction History");
		
		transactionTable = new TableView<Transaction>();
		detailTransactionTable = new TableView<Cart>();
		
		transactionHistoryScene = new Scene(bp, 1000, 800);
	}
	
	private void layout() {
	    setupMenuBar();
	    setupGridPane();
	    setupWindow();

	    bp.setTop(menuBar);
	    bp.setCenter(window);
	}

	private void setupMenuBar() {
	    menuBar.getMenus().addAll(userMenu, transactionMenu);
	    userMenu.getItems().addAll(logOut);
	    transactionMenu.getItems().addAll(buyBook, transactionHistory);
	}

	private void setupGridPane() {
	    gp.add(transactionTable, 0, 0);
	    gp.add(detailTransactionTable, 1, 0);
	    gp.setMargin(transactionTable, new Insets(30, 0, 30, 30));
	    gp.setMargin(detailTransactionTable, new Insets(30, 0, 30, 0));
	}

	private void setupWindow() {
	    window.getContentPane().getChildren().add(gp);
	    window.setTitle("Transaction History");
	}

	
	private void setTransactionTable() {
	    TableColumn<Transaction, Integer> transactionID = new TableColumn<>("ID");
	    TableColumn<Transaction, String> transactionDate = new TableColumn<>("Date");

	    setupTableColumns(transactionID, transactionDate);
	    setupTableWidths(transactionID, transactionDate);

	    transactionTable.getColumns().setAll(transactionID, transactionDate);
	    transactionTable.setPrefHeight(700);

	    retrieveTransactionData();
	}

	private void setupTableColumns(TableColumn<Transaction, Integer> transactionID, TableColumn<Transaction, String> transactionDate) {
	    transactionID.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
	    transactionDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
	}

	private void setupTableWidths(TableColumn<Transaction, Integer> transactionID, TableColumn<Transaction, String> transactionDate) {
	    double tableWidth = 300.0;
	    double columnWidth = tableWidth / 2;

	    transactionID.setPrefWidth(columnWidth);
	    transactionDate.setPrefWidth(columnWidth);
	}

	private void retrieveTransactionData() {
	    Connect c = new Connect();
	    String query = "SELECT * from headertransaction";
	    ResultSet resultSet = c.executeSelect(query);

	    try {
	        while (resultSet.next()) {
	            int transactionID = resultSet.getInt("TransactionID");
	            String transactionDate = resultSet.getString("TransactionDate");
	            Transaction transaction = new Transaction(transactionID, transactionDate);
	            transactionTable.getItems().add(transaction);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	private void setDetailTransactionTable() {
	    TableColumn<Cart, Integer> cartBookID = createTableColumn("Book ID", "cartBookID");
	    TableColumn<Cart, String> cartBookName = createTableColumn("Name", "cartBookName");
	    TableColumn<Cart, String> cartBookAuthor = createTableColumn("Author", "cartBookAuthor");
	    TableColumn<Cart, Integer> cartBookPrice = createTableColumn("Price", "cartBookPrice");
	    TableColumn<Cart, Integer> cartBookQuantity = createTableColumn("Qty", "cartBookQuantity");

	    setTableColumnWidths(cartBookID, cartBookName, cartBookAuthor, cartBookPrice, cartBookQuantity);

	    detailTransactionTable.getColumns().addAll(cartBookID, cartBookName, cartBookAuthor, cartBookPrice, cartBookQuantity);
	    detailTransactionTable.setPrefHeight(700);
	}

	private <S, T> TableColumn<S, T> createTableColumn(String columnName, String propertyName) {
	    TableColumn<S, T> column = new TableColumn<>(columnName);
	    column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
	    return column;
	}

	private void setTableColumnWidths(TableColumn<?, ?>... columns) {
	    double tableWidth = 700.0;
	    double columnWidth = tableWidth / 5.55;

	    for (TableColumn<?, ?> column : columns) {
	        column.setMinWidth(columnWidth);
	    }
	}

	
	private void eventHandler() {
	    logOut.setOnAction(event -> openLoginStage());
	    buyBook.setOnAction(event -> openBuyBookFormStage());
	    transactionHistory.setOnAction(event -> openTransactionHistoryStage());
	    transactionTable.setOnMouseClicked(event -> loadDetailTransactionTable());
	}

	private void openLoginStage() {
	    primaryStage.close();
	    Stage newStage = new Stage();
	    try {
	        new Login().start(newStage);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private void openBuyBookFormStage() {
	    primaryStage.close();
	    Stage newStage = new Stage();
	    try {
	        new BuyBookForm().start(newStage);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private void openTransactionHistoryStage() {
	    primaryStage.close();
	    Stage newStage = new Stage();
	    try {
	        new TransactionHistory().start(newStage);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private void loadDetailTransactionTable() {
	    int dt = transactionTable.getSelectionModel().getSelectedItem().getTransactionID();
	    detailTransactionTable.getItems().clear();
	    Connect cn = new Connect();
	    String q = "SELECT * from detailtransaction d join book b on d.BookID=b.BookID where TransactionID=" + dt;
	    ResultSet r = cn.executeSelect(q);
	    try {
	        while (r.next()) {
	            int i = r.getInt("BookID");
	            String nm = r.getString("BookName");
	            String a = r.getString("BookAuthor");
	            int p = r.getInt("BookPrice");
	            int qt = r.getInt("TransactionQty");
	            Cart c = new Cart(i, nm, a, p, qt);
	            detailTransactionTable.getItems().add(c);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	public void start(Stage primaryStage) throws Exception {
	
		this.primaryStage = primaryStage;
		
		initialize();
		layout();
		setTransactionTable();
		setDetailTransactionTable();
		eventHandler();
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("file:src/assets/book.png "));
		primaryStage.setTitle("Bookstore");
		primaryStage.setScene(transactionHistoryScene);
		primaryStage.show();
	}
}