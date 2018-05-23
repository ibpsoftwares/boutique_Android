package com.kftsoftwares.boutique.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kftsoftwares.boutique.Adapter.HistoryAdapter;
import com.kftsoftwares.boutique.Models.HistoryModel;
import com.kftsoftwares.boutique.R;
import com.kftsoftwares.boutique.volly.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.kftsoftwares.boutique.utils.Constants.HISTORY;
import static com.kftsoftwares.boutique.utils.Constants.MyPREFERENCES;
import static com.kftsoftwares.boutique.utils.Constants.UPDATED_TOKEN;
import static com.kftsoftwares.boutique.utils.Constants.User_ID;

public class History extends AppCompatActivity {

    ListView mListView;
    private ArrayList<HistoryModel> mHistoryArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mHistoryArray = new ArrayList<HistoryModel>();
        mListView = findViewById(R.id.listView);
        getHistory();
        mListView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(History.this, "Want to Delete", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    private void getHistory() {

        if (mHistoryArray!=null)
        {
            mHistoryArray.clear();
        }
        String tag_string_req = "string_req";

        final SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        final ProgressDialog pDialog = new ProgressDialog(History.this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                HISTORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                pDialog.cancel();
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("orderDetail");

                    HistoryModel historyModel = null;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        historyModel = new HistoryModel();

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        historyModel.setOrderId(jsonObject1.getString("order_id"));
                        historyModel.setStatus(jsonObject1.getString("status"));

                        historyModel.setCreated(jsonObject1.getString("created"));
                        historyModel.setCategoryId(jsonObject1.getString("category_id"));


                        historyModel.setClothId(jsonObject1.getString("cloth_id"));
                        historyModel.setTitle(jsonObject1.getString("title"));
                        historyModel.setQuantity(jsonObject1.getString("quantity"));
                        historyModel.setPrice(jsonObject1.getString("price"));
                        historyModel.setImage(jsonObject1.getString("image"));


                    mHistoryArray.add(historyModel);
                    }
                    HistoryAdapter historyAdapter = new HistoryAdapter(History.this,mHistoryArray);

                    mListView.setAdapter(historyAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.cancel();
                pDialog.dismiss();
                Toast.makeText(History.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", UPDATED_TOKEN);

                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("user_id", sharedPreferences.getString(User_ID, ""));
                return map;
            }
        };
// Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
