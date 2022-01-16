/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fopassignment;

import static com.company.LoginPage.runLoginPage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Chen Kang
 */
public class newUser extends SQLConnector {
    
    public static void main(String[] args) {
        System.out.println(correctPassword("u2102821", "Xuanfen0725"));
   }
    
    
    //Check whether the user exists in the database.
    public static boolean existingUser(String username){
        boolean contained = false;
        try{
            Connection con = getSQLConnection();
            
            PreparedStatement search = con.prepareStatement("SELECT * FROM userdata WHERE matricNumber = \'"+username+"\'");
            
            ResultSet result = search.executeQuery();
            
            if(result.next()){
                contained = true;
                return contained;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return contained;
    }
    
    //check whether the staff has already registered an account, return true if the staff has a registerd account. 
    public static boolean existingStaff(String username){
        boolean contained = false;
        try{
            Connection con = getSQLConnection();
            PreparedStatement search = con.prepareStatement("SELECT * FROM staffData WHERE username = \'"+username+"\' ");
            
            ResultSet result = search.executeQuery();
            
            if(result.next()){
                contained = true;
                return contained;
            }
            
        }catch(Exception e){
            System.out.println(e);
        }
        return contained;
    }
    
    //Create a new student instance in database
    public static void createStudent(){
        
        Scanner sc = new Scanner(System.in);
        System.out.print("Please input your matric number");;
        String matricNumber = sc.nextLine();
        
        boolean exist = existingUser(matricNumber);
        
        if(exist){
            System.out.println("User already existed, proceed to login page.");
            runLoginPage();
        }
        else{
             try{
                Connection con = getSQLConnection();
                 
                 System.out.print("Please input your siswamail: ");
                 String siswaMail = sc.nextLine();
                 System.out.print("Please input your full name:");
                 String name = sc.nextLine();
                 String nameUpper = name.toUpperCase();
                 System.out.print("Please enter your muet band.");
                 int muetBand = sc.nextInt();
                 sc.nextLine();
                 System.out.print("Please enter your password.");
                 String password = sc.nextLine();
                 System.out.print("Please reenter your password for confirmation.");
                 String password2 = sc.nextLine();
                 
                 if(!password2.equals(password)){
                     System.out.println("Password not matched. Please register again: ");
                     runLoginPage();
                 }
                
                 //Trying to insert user input into table "userdata" inside the database 
                PreparedStatement insert = con.prepareStatement("INSERT INTO userdata (matricNumber, siswaMail, password, muetBand, name) VALUES ('"+matricNumber+"', '"+siswaMail+"', '"+password2+"', '"+muetBand+"', \'"+nameUpper+"\')");
                insert.executeUpdate();  
                createTableForUser(matricNumber);
                sc.close();
            }catch(Exception e){
                System.out.println(e);
            }finally{
                 System.out.println("User created successfully.");
                 runLoginPage();
             }
    }
            
        }
    
    //Create a new staff instance in database
    public static void createStaff(){
        Scanner sc = new Scanner(System.in);
        try{
        Connection con = getSQLConnection();
        System.out.println("Please enter your UMmail: ");
        String UMmail = sc.nextLine();
        boolean existed = existingStaff(UMmail);
        
        if(existed){
            System.out.println("User already existed. Please proceed to login.");
            runLoginPage();
        }
        else{
        System.out.println("Please enter your username: ");
        String username = sc.nextLine();
       
        System.out.println("Please enter your full name: ");
        String name = sc.nextLine();
        String password = enterAndReturnPassword();  
        
        PreparedStatement insert = con.prepareStatement("INSERT INTO staffData (UMmail, username, password, fullName) VALUES ('"+UMmail+"', '"+username+"', '"+password+"', '"+name+"' )");
        
        insert.executeUpdate();
        sc.close();
        }
        }catch(Exception e){
            System.out.println(e);
        }finally{
            System.out.println("Account created successfully.");
            runLoginPage();
        }
        
    }
    
    //Check whether the password input matches the password exists in the database 
    public static boolean correctPassword(String matricNumber, String password2){
        
        Scanner sc = new Scanner(System.in);
        String password = "";
        boolean correct = false;
        try{
            Connection con = getSQLConnection();
            
                PreparedStatement check = con.prepareStatement("SELECT password FROM userdata WHERE matricNumber = \'"+matricNumber+"\'");
            
            
            ResultSet results = check.executeQuery();
            
            while(results.next()){
            password = results.getString(1);
            }

            
            if(password2.equals(password)){
                correct = true;
                return correct;
            }
            sc.close();

        }catch(Exception e){
            System.out.println(e);
        }
        return correct;

    }
    
    //check whether the password input by staff matches the password stored inside the table staffdata
    public static boolean correctStaffPassword(String username, String password2){
        
        Scanner sc = new Scanner(System.in);
        String password = "";
        boolean correct = false;
        try{
            Connection con = getSQLConnection();
            
            PreparedStatement check = con.prepareStatement("SELECT * FROM staffdata WHERE username = \'"+username+"\'");
            
            ResultSet results = check.executeQuery();
            
                while(results.next()){
                    password = results.getString("password");
            }
                if(password2.equals(password)){
                    correct = true;
                    return correct;
            }
                sc.close();
            
        }catch(Exception e){
            System.out.println(e);
        }
        return correct;

    }
    
    
    
    //enter and return password, check whether the password is correct
    public static String enterAndReturnPassword(){
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Please enter your password: ");
        String password = sc.nextLine();
        System.out.println("Please re-enter your password.");
        String password1 = sc.nextLine();
        
        if(password.equals(password1)){
            return password;
        }
        else{
            System.out.println("Password not matched.");
            System.exit(1);
        }
        return password;
    }
    
      public static void createTableForUser(String matricNumber){
        try{
            Connection con = getSQLConnection();
            
            PreparedStatement create = con.prepareStatement("CREATE TABLE IF NOT EXISTS "+matricNumber+" (enrollmentID int NOT NULL AUTO_INCREMENT PRIMARY KEY, courseCode varchar(255), ModuleName varchar(255), Lecturer varchar(255), Occurence int, creditHour int, Week varchar(255), TIME1 int, TIME2 int, TIME3 int)");
            
            create.executeUpdate();

        }catch(Exception e){
            System.out.println(e);
        }finally{
            System.out.println("Table created successfully.");
        }
            
    }
    
    
}
        
        


    

