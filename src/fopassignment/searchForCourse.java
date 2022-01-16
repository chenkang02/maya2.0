/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fopassignment;

import static fopassignment.getConnection.getConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Chen Kang
 */
public class searchForCourse {
    public static void main(String[] args) {
       
        
    }
    
    
//establish connection to sql local database server     
public static Connection getConnection() throws Exception{
        try{
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/maya2.0";
            String username = "root";
            String password = "CKlim@98305751";
            Class.forName(driver);
            
            Connection conn = DriverManager.getConnection(url, username, password);
            
            
            return conn;
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

//extract data from the database where the instance in the column : ModuleCode matches the user input
public static void searchCourse(String userInput){
    String input = userInput.toUpperCase();
    boolean found = courseExist(input);

    
    try{
        String dayOfTheWeek = "";
        int startingTime = 0;
        int endTime = 0;
        if(found){
            System.out.println("Search result:");
            for(int i = 0; i < 160 ; i++){
            System.out.printf("%c", '-');
        }
            System.out.println("");
            Connection con = getConnection();
            PreparedStatement extract = con.prepareStatement("SELECT * FROM raw WHERE ModuleCode = \'"+input+"\'");
            ResultSet module = extract.executeQuery();
            System.out.printf("%-40s%-15s%-15s%-30s%-55s%n","Module", "Occurrence","Activity", "Day", "Module Tutor");
            
            while(module.next()){
                //module.getString("moduleNAme"); gets data from the resultset object with the column name moduleName
                String moduleName = module.getString("ModuleName");
                int occurrence = module.getInt("occurrence");
                String activity = module.getString("Activity");
                String tutor = module.getString("Tutor");
                int credithour = module.getInt("credithour");
                dayOfTheWeek = module.getString("Week");
                startingTime = module.getInt("TIME1");
                endTime = module.getInt("TIME2");
                int time3 = module.getInt("TIME3");
                boolean timeInvalid = false;
                
                if(time3 != 0){
                    endTime = time3;
                }
                if(startingTime == 0 || endTime == 0){
                    timeInvalid = true;
                }
                String time = "";
                if(timeInvalid){
                    time = "-------";
                }
                else{
                    time = startingTime + ":00 - " + endTime + ":00";
                }
                String formattedModule = userInput + " - " + moduleName;
                String formattedActivity = "";
                if(activity.equalsIgnoreCase("lec")){
                    formattedActivity = "Lecture";;
                }
                else if(activity.equalsIgnoreCase("onl")){
                    formattedActivity = "Tutorial";
                }
                String formattedTime = dayOfTheWeek + " " + time;
                
                int creditHour = module.getInt("credithour");
                System.out.printf("%-40s%-15s%-15s%-30s%-55s%n", formattedModule, occurrence, formattedActivity, formattedTime, tutor);
            }
            
        }
            
            }catch(Exception e){
                System.out.println(e);
            }
        
        
}

//return boolean value true when the parameters of the method matches the instance of the moduleCode column
public static boolean courseExist(String userInput){
    boolean found = false;
    ArrayList<String> array = new ArrayList<String>();
    ArrayList<String> moduleCode = new ArrayList<>();
    String input = userInput.toUpperCase();
    
    try{
        Connection con = getConnection();
        
        PreparedStatement search = con.prepareStatement("SELECT ModuleName, ModuleCode FROM raw WHERE ModuleCode = \'"+input+"\'");
        
        ResultSet results = search.executeQuery();
        
        
        while(results.next()){
            array.add(results.getString("ModuleName"));
            moduleCode.add(results.getString("ModuleCode"));
        }
        
        if (moduleCode.contains(userInput)){
            found = true;
        }
        
        return found;       
    }catch (Exception e){
        System.out.println(e);
    }
        return found;
}

//extract all data related to the modules from the database and display them in a table
public static void viewAllModules(){
    Scanner sc = new Scanner(System.in);
    try{
        Connection con = getConnection();
        
        PreparedStatement view = con.prepareStatement("SELECT * FROM raw");
        ResultSet set = view.executeQuery();
        int endTime = 0;
        int startingTime = 0;
        String dayOfTheWeek = "";
        while(set.next()){
            String moduleCode = set.getString("ModuleCode");
            String moduleName = set.getString("ModuleName");
                int occurrence = set.getInt("occurrence");
                String activity = set.getString("Activity");
                String tutor = set.getString("Tutor");
                int credithour = set.getInt("credithour");
                dayOfTheWeek = set.getString("Week");
                startingTime = set.getInt("TIME1");
                endTime = set.getInt("TIME2");
                int time3 = set.getInt("TIME3");
                boolean timeInvalid = false;
                
                if(time3 != 0){
                    endTime = time3;
                }
                if(startingTime == 0 || endTime == 0){
                    timeInvalid = true;
                }
                String time = "";
                if(timeInvalid){
                    time = "-------";
                }
                else{
                    time = startingTime + ":00 - " + endTime + ":00";
                }
                String formattedModule = moduleCode + " - " + moduleName;
                String formattedActivity = "";
                if(activity.equalsIgnoreCase("lec")){
                    formattedActivity = "Lecture";;
                }
                else if(activity.equalsIgnoreCase("onl")){
                    formattedActivity = "Tutorial";
                }
                String formattedTime = dayOfTheWeek + " " + time;
                
                System.out.printf("%-40s%-15s%-15s%-30s%-55s%n", formattedModule, occurrence, formattedActivity, formattedTime, tutor);
        }
        System.out.print("Enter any key to continue: ");
        sc.nextLine();
    }catch(Exception e){
        System.out.println(e);
    }
}
}





  



