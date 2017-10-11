package com.example.aharoldk.moviedicoding2.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.aharoldk.moviedicoding2.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageFragment extends Fragment implements View.OnClickListener{
    @BindView(R.id.switch1) Switch aSwitch1;
    @BindView(R.id.switch2) Switch aSwitch2;
    @BindView(R.id.llLayout) LinearLayout linearLayout;

    public ManageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, container, false);

        declarate(view);

        return view;
    }

    private void declarate(View view) {

        ButterKnife.bind(this, view);

        linearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.equals(linearLayout)){
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
    }
}
