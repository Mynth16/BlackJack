package BlackJack2;

import java.util.ArrayList;

/**
 * Interface representing a person in a Blackjack game.
 */
public interface IPerson {

    /**
     * Draws a card and adds it to the person's hand.
     *
     * @param isHand2 a boolean indicating if the card should be added to the second hand (if applicable).
     */
    void drawCard(boolean isHand2);

    /**
     * Displays the person's current hand.
     */
    void displayHand();

    /**
     * Calculates and returns the score of a specific hand.
     *
     * @param specificHand an ArrayList of Strings representing the cards in the hand.
     * @return the score of the hand as an integer.
     */
    int getScore(ArrayList<String> specificHand);

    /**
     * Displays the score of a specific hand.
     *
     * @param specificHand an ArrayList of Strings representing the cards in the hand.
     */
    void displayScore(ArrayList<String> specificHand);

    /**
     * Checks if the score of a specific hand exceeds 21.
     *
     * @param specificHand an ArrayList of Strings representing the cards in the hand.
     * @return true if the score exceeds 21, false otherwise.
     */
    boolean isBusted(ArrayList<String> specificHand);

    /**
     * Checks if the person has a Blackjack (a score of 21 with the first two cards).
     *
     * @return true if the person has a Blackjack, false otherwise.
     */
    boolean hasBlackJack();

    /**
     * Clears the person's hand.
     */
    void clearHand();
}