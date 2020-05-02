package com.ericton.wanandroid.ui.adapter;

import android.text.Html;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ericton.wanandroid.R;
import com.ericton.wanandroid.bean.hierarchy.KnowledgeHierarchyData;
import com.ericton.wanandroid.bean.main.collect.FeedArticleData;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Tang.
 * Date: 2020-04-24
 */
public class KnowledgeMainAdapter extends BaseQuickAdapter<KnowledgeHierarchyData,BaseViewHolder> {


    public KnowledgeMainAdapter( @Nullable List<KnowledgeHierarchyData> data) {
        super(R.layout.item_knowledge_hierarchy, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, KnowledgeHierarchyData item) {
        if(item.getName() == null) {
            return;
        }
        helper.setText(R.id.item_knowledge_hierarchy_title, item.getName());
        if (item.getChildren() == null) {
            return;
        }
        StringBuilder content = new StringBuilder();
        for (KnowledgeHierarchyData data: item.getChildren()) {
            content.append(data.getName()).append("   ");
        }
        helper.setText(R.id.item_knowledge_hierarchy_content, content.toString());
    }
}
