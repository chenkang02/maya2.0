/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fopassignment;

import java.util.Scanner;

/**
 *
 * @author Chen Kang
 */
public class archiveMethod {
     public static void enterPasswordToEnterMainPage(String correctPassword) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter your password: ");
        String pass = sc.nextLine();
        int chance = 3;
        while (!pass.equals(correctPassword) && chance > 0) {
            System.out.println("Password incorrect, you have " + chance + " more chances.");
            System.out.print("Please re-enter your password: ");
            pass = sc.nextLine();
            chance--;
        }
        if (pass.equals(correctPassword)) {
            //studentdashboard
        } else {
            System.out.println("Please reset your password, you have no more chances.");
            //reset password.
        }
    }
}
