package com.oleg_kuzmenkov.android.nrgtesttask.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oleg_kuzmenkov.android.nrgtesttask.R;
import com.oleg_kuzmenkov.android.nrgtesttask.model.News;
import com.squareup.picasso.Picasso;

public class NewsActivity extends AppCompatActivity {

    private static final String INTENT_CONTENT = "INTENT_CONTENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        News news = (News) getIntent().getSerializableExtra(INTENT_CONTENT);
        displayNews(news);
    }

    /**
     * Display news data on the screen
     */
    private void displayNews(News news) {
        if (news != null) {

            TextView newsId = findViewById(R.id.news_id);
            newsId.setText(String.format("News # %d", news.getId()));

            ImageView newsImage = findViewById(R.id.news_image);
            ViewGroup.LayoutParams params =  newsImage.getLayoutParams();
            int targetWidth = params.width;
            int targetHeight = params.height;
            Picasso.get().load(news.getUrlToImage()).resize(targetWidth,targetHeight)
                    .into(newsImage);

            TextView newsTitle = findViewById(R.id.news_title);
            newsTitle.setText(news.getTitle());

            TextView newsDescription = findViewById(R.id.news_description);
            newsDescription.setText(news.getDescription());

            TextView newsSource = findViewById(R.id.news_source);
            newsSource.setText(news.getSource().getName());

            TextView newsPublicationTime = findViewById(R.id.news_publication_time);
            newsPublicationTime.setText(String.format("published at: %s", news.getPublishedAt()));

            TextView newsUrl = findViewById(R.id.news_url);
            newsUrl.setText(news.getUrl());
        }
    }
}
