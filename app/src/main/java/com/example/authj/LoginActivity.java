package com.example.authj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class LoginActivity extends AppCompatActivity {

    private WebView loginWindow;
    private String url;
    private RequestQueue queue;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_oauth);

        Bundle arguments = getIntent().getExtras();
        int arg = (int) arguments.get("argument");

        queue = Volley.newRequestQueue(this);
        loginWindow = findViewById(R.id.logout_window);
        loginWindow.getSettings().setJavaScriptEnabled(true);

        if(arg == 1){
            loginGit();
        }else if(arg == 2){
            loginVK();
        }
    }

    private void loginGit(){

        loginWindow.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                getCodeGit(request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        loginWindow.loadUrl(Constants.LOGIN_URL + "/authorize" +
                "?client_id=" + Constants.CLIENT_ID  +
                "&scope="+ Constants.SCOPE +
                "&state="+ Constants.STATE );
    }


    private void getCodeGit(String codeAndScope) {
        url = Constants.LOGIN_URL + "/access_token";
        if((codeAndScope.contains("?code="))){
            codeAndScope = codeAndScope.split(".com/")[1].substring(1);

            url += "?client_id=" + Constants.CLIENT_ID +
                    "&client_secret=" + Constants.CLIENT_SECRET +
                    "&" + codeAndScope;
           //loginWindow.loadUrl(url);
            tokenRequest();
        }
    }

    private void tokenRequest(){
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Constants.TOKEN = response.split("&")[0].substring(13);
                Constants.HEADERS.put("Authorization", "Bearer " + Constants.TOKEN);
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                intent.putExtra("argument", 1);
                startActivity(intent);
            }
        }, null);
        queue.add(request);
    }

    private void loginVK(){
        loginWindow.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                tokenRequestVK(request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        loginWindow.loadUrl(Constants.LOGIN_URL_VK + "/authorize" +
                "?client_id=" + Constants.CLIENT_ID_VK  +
                "&redirect_uri=" + Constants.REDIRECT_URL_VK +
                "&scope="+ Constants.SCOPE_VK +
                "&display=mobile" +
                "&response_type=token" +
                "&v=5.131" +
                "&state="+ Constants.STATE );
    }

    private void tokenRequestVK(String codeAndScope){

        if(codeAndScope.contains("#access_token=")){
            url = codeAndScope;
            loginWindow.loadUrl(url);
            Constants.TOKEN_VK = url.split("#")[1].split("&")[0].substring(13);
            Log.d("token", Constants.TOKEN_VK);
            Constants.USER_ID_VK = url.split("&")[2].substring(8);
            Log.d("user_id", Constants.USER_ID_VK);
            Constants.HEADERS_VK.put("Authorization", "Bearer " + Constants.TOKEN_VK);
            Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
            intent.putExtra("argument", 2);
            startActivity(intent);
        }
    }
}