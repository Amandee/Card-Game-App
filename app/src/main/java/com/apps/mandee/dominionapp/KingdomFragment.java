package com.apps.mandee.dominionapp;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Mandee on 1/14/2015.
 */
public class KingdomFragment extends Fragment{

    private View rootView;
    private Cards card;
    public KingdomFragment(Cards card){
        this.card = card;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_kingdom, container, false);

        // Set quantity at start
        updateQuantity();
        return rootView;
    }
    // Update when users buy
    public void updateQuantity(){
        Button king1Btn = (Button) rootView.findViewById(R.id.kingdom_card_1);
        king1Btn.setText("Cellar \n" + "(" + card.checkPrice("Cellar") + ")"+ card.quantityLeft("Cellar"));

        Button king2Btn = (Button) rootView.findViewById(R.id.kingdom_card_2);
        king2Btn.setText("Moat \n" + "(" + card.checkPrice("Moat") + ")" + card.quantityLeft("Moat"));

        Button king3Btn = (Button) rootView.findViewById(R.id.kingdom_card_3);
        king3Btn.setText("Village \n" + "(" + card.checkPrice("Village") + ")" + card.quantityLeft("Village"));

        Button king4Btn = (Button) rootView.findViewById(R.id.kingdom_card_4);
        king4Btn.setText("Workshop \n" + "(" + card.checkPrice("Workshop") + ")" + card.quantityLeft("Workshop"));

        Button king5Btn = (Button) rootView.findViewById(R.id.kingdom_card_5);
        king5Btn.setText("Woodcutter \n" +  "(" + card.checkPrice("Woodcutter") + ")" +card.quantityLeft("Woodcutter"));

        Button king6Btn = (Button) rootView.findViewById(R.id.kingdom_card_6);
        king6Btn.setText("Smithy \n" + "(" + card.checkPrice("Smithy") + ")" + card.quantityLeft("Smithy"));

        Button king7Btn = (Button) rootView.findViewById(R.id.kingdom_card_7);
        king7Btn.setText("Remodel \n" + "(" + card.checkPrice("Remodel") + ")" + card.quantityLeft("Remodel"));

        Button king8Btn = (Button) rootView.findViewById(R.id.kingdom_card_8);
        king8Btn.setText("Militia \n" + "(" + card.checkPrice("Militia") + ")" + card.quantityLeft("Militia"));

        Button king9Btn = (Button) rootView.findViewById(R.id.kingdom_card_9);
        king9Btn.setText("Market \n" + "(" + card.checkPrice("Market") + ")" + card.quantityLeft("Market"));

        Button king10Btn = (Button) rootView.findViewById(R.id.kingdom_card_10);
        king10Btn.setText("Mine \n" + "(" + card.checkPrice("Mine") + ")" + card.quantityLeft("Mine"));
    }

}
