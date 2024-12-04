package BlackJack2;

public class Dealer extends Person {

    public void drawTo17() {
        for (getScore(); getScore() < 17; drawCard()) {
            if (getScore() >= 17) {
                return;
            }
        }
    }
    public void displayFirstCard() {
        System.out.println("Dealer's Hand: " + hand.getFirst() + " X");
        System.out.println(" ");
    }
}