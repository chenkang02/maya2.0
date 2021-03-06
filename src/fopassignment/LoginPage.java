package fopassignment;

import static fopassignment.StudentMethod.studentDashboard;
import static fopassignment.newUser.createStaff;
import static fopassignment.newUser.createStudent;
import static fopassignment.newUser.existingStaff;
import static fopassignment.newUser.existingUser;
import static fopassignment.newUser.correctPassword;
import static fopassignment.newUser.correctStaffPassword;
import static fopassignment.staffMethods.staffDashboard;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.Scanner;
import java.sql.ResultSet;

public class LoginPage extends SQLConnector{
    public static void main(String[] args) {
        runLoginPage();
    }

    //This is the main login page
    public static void runLoginPage() {
        boolean mainSwitch = true;
        while(mainSwitch){
            Scanner sc = new Scanner(System.in);
            System.out.println("-----------------------------------------------------------------------------------------");
            System.out.println("|                    Welcome to Group G module registration platform                    |");
            System.out.println("|---------------------------------------------------------------------------------------|");
            System.out.println("|NOTE: You can press \"0\" to exit or \"-1\" to return to login page if you see \"*\".        |");
            System.out.println("|1. Login to an existing account.                                                       |");
            System.out.println("|2. Sign up a new account.                                                              |");
            System.out.println("|3. Reset Password.                                                                     |");
            System.out.println("|0. Quit program");
            System.out.println("-----------------------------------------------------------------------------------------");
            System.out.print("*Press any number stated above to continue: ");
            String instructionNum = sc.nextLine();
            while (!isValidInstruction(instructionNum)) {
                System.out.print("*Please enter valid instruction: ");
                instructionNum = sc.nextLine();
            }
            carrySpecificInstruction(instructionNum);
        }
        
    
    }
    
    //This method is used to check whether the instruction entered is valid or not
    public static boolean isValidInstruction(String instructionNum) {
        if (instructionNum.equals("-1")
                || instructionNum.equals("0")
                || instructionNum.equals("1")
                || instructionNum.equals("2")
                || instructionNum.equals("3")) {
            return true;
        }
        return false;
    }
    
    
    //The programme starts branching to respective area according to the instruction num entered 
    public static void carrySpecificInstruction(String instructionNum) {
        
        
        switch (instructionNum) {
            case "-1":
                runLoginPage();
                break;
            case "0":
                System.out.println("Bye, hope to see u again.");
                System.exit(0);
                break;
            case "1":
                enterCredential();
                break;
            case "2":
                createNewAccount();
                break;
            case "3":
                //reset password
                break;
            default:
                runLoginPage();
        }
        
    }
    
    
    
    public static void enterCredential() {
        Scanner sc = new Scanner(System.in);
        String role = getRole();
        if (role.equalsIgnoreCase("staff")) {
            System.out.print("*Please enter your username: ");
            String username = sc.next();
            if (username.equals("-1") || username.equals("0"))
                carrySpecificInstruction(username);
            else if (existingStaff(username)){
                enterPasswordToEnterMainPage(username, role);
            }
            else {
                System.out.println("The username doesn't exists. Please create an account and login again.");
                runLoginPage();
            }
        } else {
            System.out.print("*Please enter your matric number: ");
            String matricNum = sc.next();
            if (matricNum.equals("-1") || matricNum.equals("0")){
                carrySpecificInstruction(matricNum);
            }
            else if (existingUser(matricNum)){
            enterPasswordToEnterMainPage(matricNum, role);
            }
            else {
                System.out.println("The matric number doesn't exists. Please create an account and login again.");
                runLoginPage();
                System.exit(1);
            }
        }
    }

    public static void createNewAccount() {
        String role = getRole();
        if (role.equalsIgnoreCase("staff"))
            createStaff();
        else
            createStudent();
    }


    
    public static String getRole() {
        Scanner sc = new Scanner(System.in);
        String role = "";
        int i = 0;
        while ( i < 5 && !role.equalsIgnoreCase("staff") && !role.equalsIgnoreCase("student")) {
            if (i == 0)
                System.out.print("Please enter your role(staff or student): ");
            else
                System.out.print("Enter staff or student only: ");
            role = sc.nextLine();
            i++;
        }
        if (!role.equalsIgnoreCase("staff") && !role.equalsIgnoreCase("student")) {
            System.out.println("You have tried too many times. Please login again.");
            runLoginPage();
            System.exit(1);
        }
        return role;
    }
    
