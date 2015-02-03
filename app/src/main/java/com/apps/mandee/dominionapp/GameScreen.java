package com.apps.mandee.dominionapp;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import android.support.v4.app.FragmentActivity;
import android.widget.Toast;



import java.util.ArrayList;

/**
 * Created by Mandee on 1/9/2015.
 */
public class GameScreen extends FragmentActivity{


    private ViewPager viewPager;
    private TabsPagerAdapter tabAdapter; // Adapters for the fragment
    private ArrayList<String> cards_On_Hand; // Card currently held by users
    private TextView cardClicked; // card currently clicked by user
    private TextView cardDescription; // card description
    private Button actionButton; // action button for phases
    private Button phaseButton; // phase button
    private boolean onBuySupply; // See if user clicked supply card
    private Cards card; // Card class
    private boolean onCard;
    private TextView hand, num_coins, num_actions, num_buys, num_deck;
    private CoinFragment coin; // Fragments to update quantity
    private KingdomFragment kingdom;
    private PointFragment point;
    private HorizontalListView listview;
    private int indexOfUser, cellarAdd;
    private ArrayList<UsersHand> users;
    private TextView handTitle;
    private String trashedCard; // for remodel
    private TextView detailPlays; // Play details
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        String players_string= getIntent().getStringExtra("players");
        int players = Integer.parseInt(players_string);

        // Hand view
        card = new Cards();
        card.addCardsAndQuantity(players);

        listview = (HorizontalListView) findViewById(R.id.listview);
        listview.setAdapter(mAdapter);
        handTitle = (TextView) findViewById(R.id.hand_title);

        // Cards view (Title)
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabAdapter = new TabsPagerAdapter(getSupportFragmentManager(), card);
        viewPager.setAdapter(tabAdapter);
        viewPager.setCurrentItem(1);


        // Set fragments

        coin = (CoinFragment) tabAdapter.getItem(0);
        point = (PointFragment) tabAdapter.getItem(1);
        kingdom = (KingdomFragment) tabAdapter.getItem(2);

        users = new ArrayList<>();
        for(int i = 0; i<players; i++)
        {
            users.add(new UsersHand(this,card));
        }
        indexOfUser = 0;
        //Create hand
        
        users.get(indexOfUser).drawFive();
        cards_On_Hand = users.get(indexOfUser).returnHand();

        // Variables

        // Cellar
        cellarAdd = 0;

        // Card clicked and description variable
        cardClicked = (TextView) findViewById(R.id.card_selected);
        cardDescription = (TextView) findViewById(R.id.card_description_selected);
        detailPlays = (TextView) findViewById(R.id.detail_plays);

        // Action//Phase button variable
        actionButton = (Button) findViewById(R.id.actionBtn);
        phaseButton = (Button) findViewById(R.id.btnPhase);

        // ActionBuyCoins variables
        num_coins = (TextView) findViewById(R.id.num_coins);
        num_actions = (TextView) findViewById(R.id.num_actions);
        num_buys = (TextView) findViewById(R.id.num_buys);
        num_deck = (TextView) findViewById(R.id.num_in_deck);

        // Set boolean false
        onBuySupply = false;
        onCard = false;

