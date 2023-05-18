package com.example.konekq;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomAlertDialog extends Dialog {
    public interface OkayBtnClickListener{
        void onClick(CustomAlertDialog dialog);
    }
    public interface CancelBtnClickListener{
        void onClick(CustomAlertDialog dialog);
    }

    public static final int DEFAULT_DRAWABLE = R.drawable.notify;
    public static final int SUCCESS_DRAWABLE = R.drawable.undraw_winners_re_wr1l;
    public static final int ERROR_DRAWABLE = R.drawable.bug_fixing;
    public static final int WARNING_DRAWABLE = R.drawable.notify;

    ImageView imageView;
    Button btnOkay, btnCancel;
    TextView textviewTitle,textViewMessage,textViewErrorCode;
    View rootView;

    private OkayBtnClickListener okayBtnClickListener;
    private CancelBtnClickListener cancelBtnClickListener;

    public CustomAlertDialog(@NonNull Context context) {
        super(context);
        rootView = LayoutInflater.from(context).inflate(R.layout.custom_alert_dialog_layout,null,false);
        setContentView(rootView);

        imageView = rootView.findViewById(R.id.imageView);
        btnOkay = rootView.findViewById(R.id.btn_okay);
        btnCancel = rootView.findViewById(R.id.btn_cancel);
        textViewMessage = rootView.findViewById(R.id.textview_message);
        textviewTitle = rootView.findViewById(R.id.textview_title);
        textViewErrorCode = rootView.findViewById(R.id.textview_error_code);

        textviewTitle.setText("");
        textViewMessage.setText("");

        btnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnOkayBtnClicked();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnCancelBtnClicked();
            }
        });

    }

    private void OnCancelBtnClicked() {
        if(cancelBtnClickListener != null){
            cancelBtnClickListener.onClick(this);
        }else{
            this.dismiss();
        }
    }

    private void OnOkayBtnClicked() {
        if(okayBtnClickListener != null){
            okayBtnClickListener.onClick(this);
        }else{
            this.dismiss();
        }
    }

    public CustomAlertDialog setMessage(String message){
        textViewMessage.setText(message);
        return this;
    }
    public CustomAlertDialog setTitle(String title){
        textviewTitle.setText(title);
        return this;
    }

    public CustomAlertDialog setDrawable(int drawableId){
        imageView.setImageResource(drawableId);
        return this;
    }

    public void showSuccess(){

        imageView.setImageResource(SUCCESS_DRAWABLE);
        this.show();
    }

    public void showError(String errorCode){
        textviewTitle.setText("Oh snap!");
        textViewMessage.setText("Something went wrong please try again!!");
        textViewErrorCode.setText("Error code " + errorCode);
        imageView.setImageResource(ERROR_DRAWABLE);
        textViewErrorCode.setVisibility(View.VISIBLE);
        this.show();
    }

    public void showError(){

        imageView.setImageResource(ERROR_DRAWABLE);
        this.show();
    }

    public void showSimple(){
        imageView.setImageResource(DEFAULT_DRAWABLE);
        this.show();
    }

    public CustomAlertDialog setOkayBtnClickListener(OkayBtnClickListener okayBtnClickListener) {
        this.okayBtnClickListener = okayBtnClickListener;
        return this;
    }

    public CustomAlertDialog setCancelBtnClickListener(CancelBtnClickListener cancelBtnClickListener) {
        this.cancelBtnClickListener = cancelBtnClickListener;
        btnCancel.setVisibility(View.VISIBLE);
        return this;
    }

    public CustomAlertDialog setOkayBtn(String text, OkayBtnClickListener listener){
        btnOkay.setText(text);
        this.okayBtnClickListener = listener;
        return this;
    }

    public CustomAlertDialog setCancelBtn(String text, CancelBtnClickListener listener){
        btnCancel.setText(text);
        this.cancelBtnClickListener = listener;
        btnCancel.setVisibility(View.VISIBLE);
        return this;
    }
}
