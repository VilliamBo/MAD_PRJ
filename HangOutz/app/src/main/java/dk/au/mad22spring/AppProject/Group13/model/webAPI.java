package dk.au.mad22spring.AppProject.Group13.model;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class webAPI{

    private static final String TAG = "webAPI";


    private RequestQueue queue;
    private StringRequest request;
    private String URL = "https://www.breakingbadapi.com/api/character/random";


    //Constructor
    public webAPI() {}

    public void getRandomImage(MutableLiveData<String> imgURL, Context context){
        queue = Volley.newRequestQueue(context);
        request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response != null){
                    Log.d(TAG, "API response: " + response);
                    imgURL.postValue(response); //put into mutableLiveData
                }
            }
        }, error -> Log.d(TAG, "onErrorResponse: " + error));
        queue.add(request);
    }
}
