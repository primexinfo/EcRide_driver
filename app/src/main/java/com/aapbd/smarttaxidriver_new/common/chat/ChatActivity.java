package com.aapbd.smarttaxidriver_new.common.chat;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aapbd.smarttaxidriver_new.common.AppConstant;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.aapbd.smarttaxidriver_new.MvpApplication;
import com.aapbd.smarttaxidriver_new.R;
import com.aapbd.smarttaxidriver_new.base.BaseActivity;
import com.aapbd.smarttaxidriver_new.data.network.APIClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ChatActivity extends BaseActivity {

    public static String sender = "provider";
    String chatPath = null;
    @BindView(R.id.chat_lv)
    ListView chatLv;
    @BindView(R.id.message)
    EditText message;
    @BindView(R.id.send)
    ImageView send;
    @BindView(R.id.chat_controls_layout)
    LinearLayout chatControlsLayout;
    private ChatMessageAdapter mAdapter;
    private CompositeDisposable mCompositeDisposable;
    private DatabaseReference myRef;
    @BindView(R.id.toolbar_back_button)
    AppCompatImageView toolbar_back_button;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;


    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        toolbar_back_button.setOnClickListener(v -> finish());
        toolbar_title.setText(getString(R.string.chat));

        mCompositeDisposable = new CompositeDisposable();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            chatPath = extras.getString(AppConstant.SharedPref.REQUEST_ID, null);
            initChatView(chatPath);
        }
    }

    private void initChatView(String chatPath) {
        if (chatPath == null) return;

        message.setOnEditorActionListener((v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String myText = message.getText().toString().trim();
                if (myText.length() > 0) sendMessage(myText);
                handled = true;
            }
            return handled;
        });

        mAdapter = new ChatMessageAdapter(activity(), new ArrayList<>());
        chatLv.setAdapter(mAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child(chatPath)/*.child("chat")*/;
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {
                Chat mChat = dataSnapshot.getValue(Chat.class);
                if (Objects.requireNonNull(mChat).getSender() != null && mChat.getRead() != null)
                    if (!mChat.getSender().equals(sender) && mChat.getRead() == 0) {
                        mChat.setRead(1);
                        dataSnapshot.getRef().setValue(mChat);
                    }
                mAdapter.add(mChat);
                NotificationManager mManager = (NotificationManager)
                        ChatActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                mManager.cancelAll();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String prevChildKey) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @OnClick(R.id.send)
    public void onViewClicked() {
        String myText = message.getText().toString();
        if (myText.length() > 0) sendMessage(myText);
    }

    private void sendMessage(String messageStr) {
        Chat chat = new Chat();
        chat.setSender(sender);
        chat.setTimestamp(new Date().getTime());
        chat.setType("text");
        chat.setText(messageStr);
        chat.setRead(0);
        chat.setUrl("");
        chat.setDriverId(MvpApplication.DATUM.getProviderId());
        chat.setUserId(MvpApplication.DATUM.getUserId());
        myRef.push().setValue(chat);
        message.setText("");
        mCompositeDisposable.add(APIClient
                .getAPIClient()
                .postChatItem("provider", String.valueOf(MvpApplication.DATUM.getUserId()), messageStr)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> System.out.println("RRR o = " + o)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
        MvpApplication.isChatScreenOpen = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        NotificationManager mManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        mManager.cancelAll();
        MvpApplication.isChatScreenOpen = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        MvpApplication.isChatScreenOpen = false;
    }
}
