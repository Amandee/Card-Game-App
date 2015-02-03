package com.apps.mandee.dominionapp;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mandee on 1/18/2015.
 */
public class UsersHand {


    private ArrayList<String> deck;
    private ArrayList<String> played; // Played on that turn
    private int index_where_deck_is;
    private ArrayList<String> hand;
    private ArrayList<String> discard;
    private int amountOfCoinsOnHand;
    private int amount_actions_left; // amount of actions player is allowed
    private Context c;
    private Cards cards; // Cards class
    private int amount_buys_left;


    public UsersHand(Context context, Cards cards) {
        this.cards = cards;
        c = context;
        deck = new ArrayList<>();
        deck.add("Copper");
        deck.add("Copper");
        deck.add("Copper");
        deck.add("Copper");
        deck.add("Copper");
        deck.add("Copper");
        deck.add("Copper");
        deck.add("Estate");
        deck.add("Estate");
        deck.add("Estate");
        index_where_deck_is = 0;
        amountOfCoinsOnHand = 0;
        amount_buys_left = 1;
        hand = new ArrayList<>();
        discard = new ArrayList<>();
        played = new ArrayList<>();
        amount_actions_left = 1;

        Collections.shuffle(deck);
    }

    public void drawFive() {
        Log.e("Size of deck", "index deck " + index_where_deck_is );
        // If there are less than 5 cards in deck left
        if(deck.size() - 5 < index_where_deck_is)
        {


            //Add remaining rest of cards
            for(int i = index_where_deck_is; i < deck.size(); i++)
            {
                hand.add(deck.get(i));
            }
            // Make new deck from discard pile
            shuffleNewDeck();
            // While hand size isn't 5 draw
            while(hand.size()!= 5 && index_where_deck_is < deck.size())
            {
                hand.add(deck.get(index_where_deck_is));
                index_where_deck_is++;
            }
        }
        else
        {
            // If there are more than 5 cards than draw until have 5 cards
            while(hand.size()!= 5)
            {
                hand.add(deck.get(index_where_deck_is));
                index_where_deck_is++;
            }

        }
        howMuchCoins();


    }

    public ArrayList<String> returnHand(){

        Collections.sort(hand, String.CASE_INSENSITIVE_ORDER);
        return hand;
    }


    public void cleanUp() {
        amountOfCoinsOnHand = 0;
        discard.addAll(hand);
        discard.addAll(played);
        played.clear();
        hand.clear();

        amount_buys_left = 1;
        amount_actions_left = 1;
    }
    private void shuffleNewDeck(){

        // Deck is now discarded hand;
        deck.clear();
        deck.addAll(discard);
        // Clear discard
        discard.clear();
        // Shuffle new deck deck
        Collections.shuffle(deck);
        index_where_deck_is = 0; // Reset Index
    }





    public String playCard(String name){

        if(!ableToPlay())
            return "";
        playCardName(name);
        amount_actions_left--;
        return name;

    }

    public boolean buyCard(String name){

        if(!ableToBuy(name))
            return false;

        discard.add(name);
        cards.minusCard(name);
        amountOfCoinsOnHand -= cards.checkPrice(name);
        Log.e("Amount of coins", "coins is " + amountOfCoinsOnHand);
        amount_buys_left--;
        return true;


    }

