package com.example.aharoldk.moviedicoding2.utils.gcmanager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;

import com.example.aharoldk.moviedicoding2.DetailActivity;
import com.example.aharoldk.moviedicoding2.R;
import com.example.aharoldk.moviedicoding2.api.APIClient;
import com.example.aharoldk.moviedicoding2.api.APIInterface;
import com.example.aharoldk.moviedicoding2.model.Movie;
import com.example.aharoldk.moviedicoding2.model.ResultsItem;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SchedulerService extends GcmTaskService {
    static final String API_KEYS = "3ee47da55c8dae070eb764306712efc3";
    static final String LANG = "en-US";

    private List<ResultsItem> list = new ArrayList<>();

    public static String TAG_TASK_UPCOMING = "UpcomingTask";

    @Override
    public int onRunTask(TaskParams taskParams) {
        int result = 0;
        if (taskParams.getTag().equals(TAG_TASK_UPCOMING)){
            getCurrentWeather();
            result = GcmNetworkManager.RESULT_SUCCESS;
        }
        return result;
    }

    private void getCurrentWeather(){
        APIInterface apiInterface = APIClient.getApiClient().create(APIInterface.class);

        Observable<Movie> call = apiInterface.getUpcomingMovie(API_KEYS, LANG);

        call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movie>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Movie movie) {
                        list = movie.getResults();
                        showNotification(getApplicationContext(), String.valueOf(list.get(0).getId()),list.get(0).getTitle(), list.get(0).getOverview(), 100);

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();
        SchedulerTask mSchedulerTask = new SchedulerTask(this);
        mSchedulerTask.createPeriodicTask();
    }

    private void showNotification(Context context, String s, String title, String message, int notifId) {
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentText(message)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, android.R.color.black))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        Intent intentDetailActivity = new Intent(this, DetailActivity.class);
        intentDetailActivity.putExtra(DetailActivity.ID_MOVIE, s);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intentDetailActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(contentIntent);

        notificationManagerCompat.notify(notifId, builder.build());
    }

}
