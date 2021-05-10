package com.example.workshop8.ui.customers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.workshop8.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.Executors;

public class CustomersFragment extends Fragment {

    private Customer customer;
    RequestQueue requestQueue;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        customer =
                new ViewModelProvider(this).get(Customer.class);
        View root = inflater.inflate(R.layout.fragment_customers, container, false);
        final ListView lvCustomers = root.findViewById(R.id.lvCustomers);

        requestQueue = Volley.newRequestQueue(this);

        Executors.newSingleThreadExecutor().execute(new GetCustomers());

        lvCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Customer c = (Customer) lvCustomers.getAdapter().getItem(position);
                //apply setText to fields;
            }
        });

        return root;
    }

    private class GetCustomers implements Runnable {
        @Override
        public void run() {
            String url = "http://localhost:8080/workshop7_war_exploded/customers/getallcustomers";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ArrayAdapter<Customer> adapter = new ArrayAdapter<>(getApplicationContext(). android.R.layout.simple_list_item_1);
                    try {
                        JSONArray jsonArray  = new JSONArray(response);
//                        for (int i=0; 1 < jsonArray.length(); i++){
//                            JSONObject cust = jsonArray.getJSONObject(i);
//
//                            Customer c = new Customer( cust.getInt("CustomerId"),
//                                    cust.getString("CustFirstName"),
//                                    cust.getString("CustLastName"),
//                                    cust.getString("CustAddress"),
//                                    cust.getString("CustCity"),
//                                    cust.getString("CustProv"),
//                                    cust.getString("CustPostal"),
//                                    cust.getString("CustCountry"),
//                                    cust.getString("CustHomePhone"),
//                                    cust.getString("CustBusPhone"),
//                                    cust.getString("CustEmail"),
//                                    cust.getInt("AgentId"));
//                            adapter.add(c);
//                        }
                        for (int i=0; 1 < jsonArray.length(); i++){
                            Customer c = new Gson().fromJson(jsonArray.getString(i), Customer.class);
                            adapter.add(c);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(error.getMessage());
                }
            });

            requestQueue.add(stringRequest);

        }
    }
}