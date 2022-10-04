package com.moutamid.streaming_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;

import java.util.ArrayList;

public class Fav_Radio extends Fragment {

    private RecyclerView fav_recycler;
    private Adapter_Fav_Radio adapter_fav;
    ArrayList<Model_Radio> our_arraylist;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Fav_Radio() {
        // Required empty public constructor
    }

    public static Fav_Radio newInstance(String param1, String param2) {
        Fav_Radio fragment = new Fav_Radio();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav__radio, container, false);
        our_arraylist = Stash.getArrayList("name_of_radio", Model_Radio.class);
        fav_recycler = view.findViewById(R.id.recyclerView_favRadio);
        loadFav();
        return view;
    }



    private void loadFav() {
        adapter_fav = new Adapter_Fav_Radio(getContext() , our_arraylist);
        fav_recycler.setAdapter(adapter_fav);
    }
}