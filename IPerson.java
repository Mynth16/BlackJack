package BlackJack2;

import java.util.ArrayList;

public interface IPerson {
    void drawCard(boolean isHand2);
    void displayHand();
    int getScore(ArrayList<String> specificHand);
    void displayScore(ArrayList<String> specificHand);
    boolean isBusted(ArrayList<String> specificHand);
    boolean hasBlackJack();
    void clearHand();
}