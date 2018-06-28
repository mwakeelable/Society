package com.wakeel.society.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.wakeel.society.R;
import com.wakeel.society.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    private final static String TAG = HomeActivity.class.getSimpleName();
    String accessToken;
    @BindView(R.id.postsList)
    ListView postsList;
    ArrayList<String> posts = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                AccessToken.getCurrentAccessToken().getUserId() + "/posts",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject postsParent = response.getJSONObject();
                            JSONArray postsArray = postsParent.getJSONArray("data");
                            Post post = new Post();
                            for (int i = 0; i < postsArray.length(); i++) {
                                JSONObject postObj = postsArray.getJSONObject(i);
                                if (postObj.has("message")) {
                                    post.setCreated_time(postObj.optString("created_time"));
                                    post.setMessage(postObj.optString("message"));
                                    post.setId(postObj.optString("id"));
                                    posts.add(post.getMessage());
                                }
                            }
                            adapter = new ArrayAdapter<String>(HomeActivity.this, android.R.layout.simple_list_item_1, posts);
                            postsList.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }
}
