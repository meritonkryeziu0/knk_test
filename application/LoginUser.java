package application;

public class LoginUser {

  private String username; // OR EMAIL
  private String password;

  public LoginUser(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public boolean validate() {

    if (username.equals("") || username.equals(" ") || password.equals("") || password.equals(" ")) {
      return false;
    }
    if (username == null || password == null) {
      return false;
    }

    return true;
  }

}
