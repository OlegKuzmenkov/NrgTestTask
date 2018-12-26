package com.oleg_kuzmenkov.android.nrgtesttask.view;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.oleg_kuzmenkov.android.nrgtesttask.R;
import com.oleg_kuzmenkov.android.nrgtesttask.model.News;

public class NewsListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final String INTENT_CONTENT = "INTENT_CONTENT";

    private TextView mNewsId;
    private TextView mNewsTitle;
    private TextView mNewsDescription;
    private TextView mNewsSource;

    private News mNews;

    NewsListHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mNewsId = itemView.findViewById(R.id.news_id);
        mNewsTitle = itemView.findViewById(R.id.news_title);
        mNewsDescription = itemView.findViewById(R.id.news_description);
        mNewsSource = itemView.findViewById(R.id.news_source);
    }

    /**
     * Bind news
     */
    void bindNews(News news, int position) {
        mNews = news;
        // set news number according to the position in RecyclerView
        mNews.setId(++position);

        mNewsId.setText(String.format("News # %d", position));
        mNewsTitle.setText(mNews.getTitle());
        mNewsDescription.setText(mNews.getDescription());
        mNewsSource.setText(mNews.getSource().getName());
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), NewsActivity.class);
        intent.putExtra(INTENT_CONTENT, mNews);
        view.getContext().startActivity(intent);
    }
}
