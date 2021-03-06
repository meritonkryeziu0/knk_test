package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import application.LoginUser;
import application.RegisterUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.collections.*;
import javafx.event.ActionEvent;
import model.Admin;
import model.RegisterGuests;
import repository.AdminRepository;
import repository.RegisterGuestsRepository;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.time.LocalDate;

public class LoginRegisterController implements Initializable {

  @FXML
  private TextField usernameLogin;
  @FXML
  private TextField passwordLogin;
  @FXML
  private TextField nameRegister;
  @FXML
  private TextField usernameRegister;
  @FXML
  private TextField emailRegister;
  @FXML
  private PasswordField passwordRegister;
  @FXML
  private PasswordField confirmPasswordRegister;
  @FXML
  private DatePicker birthdayPicker;

  private ToggleGroup genderGroup;
  @FXML
  private RadioButton female;
  @FXML
  private RadioButton male;
  @FXML
  private ComboBox<String> locationCombo;
  @FXML
  private Button registerButton;
  @FXML
  private Button clearButton;

  


  @Override
  public void initialize(URL locaiton, ResourceBundle resources) {
    genderGroup = new ToggleGroup();
    female.setToggleGroup(genderGroup);
    male.setToggleGroup(genderGroup);
    ObservableList<String> locationList = FXCollections.observableArrayList("Albania", "Kosovo", "Germany", "France",
        "Hungary");
    locationCombo.getItems().addAll(locationList);
    locationCombo.setPromptText("Select your Country...");
    birthdayPicker.setValue(LocalDate.now());





  }

  // -------------------------- REGISTER FORM ACTIONS
  // -------------------------------------\\
  @FXML
  public void onRegisterAction(ActionEvent e) throws Exception {

    String name = nameRegister.getText();
    String username = usernameRegister.getText();
    String password = passwordRegister.getText();
    String confirmPassword = confirmPasswordRegister.getText();
    String email = emailRegister.getText();
    String date_string = "";
    try {
      date_string = birthdayPicker.getValue().toString();
    } catch (Exception except) {

      Alert dateAlert = new Alert(AlertType.ERROR);
      dateAlert.setHeaderText("Error");
      dateAlert.setContentText("Invalid data");
      dateAlert.showAndWait();
      return;
    }

    String gender = null;
    if (female.isSelected()) {
      gender = "Female";
    }
    if (male.isSelected()) {
      gender = "Male";
    }

    String location = locationCombo.getSelectionModel().getSelectedItem();
    RegisterUser registerUser = RegisterUser.fromValuesRegister(name, username, password, confirmPassword, email,
        date_string, gender, location);

    Boolean result = registerUser.validate();

    if (result) {

      RegisterGuestsRepository repository = new RegisterGuestsRepository();
      System.out.println("Name: " + name);
      System.out.println("Username: " + username);
      System.out.println("Password: " + password);
      System.out.println("Birthdate: " + date_string);
      System.out.println("Gender: " + gender);
      System.out.println("Location : " + location);

      RegisterGuests guest = RegisterGuests.createGuest(0, name, username, email, password, date_string,
          "", gender,
          location);

      repository.create(guest);
      System.out.println("Successfully registerd");
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setContentText("Successfully registered ");
      alert.setHeaderText("Great work");
      alert.showAndWait();

    } else {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setContentText("Invalid data");
      alert.setHeaderText("Error");
      alert.showAndWait();
    }

  }

  @FXML
  public void onRegisterClearAction(ActionEvent e) {

    // this try and catch because birthday has no value and you try to clear it , it
    // throws Exception
    try {
      nameRegister.clear();
      usernameRegister.clear();
      emailRegister.clear();
      passwordRegister.clear();
      confirmPasswordRegister.clear();
      birthdayPicker.setValue(null);
      genderGroup.selectToggle(null);
      locationCombo.setValue(null);
    } catch (Exception except) {
      return;
    }

  }

  // -------------------------- LOGIN FORM ACTIONS -------------------------- \\
  @FXML
  public void onLoginAction(ActionEvent e) throws Exception {

    System.out.println("Aridon Kransiqi");
    String username = usernameLogin.getText();
    String password = passwordLogin.getText();

    // Validation first
    LoginUser loginUser = new LoginUser(username, password);
    Boolean result = loginUser.validate();

    if (result) {

      // check first for admin
      AdminRepository adminRepository = new AdminRepository();
      Admin admin = adminRepository.findByUsernamePassword(username, password);
      // then check if user is guest
      RegisterGuestsRepository repository = new RegisterGuestsRepository();
      RegisterGuests guest = repository.findByUsernamePassword(username, password);


      if(admin != null) {
          Stage primaryStage = (Stage) (((Node) e.getSource()).getScene().getWindow());
          Parent adminPage = FXMLLoader.load(getClass().getResource("../views/admin.fxml"));
          Scene scene = new Scene(adminPage);
          primaryStage.setScene(scene);
          primaryStage.show();
      }
      else
      {
        if (guest != null) {
          Alert alert = new Alert(AlertType.INFORMATION);
          alert.setHeaderText("Information");
          alert.setContentText("Successfully logged in ");
          alert.showAndWait();
          System.out.println("Username: " + guest.getUsername());
          System.out.println("Password: " + guest.getPassword());

          // TODO:
          // FIXME:
          // ----------------------- LOAD MAIN PAGE -----------------------
          // Stage stage = (Stage) ((Node)e.getSource()) .getScene().getWindow();

        } else {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setHeaderText("Error");
          alert.setContentText("Wrong username or password");
          alert.showAndWait();

        }
      }


    } else {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setContentText("Invalid data");
      alert.showAndWait();
    }

  }

  @FXML
  public void onLoginClearAction(ActionEvent e) {
    usernameLogin.clear();
    passwordLogin.clear();
  }




}
