package com.example.workshop8.ui.products;

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
import com.example.workshop8.ui.products.Product;
import com.example.workshop8.ui.products.ProductsFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.Executors;

public class ProductsFragment extends Fragment {

    RequestQueue requestQueue;
    ListView lvProducts;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        customer = new ViewModelProvider(this).get(Customer.class);
        View root = inflater.inflate(R.layout.fragment_products, container, false);
        lvProducts = root.findViewById(R.id.lvProducts);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        Executors.newSingleThreadExecutor().execute(new ProductsFragment.GetProducts());

        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product p = (Product) lvProducts.getAdapter().getItem(position);
                //apply setText to fields;
            }
        });

        return root;
    }

    private class GetProducts implements Runnable {
        @Override
        public void run() {
            String url = "http://10.0.0.165:8080/workshop7_war_exploded/products/getallproducts";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ArrayAdapter<Product> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++){
                            Product p = new Gson().fromJson(jsonArray.getString(i), Product.class);
                            adapter.add(p);
                        }
                        lvProducts.setAdapter(adapter);

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