    //Uses boolean from correctPassword() to login 
    public static void enterPasswordToEnterMainPage(String matricNumber, String role){
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter your password: ");
        String pass = sc.nextLine();
        int chance = 3;
        
        boolean correct = false;
        if(role.equalsIgnoreCase("staff")){
            correct = correctStaffPassword(matricNumber,pass);
        }
        else if(role.equalsIgnoreCase("student")){
            correct = correctPassword(matricNumber,pass);
        }
        while (!correct && chance > 0) {
            
            System.out.println("Password incorrect, you have " + chance + " more chances.");
            System.out.print("Please re-enter your password: ");
            pass = sc.nextLine();
            chance--;
            if(role.equalsIgnoreCase("student")){
            correct = correctPassword(matricNumber, pass);
        }
            else if(role.equalsIgnoreCase("staff")){
            correct = correctStaffPassword(matricNumber, pass);
            }
        }    
        String nameOfStudent = "";
        String nameOfStaff = "";
        if (correct) {
            System.out.println("Succesfully login.");
            
            try{
                Connection con = getSQLConnection();
                PreparedStatement getName = con.prepareStatement("SELECT * FROM userdata WHERE matricNumber = \'"+matricNumber+"\'");
                PreparedStatement getStaff = con.prepareStatement("SELECT * FROM staffData WHERE username = \'"+matricNumber+"\'");
                
                ResultSet name = getName.executeQuery();
                ResultSet staffName = getStaff.executeQuery();
                
                while(name.next()){
                    nameOfStudent = name.getString("name");
                }
                while(staffName.next()){
                    nameOfStaff = staffName.getString("fullName");
                }
                
            }catch(Exception e){
                System.out.println(e);
            }
            
            
            if(role.equalsIgnoreCase("student")){
                System.out.println("Welcome " + nameOfStudent + "!");
                studentDashboard(matricNumber);
            }
            
            else if(role.equalsIgnoreCase("staff")){
                System.out.println("Welcome " + nameOfStaff + "!");
                staffDashboard(matricNumber);
            }
        } else {
            System.out.println("Please reset your password, you have no more chances.");
            resetPassword(matricNumber, role);
        }
        
    }
    
    public static void resetPassword(String matricNumber, String role){
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter your new password: ");
        String password = sc.nextLine();
        System.out.print("Please reenter your new password: ");
        String password2= sc.nextLine();
        String oldPassword = "";
        String newPassword = "";
        if(password.equals(password2)){
            newPassword = password2;
            }
        
        if(role.equalsIgnoreCase("student")){
            try{
                Connection con = getSQLConnection();

                PreparedStatement select = con.prepareStatement("SELECT * FROM userdata WHERE matricNumber = \'"+matricNumber+"\'");
                ResultSet data = select.executeQuery();
                while(data.next()){
                    oldPassword = data.getString("password");
                }
            if(newPassword.equals(oldPassword)){
                System.out.println("Sorry, new password cannot be old password");
                return;
            }
            else{
                    PreparedStatement change = con.prepareStatement("UPDATE userdata SET password = \'"+newPassword+"\' WHERE matricNumber = \'"+matricNumber+"\'");
                    change.executeUpdate();
                    System.out.println("Password changed successfully.");
                }
            }catch(Exception e){
                System.out.println(e);
            }
        }
        else if(role.equalsIgnoreCase("staff")){
            try{
                Connection con = getSQLConnection();
                PreparedStatement select = con.prepareStatement("SELECT * FROM staffData WHERE username = \'"+matricNumber+"\'");
                ResultSet data = select.executeQuery();
                while(data.next()){
                    oldPassword = data.getString("password");
                }
                
                if(newPassword.equals(oldPassword)){
                System.out.println("Sorry, new password cannot be old password");
                return;
                }
                else{
                    PreparedStatement change = con.prepareStatement("UPDATE staffData SET password = \'"+newPassword+"\' WHERE username = \'"+matricNumber+"\'");
                    change.executeUpdate();
                    System.out.println("Password successfully changed.");
                }
          
            }catch(Exception e){
                System.out.println(e);
                
            }
        }
                    
                    
}
}
