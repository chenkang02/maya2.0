/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fopassignment;

import static fopassignment.newUser.existingUser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.util.Scanner;

/**
 *
 * @author Chen Kang
 */
public class getConnection {
    
    public static void main(String[] args) {

    }

    public static Connection getConnection() throws Exception{
        try{
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/maya2.0";
            String username = "root";
            String password = "CKlim@98305751";
            Class.forName(driver);
            
            Connection con = DriverManager.getConnection(url, username, password);
            return con;
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
        
    }
    
    
   
}
