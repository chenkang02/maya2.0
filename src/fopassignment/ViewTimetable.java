/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fopassignment;

import static fopassignment.StudentMethod.studentDashboard;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;


/**
 *
 * @author Chen Kang
 */
public class ViewTimetable extends SQLConnector {
    public static void main(String[] args) {
        viewTimetable("u2102821");
    }
    
    public static void viewTimetable(String matricNumber){
        Scanner sc = new Scanner(System.in);
        
        int[][] time = new int[5][13];
        String[][] module = new String[5][13];
        
        System.out.println("My Timetable");
        
        System.out.printf("%-15s%-12s%-12s%-12s%-12s%-12s%-12s%-12s%-12s%-12s%-12s%-12s%-12s%-12s", "","8:00 a.m.", "9:00 a.m.","10:00 a.m.","11:00 a.m.","12:00 p.m.","1:00 p.m.","2:00 p.m.","3:00 p.m.","4:00 p.m.","5:00 p.m.","6:00 p.m.","7:00 p.m.","8:00 p.m.");
        
        try{
            Connection con = getSQLConnection();
            System.out.println("");
            
            PreparedStatement select = con.prepareStatement("SELECT * FROM "+matricNumber+"");
            ResultSet results = select.executeQuery();
            while(results.next()){
                String moduleCode = results.getString("courseCode");
                int dayWeek = 0;
                String day = results.getString("Week");
                String dayLower = day.toLowerCase();
                int time1 = results.getInt("TIME1");
                int time2 = results.getInt("TIME2");
                int time3 = results.getInt("TIME3");
                
                switch(dayLower){
                    case "monday":
                        dayWeek = 0;
                        break;
                    case "tuesday":
                        dayWeek = 1;
                        break;
                    case "wednesday":
                        dayWeek = 2;
                        break;
                    case "thursday":
                        dayWeek = 3;
                        break;
                    case "friday" : 
                        dayWeek = 4;
                        break;
                }
                for(int i = 8; i <= 20; i++){
                    if(time1 == i){
                        time[dayWeek][i - 8] = 1;
                        module[dayWeek][i - 8] = moduleCode;
                    }
                    if(time2 == i){
                        time[dayWeek][i - 8] = 1;
                        module[dayWeek][i - 8] = moduleCode;
                    }
                    if(time3 == i){
                        time[dayWeek][i - 8] = 1;
                        module[dayWeek][i - 8] = moduleCode;
                    }
                }
            }
                String[] dayOfTheWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};;
                for(int i = 0; i < time.length;i++){
                    System.out.printf("%-15s",dayOfTheWeek[i]);
                    for(int j = 0; j < time[0].length; j++){
                        if(time[i][j] == 1){
                            System.out.printf("%-12s", module[i][j]);
                        }
                        else{
                            System.out.printf("%-12s", " ");
                        }
                    }
                    System.out.println("");
                }
                
                int choice = 0;
                do{
                System.out.print("Input -1 to return");
                choice = sc.nextInt();    
                } while(choice != -1);
                studentDashboard(matricNumber);
                sc.close();
            
        }catch(Exception e){
        System.out.println(e);
    }
    }
}
