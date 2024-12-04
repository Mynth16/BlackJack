package BlackJack2;

import java.util.Scanner;

public class GameManager {
    private final Player player = new Player();
    private final Dealer dealer = new Dealer();
    private static double bank = 300;
    private static final Scanner scanner = new Scanner(System.in);
    ResultStates result = ResultStates.DEFAULT;
    double bet = 0;

    public void startGame() {
        boolean deciding = true;
        boolean doubled = false;

        bet = 0;
        player.clearHand();
        dealer.clearHand();

        bet = setBet();

        player.drawCard();
        player.drawCard();
        dealer.drawCard();
        dealer.drawCard();

        player.displayHand();
        player.getScore();
        player.displayScore();
        dealer.displayFirstCard();
        dealer.getScore();

        if (checkForBlackJack()) {
            return;
        }

        while (deciding && !player.isBusted()) {
            int action = player.decideAction();
            switch (action) {
                case 1:
                    player.drawCard();
                    player.displayHand();
                    player.displayScore();
                    break;
                case 2:
                    deciding = false;
                    break;
                case 3:
                    doubled = true;
                    player.drawCard();
                    player.displayHand();
                    player.displayScore();
                    deciding = false;
                    break;
                case 4:
                    System.out.println("Split not implemented yet.");
                    break;
            }
        }

        if (player.isBusted()) {
            System.out.println("Busted!");
            bank -= bet * (doubled ? 2 : 1);
            return;
        }

        dealer.drawTo17();
        System.out.print("Dealer ");
        dealer.displayHand();
        System.out.print("Dealer ");
        dealer.displayScore();

        if (dealer.isBusted()) {
            System.out.println("Dealer Busted!");
            bank += bet * (doubled ? 2 : 1);
            return;
        }

        getGameResult(bet, doubled);
        calculateBankAmount(bet, result);
    }


    private boolean checkForBlackJack() {
        if (player.hasBlackJack()) {
            System.out.println("Blackjack!");
            System.out.println("Winnings: " + bet * 1.5);
            System.out.println(" ");
            bank += bet * 1.5;
            return true;
        } else if (dealer.hasBlackJack()) {
            System.out.println("Dealer BlackJack!");
            System.out.println("Lost: " + bet);
            System.out.println(" ");
            bank -= bet;
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
                System.out.println(" ");
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


    private void getGameResult(double bet, boolean doubled) {
        int playerScore = player.getScore();
        int dealerScore = dealer.getScore();
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


    private static void calculateBankAmount(double bet, ResultStates result) {
        switch (result) {
            case ResultStates.WIN -> bank += bet;
            case ResultStates.DOUBLE_WIN -> bank += bet * 2;
            case ResultStates.DRAW -> bank += 0;
            case ResultStates.LOSE -> bank -= bet;
            case ResultStates.DOUBLE_LOSE -> bank -= bet * 2;
            default -> System.out.println("Invalid result.");
        }
    }


    public static double getBank() {
        return bank;
    }
}