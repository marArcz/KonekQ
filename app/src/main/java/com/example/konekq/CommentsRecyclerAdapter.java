package com.example.konekq;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.konekq.BackendAPI.APIResponse;
import com.example.konekq.BackendAPI.RetrofitClient;
import com.example.konekq.Models.Comment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ItemViewHolder> {

    Context context;
    ArrayList<Comment> comments;

    public CommentsRecyclerAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_comment_item_layout,null,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.textViewName.setText(String.format("%s %s", comment.getUser().getFirstname(), comment.getUser().getLastname()));
        holder.textViewComment.setText(comment.getContent());

        Glide.with(context).load(comment.getUser().getProfile_photo()).into(holder.imageViewProfile);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(comment.getUser_id() == AppManager.getUser(context).getId()){
                    showBottomSheetDialog(comment);
                }
                return true;
            }
        });
    }

    private void showBottomSheetDialog(Comment comment) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.comments_bottom_sheet_layout,null,false);
        bottomSheetDialog.setContentView(view);
        ArrayList<BottomSheetMenuAdapter.Option> options = new ArrayList<>();
        options.add(new BottomSheetMenuAdapter.Option(R.drawable.baseline_edit_dark_12,"Edit Comment"));
        options.add(new BottomSheetMenuAdapter.Option(R.drawable.baseline_delete_24_dark,"Delete Comment"));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        BottomSheetMenuAdapter adapter = new BottomSheetMenuAdapter(options, context, new BottomSheetMenuAdapter.ItemClickedListener() {
            @Override
            public void onClick(BottomSheetMenuAdapter.Option option, int position) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        bottomSheetDialog.dismiss();
                        deleteComment(comment);
                        break;
                }
            }
        });

        recyclerView.setAdapter(adapter);

        bottomSheetDialog.show();

    }

    private void deleteComment(Comment comment) {
        String token = AppManager.getToken(context);
        Call<APIResponse> deleteCommentCall = RetrofitClient.getCommentService(token).deleteComment(comment.getId());
        String errorCode = ErrorCodes.DELETING_COMMENT;
        deleteCommentCall.enqueue(new Callback<APIResponse>() {
            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                if(response.body() == null){
                    Toast.makeText(context, "Server response is empty!", Toast.LENGTH_SHORT).show();
                }else{
                    if(!response.body().isSuccess()){
                        new CustomAlertDialog(context)
                                .setMessage(response.body().getMessage())
                                .showError();
                    }else{
                        int index = comments.indexOf(comment);
                        comments.remove(comment);
                        Toast.makeText(context, "Comment is deleted", Toast.LENGTH_SHORT).show();
                        CommentsRecyclerAdapter.this.notifyItemRemoved(index);
                    }
                }
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                new CustomAlertDialog(context)
                        .showError(errorCode);
                Log.d(errorCode,t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName,textViewLikes,textViewDateTime,textViewComment;
        ImageView imageViewProfile;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProfile = itemView.findViewById(R.id.imageView_profile);
            textViewName = itemView.findViewById(R.id.textView_name);
            textViewDateTime = itemView.findViewById(R.id.textview_date_time);
            textViewComment = itemView.findViewById(R.id.textView_comment);
        }
    }
}
