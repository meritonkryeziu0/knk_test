package repository;


import java.sql.ResultSet;

import Database.DBConnection;
import model.Admin;

public class AdminRepository {

  private DBConnection connection;

  public AdminRepository() {
    this.connection = DBConnection.getConnection();
  }

  public Admin findByUsernamePassword(String username, String password) throws Exception {
    String query = "select * from admin where username = '" + username.trim() + "' and password = '"
        + password.trim() + "';";

    ResultSet res = this.connection.executeQuery(query);
    Admin guest;
    if (res.next()) {
      System.out.println("We have results");
      guest = Admin.fromResultSet(res);
      if (guest != null) {
        return guest;
      }

    }
    return null;

  }
}
