package com.oleg_kuzmenkov.android.nrgtesttask.model;

import java.util.List;

public interface Repository {

    void getNewsList(ReadNewsCallback listener);

    interface ReadNewsCallback {
        void onFinished(List<News> list);
    }
}
