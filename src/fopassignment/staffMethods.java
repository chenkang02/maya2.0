/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fopassignment;

import static com.company.LoginPage.runLoginPage;
import static fopassignment.searchForCourse.courseExist;
import static fopassignment.searchForCourse.getConnection;
import static fopassignment.searchForCourse.searchCourse;
import static fopassignment.searchForCourse.viewAllModules;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLSyntaxErrorException;

/**
 *
 * @author Chen Kang
 */
public class staffMethods{
    public static void main(String[] args) {
        staffDashboard("username");
        
        
    }
    
    
    public static void staffDashboard(String username){
        boolean mainSwitch = true;
        Scanner sc = new Scanner(System.in);
        while(mainSwitch){
        System.out.println("1.Modify module\n2.View all modules\n3.View registered student\n4.Log out\n-1. Quit");
        int input = sc.nextInt();
        switch(input){
            case 1:
                modifyModule(username);
                break;
            case 2:
                viewAllModules();
                break;
            case 3:
                viewAllRegisteredStudent();
                break;
            case 4:
                runLoginPage();
                mainSwitch = false;
                break;
            case -1:
                System.exit(0);
            default:
                System.out.println("PLease enter a valid instruction: ");
                staffDashboard(username);
                break;

        }
        }
        
    }
    
    public static void modifyModule(String username){
        Scanner sc = new Scanner(System.in);
        System.out.print("What do you wish to do?");
        System.out.println("\n1.Create new module\n2.Delete existing module.\n3. Edit module");
        int input = sc.nextInt();
        
        switch(input){
            case 1:
                createModule(username);
                break;
            case 2:
                deleteModule(username);
                break;
            case 3:
                editModule(username);
                break;
        }
        
        
    }
    
