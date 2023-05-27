package com.example.konekq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BottomSheetMenuAdapter extends RecyclerView.Adapter<BottomSheetMenuAdapter.ViewHolder> {
    public static class Option{
        public Option(int drawable, String text) {
            this.drawable = drawable;
            this.text = text;
        }

        private int drawable;
        private String text;

        public int getDrawable() {
            return drawable;
        }

        public void setDrawable(int drawable) {
            this.drawable = drawable;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
    public static interface ItemClickedListener{
        void onClick(Option option, int position);
    }
    ArrayList<Option> options;
    Context context;
    ItemClickedListener itemClickedListener;

    public BottomSheetMenuAdapter(ArrayList<Option> options, Context context, ItemClickedListener itemClickedListener) {
        this.options = options;
        this.context = context;
        this.itemClickedListener = itemClickedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.comment_bottom_sheet_option,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int index = position;
        Option option = options.get(index);
        holder.textView.setText(option.getText());
        holder.imageView.setImageDrawable(context.getDrawable(option.getDrawable()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickedListener != null)itemClickedListener.onClick(option, index);
            }
        });
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
