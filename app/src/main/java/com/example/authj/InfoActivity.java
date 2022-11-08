package com.example.authj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class InfoActivity extends AppCompatActivity {

    private ImageView avatar;
    private TextView followers;
    private TextView following;
    private TextView repositories;
    private TextView username;
    private TextView birthday;
    private RequestQueue queue;
    private WebView logoutWindow;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        queue = Volley.newRequestQueue(getApplicationContext());

        Bundle arguments = getIntent().getExtras();
        int arg = (int) arguments.get("argument");

        if(arg == 1){
            workGit();
        }else if (arg==2){
            workVK();
        }


    }

    private void workGit(){

        avatar = findViewById(R.id.avatar);
        followers = findViewById(R.id.followers_tv);
        following = findViewById(R.id.following_tv);
        repositories = findViewById(R.id.repo_tv);
        username = findViewById(R.id.username_tv);
        linearLayout = findViewById(R.id.linear_layout);
        linearLayout.setVisibility(View.VISIBLE);
        logoutWindow = findViewById(R.id.logout_window);
        logoutWindow.setVisibility(View.GONE);

        findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.GONE);
                logoutWindow.setVisibility(View.VISIBLE);
                logoutWindow.getSettings().setJavaScriptEnabled(true);
                logoutWindow.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        if(request.getUrl().toString().equals("https://github.com/")){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                        return super.shouldOverrideUrlLoading(view, request);
                    }
                });
                logoutWindow.loadUrl("https://github.com/logout");
            }
        });

        requestUser();
    }

    private void requestUser(){
        final JsonObjectRequest request = new JsonObjectRequest(
                Constants.BASE_URL + "/user",
                null,
                response -> {
                    try {
                        JSONObject jObject = new JSONObject(response.toString());
                        Log.d("json2", jObject.toString());
                        User user = new User(
                                Constants.checkNull(response.getString("name")),
                                Constants.checkNull(response.getString("login")),
                                Constants.checkNull(response.getString("company")),
                                Constants.checkNull(response.getString("bio")),
                                response.getInt("followers"),
                                response.getInt("following"),
                                response.getInt("public_repos") +
                                        response.getInt("total_private_repos"),
                                Constants.checkNull(response.getString("avatar_url"))
                        );
                        setComponents(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Constants.HEADERS.put("Content-type", "application/json");
                return Constants.HEADERS;
            }
        };
        queue.add(request);
    }

    private void setComponents(User user){
        username.setText(String.valueOf(user.getUsername()));
        followers.setText(String.valueOf(user.getFollowers()));
        following.setText(String.valueOf(user.getFollowing()));
        repositories.setText(String.valueOf(user.getRepositories()));
        Glide.with(getApplicationContext()).load(user.getAvatarUrl()).centerInside().into(avatar);
    }

    private void workVK(){

        avatar = findViewById(R.id.avatar_vk);
        username = findViewById(R.id.vk_username_tv);
        birthday = findViewById(R.id.birthday_date_tv);
        linearLayout = findViewById(R.id.linear_layout_vk);
        linearLayout.setVisibility(View.VISIBLE);
        logoutWindow = findViewById(R.id.logout_window);
        logoutWindow.setVisibility(View.GONE);

        findViewById(R.id.logout_button_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.GONE);
                logoutWindow.setVisibility(View.VISIBLE);
                logoutWindow.getSettings().setJavaScriptEnabled(true);
                logoutWindow.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        if(request.getUrl().toString().equals("https://vk.com/")){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                        return super.shouldOverrideUrlLoading(view, request);
                    }
                });
                logoutWindow.loadUrl("https://login.vk.com/?act=logout&hash=eff9006d3d9f1a44db&_origin=https%3A%2F%2Fvk.com&lrt=47DEQpj8HBSa-_TImW-5JCeuQeRkm5NMpJWZG3hSuFU&reason=tn");
            }
        });

        requestUserVK();
    }

    private void requestUserVK(){
        String u = "https://api.vk.com/method/users.get" +
                "?users_ids=" + Constants.USER_ID_VK +
                "&fields=bdate,photo_200" +
                "&access_token=" + Constants.TOKEN_VK +
                "&v=5.131";

        Log.d("uuu", u);

        final JsonObjectRequest request = new JsonObjectRequest(
                u,
                null,
                response -> {
                    try {
                        Log.d("json", response.getJSONArray("response").getJSONObject(0).getString("first_name"));
                        UserVK user = new UserVK(
                                Constants.checkNull(response.getJSONArray("response").getJSONObject(0).getString("first_name")),
                                Constants.checkNull(response.getJSONArray("response").getJSONObject(0).getString("last_name")),
                                Constants.checkNull(response.getJSONArray("response").getJSONObject(0).getString("bdate")),
                                Constants.checkNull(response.getJSONArray("response").getJSONObject(0).getString("photo_200"))
                        );
                        setComponentsVK(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Constants.HEADERS_VK.put("Content-type", "application/json");
                return Constants.HEADERS_VK;
            }
        };
        queue.add(request);
    }


    private void setComponentsVK(UserVK user){
        username.setText(String.valueOf(user.getFirst_name()) + " " + String.valueOf(user.getLast_name()));
        birthday.setText(String.valueOf(user.getBdate()));
        Glide.with(getApplicationContext()).load(user.getAvatar_url()).centerInside().into(avatar);
    }
}