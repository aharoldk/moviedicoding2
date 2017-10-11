package com.example.aharoldk.moviedicoding2.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aharoldk.moviedicoding2.BuildConfig;
import com.example.aharoldk.moviedicoding2.DetailActivity;
import com.example.aharoldk.moviedicoding2.R;
import com.example.aharoldk.moviedicoding2.adapter.DetailClickListener;
import com.example.aharoldk.moviedicoding2.adapter.MovieAdapter;
import com.example.aharoldk.moviedicoding2.api.APIClient;
import com.example.aharoldk.moviedicoding2.api.APIInterface;
import com.example.aharoldk.moviedicoding2.model.Movie;
import com.example.aharoldk.moviedicoding2.model.ResultsItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements DetailClickListener, View.OnClickListener {
    @BindView(R.id.etSearch) EditText etSearch;
    @BindView(R.id.btnCari) Button btnSearch;
    @BindView(R.id.rvMain) RecyclerView rvMain;

    private List<ResultsItem> list = new ArrayList<>();
    private MovieAdapter mAdapter;

    String search;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        search = etSearch.getText().toString();
        outState.putString("search", search);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            // Restore last state for checked position.
            search = savedInstanceState.getString("search");
            parteeeehRetrofit(search);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        declarate(view);

        return view;
    }

    private void declarate(View view) {
        ButterKnife.bind(this, view);

        mAdapter = new MovieAdapter(list);

        rvMain.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvMain.setHasFixedSize(true);
        rvMain.setNestedScrollingEnabled(false);
        rvMain.setAdapter(mAdapter);

        mAdapter.setItemClickListener(this);

        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onItemDetailClicked(String idMovie) {
        Intent intentDetailActivity = new Intent(getActivity(), DetailActivity.class);
        intentDetailActivity.putExtra(DetailActivity.ID_MOVIE, idMovie);

        startActivity(intentDetailActivity);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCari:
                checkSearchEditText();
                break;

        }
    }

    private void checkSearchEditText() {
        search = etSearch.getText().toString();

        if(!TextUtils.isEmpty(search)){
            parteeeehRetrofit(search);
        } else {
            Toast.makeText(getContext(), "Please Fill Search", Toast.LENGTH_SHORT).show();
        }
    }

    private void parteeeehRetrofit(String search) {
        APIInterface apiInterface = APIClient.getApiClient().create(APIInterface.class);

        Observable<Movie> call = apiInterface.getSearchMovie(BuildConfig.API_KEY, BuildConfig.LANG, search);

        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movie>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Movie movie) {
                        list = movie.getResults();
                        mAdapter.setData(list);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
