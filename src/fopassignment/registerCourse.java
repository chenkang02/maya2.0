/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fopassignment;

import static fopassignment.CourseSearchingController.isCourseExist;
import static fopassignment.StudentMethod.studentDashboard;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 *
 * @author Chen Kang
 */
public class registerCourse extends SQLConnector{
    public static void main(String[] args) {
        
    }
    
    
    /*View the registered course of the student, implementation : select all from 
    the table named after the matric number of the student, assign the results to 
    respective variables and output the variables into the terminal window. 
    */
    public static void viewRegisteredModule(String matricNumber) {
    
        try{
            Connection con = getSQLConnection();
            
            
            boolean mainSwitch = true;
            while(mainSwitch){
                ArrayList<String> moduleName = new ArrayList<>();
                ArrayList<String> courseCode = new ArrayList<>();
                ArrayList<Integer> enrollmentID = new ArrayList<>();
                ArrayList<Integer> occurrence = new ArrayList<>();
                int creditHours = 0;
                PreparedStatement check = con.prepareStatement("Select creditHour FROM "+matricNumber+"");
                
                ResultSet sets = check.executeQuery();
                
                while(sets.next()){
                    int hour = sets.getInt("creditHour");
                    creditHours += hour;
                }
                
                System.out.print("---------------\nModule Registeration\nCurrent Credit Hour: "+creditHours+"\nMaximum credit hours : 22\nCurrent registered modules\n");
                
                PreparedStatement registeredModule = con.prepareStatement("SELECT * FROM "+matricNumber+" ");
                ResultSet module = registeredModule.executeQuery();
                
                while(module.next()){
                    int startTime = module.getInt("TIME1");
                    int TIME2 = module.getInt("TIME2");
                    
                    enrollmentID.add(module.getInt("enrollmentID"));
                    courseCode.add(module.getString("courseCode"));
                    moduleName.add(module.getString("ModuleName"));
                    occurrence.add(module.getInt("Occurence"));
                    
                }
                
                for(int i = 0; i < enrollmentID.size(); i++){
                    System.out.print(enrollmentID.get(i)+". "+courseCode.get(i)+" "+moduleName.get(i)+" occurrence "+occurrence.get(i));
                    System.out.println("");
                }
                
                System.out.println("");
                
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Add module\n2. Drop module\n3. Return\n\n");
                System.out.print("Please enter your choice:");
                int choice = sc.nextInt();
                sc.nextLine();
                
                boolean switch2 = true;
                switch(choice){
                    case 1:
                        System.out.print("Please input the moduleCode you wish to register.");
                        String code = sc.nextLine();
                        add(con, code, matricNumber);
                        break;
                    case 2:
                        drop(matricNumber);
                        break;
                    case 3:
                        studentDashboard(matricNumber);
                        break;
                    case -1:
                        mainSwitch = false;
                        break;
                
                }
                
            }
        }catch(Exception e){
            System.out.println(e);
        }
        
        
}
    //add the matric number of the student into the table named after the coursecode to keep a record on how many students are registered under a specific course and occurrence.
    public static void addToTable(String matricNumber, String courseCode, int occurrence){
        try{
            Connection con = getSQLConnection();
            String name = "";
            String email = "";
            PreparedStatement statement = con.prepareStatement("SELECT * FROM userdata WHERE matricNumber = \'"+matricNumber+"\'");
            ResultSet set = statement.executeQuery();
            
            while(set.next()){
                name = set.getString("name");
                email = set.getString("siswamail");
            }
            
            PreparedStatement insert = con.prepareStatement("INSERT INTO "+courseCode+" (matricNumber, name, email, occurrence) VALUES (\'"+matricNumber+"\', \'"+name+"\', \'"+email+"\',"+occurrence+")");
            insert.executeUpdate();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    /*delete the module instance the student registered from the 'matricNumber' table and
    and also the instance of the student from the course table */
    public static void drop(String matricNumber){
        Scanner sc = new Scanner(System.in);
        int occurrence = 0;
        String moduleName = "";
        try{
            Connection con = getSQLConnection();
            System.out.println("Please enter the course code of the couse you wish to drop.");
            String courseCode = sc.nextLine();
            boolean isRegistered = isRegistered(matricNumber, courseCode);
            
            if(isRegistered){
                PreparedStatement statement = con.prepareStatement("SELECT * FROM "+matricNumber+" WHERE courseCode = \'"+courseCode+"\' ");
                ResultSet set = statement.executeQuery();
                
                while(set.next()){
                    occurrence = set.getInt("Occurence");
                    moduleName = set.getString("ModuleName");
                }
                
                System.out.println("Are you sure you want to drop " + courseCode + " " + moduleName + " Occurrence " + occurrence + "? ");
                String answer = sc.nextLine();
                
                if(answer.equalsIgnoreCase("y")){
                    PreparedStatement drop = con.prepareStatement("DELETE FROM "+matricNumber+" WHERE courseCode = \'"+courseCode+"\' ");
                    drop.executeUpdate();
                    PreparedStatement drop2 = con.prepareStatement("DELETE FROM "+courseCode+" WHERE matricNumber = \'"+matricNumber+"\' ");
                    drop2.executeUpdate();
                    System.out.println("Module successfully dropped.");
                }
                else{
                    System.out.println("No changes have been made.");
                    viewRegisteredModule(matricNumber);
                }
            }else{
                System.out.println("Dropping module failed because module is not registered.");
            }
            
            
            
        }catch(Exception e){
            System.out.println(e);
        }
        
       
    }
    
    //return true if the user has already registered the course in the past 
     public static boolean isRegistered(String matricNumber, String courseCode){
            boolean isRegistered = false;
            
            try{
                Connection con = getSQLConnection();
                PreparedStatement check = con.prepareStatement("SELECT * FROM "+matricNumber+" WHERE courseCode = \'"+courseCode+"\' ");
                ResultSet set = check.executeQuery();
                
                if(set.next()){
                    isRegistered = true;
                    return isRegistered;
                }
                
            }catch(Exception e){
                System.out.println(e);
            }
            return isRegistered;
        }
     
    
    public static String addSlash(String s){
        int slash = 0;
        for(int i=0; i<s.length(); i++){
            if(s.charAt(i)=='\''){
                slash=i;
            }
        }
        if(slash > 0){
        return s.substring(0, slash) + "\\"+ s.substring(slash+1);
        }
        return s;
    }
    
    /*add an instance of the course into the table of the student as a record
    of the student registering the course*/
    public static void add(Connection con, String moduleCode, String matricNumber) throws Exception{
        boolean registered = isRegistered(matricNumber, moduleCode);
                if(registered){
                    System.out.println("Course is already registerd!");
                    //studentDashboard(matricNumber);
                    return;
                }
                
        boolean notMax = notMaximum(matricNumber);
        
        if(!notMax){
            System.out.println("Credit hours have exceeded 22 hours, please drop some module to continue adding new modules.");
            return;
        }
        boolean exist = isCourseExist(moduleCode);
        int startingTime = 0;
        int endTime = 0;
        String dayOfTheWeek = "";

        if(exist){
            try{
                PreparedStatement extract = con.prepareStatement("SELECT * FROM raw WHERE ModuleCode = \'"+moduleCode+"\'");
            ResultSet module = extract.executeQuery();
            System.out.printf("%-20s%-55s%-20s%-10s%-55s%-13s%-17s%-15s%n","ModuleCode", "ModuleName", "Occurrence","Activity", "Tutor", "Week", "Time", "Credit hours");
            
            while(module.next()){
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
                int creditHour = module.getInt("credithour");
                System.out.printf("%-20s%-55s%-20s%-10s%-55s%-13s%-17s%-15s%n", moduleCode, moduleName, occurrence, activity, tutor, dayOfTheWeek, time , credithour);
            }
            
            }catch(Exception e){
                System.out.println(e);
            }
            
            
            //pick occurence
            System.out.println("Please select an occurence");
            int occurrence = selectOccurrence(moduleCode, matricNumber);
            
            int creditHour = 0;
            
            if(occurrence == 0){
                System.out.println("Occurrence does not exist.");
                return;
            }
            else if (occurrence == -1){
                return;
            }
            else{
                try{
                    PreparedStatement select = con.prepareStatement("SELECT * FROM raw WHERE ModuleCode = \'"+moduleCode+"\' And Occurrence = \'"+occurrence+"\'  ");
                    
                    ResultSet results = select.executeQuery();
                    try{
                        PreparedStatement count = con.prepareStatement("SELECT * FROM "+matricNumber+"");
                        ResultSet set = count.executeQuery();

                        while(set.next()){
                            int temp = set.getInt("credithour");
                            creditHour += temp;
                        }
                        createCourseTable(moduleCode);

                    }catch(SQLException e){
                        System.out.println(e);
                    }
                    
                    
                    //check whether the credit hour has exceeded 22 or not;
                    while(results.next()){
                        String courseCode = results.getString("ModuleCode");
                        String moduleName = results.getString("ModuleName");
                        String lecturer = results.getString("Tutor");
                        int creditHours = results.getInt("credithour");
                        String week = results.getString("Week");
                        int TIME1 = results.getInt("TIME1");
                        int TIME2 = results.getInt("TIME2");
                        int TIME3 = results.getInt("TIME3");
                        
                        if(creditHour + creditHours > 22){
                            System.out.println("You have reached maximum credit hour, please drop some module to add new modules.");
                            viewRegisteredModule(matricNumber);
                        }
                        
                        PreparedStatement insert = con.prepareStatement("INSERT INTO "+matricNumber
                                + "(courseCode, ModuleName, Lecturer, Occurence, creditHour, Week, TIME1, TIME2, TIME3) "
                                + "VALUES (\'"+courseCode+"\', \'"+moduleName+"\', \'"+addSlash(lecturer)+"\', "+occurrence+", "+creditHours+", \'"+week+"\', "+TIME1+", "+TIME2+", "+TIME3+");");
                        insert.executeUpdate();
                        
                    }
                    
                    
                    addToTable(matricNumber, moduleCode, occurrence);
                }catch(SQLException e){
                    System.out.println(e);
                }catch(Exception e){
                    System.out.println(e);
                }
            }

        }
        else{
            System.out.println("Module does not exist.");
            viewRegisteredModule(matricNumber);
        }
        
    }
    
    /*check whether the course the student wishes to register clash with his timetable
    return true if it clashes. The method uses the between function in sql to check 
    whether the time of the selected course is between any of the time of student's
    registered course.
    */
    public static boolean clash(String matricNumber, String week, int startTime, int endTime, int occurrence) {
        boolean crash = false;

        try{
            Connection con = getSQLConnection();
            PreparedStatement check = con.prepareStatement("SELECT TIME1, TIME2, TIME3 FROM "+matricNumber+" WHERE (TIME1 between \'"+startTime+"\' And \'"+endTime+"\' Or TIME2 between \'"+startTime+"\' And \'"+endTime+"\' Or TIME3 between \'"+startTime+"\' And \'"+endTime+"\') And Week = \'"+week+"\'");
            ResultSet set = check.executeQuery();

            if(set.next()){
                crash = true;
            }
            
        }catch(Exception e){
            System.out.println(e);
        }
        
        return crash;
    }
    

    /*check and return the occurrence the students wishes to add as an integer,
    if the occurrence input is invalid, return 0
    */ 
    public static int selectOccurrence(String courseCode, String matricNumber){
        Scanner sc = new Scanner(System.in);
        int occurrence = sc.nextInt();
        boolean valid = checkOccurrence(courseCode, occurrence);
        boolean clash = false;
        boolean clashLecture = false;
        boolean clashTutorial = false;
        ArrayList<Integer> startTime = new ArrayList<Integer>();
        ArrayList<Integer> endTime = new ArrayList<Integer>();
        ArrayList<String> week = new ArrayList<String>();
        
        try{
            Connection con = getSQLConnection();
            
            if(valid){
                //check if the selected occurrence crashes with the student's timetable
                PreparedStatement fromRaw = con.prepareStatement("SELECT Week, TIME1, TIME2, TIME3 FROM raw WHERE ModuleCode = \'"+courseCode+"\' And Occurrence = \'"+occurrence+"\'");
                ResultSet raw = fromRaw.executeQuery();
                int counter = 0;
                while(raw.next()){
                    int time1 = raw.getInt("TIME1");
                    int time2 = raw.getInt("TIME2");
                    int time3 = raw.getInt("TIME3");

                    
                    startTime.add(time1);
                    
                    if(time3 != 0){
                        endTime.add(time3);
                    }
                    else{
                        endTime.add(time2);
                    }
                    week.add(raw.getString("Week"));
                    counter++;
                }
                
                /*
                if counter == 1, the course selected has only 1 type of activity
                ie.(lecture || tutorial)
                if counter == 2, the selected course has 2 types of activity
                ie.(lecture && tutorial)
                */    
                if(counter == 1){
                    clash = clash(matricNumber, week.get(0), startTime.get(0), endTime.get(0), occurrence);
                }
                else if(counter == 2){
                    clashLecture = clash(matricNumber, week.get(0), startTime.get(0), endTime.get(0), occurrence);
                    clashTutorial = clash(matricNumber, week.get(1), startTime.get(1), endTime.get(1), occurrence);                    
                }
                    
                    
                    if(clashLecture || clashTutorial || clash){
                        System.out.println("Selected occurrences clashes with your current timetable, please select a new occurrence.");
                        return -1;
                    }
                    else{
                        return occurrence;
                    }
                
                
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return 0;
        
    }
    
    //check if the occurrence the student selected is valid and exists
    public static boolean checkOccurrence(String courseCode, int occurrence2){
        boolean valid = false;
        ArrayList<Integer> occurrence = new ArrayList<Integer>();
        try{
            Connection con = getSQLConnection();
            PreparedStatement check = con.prepareStatement("SELECT Occurrence FROM raw WHERE ModuleCode = \'"+courseCode+"\'");
            ResultSet set = check.executeQuery();
            
            while(set.next()){
                occurrence.add(set.getInt("Occurrence"));
            }
            
            if(occurrence.contains(occurrence2)){
                valid = true;
            }
            
        }catch(Exception e){
            System.out.println(e);
        }
        return valid;
    }
    
    
    /*create a seperate table for each course to store the data of the students
    who have registered the course.
    */
    public static void createCourseTable(String courseCode){
        try{
            Connection con = getSQLConnection();
            
            PreparedStatement create = con.prepareStatement("CREATE TABLE IF NOT EXISTS "+courseCode+" (matricNumber varchar(255) NOT NULL PRIMARY KEY, FOREIGN KEY (matricNumber) REFERENCES userdata(matricNumber), name varchar(255) NOT NULL, FOREIGN KEY (name) REFERENCES userdata(NAME), email varchar(255) NOT NULL, occurrence int)");
            create.executeUpdate();
            
            
            
        }catch(Exception e){
            System.out.println(e);
        }finally{
            System.out.println("Table created successfully.");
        }
    }
    
    
    /*return boolean value true if the credit hour of a student is less than the 
    maximum credit hours which is 22.
    */
    public static boolean notMaximum(String matricNumber){
        boolean notMax = false;
        
        try{
            Connection con = getSQLConnection();
            
            int creditHour = 0;
            
            PreparedStatement count = con.prepareStatement("SELECT * FROM "+matricNumber+"");
            ResultSet set = count.executeQuery();
            
            while(set.next()){
                int temp = set.getInt("credithour");
                creditHour += temp;
            }
            System.out.println(creditHour);
            if(creditHour < 22){
                notMax = true;
            }
            else if (creditHour > 22){
                notMax = false;
            }
            
        }catch(Exception e){
            System.out.println(e);
        }
        return notMax;
    }
    
    public static int returnBand(String matricNumber){
        int muetBand = 0;
        try{
            Connection con = getSQLConnection();
            
            PreparedStatement obtain = con.prepareStatement("SELECT * FROM userdata WHERE matricNumber = \'"+matricNumber+"\'");
            ResultSet band = obtain.executeQuery();
            
            while(band.next()){
                muetBand = band.getInt("muetBand");
            }
            
            

        }catch(Exception e){
            System.out.println(e);
        }
        return muetBand;
        
    }
    
    /*public static boolean qualifiedForCourse(int muetBand, String courseCode){
        
        try{
            Connection con = getConnection();
            
            if(muetBand == 4){
                //qualified for glt1025
            }
            
            if(muetBand == 5){
                //qualified for glt1024
            }
            
        }catch(Exception e){
            System.out.println(e);
        }
    } 
    */

}

