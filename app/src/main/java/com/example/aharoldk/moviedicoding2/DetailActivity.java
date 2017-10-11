package com.example.aharoldk.moviedicoding2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aharoldk.moviedicoding2.api.APIClient;
import com.example.aharoldk.moviedicoding2.api.APIInterface;
import com.example.aharoldk.moviedicoding2.model.Detail.Detail;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailActivity extends AppCompatActivity {

    public static final String ID_MOVIE = "idMovie";

    @BindView(R.id.ivPoster)
    ImageView ivPoster;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDate) TextView tvDate;
    @BindView(R.id.tvDesk) TextView tvDesk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        // remove the following flag for version < API 19
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
        setContentView(R.layout.activity_detail);


        String idMovie = getIntent().getStringExtra(ID_MOVIE);

        ButterKnife.bind(this);

        retrofit(idMovie);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void retrofit(String idMovie) {
        APIInterface apiInterface = APIClient.getApiClient().create(APIInterface.class);

        Observable<Detail> call = apiInterface.getDetailMovie(idMovie, BuildConfig.API_KEY, BuildConfig.LANG);

        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Detail>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Detail detail) {
                        Picasso.with(getApplicationContext())
                                .load("https://image.tmdb.org/t/p/w185"+detail.getPosterPath())
                                .into(ivPoster);

                        tvTitle.setText(String.valueOf(detail.getTitle()));
                        tvDate.setText(String.valueOf(detail.getReleaseDate()));
                        tvDesk.setText(String.valueOf(detail.getOverview()));
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
