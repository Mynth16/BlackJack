package BlackJack2;

import java.util.ArrayList;
import java.util.Random;

public class CardGenerator {
    private static final String[] CARD_FACES = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    private static final Random random = new Random();

    public String getRandomCard() {
        return CARD_FACES[random.nextInt(CARD_FACES.length)];
    }

    public int getCardValue(String cardFace) {
        return switch (cardFace) {
            case "A" -> 11;
            case "K", "Q", "J" -> 10;
            default -> Integer.parseInt(cardFace);
        };
    }

    public void displayHand(ArrayList<String> cards) {
        for (String card : cards) {
                System.out.print(card + " ");
        }
        System.out.println();
    }

    public int calculateTotalScore(ArrayList<String> hand) {
        int total = 0;
        int aceCount = 0;

        for (String card : hand) {
            int cardValue = getCardValue(card);
            total += cardValue;
            if (card.equals("A")) {
                aceCount++;
            }
        }

        while (total > 21 && aceCount > 0) {
            total -= 10;
            aceCount--;
        }
        return total;
    }
}