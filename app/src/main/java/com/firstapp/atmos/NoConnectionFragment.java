package com.firstapp.atmos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NoConnectionFragment extends Fragment {

    public SwipeRefreshLayout swipeRefreshLayout;
    Context context;

    public NoConnectionFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_no_connection, container, false);
        swipeRefreshLayout = rootView.findViewById(R.id.id_swipe_refresh_layout);

        swipeToRefresh();
        return rootView;
    }


    public void swipeToRefresh(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFragmentManager().beginTransaction().remove(NoConnectionFragment.this).commit();
                getFragmentManager().popBackStack("noConn", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getActivity().finish();
                startActivity(new Intent(getActivity(), MainActivity.class));
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}