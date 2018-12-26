package com.oleg_kuzmenkov.android.nrgtesttask.view;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.oleg_kuzmenkov.android.nrgtesttask.R;
import com.oleg_kuzmenkov.android.nrgtesttask.model.News;
import com.oleg_kuzmenkov.android.nrgtesttask.model.Model;
import com.oleg_kuzmenkov.android.nrgtesttask.presenter.NewsPresenter;

import java.util.List;

public class NewsListActivity extends AppCompatActivity implements NewsListView {
    public static final String LOG_TAG = "NEWS_LIST_ACTIVITY";

    private NewsPresenter mPresenter;

    private RecyclerView mRecycler;
    private NewsListAdapter mAdapter;
    private FloatingActionButton mFab;
    private TextView mNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        initControls();

        setupPresenter();
        mPresenter.getNews();
    }

    /**
     * Display news list
     */
    @Override
    public void displayNews(List<News> newsList) {
        mNotification.setText(R.string.news_loading_finished_notification);

        if (mAdapter == null) {
            mAdapter = new NewsListAdapter(this, newsList);
        }

        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }

    /**
     * Setup presenter
     */
    private void setupPresenter() {
        mPresenter = new NewsPresenter();
        mPresenter.setView(this);
        mPresenter.setRepository(Model.get());
    }

    /**
     * Initialize all controls
     */
    private void initControls() {
        mNotification = findViewById(R.id.loading_text_view);

        mFab = findViewById(R.id.floating_action_button);
        mFab.hide();

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecycler.smoothScrollToPosition(0);
            }
        });

        initRecyclerView();
    }

    /**
     * Initialize RecyclerView
     */
    private void initRecyclerView() {
        mRecycler = findViewById(R.id.news_recycler_view);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // scrolling up
                    mFab.show();
                } else {
                    // scrolling down
                    mFab.hide();
                }
            }
        });
    }
}

