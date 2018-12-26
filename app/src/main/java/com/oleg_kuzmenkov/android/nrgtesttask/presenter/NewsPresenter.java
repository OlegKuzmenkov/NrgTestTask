package com.oleg_kuzmenkov.android.nrgtesttask.presenter;

import com.oleg_kuzmenkov.android.nrgtesttask.model.News;
import com.oleg_kuzmenkov.android.nrgtesttask.model.Repository;
import com.oleg_kuzmenkov.android.nrgtesttask.view.NewsListView;

import java.util.List;

public class NewsPresenter implements Repository.ReadNewsCallback {

    private NewsListView mNewsView;
    private Repository mRepository;

    public NewsPresenter() {
    }

    public void setView(NewsListView newsView) {
        mNewsView = newsView;
    }

    public void setRepository(Repository repository) {
        mRepository = repository;
    }

    /**
     * Detach presenter with view and repository
     */
    public void detach() {
        mNewsView = null;
        mRepository = null;
    }

    /**
     * Get news list from Model
     */
    public void getNews() {
        mRepository.getNewsList(this);
    }

    /**
     * This is callback from Model. Show list of available news
     */
    @Override
    public void onFinished(List<News> list) {
        mNewsView.displayNews(list);
    }
}
