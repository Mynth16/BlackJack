package BlackJack2;

import java.util.ArrayList;
import java.util.Scanner;

public class GameManager {
    private final Player player = new Player();
    private final Dealer dealer = new Dealer();
    private static double bank = 300;
    private static final Scanner scanner = new Scanner(System.in);
    ResultStates result = ResultStates.DEFAULT;
    double bet = 0;

    // all money calculations are done here, DO NOT implement bank logic anywhere else
    private static void calculateBankAmount(double bet, ResultStates result) {
        switch (result) {
            case ResultStates.WIN, ResultStates.DEALER_BUST -> bank += bet;
            case ResultStates.DOUBLE_WIN, ResultStates.DOUBLE_DEALER_BUST -> bank += bet * 2;
            case ResultStates.DRAW -> bank += 0;
            case ResultStates.LOSE, ResultStates.PLAYER_BUST, ResultStates.DEALER_BLACKJACK -> bank -= bet;
            case ResultStates.DOUBLE_LOSE, ResultStates.DOUBLE_PLAYER_BUST -> bank -= bet * 2;
            case ResultStates.PLAYER_BLACKJACK -> bank += bet * 1.5;
            default -> System.out.println("Invalid result.");
        }
    }


    private boolean checkForBlackJack() {
        if (player.hasBlackJack()) {
            System.out.println("Blackjack!");
            System.out.println("Winnings: " + bet * 1.5);
            result = ResultStates.PLAYER_BLACKJACK;
            calculateBankAmount(bet, result);
            return true;
        } else if (dealer.hasBlackJack()) {
            System.out.println("Dealer BlackJack!");
            System.out.println("Lost: " + bet);
            result = ResultStates.DEALER_BLACKJACK;
            calculateBankAmount(bet, result);
            return true;
        }
        return false;
    }


    private static double setBet() {
        while (true) {
            System.out.println(" ");
            System.out.println("Current Balance: " + bank);
            System.out.print("Enter bet amount (min 5, max 50): ");

            if (!scanner.hasNextDouble()) {
                System.out.println("Invalid input. Please enter a valid number.");
                scanner.nextLine();
                continue;
            }
            double bet = scanner.nextDouble();

            if (bet < 5 || bet > 50) {
                System.out.println("Invalid bet amount. Please enter a bet between 5 and 50.");
                System.out.println(" ");
                continue;
            }

            if (bet > bank) {
                System.out.println("Insufficient funds. Please enter a bet within your balance.");
                System.out.println(" ");
                continue;
            }
            return bet;
        }
    }

    public void startGame() {
        // restarts all variables
        boolean deciding = true;
        boolean doubled = false;
        boolean canDouble = true;
        bet = 0;
        player.clearHand();
        dealer.clearHand();

        // Starting point
        bet = setBet();

        // Deal initial cards, the boolean for drawCard is passed as false for every card draw that isn't for the split hand
        player.drawCard(false);
        player.drawCard(false);
        dealer.drawCard(false);
        dealer.drawCard(false);

        player.displayHand();
        player.displayScore(player.getHand());
        dealer.displayFirstCard();

        // ends the game if either player has a blackjack
        if (checkForBlackJack()) {
            return;
        }

        // Player's turn
        while (deciding && !player.isBusted(player.getHand())) {
            if (bank < bet * 2) {
                canDouble = false;
            }
            int action = player.decideAction();
            switch (action) {
                case 1:
                    canDouble = false;
                    player.drawCard(false);
                    player.displayHand();
                    player.displayScore(player.getHand());
                    break;
                case 2:
                    deciding = false;
                    break;
                case 3:
                    if (!canDouble) {
                        System.out.println("You cannot double down.");
                        break;
                    }
                    doubled = true;
                    player.drawCard(false);
                    player.displayHand();
                    player.displayScore(player.getHand());
                    deciding = false;
                    break;
                case 4:
                    if (player.canSplit()) {
                        player.splitHand();
                        playSplitHands(doubled);
                        return;
                    } else {
                        System.out.println("You can only split with two cards of the same value.");
                    }
                    break;
                default:
                    System.out.println("Invalid input.");
            }
        }

        // read the result as; IF doubled THEN double result ELSE result, this will be used everywhere
        if (player.isBusted(player.getHand())) {
            System.out.println("Busted!");
            result = doubled ? ResultStates.DOUBLE_PLAYER_BUST : ResultStates.PLAYER_BUST;
            calculateBankAmount(bet, result);
            return;
        }

        // Dealer's turn
        dealer.drawTo17();
        System.out.print("Dealer ");
        dealer.displayHand();
        System.out.print("Dealer ");
        dealer.displayScore(dealer.getHand());

        if (dealer.isBusted(dealer.getHand())) {
            System.out.println("Dealer Busted!");
            result = doubled ? ResultStates.DOUBLE_DEALER_BUST : ResultStates.DEALER_BUST;
            calculateBankAmount(bet, result);
            return;
        }

        // if no one busted, compare scores
        getGameResult(bet, doubled, player.getHand());
        calculateBankAmount(bet, result);
    }

