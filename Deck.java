package deckOfCards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {

	private ArrayList<Card> deck = new ArrayList<Card>();

	public Deck() {
		for (Suit curSuit : Suit.values()) {
			for (Rank curRank : Rank.values()) {
				Card tempCard = new Card(curRank, curSuit);
				deck.add(tempCard);
			}
		}
	}

	public void shuffle(Random randomNumberGenerator) {
		Collections.shuffle(deck, randomNumberGenerator);
	}

	public Card dealOneCard() {
		Card tempCard = deck.get(0);
		deck.remove(0);
		return tempCard;
	}

}
