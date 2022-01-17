/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fopassignment;

import static fopassignment.CourseSearchingController.searchCourse;
import static fopassignment.LoginPage.runLoginPage;
import static fopassignment.CourseSearchingController.viewAllModules;
import static fopassignment.ViewTimetable.viewTimetable;
import static fopassignment.registerCourse.viewRegisteredModule;
import java.util.Scanner;

/**
 *
 * @author Chen Kang
 */
public class StudentMethod {
    
    /*main method that is called when the student successfully login, student will 
    be prompted to input numbers to determine what they want to do.*/
    public static void studentDashboard(String matricNumber){
        Scanner sc = new Scanner(System.in);
        boolean mainSwitch = true;
        while(mainSwitch){
        System.out.println("1. View my timetable\n2. Alter my timetable\n3. Search for specified modules\n4. View all modules\n5. Logout\n-1. Quit");
        System.out.print("What do you want to do today?");
        int choice = sc.nextInt();
        sc.nextLine();
        switch(choice){
            case 1:
                viewTimetable(matricNumber);
                break;
            case 2:
                viewRegisteredModule(matricNumber);
                break;
            case 3:
                System.out.print("Input search: ");
                String userInput = sc.nextLine();
                searchCourse(userInput);
                System.out.print("Enter any key to continue: ");
                sc.nextLine();
                break;
            case 4:
                viewAllModules();
                break;
            case 5:
                logout(matricNumber);
                break;
            case -1:
                System.exit(0);
                break;
            default:
                studentDashboard(matricNumber);
        }
        }
    }
    
    /*prompt the user to enter whether they want to log out of the system or not. If the students chose to log out, the login page will be called
    else, the programme will return to the student dashboard*/
    public static void logout(String matricNumber){
        Scanner sc = new Scanner(System.in);
        System.out.print("Are you sure you want to logout? (Y/N)");
        String input = sc.nextLine();
        
        if(input.equalsIgnoreCase("y")){
            System.out.println("Logout succesfull.");
            runLoginPage();
        }
        else if (input.equalsIgnoreCase("n")){
            studentDashboard(matricNumber);
        }
    }
    
    
}
