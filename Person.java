package BlackJack2;

import java.util.ArrayList;

public abstract class Person implements IPerson {
    protected ArrayList<String> hand = new ArrayList<>();
    protected ArrayList<String> hand2 = new ArrayList<>();
    private final CardGenerator cardGenerator = new CardGenerator();

    public ArrayList<String> getHand() {
        return hand;
    }

    public ArrayList<String> getHand2() {
        return hand2;
    }

    public void drawCard(boolean isHand2) {
        String card = cardGenerator.getRandomCard();
        if (isHand2 && !hand2.isEmpty()) {
            hand2.add(card);
        } else {
            hand.add(card);
        }
    }

    public void displayHand() {
        System.out.print("Player Hand: ");
        for (String card : hand) {
            System.out.print(card + " ");
        }
        System.out.println();

        if (!hand2.isEmpty()) {
            System.out.print("Second Hand: ");
            for (String card : hand2) {
                System.out.print(card + " ");
            }
            System.out.println();
        }
    }

    public int getScore(ArrayList<String> specificHand) {
        return cardGenerator.calculateTotalScore(specificHand);
    }

    public void displayScore(ArrayList<String> specificHand) {
        int score = getScore(specificHand);
        System.out.println("Score: " + score);
    }

    public boolean isBusted(ArrayList<String> specificHand) {
        return getScore(specificHand) > 21;
    }

    public boolean hasBlackJack() {
        return getScore(hand) == 21;
    }

    public void clearHand() {
        hand.clear();
        hand2.clear();
    }

    public void splitHand() {
        if (hand.size() == 2 && cardGenerator.getCardValue(hand.get(0)) == cardGenerator.getCardValue(hand.get(1))) {
            hand2.clear();
            hand2.add(hand.get(1));
            hand.remove(1);
            hand.add(cardGenerator.getRandomCard());
            hand2.add(cardGenerator.getRandomCard());
        } else {
            System.out.println("You cannot split this hand.");
        }
    }

    public boolean canSplit() {
        return hand.size() == 2 && cardGenerator.getCardValue(hand.get(0)) == cardGenerator.getCardValue(hand.get(1));
    }

    public void removeHand(ArrayList<String> specificHand) {
        specificHand.clear();
    }

    public abstract int decideAction();
    public abstract void drawTo17();


}