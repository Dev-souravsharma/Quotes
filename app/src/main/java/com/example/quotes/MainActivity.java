package com.example.quotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    TextView author, quote;
    ProgressBar progressBar;
    ImageView imageView;
    public static final String URL = "http://quotes.rest/qod.json";
    public static final String TAG = "sourav";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        author = findViewById(R.id.edtAuthor);
        quote = findViewById(R.id.edtQuote);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.edtProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        jsonCall();
    }

    private void jsonCall() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = response.getJSONObject("contents");
                    JSONArray jsonArray = jsonObject.getJSONArray("quotes");
                    JSONObject data = jsonArray.getJSONObject(0);
                    String jsonQuote = data.getString("quote");
                    String jsonAuthor = data.getString("author");
                    Glide.with(getApplicationContext())
                            .load(data.getString("background"))
                            .centerCrop()
                            .into(imageView);
                    quote.setText(jsonQuote);
                    author.setText(jsonAuthor);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, Objects.requireNonNull(error.getLocalizedMessage()));
            }
        });
        SingletonClass.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}