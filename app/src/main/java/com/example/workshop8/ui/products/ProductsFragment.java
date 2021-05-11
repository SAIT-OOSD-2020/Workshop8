package com.example.workshop8.ui.products;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.workshop8.R;
import com.example.workshop8.ui.customers.Customer;
import com.example.workshop8.ui.products.Product;
import com.example.workshop8.ui.products.ProductsFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class ProductsFragment extends Fragment {

    private String urlStart = "http://10.0.0.165:8080/workshop7_war_exploded/products/";
    //private String urlStart = "http://10.0.2.2:8081/workshop7_war_exploded/products/";

    RequestQueue requestQueue;
    ListView lvProducts;
    EditText etProductId, etProdName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        products = new ViewModelProvider(this).get(Products.class);
        View root = inflater.inflate(R.layout.fragment_products, container, false);
        lvProducts = root.findViewById(R.id.lvProducts);

        etProductId = root.findViewById(R.id.etProductId);
        etProdName = root.findViewById(R.id.etProdName);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        Executors.newSingleThreadExecutor().execute(new ProductsFragment.GetProducts());

        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product p = (Product) lvProducts.getAdapter().getItem(position);
                etProductId.setText(p.getProductId() + "");
                etProdName.setText(p.getProdName());
            }
        });

        return root;
    }

    private class GetProducts implements Runnable {
        @Override
        public void run() {
            listProducts();
        }
    }

    private void listProducts(){
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

    private class PutProduct implements Runnable {
        JSONObject prodJson;

        public PutProduct(Product product) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(product);
            try {
                prodJson = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String url = urlStart + "putproduct";
            JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, prodJson,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            VolleyLog.d("!!!Response" + response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            VolleyLog.d("!!!Error.Response" + error);
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }

            };

            requestQueue.add(putRequest);

        }
    }

    private class PostProduct implements Runnable {
        JSONObject prodJson;

        public PostProduct(Product product) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(product);
            try {
                prodJson = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String url = urlStart + "postproduct";
            JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.POST, url, prodJson,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            VolleyLog.d("!!!Response" + response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            VolleyLog.d("!!!Error.Response" + error);
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }

            };

            requestQueue.add(putRequest);

        }
    }

    private class DeleteProduct implements Runnable {
        private int prodId;
        public DeleteProduct(int prodId) {
            this.prodId = prodId;
        }

        @Override
        public void run() {
            String url = urlStart + "deleteproduct/" + prodId;
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            VolleyLog.d("!!!Response" + response);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            VolleyLog.d("!!!Error.Response" + error);
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }

            };

            requestQueue.add(stringRequest);
        }
    }
}