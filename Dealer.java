package BlackJack2;

public class Dealer extends Person {

    @Override
    public void drawTo17() {
        while (getScore(hand) < 17) {
            drawCard(false);
        }
    }
    public void displayFirstCard() {
        System.out.println("Dealer's Hand: " + hand.getFirst() + " X");
        System.out.println(" ");
    }

    @Override
    public int decideAction() {
        return 0;
    }
}