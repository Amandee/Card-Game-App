package com.apps.mandee.dominionapp;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Mandee on 1/14/2015.
 */
public class CoinFragment extends Fragment {

    private Cards card;


    public CoinFragment(Cards card){
        this.card =card;
    }
    private View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_coin, container, false);
        updateQuantity();
        return rootView;
    }
    public void updateQuantity(){
        Button copperBtn = (Button) rootView.findViewById(R.id.copper_card);
        copperBtn.setText("Copper " + "(" + card.checkPrice("Copper") + ")\n" + card.quantityLeft("Copper"));

        Button silverBtn = (Button) rootView.findViewById(R.id.silver_card);
        silverBtn.setText("Silver " + "(" + card.checkPrice("Silver") + ")\n" + card.quantityLeft("Silver"));

        Button goldBtn = (Button) rootView.findViewById(R.id.gold_card);
        goldBtn.setText("Gold " + "(" + card.checkPrice("Gold") + ")\n" + card.quantityLeft("Gold"));
    }

}
