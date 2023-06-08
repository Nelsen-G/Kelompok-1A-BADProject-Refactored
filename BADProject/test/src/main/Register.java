package main;

import java.time.LocalDate;
import java.util.Optional;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import util.Connect;

public class Register implements EventHandler<MouseEvent> {
	Stage primaryStage;
	Scene registerScene;
	
	BorderPane bp;
	GridPane gp;
	FlowPane fp;
	HBox hb;
	VBox vb;
	
	Text titleTxt, accountTxt;
	Label nameLbl, emailLbl, passwordLbl, confirmPassLbl, addressLbl, dobLbl, genderLbl;
	TextField nameField, emailField, addressField;
	PasswordField passwordField, confirmPassField;
	DatePicker dobPicker;
	RadioButton maleBtn, femaleBtn;
	ToggleGroup genderGroup;
	CheckBox checkBox;
	static Hyperlink loginLink;
	Button registerBtn;
	
	Font font1, font2;
	Alert alertMessage;
	Optional<ButtonType> result;

	public void initialize() {
		bp = new BorderPane();
		gp = new GridPane();
		fp = new FlowPane();
		
		font1 = Font.font("Verdana", FontWeight.EXTRA_BOLD, 20);
		font2 = Font.font("Verdana", FontWeight.BOLD, 12);
		
		titleTxt = new Text("Register");
		titleTxt.setFont(font1);
		
		nameLbl = new Label("Full Name");
		nameField = new TextField();
		nameField.setPromptText("Full Name");
		nameField.setMaxWidth(300);
		
		emailLbl = new Label("Email");
		emailField = new TextField();
		emailField.setPromptText("Email");
		emailField.setMaxWidth(300);
		
		passwordLbl = new Label("Password");
		passwordField = new PasswordField();
		passwordField.setPromptText("Password");
		passwordField.setMaxWidth(300);
		
		confirmPassLbl = new Label("Confirm Password");
		confirmPassField = new PasswordField();
		confirmPassField.setPromptText("Confirm Password");
		confirmPassField.setMaxWidth(300);
		
		addressLbl = new Label("Address");
		addressField = new TextField();
		addressField.setPromptText("Address");
		addressField.setMaxWidth(300);
		
		dobLbl = new Label("Date of Birth");
		dobPicker = new DatePicker();
		dobPicker.setPromptText("yyyy-mm-dd");
		dobPicker.setMaxWidth(300);
		
		genderLbl = new Label("Gender");
		maleBtn = new RadioButton("Male");
		femaleBtn = new RadioButton("Female");
		
		genderGroup = new ToggleGroup();
		maleBtn.setToggleGroup(genderGroup);
		femaleBtn.setToggleGroup(genderGroup);
		
		checkBox = new CheckBox("Agree to the terms and conditions");
		
		registerBtn = new Button("Register");
		registerBtn.setMaxWidth(300);
		registerBtn.setMinHeight(35);
		registerBtn.setBackground(new Background(new BackgroundFill(Color.SLATEBLUE, null, null)));
		registerBtn.setFont(font2);
		registerBtn.setTextFill(Color.WHITE);
		
		accountTxt = new Text("Already have an account?");
		
		loginLink = new Hyperlink("Login");
		loginLink.setOnMouseClicked(this);	
		
		registerScene = new Scene(bp, 500, 700);
	}
	
	private void layout() {
	    addControls();
	    setLayoutProperties();
	}

	private void addControls() {
	    gp.add(nameLbl, 0, 0);
	    gp.add(nameField, 1, 0);

	    gp.add(emailLbl, 0, 1);
	    gp.add(emailField, 1, 1);

	    gp.add(passwordLbl, 0, 2);
	    gp.add(passwordField, 1, 2);

	    gp.add(confirmPassLbl, 0, 3);
	    gp.add(confirmPassField, 1, 3);

	    gp.add(addressLbl, 0, 4);
	    gp.add(addressField, 1, 4);

	    gp.add(dobLbl, 0, 5);
	    gp.add(dobPicker, 1, 5);

	    gp.add(genderLbl, 0, 6);
	    hb = new HBox(maleBtn, femaleBtn);
	    hb.setSpacing(50);
	    gp.add(hb, 1, 6);

	    gp.add(checkBox, 1, 7);

	    gp.add(registerBtn, 1, 8);
	}