    public static void createModule(String username){
        Scanner sc = new Scanner(System.in);
        try{
            Connection con = getConnection();
            System.out.print("Enter the module code of the course you wish to add:");
            String moduleCode = sc.nextLine();
            System.out.print("Enter the module name of the course you wish to add:");
            String moduleName = sc.nextLine();
            System.out.print("Enter the number of occurrence you wish to add: ");
            int occurrence = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter the number of students for a class: ");
            int target = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter the type of activity for the class (ONL/LEC): ");
            String activity = sc.nextLine();
            System.out.print("Enter the name of the lecturer: ");
            String tutor = sc.nextLine();
            System.out.print("Enter the credit hour for the class: ");
            int creditHour = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter the day of the week for the class: ");
            String week = sc.nextLine();
            System.out.print("Enter the starting time of the class in 24 hours format:");
            int startingTime = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter the end time of the class:");
            int endTime = sc.nextInt();
            sc.nextLine();
            
            int duration = endTime - startingTime;
            int time1 = 0;
            int time2 = 0;
            int time3 = 0;
            
            if(duration == 2){
                time1 = startingTime;
                time2 = startingTime + 1;
                time3 = endTime;
            }
            else if(duration == 1){
                time1 = startingTime; 
                time2 = endTime;
            }
            
            PreparedStatement insert = con.prepareStatement("INSERT INTO raw (ModuleCode, ModuleName, Occurrence, Target, Activity, Tutor, credithour, Week, TIME1, TIME2, TIME3) VALUES"
                    + "(\'"+moduleCode+"\',\'"+moduleName+"\',"+occurrence+","+target+",\'"+activity+"\',\'"+tutor+"\',"+creditHour+",'MONDAY',"+time1+","+time2+","+time3+")");
            insert.executeUpdate();
        }catch(Exception e){
            System.out.println(e);
        }finally{
            System.out.println("Module created successfully.");
            System.out.print("\nInput any character to return:");
            sc.nextLine();
            staffDashboard(username);
        }
    }
    public static void deleteModule(String username){
        Scanner sc = new Scanner(System.in);
        System.out.print("Input the course code of the course you wish to delete: ");
        String moduleCode = sc.nextLine();
        searchCourse(moduleCode);
        System.out.print("Insert the occurrence of the module you wish to delete: ");
        boolean exist = courseExist(moduleCode);
        if(exist){
        String occurrence = sc.nextLine();
        System.out.print("Are you sure you want to delete this module? Any changes cannot be undone. (Y/N)");
        String answer = sc.nextLine();
        
        try{
            Connection con = getConnection();
            if(answer.equalsIgnoreCase("y")){
                PreparedStatement delete = con.prepareStatement("DELETE FROM raw WHERE ModuleCode = \'"+moduleCode+"\' And occurrence = "+occurrence+"");
                
                delete.executeUpdate();
                
            }    
        }catch(Exception e){
            System.out.println(e);
        }finally{
            System.out.println("Module is deleted successfully. Input any character to return");
            String input = sc.nextLine();
            staffDashboard(username);
        }
        }
        else{
            System.out.println("Course does not exist.");
        }
        
    }
    public static void viewAllRegisteredStudent(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the module code of the course you wish to inspect:");
        String courseCode = sc.nextLine();
        System.out.print("Please enter the occurrence of the course: ");
        int occurrence = sc.nextInt();
        try{
            Connection con = getConnection();
            PreparedStatement statement = con.prepareStatement("SELECT * FROM "+courseCode+" WHERE occurrence = "+occurrence+"");
            ResultSet results = statement.executeQuery();
            
            int counter = 1;
            System.out.println("Registered student:");    
            for(int i = 0; i < 70; i++){
                System.out.print("-");
             }
            System.out.println("");
            System.out.printf("%-5s%-17s%-17s%-25s", "ID", "Matric Number", "Name", "Email");
            System.out.println("");
           
            while(results.next()){
                String matricNumber = results.getString("matricNumber");
                String name = results.getString("name");
                String email = results.getString("email");
                
                
                
                System.out.printf("%-5d%-17s%-17s%-25s",counter, matricNumber, name, email);
                System.out.println("");
                
            }
        }catch(SQLSyntaxErrorException e){
            System.out.println("Selected course do not have registered students yet. ");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    public static void editModule(String username){
        Scanner sc = new Scanner(System.in);
        System.out.print("Input the module code of the module you wishes to edit:");
        String moduleCode = sc.nextLine();
        System.out.println("Please select the properties you wish to edit:");
        System.out.println("\n1.Module Name\n2.Properties of module");
        int option = sc.nextInt();
        
        switch(option){
            case 1: 
                changeNameOfCourse(moduleCode, username);
                break;
            case 2:
                editProperties(moduleCode);
                break;
        }       
        
    }
    
    public static void changeNameOfCourse(String moduleCode, String username){
        Scanner sc = new Scanner(System.in);
        System.out.print("Input the new name of the course:");
        String newName = sc.nextLine();
        try{
            Connection con = getConnection();
            
            boolean exist = courseExist(moduleCode);
            
            if(exist){
            
            PreparedStatement change = con.prepareStatement("UPDATE raw "
                    + " SET ModuleName = \'"+newName+"\' WHERE ModuleCode = \'"+moduleCode+"\'");
            change.executeUpdate();
            }
            else{
                System.out.println("Module does not exist");
                staffDashboard(username);
            }
            
        }catch(Exception e){
            System.out.println(e);
        }
        
    
    }
    
    public static void editProperties(String moduleCode){
        Scanner sc = new Scanner(System.in);
        System.out.println("Input the occurrence of the module you wishes to edit:");
        int occurrence = sc.nextInt();
        sc.nextLine();
        System.out.println("Input the properties you wishes to edit:"
                + "\n(MAVName/Target/Activity/Tutor/credithour/Week/TIME1/TIME2/TIME3)");
        String properties = sc.nextLine();
        System.out.print("Input the new "+properties+":");
        String newProperties = sc.nextLine();
        int number = 0;
        boolean isInteger = true;
        /*try{
           
            number = Integer.parseInt(newProperties);
            
        }catch(NumberFormatException e){
            
            isInteger = false;
        }
        
        if(isInteger == false){*/
        try{
            Connection con = getConnection();
            
            String statement = "UPDATE RAW SET "+properties+"= \'"+newProperties+"\' WHERE ModuleCode = \'"+moduleCode+"\' and occurrence = "+occurrence+"";
            
            PreparedStatement edit = con.prepareStatement(statement);
            edit.executeUpdate();
        }catch(Exception e){
            System.out.println(e);
        }
        }
        /*else{
            try{
                Connection con = getConnection();
                
                PreparedStatement edit = con.prepareStatement("UPDATE raw"
                    + "SET "+properties+" = "+number+ "WHERE ModuleCode = \'"+moduleCode+"\' And Occurrence = \'"+occurrence+"\'");
                edit.executeUpdate();
            }catch(Exception e){
                System.out.println(e);
            }*/
        }
    

