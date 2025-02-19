package BlackJack2;

public class Main {
    public static void main(String[] args) {
        System.out.println("\nDealer must draw to 16 and stand on 17");
        System.out.println("Insurance pays out 3:1");
        System.out.println("You can only cash out after winning $1000 or more");
        GameManager gameManager = new GameManager();

        // game will keep looping until the player runs out of money or wins $1000
        while (GameManager.getBank() > 0 && GameManager.getBank() < 1000) {
            gameManager.startGame();

            if (GameManager.getBank() <= 0) {
                System.out.println("You're out of money!");
                break;
            } else if (GameManager.getBank() >= 1000) {
                System.out.println("You've won!");
                System.out.println("Total Winnings: $" + GameManager.getBank());
                break;
            }
        }
    }
}