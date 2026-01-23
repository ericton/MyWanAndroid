package com.example.myapplication

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class RecycleViewActivity :FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        val rv=findViewById<RecyclerView>(R.id.rv)
        rv.layoutManager= StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        val list= mutableListOf("1","2","3","4","5","6")
        rv.adapter = MAdapter(list)

    }

    inner class MAdapter(val d:MutableList<String>):BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_string,d){
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.setText(R.id.tv_string,item)
            if(getItemPosition(item)==0){
                holder.itemView.layoutParams=ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150)
            }
        }

    }

}