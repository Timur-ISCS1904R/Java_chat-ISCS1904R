package client;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    private Connection connection;

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/chatnames?useUnicode=true&serverTimezone=UTC", "root", ""
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addName(int pn, String s) {
        try {

            PreparedStatement statement = connection.prepareStatement("" +
                    "INSERT into names (id, name,pn) " +
                    "VALUES (null, ?,?)"
            );
            statement.setString(1, s);
            statement.setInt(2, pn);
            int rows = statement.executeUpdate();

            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeName(int pn, String s) {
        try {

            PreparedStatement statement = connection.prepareStatement("" +
                    "UPDATE names SET Name=? WHERE pn=" + pn

            );
            statement.setString(1, s);
            statement.executeUpdate();

            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void Delete(int pn){

        try{
            PreparedStatement statement = connection.prepareStatement("" +
                    "DELETE FROM names WHERE pn = ?"
            );
            statement.setInt(1, pn);
            int rows = statement.executeUpdate();
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public ArrayList AllConnections(){
        ArrayList nicks=new ArrayList();
        try{
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM names");
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                String name = resultSet.getString("name");
                nicks.add(name);
            }
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return nicks;
    }

}