    // For cellar
    public boolean discard(String cardName)
    {
        if(cardName.equals(""))
        {
            Toast.makeText(c,"Select card to discard", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(hand.size() == 0) {
            Toast.makeText(c, "No more cards to discard", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!contains(cardName))
        {
            Toast.makeText(c,"Unable to discard", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            discardPlayed(cardName);
            return true;
        }
    }
    // For workshop
    public boolean gain(String cardName){
        if(!cards.checkQuantity(cardName))
        {
            Toast.makeText(c,"No more cards left.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(cards.checkPrice(cardName) <= 4)
        {
            discard.add(cardName);
            cards.minusCard(cardName);
            return true;
        }
        else
        {
            Toast.makeText(c,"Unable to gain card.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    // For remodel
    public boolean remodel(String trashed, String cardName)
    {
        if(!cards.checkQuantity(cardName))
        {
            Toast.makeText(c,"No more cards left.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if((cards.checkPrice(trashed) + 2) >= cards.checkPrice(cardName))
        {
            discard.add(cardName);
            cards.minusCard(cardName);
            return true;
        }
        else
        {
            Toast.makeText(c,"Unable to choose card.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    // For remodel
    public boolean trash(String trashedCard){
       if(trashedCard.equals(""))
        {
            Toast.makeText(c,"Select card to trash", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(hand.size() == 0) {
            Toast.makeText(c, "No more cards to trash", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!contains(trashedCard))
        {
            Toast.makeText(c,"Unable to discard", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            trashCard(trashedCard);
            return true;
        }
    }
    // For mine
    public boolean getTreasureCard(String trashedTreasure, String cardName)
    {
        if(!cards.checkQuantity(cardName))
        {
            Toast.makeText(c,"No more cards left.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if((cards.checkPrice(trashedTreasure) + 3) >= cards.checkPrice(cardName))
        {
            hand.add(cardName);
            checkIfCoin(cardName);
            cards.minusCard(cardName);
            return true;
        }
        else
        {
            Toast.makeText(c,"Unable to choose card.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public int gameOver()
    {
        discard.addAll(hand);
        discard.addAll(played);
        deck.addAll(discard);
        int points = 0;
        for(int i = 0; i < deck.size(); i++)
        {
            switch(deck.get(i))
            {
                case "Estate":
                    points +=1;
                    break;
                case "Duchy":
                    points +=3;
                    break;
                case "Province":
                    points +=6;
                    break;

            }
        }
        return points;
    }


    public boolean contains(String cardName){
        return hand.contains(cardName);
    }

    public int getActions(){
        return amount_actions_left;
    }

    public int getBuys(){
        return amount_buys_left;
    }

    public int getDeck() {return deck.size() - index_where_deck_is;}

    public int coinsToSpend(){
        return amountOfCoinsOnHand;
    }
    // Starting amount of coins
    private int howMuchCoins() {

        Log.e("Coins on hand", "Coins: " + amountOfCoinsOnHand);
        amountOfCoinsOnHand = 0;
        for (int i = 0; i < hand.size(); i++) {
            switch (hand.get(i)) {
                case "Copper":
                    amountOfCoinsOnHand += 1;
                    break;
                case "Silver":
                    amountOfCoinsOnHand += 2;
                    break;
                case "Gold":
                    amountOfCoinsOnHand += 3;
                    break;
            }
        }
        return amountOfCoinsOnHand;
    }

    // checks if user is able to play the card
    private boolean ableToPlay(){
        if(amount_actions_left == 0)
        {
            Toast.makeText(c, "No actions left to play",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }



    // Checks if user is able to buy said card
    private boolean ableToBuy(String cardName){

        // checks if quantity is still left
        if(!cards.checkQuantity(cardName))
        {
            Toast.makeText(c,"No more cards left.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(cards.checkPrice(cardName) > amountOfCoinsOnHand)
        {
            Toast.makeText(c,"Not enough coins.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(amount_buys_left == 0)
        {
            Toast.makeText(c,"Unable to purchase more cards.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    private void increaseBuy(int num){
        amount_buys_left += num;
    }
    private void increaseAction(int num){
        amount_actions_left += num;
    }

    // Draw x amount of cards
    public void draw(int cardsToDraw){

        // Check if the draw will be bigger than the deck
        Log.e("Index where deck is", "Index is :" + index_where_deck_is);
        Log.e("Size of deck", "Deck size is: " + deck.size());
        if(index_where_deck_is + cardsToDraw >= deck.size())
        {
            // Draw until deck is empty
            for (int i = index_where_deck_is; i < deck.size(); i++) {
                hand.add(deck.get(i));
                checkIfCoin(deck.get(i));
                cardsToDraw--;
            }
            shuffleNewDeck();
            // Draw until no more cards to draw or until deck runs out
            while(cardsToDraw != 0 && index_where_deck_is < deck.size() -1)
            {
                hand.add(deck.get(index_where_deck_is));
                checkIfCoin(deck.get(index_where_deck_is));
                index_where_deck_is++;
                cardsToDraw--;
            }


        }
        else
        {   // Else just draw x amount of cards
            for(int i = index_where_deck_is; i < index_where_deck_is + cardsToDraw;i++)
            {
                hand.add(deck.get(i));
                checkIfCoin(deck.get(i));

            }
            index_where_deck_is+= cardsToDraw;
        }
        // Sort hand
        Collections.sort(hand, String.CASE_INSENSITIVE_ORDER);
    }

    // check if card drawn is a coin and if it is add to amountOfCoinsOnHand
    private void checkIfCoin(String name)
    {
        switch (name) {
            case "Copper":
                amountOfCoinsOnHand += 1;
                break;
            case "Silver":
                amountOfCoinsOnHand += 2;
                break;
            case "Gold":
                amountOfCoinsOnHand += 3;
                break;
        }
    }


    private void playCardName(String name)
    {
        switch(name)
        {
            case "Cellar":
                increaseAction(1);
                break;
            case "Moat": // Need to be able to reveal card
                draw(2);
                break;
            case "Village":
                increaseAction(2);
                draw(1);
                break;
            case "Workshop":
                break;
            case "Woodcutter":
                increaseBuy(1);
                amountOfCoinsOnHand +=2;
                break;
            case "Smithy":
                draw(3);
                break;
            case "Remodel":
                break;
            case "Militia":
                amountOfCoinsOnHand += 2;
                break;
            case "Market":
                draw(1);
                increaseBuy(1);
                increaseAction(1);
                amountOfCoinsOnHand +=1;
                break;
            case "Mine":
                break;

            default:
                Log.e("Error","Shouldn't be here");
                break;
        }

        removePlayed(name);

    }

    private void removePlayed(String name)
    {
        hand.remove(name);
        played.add(name);
    }

    // Discards card that is played
    private void discardPlayed(String name)
    {
        hand.remove(name);
        switch(name)
        {
            case "Copper":
                amountOfCoinsOnHand -=1;
                break;
            case "Silver":
                amountOfCoinsOnHand -=2;
                break;
            case "Gold":
                amountOfCoinsOnHand -=3;
                break;
        }
        discard.add(name);
    }

    private void trashCard(String trashedCard)
    {
        switch(trashedCard)
        {
            case "Copper":
                amountOfCoinsOnHand -=1;
                break;
            case "Silver":
                amountOfCoinsOnHand -=2;
                break;
            case "Gold":
                amountOfCoinsOnHand -=3;
                break;
        }
        hand.remove(trashedCard);
    }

}




