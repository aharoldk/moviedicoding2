package com.example.aharoldk.moviedicoding2.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class UpcomingFragment extends Fragment implements DetailClickListener{

    @BindView(R.id.rvMain) RecyclerView rvMain;

    private List<ResultsItem> list = new ArrayList<>();
    private MovieAdapter mAdapter;

    public UpcomingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);

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

        parteeeehRetrofit();
    }

    private void parteeeehRetrofit() {
        APIInterface apiInterface = APIClient.getApiClient().create(APIInterface.class);

        Observable<Movie> call = apiInterface.getUpcomingMovie(BuildConfig.API_KEY, BuildConfig.LANG);

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

    @Override
    public void onItemDetailClicked(String idMovie) {
        Intent intentDetailActivity = new Intent(getActivity(), DetailActivity.class);
        intentDetailActivity.putExtra(DetailActivity.ID_MOVIE, idMovie);

        startActivity(intentDetailActivity);
    }
}
