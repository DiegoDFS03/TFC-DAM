package com.example.gamesaverx.gamesaverx.Screens;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.gamesaverx.R;
import com.example.gamesaverx.gamesaverx.Interfaces.IsSavedListener;
import com.example.gamesaverx.gamesaverx.client.RestClient;


public class DetailFragment extends Fragment {
    private Context context;
    private RestClient restClient;
    boolean buttonStatus;
    private ImageButton saved_button;
    private ImageView image;
    private Button buybutton;
    private TextView description,title,original_price,discount_price,genre,end_date,release_date,publisher,developer,url,discount_percentage;
    private String id;

    private static final String ARG_PARAM1 = "param1";


    public DetailFragment() {
        // Required empty public constructor
    }


    public static Fragment newInstance(String param1) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        image = view.findViewById(R.id.image);
        description = view.findViewById(R.id.desc);
        title = view.findViewById(R.id.title);
        original_price = view.findViewById(R.id.original_Price);
        discount_price = view.findViewById(R.id.discount_price);
        genre = view.findViewById(R.id.genre);
        end_date = view.findViewById(R.id.end_date);
        release_date = view.findViewById(R.id.release_date);
        publisher = view.findViewById(R.id.publisher);
        developer = view.findViewById(R.id.developer);
        url = view.findViewById(R.id.url);
        discount_percentage = view.findViewById(R.id.discount_percentage);

        saved_button = view.findViewById(R.id.addbutton);
        saved_button.setOnClickListener(savelistener);

        buybutton = view.findViewById(R.id.buybutton);
        buybutton.setOnClickListener(urllistener);


        context = getActivity().getApplicationContext();
        restClient = RestClient.getInstance(context);
        restClient.offer(id,context,title,description,image,url,original_price,discount_price,genre,release_date,developer,publisher,discount_percentage,end_date);


        restClient.isFavourite(id, new IsSavedListener() {
            @Override
            public void onResponseReceived(boolean isSaved) {
                if (isSaved == true){
                    saved_button.setBackgroundResource(R.drawable.ic_full_saved);
                    buttonStatus = true;
                }else {
                    saved_button.setBackgroundResource(R.drawable.ic_save);
                    buttonStatus = false;
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
    private View.OnClickListener savelistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (buttonStatus == true){
                buttonStatus = false;
                saved_button.setEnabled(false); //DESHABILITA EL BOTÓN
                restClient.deleteFavorites(id, saved_button);
                //ELIMINAR FAVORITOS
            }else{
                buttonStatus = true;
                saved_button.setEnabled(false); //DESHABILITA EL BOTÓN
                restClient.addFavorites(id, saved_button);
                //AÑADIR FAVORITOS
            }
        }
    };
    private View.OnClickListener urllistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url.getText().toString()));
                    startActivity(intent);


        }

    };

}
