package com.example.gamesaverx.gamesaverx.Screens;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import android.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gamesaverx.R;
import com.example.gamesaverx.gamesaverx.Interfaces.OnOfferClickListener;
import com.example.gamesaverx.gamesaverx.Interfaces.ResponseListener;
import com.example.gamesaverx.gamesaverx.Utils.Offer;
import com.example.gamesaverx.gamesaverx.Utils.RecyclerAdapter;
import com.example.gamesaverx.gamesaverx.client.RestClient;

import java.util.List;


public class Home extends Fragment implements OnOfferClickListener, ResponseListener,SearchView.OnQueryTextListener {
    private SearchView searchView;
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private RestClient restClient;
    private List<Offer> items;
    private Context context;
    private int size = 20;
    private int offset = 0;
    private String query= "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);

        recyclerView = view.findViewById(R.id.recyclerView);

        context = getActivity().getApplicationContext();

        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);

        recyclerAdapter = new RecyclerAdapter(items,this);
        recyclerView.setAdapter(recyclerAdapter);

        peticion(query);

        return view;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    // Método que llama cada vez que se escribe o borra un caracter
    @Override
    public boolean onQueryTextChange(String query) {
        offset = 0;
        this.query = query;
        peticion(query);
        return false;
    }

    @Override
    public void itemClick(Offer offer) {

    }

    private void peticion(String query) {
        restClient = RestClient.getInstance(context);
        restClient.offers(query, size, offset,this, recyclerView, this);
    }

    @Override
    public void onOffersResponse(int count) {

    }
}