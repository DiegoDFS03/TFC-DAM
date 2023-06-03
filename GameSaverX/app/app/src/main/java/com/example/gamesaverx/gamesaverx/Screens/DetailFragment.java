package com.example.gamesaverx.gamesaverx.Screens;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gamesaverx.R;
import com.example.gamesaverx.gamesaverx.client.RestClient;

import org.w3c.dom.Text;


public class DetailFragment extends Fragment {
    private Context context;
    private RestClient restClient;
    private boolean buttonStatus;
    private ImageButton saved_button;
    private ImageView image;
    private Button buybutton;
    private TextView description,title,original_price,discount_price,genre,end_date,release_date,publisher;
    String id;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }
    public void OnViewCreated(View view, @Nullable Bundle savedInstanceState){
        image.findViewById(R.id.image);
        description.findViewById(R.id.desc);
        title.findViewById(R.id.title);
        original_price.findViewById(R.id.original_Price);
        discount_price.findViewById(R.id.discount_price);
        genre.findViewById(R.id.genre);
        end_date.findViewById(R.id.end_date);
        release_date.findViewById(R.id.release_date);
        publisher.findViewById(R.id.publisher);


        context = getActivity().getApplicationContext();
        restClient = RestClient.getInstance(context);
        restClient.offer();

        saved_button.findViewById(R.id.addbutton);
        saved_button.setOnClickListener(/*savelistener)*/;

        buybutton.findViewById(R.id.buybutton);
        buybutton.setOnClickListener();

    }

}