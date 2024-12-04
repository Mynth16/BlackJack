import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

public class Blackjack {
    static String[] cards = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    static Scanner scanner = new Scanner(System.in);
    static double bank = 300.0;
    static boolean doubled = false;
    static boolean secondHand = false;

    public static void main(String[] args) {

        Random random = new Random();
        int action = 0;
        ResultStates endResult = ResultStates.DEFAULT;
        int bet = 0;
        boolean isValid = false;
        boolean gameOver = false;
        boolean dealerBusted = false;
        boolean loop = false;
        ArrayList<String> playerCards = new ArrayList<>();
        ArrayList<String> dealerCards = new ArrayList<>();
        ArrayList<String> hand1 = new ArrayList<>();
        ArrayList<String> hand2 = new ArrayList<>();

        System.out.println("Dealer must draw to 16 and stand on 17");
        System.out.println("You can only cash out after getting 1000$ or more");

        while (!loop) {
            if (bank > 0 && bank < 1000) {

                gameOver = false;
                dealerBusted = false;
                doubled = false;
                secondHand = false;

                hand1.clear();
                hand2.clear();
                dealerCards.clear();
                playerCards.clear();

                bet = getBetAmount();
                playerCards.add(getRandomCard());
                playerCards.add(getRandomCard());

                dealerCards.add(getRandomCard());
                dealerCards.add(getRandomCard());

                System.out.println("Your hand: ");
                showCardInHand(playerCards);
                System.out.println();

                System.out.println("Dealer's hand: ");
                System.out.println(dealerCards.get(0) + " X");

                if (calculateTotalScore(playerCards) == 21) {
                    System.out.println("Blackjack!");
                    System.out.println("Winnings: $" + bet * 2.5);
                    bank += bet * 2.5;
                    continue;
                }

                if (calculateTotalScore(dealerCards) == 21) {
                    showCardInHand(dealerCards);
                    System.out.println();
                    System.out.println("Dealer Blackjack!");
                    System.out.println("Lost: $" + bet);
                    continue;
                }

                while (!isValid && !gameOver) {
                    action = promptAction();
                    switch (action) {
                        case 1:
                            hit(playerCards);
                            if (hitResult(playerCards, bet, doubled)) {
                                gameOver = true;
                            }
                            break;
                        case 2:
                            isValid = true;
                            break;
                        case 3:
                            bank -= bet;
                            doubled = true;
                            hit(playerCards);
                            if (hitResult(playerCards, bet * 2, doubled)) {
                                gameOver = true;
                                break;
                            } else {
                                isValid = true;
                                break;
                            }
                        case 4:
                            bank -= bet;
                            if (getCardValue(playerCards.get(0)) == getCardValue(playerCards.get(1))) {
                                bank -= bet;
                                hand1.add(playerCards.get(0));
                                hand2.add(playerCards.get(1));
                                hand1.add(getRandomCard());
                                hand2.add(getRandomCard());

                                System.out.println("Hand 1: ");
                                showCardInHand(hand1);
                                System.out.println(" ");

                                action = promptAction();
                                switch (action) {
                                    case 1:
                                        hit(hand1);
                                        if (hitResult(hand1, bet, doubled)) {
                                            gameOver = true;
                                        }
                                        break;
                                    case 2:
                                        isValid = true;
                                        break;
                                    case 3:
                                        bank -= bet;
                                        doubled = true;
                                        hit(hand1);
                                        if (hitResult(hand1, bet * 2, doubled)) {
                                            gameOver = true;
                                        } else {
                                            isValid = true;
                                        }
                                        break;
                                    case 4:
                                        System.out.println("Cannot split again");
                                        break;
                                }
                                secondHand = true;

                                System.out.println("Hand 2: ");
                                showCardInHand(hand2);
                                System.out.println(" ");

                                action = promptAction();
                                switch (action) {
                                    case 1:
                                        hit(hand2);
                                        if (hitResult(hand2, bet, doubled)) {
                                            gameOver = true;
                                        }
                                        break;
                                    case 2:
                                        isValid = true;
                                        break;
                                    case 3:
                                        bank -= bet;
                                        doubled = true;
                                        hit(hand2);
                                        if (hitResult(hand2, bet * 2, doubled)) {
                                            gameOver = true;
                                        } else {
                                            isValid = true;
                                        }
                                        break;
                                    case 4:
                                        System.out.println("Cannot split again");
                                        break;
                                }
                                if (gameOver) {
                                    System.out.println("Bust");
                                    System.out.println("Lost: $" + bet);
                                    System.out.println("Aw dang it");
                                }
                            } else {
                                System.out.println("You can only split two of the same card");
                                break;
                            }
                    }
                }


                isValid = false;

                if (!gameOver) {
                    for (int i = 0; calculateTotalScore(dealerCards) < 17; i++) {
                        dealerCards.add(getRandomCard());
                        if (calculateTotalScore(dealerCards) > 21) {
                            dealerResult(dealerCards);
                            System.out.println("Dealer Bust");
                            if (!doubled) {
                                System.out.println("Winnings: " + bet * 2);
                                bank += bet * 2;
                                dealerBusted = true;
                                break;
                            } else {
                                System.out.println("Winnings: $" + bet * 4);
                                bank += bet * 4;
                                dealerBusted = true;
                                break;
                            }
                        }
                    }
                    if (!hand1.isEmpty()) {
                        if (dealerBusted) {
                            gameOver = true;
                            if (!doubled) {
                                bank += bet;
                            } else {
                                bank += bet * 3;
                            }
                            continue;
                        }
                        dealerResult(dealerCards);
                        endResult = calculateResult(playerCards, dealerCards, hand1, hand2, bet, doubled);
                        bank = calculateBankAmount(endResult, bet);
                        endResult = calculateResult(playerCards, dealerCards, hand1, hand2, bet, doubled);
                        bank = calculateBankAmount(endResult, bet);
                    }

                    if (dealerBusted) {
                        gameOver = true;
                        continue;
                    }

                    dealerResult(dealerCards);
                    endResult = calculateResult(playerCards, dealerCards, hand1, hand2, bet, doubled);
                    bank = calculateBankAmount(endResult, bet);


                }
            }   else if (bank >= 1000) {
                System.out.println("YOU WIN!");
                System.out.println("Total earnings: $" + bank);
                loop = true;
                return;
            }   else {
                System.out.println("Busted");
                loop = true;
                return;
            }
        }
    }


