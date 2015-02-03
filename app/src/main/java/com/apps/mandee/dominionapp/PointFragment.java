package com.apps.mandee.dominionapp;



import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Mandee on 1/14/2015.
 */
public class PointFragment extends Fragment {

    private View rootView;
    private Cards card;

    public PointFragment(Cards card){
        this.card = card;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_point, container, false);
        // Set quantity at start
        updateQuantity();

        return rootView;
    }
    // update when users buy
    public void updateQuantity(){
        Button estateBtn = (Button) rootView.findViewById(R.id.estate_card);
        estateBtn.setText("Estate " + "(" + card.checkPrice("Estate") + ")\n" + card.quantityLeft("Estate"));

        Button duchybtn = (Button) rootView.findViewById(R.id.duchy_card);
        duchybtn.setText("Duchy " + "(" + card.checkPrice("Duchy") + ")\n" + card.quantityLeft("Duchy"));

        Button provinceBtn = (Button) rootView.findViewById(R.id.province_card);
        provinceBtn.setText("Province " + "(" + card.checkPrice("Province") + ")\n" + card.quantityLeft("Province"));

        Button curseBtn = (Button) rootView.findViewById(R.id.curse_card);
        curseBtn.setText("Curse " + "(" + card.checkPrice("Curse") + ")\n" + card.quantityLeft("Curse"));
    }

}
