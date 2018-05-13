package com.ratna.foosip;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import SharedPreferences.SavedParameter;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

/**
 * Created by ratna on 4/12/2018.
 */

public class GroupFragment extends Fragment implements View.OnClickListener {

    private DatabaseReference mFirebaseDatabaseReference;

    private FirebaseAuth mAuth;

    private View mMainView;


    private String mCurrentUserId;


    //Views UI
    private RecyclerView rvListMessage;
    private LinearLayoutManager mLinearLayoutManager;
    private ImageView btSendMessage, btEmoji;
    private EmojiconEditText edMessage;
    private View contentRoot;
    private EmojIconActions emojIcon;

    SavedParameter savedParameter;

    public GroupFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.group_frag_chat, container, false);


        contentRoot = mMainView.findViewById(R.id.contentRoot);
        edMessage = (EmojiconEditText) mMainView.findViewById(R.id.editTextMessage);
        btSendMessage = (ImageView) mMainView.findViewById(R.id.buttonMessage);
        btEmoji = (ImageView) mMainView.findViewById(R.id.buttonEmoji);
        emojIcon = new EmojIconActions(getActivity(), contentRoot, edMessage, btEmoji);
        emojIcon.ShowEmojIcon();
        rvListMessage = (RecyclerView) mMainView.findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(true);

//        mChatSendBtn = (ImageButton) mMainView.findViewById(R.id.chat_send_btn);
//        mChatAddBtn = (ImageButton) mMainView.findViewById(R.id.chat_add_btn);
//        mChatMessageView = (EditText) mMainView.findViewById(R.id.chat_message_view);
//        mMessagesList = (RecyclerView) mMainView.findViewById(R.id.messages_list);
//        mRefreshLayout = (SwipeRefreshLayout) mMainView.findViewById(R.id.message_swipe_layout);
        return mMainView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        savedParameter = new SavedParameter(getActivity());



        lerMessagensFirebase();


        btSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SendMessage();

            }
        });


    }


    private void lerMessagensFirebase() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        final GroupMessageAdapter firebaseAdapter = new GroupMessageAdapter(mFirebaseDatabaseReference.child("restaurant").child(savedParameter.getQrCode(getActivity())));
        firebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = firebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    rvListMessage.scrollToPosition(positionStart);
                }
            }
        });
        rvListMessage.setLayoutManager(mLinearLayoutManager);
        rvListMessage.setAdapter(firebaseAdapter);
    }


    void SendMessage() {
        String message = edMessage.getText().toString();

        if (!TextUtils.isEmpty(message)) {

            String current_user_ref = "restaurant/" + savedParameter.getQrCode(getActivity());

            DatabaseReference user_message_push = mFirebaseDatabaseReference.child("restaurant")
                    .push();


            String push_id = user_message_push.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from", mCurrentUserId);

            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);

            edMessage.setText("");


            mFirebaseDatabaseReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if (databaseError != null) {

                        Log.d("CHAT_LOG", databaseError.getMessage().toString());

                    }

                }
            });


        }
    }


    @Override
    public void onClick(View view) {

    }
}
