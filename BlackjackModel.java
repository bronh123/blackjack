package blackjack;

import java.util.ArrayList;
import java.util.Random;

import com.sun.jdi.Value;

import deckOfCards.*;

public class BlackjackModel {

	private ArrayList<Card> dealerCards = new ArrayList<Card>();
	private ArrayList<Card> playerCards = new ArrayList<Card>();
	private Deck deck = new Deck();

	// returns a new arraylist in order to protect augmentation to original
	// dealercards
	public ArrayList<Card> getDealerCards() {
		return new ArrayList<Card>(dealerCards);
	}

	// returns a new arraylist in order to protect augmentation to original
	// playercards
	public ArrayList<Card> getPlayerCards() {
		return new ArrayList<Card>(playerCards);
	}

	public void setDealerCards(ArrayList<Card> cards) {
		this.dealerCards = cards;
	}

	public void setPlayerCards(ArrayList<Card> cards) {
		this.playerCards = cards;
	}

	public void createAndShuffleDeck(Random random) {
		this.deck = new Deck();
		deck.shuffle(random);
	}

	// deals out 2 cards while clearing cards from last round
	public void initialDealerCards() {
		dealerCards.removeAll(dealerCards);
		dealerCards.add(deck.dealOneCard());
		dealerCards.add(deck.dealOneCard());
	}

	// deals out 2 cards while clearing cards from last round
	public void initialPlayerCards() {
		playerCards.removeAll(playerCards);
		playerCards.add(deck.dealOneCard());
		playerCards.add(deck.dealOneCard());
	}

	public void dealerTakeCard() {
		dealerCards.add(deck.dealOneCard());
	}

	public void playerTakeCard() {
		playerCards.add(deck.dealOneCard());
	}

	// possibletotal is if ace is a one, total is general value and if ace is a 10
	public static ArrayList<Integer> possibleHandValues(ArrayList<Card> hand) {
		int total = 0;
		int possibleTotal = 0;
		boolean ace = false;
		ArrayList<Integer> posHandVals = new ArrayList<Integer>();
		for (Card card : hand) {
			int value = card.getRank().getValue();
			if (value == 1) {
				ace = true;
			}
			if (ace && total + 11 <= 21) {
				possibleTotal = total + 1;
				total += 11;
			} else {
				possibleTotal += value;
				total += value;
			}
			ace = false;
		}
		// adds total and possibletotal if both are seperate values. Adds only
		// possibletotal if total is greater then 21.
		if (total <= 21 && total != possibleTotal) {
			posHandVals.add(possibleTotal);
			posHandVals.add(total);
		} else if (total > 21)
			posHandVals.add(possibleTotal);
		else
			posHandVals.add(total);
		return posHandVals;
	}

	// Busts if hand is over 21, natural blackjack if having 2 cards is equal to 21,
	// otherwise its normal.
	public static HandAssessment assessHand(ArrayList<Card> hand) {
		ArrayList<Integer> inputHand = possibleHandValues(hand);
		if (hand.size() < 2 || hand == null)
			return HandAssessment.INSUFFICIENT_CARDS;
		else if (inputHand.get(inputHand.size() - 1) > 21)
			return HandAssessment.BUST;
		else if (hand.size() == 2 && inputHand.get(inputHand.size() - 1) == 21)
			return HandAssessment.NATURAL_BLACKJACK;
		return HandAssessment.NORMAL;
	}

	// Natural blackjack if only user has. Pushes if even or both natural. Lost if
	// bust or less, won if dealer busts or has less.
	public GameResult gameAssessment() {
		ArrayList<Integer> dealerHand = possibleHandValues(dealerCards);
		ArrayList<Integer> playerHand = possibleHandValues(playerCards);
		if (assessHand(playerCards) == HandAssessment.NATURAL_BLACKJACK
				&& assessHand(dealerCards) != HandAssessment.NATURAL_BLACKJACK)
			return GameResult.NATURAL_BLACKJACK;
		else if (assessHand(playerCards) == HandAssessment.NATURAL_BLACKJACK
				&& assessHand(dealerCards) == HandAssessment.NATURAL_BLACKJACK)
			return GameResult.PUSH;
		else if (assessHand(playerCards) == HandAssessment.BUST)
			return GameResult.PLAYER_LOST;
		else if (assessHand(dealerCards) == HandAssessment.BUST)
			return GameResult.PLAYER_WON;
		if (dealerHand.get(dealerHand.size() - 1) > playerHand.get(playerHand.size() - 1))
			return GameResult.PLAYER_LOST;
		else if (dealerHand.get(dealerHand.size() - 1) == playerHand.get(playerHand.size() - 1))
			return GameResult.PUSH;
		else
			return GameResult.PLAYER_WON;
	}

	// draws depending on criteria in rubric
	public boolean dealerShouldTakeCard() {
		ArrayList<Integer> dealerHand = possibleHandValues(dealerCards);
		if (!(dealerHand.contains(7)) && dealerHand.contains(17))
			return false;
		else if (dealerHand.contains(17) && dealerHand.contains(7))
			return true;
		else if (dealerHand.get(dealerHand.size() - 1) <= 16)
			return true;
		else if (dealerHand.contains(18))
			return false;
		else
			return false;
	}

}