    private static int getBetAmount() {
        boolean isValid = false;
        int betAmount = 0;
        System.out.println(" ");
        System.out.println("LETS GO GAMBLING");

        while (!isValid) {
            System.out.println("Enter bet amount (min 5, max 50): ");
            System.out.println("Current Balace: " + bank);
            System.out.print("Enter your bet amount: ");
            if (scanner.hasNextInt()) {
                betAmount = scanner.nextInt();
                if (betAmount >= 5 && betAmount <= 50 && betAmount <= bank) {
                    bank -= betAmount;
                    isValid = true;
                } else {
                    System.out.println(" ");
                    System.out.println("invalid bet amount");
                }
            } else {
                System.out.println(" ");
                System.out.println("please enter a bet amount");
                scanner.next();
            }
        }

        isValid = false;
        return betAmount;
    }


    private static String getRandomCard() {
        Random random = new Random();
        return cards[random.nextInt(cards.length)];
    }


    private static void showCardInHand(ArrayList<String> cards) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i) != null)
                System.out.print(cards.get(i) + " ");
        }
    }


    private static int getCardValue(String cardFace) {
        return switch (cardFace) {
            case "A" -> 11;
            case "J", "Q", "K" -> 10;
            default -> Integer.parseInt(cardFace);
        };
    }


    private static int calculateTotalScore(ArrayList<String> cards) {
        int total = 0;
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i) != null)
                total += getCardValue(cards.get(i));
        }

        if (total > 21) {
            for (int i = 0; i < cards.size(); i++) {
                if (cards.get(i) == "A")
                    total -= 10;
            }
        }


        return total;
    }


    private static int promptAction() {
        boolean isValid = false;
        int action = 0;
        while (!isValid) {
            System.out.println("1.Hit  2.Stand  3.Double  4.Split");
            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                action = scanner.nextInt();
                if (action >= 1 && action <= 4) {
                    isValid = true;
                } else {
                    System.out.println("invalid option");
                }
            } else {
                System.out.println("please choose an action");
                scanner.next();
            }
        }
        isValid = false;

        return action;
    }


    private static int hit(ArrayList<String> card) {
        card.add(getRandomCard());
        System.out.print("Your hand: ");
        showCardInHand(card);
        System.out.println();
        System.out.println("Your score: " + calculateTotalScore(card));

        return calculateTotalScore(card);
    }


    private static boolean hitResult(ArrayList<String> card, double bet, boolean doubled) {
        if (calculateTotalScore(card) > 21) {
            System.out.println("Bust");
            if (doubled) {
                System.out.println("Lost: $" + bet * 2);
            } else {
                System.out.println("Lost: $" + bet);
            }
            System.out.println("Aw dang it");
            return true;
        }
        return false;
    }


    private static void dealerResult(ArrayList<String> dealerCards) {
        System.out.println(" ");
        System.out.print("Dealer Cards: ");
        showCardInHand(dealerCards);
        System.out.println(" ");
        System.out.println("Dealer Score: " + calculateTotalScore(dealerCards));
    }


    public enum ResultStates {
        WIN,
        DOUBLE_WIN,
        DRAW,
        DOUBLE_DRAW,
        LOSE,
        DOUBLE_LOSE,
        DEFAULT,
    }


    private static ResultStates calculateResult(ArrayList<String> playerCards, ArrayList<String> dealerCards, ArrayList<String> hand1, ArrayList<String> hand2, int bet, boolean doubled) {
        int playerScore = 0;
        int dealerScore = calculateTotalScore(dealerCards);
        ResultStates result = ResultStates.DEFAULT;


        if (!hand2.isEmpty()) {
            int secondHandScore = calculateTotalScore(hand2);

            if (secondHandScore > dealerScore) {
                System.out.println("Hand 2: You Won!");
                if (!doubled) {
                    System.out.println("Winnings: $" + bet * 2);
                    result = ResultStates.WIN;
                } else {
                    System.out.println("Winnings: $" + bet * 4);
                    System.out.println("JACKPOT!");
                    result = ResultStates.DOUBLE_WIN;
                }
            } else if (secondHandScore == dealerScore && !doubled) {
                System.out.println("Hand 2: Draw");
                result = ResultStates.DRAW;
            } else if (playerScore == dealerScore && doubled) {
                System.out.println("Hand 1: Draw");
                result = ResultStates.DOUBLE_DRAW;
            } else {
                System.out.println("Hand 2: You Lost");
                if (!doubled) {
                    System.out.println("Lost: " + bet);
                    System.out.println("Aw dang it");
                    result = ResultStates.LOSE;

                } else {
                    System.out.println("Lost: " + bet * 2);
                    System.out.println("Aw dang it");
                    result = ResultStates.DOUBLE_LOSE;
                }
            }
            return result;
        }
        else  {
            playerScore = !hand1.isEmpty() ? calculateTotalScore(hand1) : calculateTotalScore(playerCards);
            System.out.println("Player Score(hand1): " + playerScore);

            if (playerScore > dealerScore) {
                System.out.println("Hand 1: You Won!");
                if (!doubled) {
                    System.out.println("Winnings: $" + bet * 2);
                    result = ResultStates.WIN;
                } else {
                    System.out.println("Winnings: $" + bet * 4);
                    System.out.println("JACKPOT!");
                    result = ResultStates.DOUBLE_WIN;
                }
            } else if (playerScore == dealerScore && !doubled) {
                System.out.println("Hand 1: Draw");
                result = ResultStates.DRAW;
            } else if (playerScore == dealerScore && doubled) {
                System.out.println("Hand 1: Draw");
                result = ResultStates.DOUBLE_DRAW;
            } else {
                System.out.println("Hand 1: You Lost");
                if (!doubled) {
                    System.out.println("Lost: " + bet);
                    System.out.println("Aw dang it");
                    result = ResultStates.LOSE;
                } else {
                    System.out.println("Lost: " + bet * 2);
                    System.out.println("Aw dang it");
                    result = ResultStates.DOUBLE_LOSE;
                }
            }
        }

        return result;
    }


    private static double calculateBankAmount(ResultStates result, int bet) {
        return switch (result) {
            case ResultStates.WIN -> bank += bet * 2;
            case ResultStates.DOUBLE_WIN -> bank += bet * 4;
            case ResultStates.DRAW -> bank += bet;
            case ResultStates.DOUBLE_DRAW -> bank += bet * 2;
            case ResultStates.LOSE -> bank;
            case ResultStates.DOUBLE_LOSE -> bank;
            default -> bank;
        };
    }
}