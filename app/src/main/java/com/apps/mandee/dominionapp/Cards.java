package com.apps.mandee.dominionapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mandee on 1/19/2015.
 */
public class Cards {



    private Map<String,Integer> card_price;
    private Map<String,Integer> card_quantity;
    private int pileGone; // For game over
    public Cards(){

        pileGone = 0;
        card_price = new HashMap<>();
        card_quantity = new HashMap<>();



    }

    public void addCardsAndQuantity(int players)
    {

        card_price.put("Copper",0);
        card_price.put("Silver",3);
        card_price.put("Gold",6);
        card_price.put("Estate",2);
        card_price.put("Duchy",5);
        card_price.put("Province",8);
        card_price.put("Curse",0);
        card_price.put("Moat",2);
        card_price.put("Cellar",2);
        card_price.put("Village",3);
        card_price.put("Workshop",3);
        card_price.put("Woodcutter",3);
        card_price.put("Smithy",4);
        card_price.put("Remodel",4);
        card_price.put("Militia",4);
        card_price.put("Market",5);
        card_price.put("Mine",5);
        int victoryCards;
        int curse;
        if(players < 3)
        {
            victoryCards = 8;
            curse = 10;


        }
        else
        {
            victoryCards = 12;
            curse = 20;
        }
        if(players > 4)
            curse = 30;
        int copperCard = 60 - (7* players);
        card_quantity.put("Copper",copperCard);
        card_quantity.put("Silver",40);
        card_quantity.put("Gold",30);
        card_quantity.put("Estate",victoryCards);
        card_quantity.put("Duchy",victoryCards);
        card_quantity.put("Province",victoryCards);
        card_quantity.put("Curse",curse);
        card_quantity.put("Cellar",10);
        card_quantity.put("Moat",10);
        card_quantity.put("Village",10);
        card_quantity.put("Workshop",10);
        card_quantity.put("Woodcutter",10);
        card_quantity.put("Smithy",10);
        card_quantity.put("Remodel",10);
        card_quantity.put("Militia",10);
        card_quantity.put("Market",10);
        card_quantity.put("Mine",10);
    }

    public void minusCard(String name)
    {
           int card_left = card_quantity.get(name);
           card_left--;
           if(card_left == 0 && !name.equals("Duchy") && !name.equals("Province") && !name.equals("Estate"))
           {
               pileGone++;
           }
           card_quantity.put(name, card_left);

    }




    public int checkPrice(String cardName){


        return card_price.get(cardName);

    }

    public boolean checkQuantity(String card_name){

        if(card_quantity.get(card_name) != 0)
        {
            return true;
        }
        return false;

    }


    public int quantityLeft(String name){
        return card_quantity.get(name);
    }

    public boolean gameOver()
    {
        if(!checkQuantity("Province") && !checkQuantity("Estate") && !checkQuantity("Duchy") )
            return true;
        if(pileGone == 3)
        {
            return true;
        }
        return false;
    }



}
