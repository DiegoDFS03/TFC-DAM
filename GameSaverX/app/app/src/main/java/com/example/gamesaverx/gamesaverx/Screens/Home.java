package com.example.gamesaverx.gamesaverx.Screens;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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


public class Home extends Fragment implements OnOfferClickListener, ResponseListener {

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private RestClient restClient;
    private List<Offer> items;
    private Context context;


    public static Home newInstance() {
        Home fragment = new Home();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        recyclerView = view.findViewById(R.id.recyclerView);

        context = getActivity().getApplicationContext();

        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);

        recyclerAdapter = new RecyclerAdapter(items,this);
        recyclerView.setAdapter(recyclerAdapter);

        return view;
    }

    @Override
    public void itemClick(Offer offer) {

    }

    @Override
    public void onWinesResponse(int count) {

    }
}