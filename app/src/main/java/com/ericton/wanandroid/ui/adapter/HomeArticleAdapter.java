package com.ericton.wanandroid.ui.adapter;

import android.text.Html;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ericton.wanandroid.R;
import com.ericton.wanandroid.bean.main.collect.FeedArticleData;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Tang.
 * Date: 2020-04-24
 */
public class HomeArticleAdapter extends BaseQuickAdapter<FeedArticleData,BaseViewHolder> {


    public HomeArticleAdapter(@Nullable List data) {
        super(R.layout.item_search_pager,data);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder helper, FeedArticleData article) {
        if (!TextUtils.isEmpty(article.getTitle())) {
            helper.setText(R.id.item_search_pager_title, Html.fromHtml(article.getTitle()));
        }
        if (article.isCollect()) {
            helper.setImageResource(R.id.item_search_pager_like_iv, R.drawable.icon_like);
        } else {
            helper.setImageResource(R.id.item_search_pager_like_iv, R.drawable.icon_like_article_not_selected);
        }
        if (!TextUtils.isEmpty(article.getAuthor())) {
            helper.setText(R.id.item_search_pager_author, article.getAuthor());
        }
        if (!TextUtils.isEmpty(article.getChapterName())) {
            String classifyName = article.getSuperChapterName() + " / " + article.getChapterName();
                helper.setText(R.id.item_search_pager_chapterName, classifyName);
        }
        if (!TextUtils.isEmpty(article.getNiceDate())) {
            helper.setText(R.id.item_search_pager_niceDate, article.getNiceDate());
        }
        helper.addOnClickListener(R.id.item_search_pager_chapterName);
        helper.addOnClickListener(R.id.item_search_pager_like_iv);
        helper.addOnClickListener(R.id.item_search_pager_tag_red_tv);
    }
}
