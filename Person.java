package BlackJack2;

import java.util.ArrayList;

public abstract class Person implements IPerson {
    protected ArrayList<String> hand = new ArrayList<>();
    protected final CardGenerator cardGenerator = new CardGenerator();

    public void drawCard() {
        String card = cardGenerator.getRandomCard();
        hand.add(card);
    }

    public void displayHand() {
        System.out.print("Hand: ");
        cardGenerator.displayHand(hand);
    }

    public int getScore() {
        return cardGenerator.calculateTotalScore(hand);
    }

    public void displayScore() {
        int score = getScore();
        System.out.println("Score: " + score);
    }

    public boolean isBusted() {
        return getScore() > 21;
    }

    public boolean hasBlackJack() {
        return getScore() == 21;
    }

    public void clearHand() {
        hand.clear();
    }
}