        updateABCD();
    }



    // Adapter for the hand view
    private BaseAdapter mAdapter = new BaseAdapter() {

        private View.OnClickListener handCardClicked = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String cardSelected = ((TextView)v).getText().toString();
                cardClicked.setText(cardSelected);
                setCardDescription(cardSelected);

                detailPlays = (TextView) findViewById(R.id.detail_plays);
                onBuySupply = false;
                onCard = true;



            }
        };

        @Override
        public int getCount() {
            return cards_On_Hand.size();
        }

        @Override
        public Object getItem(int position) {
            return hand;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewhand, null);
            hand = (TextView) retval.findViewById(R.id.hand);
            hand.setOnClickListener(handCardClicked);
            hand.setText(cards_On_Hand.get(position).toString());
            coin = (CoinFragment) tabAdapter.getItem(0);
            point = (PointFragment) tabAdapter.getItem(1);
            kingdom = (KingdomFragment) tabAdapter.getItem(2);
            return retval;
         }





    };

    // Each button the the coin/point/kingdom fragments go to this to update currently selected
    // and to update description

    public void goToCurrentlySelected(View view) {

        String cardSelected = (((TextView)view).getText().toString());
        cardSelected = cardSelected.substring(0, cardSelected.indexOf(" ")); // Cut the string to first word
        cardClicked.setText(cardSelected);
        setCardDescription(cardSelected);
        onBuySupply = true;
        onCard = false;

    }







    public void changePhase(View view) {

        String textOfButton = (((TextView)view).getText().toString());
        listview.setVisibility(View.VISIBLE);
        switch(textOfButton){
            case "Done":
                alertDialogBuilder = new AlertDialog.Builder(
                        this);

                // set title
                alertDialogBuilder.setTitle("Go Back to Play Phase");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure you're done?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                if(cellarAdd != 0)
                                {
                                    Log.e("Cellar", "cellar add is " + cellarAdd );
                                    users.get(indexOfUser).draw(cellarAdd);
                                    cards_On_Hand = users.get(indexOfUser).returnHand();
                                    listview.setAdapter(mAdapter);
                                    cellarAdd = 0;

                                }
                                actionButton.setText("Play");
                                phaseButton.setText("Go To \n Buy Phase");
                                updateABCD();
                                updateQuantity();



                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });
                alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                break;


            case "Go To \n Buy Phase":
                actionButton.setText("Buy");
                phaseButton.setText("Go To \n Clean Phase");
                break;
            case "Go To \n Clean Phase":


                alertDialogBuilder = new AlertDialog.Builder(
                        this);

                // set title
                alertDialogBuilder.setTitle("Next Player");
                listview.setVisibility(HorizontalListView.INVISIBLE);
                // set dialog message
                alertDialogBuilder
                        .setMessage("Move to next Player?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                users.get(indexOfUser).cleanUp();
                                actionButton.setText("Cleaning Up!");
                                phaseButton.setText("Next Player");
                                GameScreen.this.moveToNextPlayer();
                                listview.setVisibility(HorizontalListView.VISIBLE);


                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                                listview.setVisibility(HorizontalListView.VISIBLE);
                            }
                        });

                // create alert dialog
                alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();



        }


    }

    public void moveToNextPlayer()
    {
        if(indexOfUser == users.size()-1)
            indexOfUser = 0;
        else indexOfUser++;
        handTitle.setText("Your Hand (Player " + (indexOfUser + 1) + ")");
        users.get(indexOfUser).drawFive();
        cards_On_Hand = users.get(indexOfUser).returnHand();
        listview.setAdapter(mAdapter);
        actionButton.setText("Play");
        phaseButton.setText("Go To \n Buy Phase");
        updateABCD();

    }
    public void performAction(View view) {
        String cardClickedOn = cardClicked.getText().toString();
        if(actionButton.getText().toString().equals("Play"))
        {
            playCard();
        }
        else if(actionButton.getText().toString().equals("Buy"))
        {
            buyCard();
        }
        else if(actionButton.getText().toString().equals("Discard"))
        {
            if(users.get(indexOfUser).discard(cardClickedOn)) {

                Toast.makeText(getApplicationContext(), "Discarded " + cardClickedOn,
                        Toast.LENGTH_SHORT).show();
                cardClicked.setText("");
                cellarAdd++;
                detailPlays.append("Player " + (indexOfUser + 1) + " discarded " + cardClickedOn + "\n");
            }
        }
        // For workshop
        else if(actionButton.getText().toString().equals("Gain"))
        {
            if(users.get(indexOfUser).gain(cardClickedOn))
            {
                actionButton.setText("Play");
                phaseButton.setText("Go To \n Buy Phase");
                detailPlays.append("Player " + (indexOfUser + 1) + " gained " + cardClickedOn + "\n");
            }
        }
        // For remodel
        else if(actionButton.getText().toString().equals("Trash"))
        {
            if(!onCard)
            {
                Toast.makeText(getApplicationContext(), "Must select a card from hand", Toast.LENGTH_SHORT).show();
            }
            else if(users.get(indexOfUser).trash(cardClickedOn)){

                trashedCard = cardClicked.getText().toString();
                Toast.makeText(getApplicationContext(),"Trashed " + trashedCard
                        + "\n Choose a card up to " + (card.checkPrice(trashedCard) + 2) +".",Toast.LENGTH_LONG).show();
                cardClicked.setText("");
                actionButton.setText("Choose");
                detailPlays.append("Player " + (indexOfUser + 1) + " trashed " + cardClickedOn + "\n");
            }
        }
        else if(actionButton.getText().toString().equals("Choose"))
        {
            if(cardClicked.getText().toString().equals("")  || !onBuySupply)
            {
                Toast.makeText(getApplicationContext(), "Must select a card from supply", Toast.LENGTH_SHORT).show();

            }
            else
            {
                if(users.get(indexOfUser).remodel(trashedCard, cardClicked.getText().toString()))
                {
                    detailPlays.append("Player " + (indexOfUser + 1) + " gained " + cardClickedOn + "\n");
                    actionButton.setText("Play");
                    phaseButton.setText("Go To \n Buy Phase");
                }

            }
        }
        // For mine card
        else if (actionButton.getText().toString().equals("Trash Treasure"))
        {
            Log.e("onCard", "On card is :" + onCard);
            Log.e("cardClicked", "On card clicked is :" + !cardClickedOn.equals("Copper"));
            if(onCard && (cardClickedOn.equals("Copper") || cardClickedOn.equals("Silver") || cardClickedOn.equals("Gold")) && users.get(indexOfUser).trash(cardClicked.getText().toString()))
            {

                trashedCard = cardClicked.getText().toString();
                Toast.makeText(getApplicationContext(), "Trashed " + trashedCard
                        + "Choose a treasure card.", Toast.LENGTH_SHORT).show();
                cardClicked.setText("");
                actionButton.setText("Choose Treasure");
                detailPlays.append("Player " + (indexOfUser + 1) + " trashed treasure " + cardClickedOn + "\n");


            }
            else{

                Toast.makeText(getApplicationContext(), "Must select a treasure card from hand.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(actionButton.getText().toString().equals("Choose Treasure"))
        {
            if(!cardClicked.getText().toString().equals("") && onBuySupply && (cardClickedOn.equals("Copper")
                    || cardClickedOn.equals("Silver") || cardClickedOn.equals("Gold")))
            {
                if(users.get(indexOfUser).getTreasureCard(trashedCard, cardClicked.getText().toString()))
                {
                    detailPlays.append("Player " + (indexOfUser + 1) + " gained treasure " + cardClickedOn + "\n");
                    actionButton.setText("Play");
                    phaseButton.setText("Go To \n Buy Phase");
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Must select a treasure card from supply.", Toast.LENGTH_SHORT).show();
            }
        }
        if(card.gameOver())
        {
            findWinner();
        }
        cards_On_Hand = users.get(indexOfUser).returnHand();
        listview.setAdapter(mAdapter);
        if(cards_On_Hand.size() == 0)
        {
            listview.setVisibility(View.GONE);
        }
        else
        {
            listview.setVisibility(View.VISIBLE);
        }

        updateABCD();
        updateQuantity();
    }

    // Play currently selected card
    public void playCard(){

        String textOfCardClicked = cardClicked.getText().toString();
        // Checks its from hand
        if(onBuySupply || !users.get(indexOfUser).contains(textOfCardClicked))
        {
            Toast.makeText(getApplicationContext(), "Must select a card on hand", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check if its not a coin or point
        if(textOfCardClicked.equals("Copper") || textOfCardClicked.equals("Silver") ||textOfCardClicked.equals("Gold")
            ||textOfCardClicked.equals("Estate") || textOfCardClicked.equals("Duchy") || textOfCardClicked.equals("Province")
            || textOfCardClicked.equals("Curse"))
        {
            Toast.makeText(getApplicationContext(), "Can't play coins or points", Toast.LENGTH_SHORT).show();
            return;
        }

        String cardPlayed = users.get(indexOfUser).playCard(textOfCardClicked);
        if(!cardPlayed.equals(""))
            detailPlays.append("Player " + (indexOfUser + 1) + " played " + cardPlayed + "\n");
        switch(cardPlayed)
        {
            case "Cellar":
                cellarCard();
                break;
            case "Workshop":
                workshopCard();
                break;
            case "Remodel":
                remodelCard();
                break;
            case "Mine":
                mineCard();
                break;
        }
        cardClicked.setText("");
        updateABCD();
        cards_On_Hand = users.get(indexOfUser).returnHand();
        listview.setAdapter(mAdapter);



    }

    public void buyCard(){

        String textOfCardClicked = cardClicked.getText().toString();
        // Checks to make sure from
        if(onCard || textOfCardClicked.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Must select a card from supply", Toast.LENGTH_SHORT).show();
            return;
        }


        if(users.get(indexOfUser).buyCard(textOfCardClicked))
            detailPlays.append("Player " + (indexOfUser + 1) + " bought " + textOfCardClicked + "\n");
        if(card.gameOver())
        {
            findWinner();
        }
        updateQuantity();
        updateABCD();

    }

    public void updateQuantity(){
        coin.updateQuantity();
        point.updateQuantity();
        kingdom.updateQuantity();
    }

    public void updateABCD(){

        num_actions.setText("#Actions: " + users.get(indexOfUser).getActions() + "\t");
        num_buys.setText("#Buys: " + users.get(indexOfUser).getBuys());
        num_coins.setText("#Coins: " + users.get(indexOfUser).coinsToSpend());
        num_deck.setText("#Deck: " + users.get(indexOfUser).getDeck());

    }

    private void cellarCard()
    {
        Toast.makeText(getApplicationContext(), "Select cards one by one and discard", Toast.LENGTH_LONG).show();
        cardClicked.setText("");
        actionButton.setText("Discard");
        phaseButton.setText("Done");
    }

    private void remodelCard()
    {
        Toast.makeText(getApplicationContext(),"Trash a card", Toast.LENGTH_SHORT).show();
        cardClicked.setText("");
        actionButton.setText("Trash");
        phaseButton.setText("Done");
    }

    private void workshopCard()
    {
        Toast.makeText(getApplicationContext(),"Choose a card up to 4 coins", Toast.LENGTH_SHORT).show();
        cardClicked.setText("");
        actionButton.setText("Gain");
        phaseButton.setText("Done");
    }

    private void mineCard()
    {
        Toast.makeText(getApplicationContext(),"Trash a treasure card", Toast.LENGTH_SHORT).show();
        cardClicked.setText("");
        actionButton.setText("Trash Treasure");
        phaseButton.setText("Done");
    }

    // Updates the description of the currently selected card
    private void setCardDescription(String cardName) {

        switch (cardName) {

            case "Copper":
                cardDescription.setText("1 Coin");
                break;
            case "Silver":
                cardDescription.setText("2 Coin");
                break;
            case "Gold":
                cardDescription.setText("3 Coin");
                break;
            case "Estate":
                cardDescription.setText("1 Point");
                break;
            case "Duchy":
                cardDescription.setText("3 Point");
                break;
            case "Province":
                cardDescription.setText("6 Point");
                break;
            case "Curse":
                cardDescription.setText("-1 Point");
                break;
            case "Cellar":
                cardDescription.setText("+1 Action Discard any number of cards. +1 per card discarded");
                break;
            case "Moat": // Need to be able to reveal card
                cardDescription.setText("+2 Cards // Will add reaction part later");
                break;
            case "Village":
                cardDescription.setText("+1 Card +2 Actions");
                break;
            case "Workshop":
                cardDescription.setText("Gain a card costing up to 4 coins");
                break;
            case "Woodcutter":
                cardDescription.setText("+1 Buy +2 coins");
                break;
            case "Smithy":
                cardDescription.setText("+3 cards");
                break;
            case "Remodel":
                cardDescription.setText("Trash a card from your hand, gain a card up to 2 more coins " +
                        "than trashed card");
                break;
            case "Militia":
                cardDescription.setText("+2 coins // Working on attack soon");
                break;
            case "Market":
                cardDescription.setText("+1 card, +1 action, +1 buy, +1 coin");
                break;
            case "Mine":
                cardDescription.setText("Trash treasure from hand, gain treasure costing up to 3 more " +
                        "and put in hand");
                break;
        }
    }

    private void findWinner()
    {
        String scores = "";
        int points;
        int highestPoints = 0;
        int winner = -1;
        for(int i =0; i< users.size(); i++)
        {
            points = users.get(i).gameOver();
            scores += ("Player (" + (i+ 1) +") got " + points + "points. \n"  );
            if(highestPoints < points)
            {
                winner = i + 1;
                highestPoints = points;
            }
        }
        scores += ("The winner is Player (" + winner +")");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("The winner is Player (" + winner + ")");
        // set dialog message
        alertDialogBuilder
                .setMessage(scores + " \n Restart game?")
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        Intent sendToHome = new Intent(getApplicationContext(), MainActivity.class);

                        startActivity(sendToHome);

                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();


            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit the game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        finish();
                        //close();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                })
                .show();

    }
}
