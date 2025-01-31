package BlackJack2;

public class Dealer extends Person {

    public void drawTo17() {
        while (getScore(hand) < 17) drawCard(false);
    }

    public void displayFirstCard() {
        System.out.println("Dealer's Hand: " + hand.getFirst() + " X \n");
    }

    public boolean hasAce() {
        return hand.getFirst().charAt(0) == 'A';
    }
}
