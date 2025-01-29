package BlackJack2;

public class Dealer extends Person {

    // Dealer must draw to 16 and stand on 17
    @Override
    public void drawTo17() {
        while (getScore(hand) < 17) drawCard(false);
    }
    public void displayFirstCard() {
        System.out.println("Dealer's Hand: " + hand.getFirst() + " X \n");
    }

    @Override
    public int decideAction() {
        return 0;
    }

    public boolean hasAce() {
        return hand.getFirst().charAt(0) == 'A';
    }
}