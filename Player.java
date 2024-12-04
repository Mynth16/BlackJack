package BlackJack2;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Player extends Person {
    private static final Scanner scanner = new Scanner(System.in);

    public int decideAction() {
        while (true) {
            try {
                System.out.print("1. Hit\n2. Stand\n3. Double Down\n4. Split\nEnter your choice: ");
                int input = scanner.nextInt();
                if (input >= 1 && input <= 4) {
                    System.out.println(" ");
                    return input;
                } else {
                    System.out.println("Invalid input. Please enter a number between 1 and 4.");
                    System.out.println(" ");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                System.out.println(" ");
                scanner.next();
            }
        }
    }
}