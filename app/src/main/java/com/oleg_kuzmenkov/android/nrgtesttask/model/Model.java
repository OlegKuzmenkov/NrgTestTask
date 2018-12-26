package com.oleg_kuzmenkov.android.nrgtesttask.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.oleg_kuzmenkov.android.nrgtesttask.view.NewsListActivity;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

public class Model implements Repository {
    private static final String NEWS_URL = "https://newsapi.org/v2/";
    private static final String PHOTOS_URL = "https://api.unsplash.com/";
    private static Model sRepository;

    private List<News> mNewsList;

    public static Model get() {
        if (sRepository == null) {
            sRepository = new Model();
        }

        return sRepository;
    }

    private Model() {
    }

    /**
     * Get news list
     */
    @Override
    public void getNewsList(final ReadNewsCallback listener) {
        if (mNewsList == null) {
            Log.i(NewsListActivity.LOG_TAG, "Start downloading list news");
            downloadNews(listener);
        } else {
            Log.i(NewsListActivity.LOG_TAG, "News list is exist. Loading is not started.");
            listener.onFinished(mNewsList);
        }
    }

    /**
     * Setup API's request interface
     */
    private NewsApi setupApiRequest(String url) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build().create(NewsApi.class);
    }

    /**
     * Download news list from the newsapi.org and pictures from api.unsplash.com
     */
    private void downloadNews(final ReadNewsCallback listener) {
        NewsApi newsRequestInterface = setupApiRequest(NEWS_URL);
        NewsApi photosRequestInterface = setupApiRequest(PHOTOS_URL);

        Observable<NewsList> newsList = newsRequestInterface.getNews("ru",
                "cbf087cac2c449ada1880fdf9a4587ab");

        Observable<List<JsonObject>> photoList = photosRequestInterface.getRandomPhotos(20,
                "81ca51653512bbe4edab6b6f8197c8eb61f7e57a3e233095e0a11be9032c1896");

        Observable.zip(newsList, photoList, new BiFunction<NewsList, List<JsonObject>, NewsList>() {
            @Override
            public NewsList apply(NewsList newsList, List<JsonObject> photoList)  {
                int index = 0;

                for (JsonObject photoObject : photoList) {
                    try {
                        JSONObject photo = new JSONObject(photoObject.toString());
                        JSONObject urls = photo.getJSONObject("urls");
                        newsList.getNews().get(index).setUrlToImage(urls.getString("small"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    index++;
                }

                return newsList;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsList>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(NewsListActivity.LOG_TAG, "onSubscribe!");
                    }

                    @Override
                    public void onNext(NewsList newsList) {
                        Log.i(NewsListActivity.LOG_TAG, "onNext!");
                        mNewsList = newsList.getNews();
                        listener.onFinished(mNewsList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(NewsListActivity.LOG_TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(NewsListActivity.LOG_TAG, "onComplete!");
                    }
                });
    }
}