	private void setLayoutProperties() {
	    gp.setHgap(20);
	    gp.setVgap(10);
	    gp.setAlignment(Pos.CENTER);

	    vb = new VBox(titleTxt, gp, accountTxt, loginLink);
	    vb.setMargin(titleTxt, new Insets(35));
	    vb.setMargin(accountTxt, new Insets(35, 0, 15, 0));
	    vb.setAlignment(Pos.CENTER);

	    bp.setCenter(vb);
	}

	
	public boolean checkAlNumPassword(String password) {
	    boolean alphaPassword = containsAlpha(password);
	    boolean numericPassword = containsNumeric(password);

	    return !alphaPassword || !numericPassword;
	}

	private boolean containsAlpha(String password) {
	    for (int i = 0; i < password.length(); i++) {
	        char c = password.charAt(i);
	        if (Character.isLetter(c)) {
	            return true;
	        }
	    }
	    return false;
	}

	private boolean containsNumeric(String password) {
	    for (int i = 0; i < password.length(); i++) {
	        char c = password.charAt(i);
	        if (Character.isDigit(c)) {
	            return true;
	        }
	    }
	    return false;
	}

	private boolean isValidEmail(String email) {
	    if (email.endsWith("@") || email.endsWith(".")) {
	        showAlert("Invalid email format!");
	        return false;
	    }

	    int dotIndex = email.indexOf('.');
	    int atIndex = email.indexOf('@');

	    if (dotIndex == -1 || atIndex == -1 || dotIndex < atIndex) {
	        showAlert("Invalid email format!");
	        return false;
	    }

	    return true;
	}
	
	private void eventHandler() {
	    registerBtn.setOnAction((event) -> {
	        if (validateInput()) {
	            registerUser();
	            closeStage();
	            openLoginStage();
	        }
	    });
	}

	private boolean validateInput() {
	    String name = nameField.getText();
	    String email = emailField.getText();
	    String password = passwordField.getText();
	    String confirmPassword = confirmPassField.getText();
	    String address = addressField.getText();
	    LocalDate date = dobPicker.getValue();

	    if (name.length() < 5 || name.length() > 30) {
	        showAlert("Full name must be between 5 - 30 characters!");
	        return false;
	    }

	    if (!isValidEmail(email)) {
	        showAlert("Invalid email format!");
	        return false;
	    }

	    if (checkAlNumPassword(password)) {
	        showAlert("Invalid password format!");
	        return false;
	    }

	    if (!confirmPassword.equals(password)) {
	        showAlert("Invalid password, it does not match!");
	        return false;
	    }

	    if (address.isEmpty()) {
	        showAlert("Address must be filled!");
	        return false;
	    }

	    if (date == null) {
	        showAlert("Date must be filled!");
	        return false;
	    }

	    if (genderGroup.getSelectedToggle() == null) {
	        showAlert("Please choose a gender!");
	        return false;
	    }

	    if (!checkBox.isSelected()) {
	        showAlert("Terms and conditions must be checked!");
	        return false;
	    }

	    return true;
	}

	private void showAlert(String message) {
	    Alert alertMessage = new Alert(AlertType.ERROR);
	    alertMessage.setTitle("Error");
	    alertMessage.setHeaderText(message);
	    alertMessage.show();
	}
	
	private void registerUser() {
	    String name = nameField.getText();
	    String email = emailField.getText();
	    String password = passwordField.getText();
	    String address = addressField.getText();
	    LocalDate date = dobPicker.getValue();
	    String gender = (maleBtn.isSelected()) ? "Male" : "Female";

	    String query = "INSERT INTO users VALUES(null, \"" + name + "\", \"" + email + "\", \"" + password + "\", \"" + address + "\", \"" + date + "\", \"" + gender + "\", \"user\")";
	    Connect c = new Connect();
	    c.execute(query);
	}

	private void closeStage() {
	    primaryStage.close();
	}

	private void openLoginStage() {
	    Stage newStage = new Stage();
	    try {
	        new Login().start(newStage);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	
	public Scene getRegisterScene() {
		initialize();
		return registerScene;
	}

	public void setRegisterScene(Scene registerScene) {
		this.registerScene = registerScene;
	}
	
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		
		initialize();
		layout();
		eventHandler();
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("file:src/assets/book.png"));
		primaryStage.setTitle("Bookstore");
		primaryStage.setScene(registerScene);
		primaryStage.show();
	}
	
	@Override
	public void handle(MouseEvent event) {
		if(event.getEventType() == event.MOUSE_CLICKED) {
			if(event.getSource() == loginLink) {
				
				closeStage();
				openLoginStage();
			}
		}
	}
	
}