package com.coin.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public abstract class BaseRecyclerAdapter extends RecyclerView.Adapter<BaseRecyclerAdapter.BaseViewHolder> {

    protected final String TAG = getClass().getSimpleName();

    @Override
    public final void onBindViewHolder(@NonNull BaseViewHolder baseViewHolder, int i) {
        baseViewHolder.bindData(i);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new BaseViewHolder(getItemView(viewGroup, i));
    }

    protected View getItemView(ViewGroup viewGroup, int itemType) {
        return LayoutInflater.from(
                viewGroup.getContext()).inflate(itemType, viewGroup, false);
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected final String TAG = getClass().getSimpleName();
        protected int position;

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindData(int position) {
            this.position = position;
        }

        @CallSuper
        @Override
        public void onClick(View v) {
            Log.d("TAG", "onClick on");
        }
    }
}
