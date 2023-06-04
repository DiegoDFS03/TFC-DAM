package com.example.gamesaverx.gamesaverx.Screens;

import android.content.Context;
import android.os.Bundle;

import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gamesaverx.R;
import com.example.gamesaverx.gamesaverx.Interfaces.OnOfferClickListener;
import com.example.gamesaverx.gamesaverx.Interfaces.ResponseListener;
import com.example.gamesaverx.gamesaverx.Utils.Offer;
import com.example.gamesaverx.gamesaverx.client.RestClient;


public class Saved extends Fragment implements OnOfferClickListener, ResponseListener {

    private Context context;
    private RecyclerView save_recycler;

    private RestClient restClient;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved, container, false);
        context = getActivity().getApplicationContext();

        save_recycler = view.findViewById(R.id.recycler_save);

        peticion();
        return view;
    }

    private void peticion() {
        restClient = RestClient.getInstance(context);
        restClient.saved(save_recycler,this);
    }

    @Override
    public void itemClick(Offer offer) {
        getActivity().getFragmentManager().beginTransaction().replace(R.id.home_fragment, DetailFragment.newInstance(offer.getId())).commit();
    }

    @Override
    public void onOffersResponse(int count) {

    }
}