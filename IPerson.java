package BlackJack2;

public interface IPerson {
    void drawCard();
    void displayHand();
    int getScore();
    void displayScore();
    boolean isBusted();
    boolean hasBlackJack();
    void clearHand();
}