    // idk why it doesn't work when i don't pass the boolean for doubled
    private void playSplitHands(boolean doubled) {
        double secondBet = bet;

        player.displayHand();
        System.out.println("\nPlaying Hand 1:");
        playSingleHand(player.getHand(), doubled, false);

        System.out.println("\nPlaying Hand 2:");
        playSingleHand(player.getHand2(), doubled, true);

        dealer.drawTo17();
        System.out.print("Dealer ");
        dealer.displayHand();
        System.out.print("Dealer ");
        dealer.displayScore(dealer.getHand());

        if (dealer.isBusted(dealer.getHand())) {
            System.out.println("Dealer Busted!");
            result = doubled ? ResultStates.DOUBLE_DEALER_BUST : ResultStates.DEALER_BUST;
            calculateBankAmount(bet, result);
            result = doubled ? ResultStates.DOUBLE_DEALER_BUST : ResultStates.DEALER_BUST;
            calculateBankAmount(secondBet, result);
            return;
        }

        System.out.println("\nHand 1 result:");
        getGameResult(bet, doubled, player.getHand());
        calculateBankAmount(bet, result);

        System.out.println("\nHand 2 result:");
        getGameResult(secondBet, doubled, player.getHand2());
        calculateBankAmount(secondBet, result);
    }

    // only used for split hands, works exactly the same as the regular player's turn except case 4
    // the boolean isHand2 is used to determine which hand to draw to
    private void playSingleHand(ArrayList<String> hand, boolean doubled, boolean isHand2) {
        boolean deciding = true;
        boolean canDouble = true;

        while (deciding && !player.isBusted(hand)) {
            if (bank < bet * 2) {
                canDouble = false;
            }
            int action = player.decideAction();
            switch (action) {
                case 1:
                    canDouble = false;
                    player.drawCard(isHand2);
                    player.displayHand();
                    player.displayScore(hand);
                    break;
                case 2:
                    deciding = false;
                    break;
                case 3:
                    if (!canDouble) {
                        System.out.println("You cannot double down.");
                        break;
                    }
                    doubled = true;
                    bet *= 2;
                    player.drawCard(isHand2);
                    player.displayHand();
                    player.displayScore(hand);
                    deciding = false;
                    break;
                case 4:
                    System.out.println("You cannot split again.");
                    break;
                default:
                    System.out.println("Invalid action. Try again.");
            }
        }
        // If the hand busts, I remove it from the player so it doesn't get checked for game result
        if (player.isBusted(hand)) {
            System.out.println("Busted!");
            result = doubled ? ResultStates.DOUBLE_PLAYER_BUST : ResultStates.PLAYER_BUST;
            player.removeHand(hand);
        }
    }

    // compares the scores of the player and dealer, and determines the result of the game
    // the bet is only passed for the display amount, it does not affect the bank
    private void getGameResult(double bet, boolean doubled, ArrayList<String> hand) {
        int playerScore = player.getScore(hand);
        int dealerScore = dealer.getScore(dealer.getHand());
        double payout = doubled ? bet * 2 : bet;

        if (playerScore > dealerScore) {
            System.out.println("You win!");
            System.out.println("Winnings: " + payout);
            result = doubled ? ResultStates.DOUBLE_WIN : ResultStates.WIN;
        } else if (playerScore == dealerScore) {
            System.out.println("Draw!");
            result = ResultStates.DRAW;
        } else {
            System.out.println("You lost!");
            System.out.println("Lost: " + payout);
            result = doubled ? ResultStates.DOUBLE_LOSE : ResultStates.LOSE;
        }
    }

    public static double getBank() {
        return bank;
    }
}
