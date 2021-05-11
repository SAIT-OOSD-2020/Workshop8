package com.example.workshop8.ui.suppliers;

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
import com.example.workshop8.ui.suppliers.Supplier;
import com.example.workshop8.ui.suppliers.SuppliersFragment;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class SuppliersFragment extends Fragment {

    private String urlStart = "http://10.0.0.165:8080/workshop7_war_exploded/suppliers/";
    //private String urlStart = "http://10.0.2.2:8081/workshop7_war_exploded/suppliers/";

    RequestQueue requestQueue;
    ListView lvSuppliers;
    EditText etSupplierId, etSupName;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        supplier = new ViewModelProvider(this).get(Supplier.class);
        View root = inflater.inflate(R.layout.fragment_suppliers, container, false);
        lvSuppliers = root.findViewById(R.id.lvSuppliers);
        etSupplierId = root.findViewById(R.id.etSupplierId);
        etSupName = root.findViewById(R.id.etSupName);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        Executors.newSingleThreadExecutor().execute(new SuppliersFragment.GetSuppliers());

        lvSuppliers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Supplier s = (Supplier) lvSuppliers.getAdapter().getItem(position);
                etSupplierId.setText(s.getSupplierId() + "");
                etSupName.setText(s.getSupName());
            }
        });

        return root;
    }

    private class GetSuppliers implements Runnable {
        @Override
        public void run() {
            listSuppliers();
        }
    }

    private void listSuppliers(){
        String url = "http://10.0.0.165:8080/workshop7_war_exploded/suppliers/getallsuppliers";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayAdapter<Supplier> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++){
                        Supplier s = new Gson().fromJson(jsonArray.getString(i), Supplier.class);
                        adapter.add(s);
                    }
                    lvSuppliers.setAdapter(adapter);

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

    private class PutSupplier implements Runnable {
        JSONObject supplierJson;

        public PutSupplier(Supplier supplier) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(supplier);
            try {
                supplierJson = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String url = urlStart + "putsupplier";
            JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, supplierJson,
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

    private class PostSupplier implements Runnable {
        JSONObject supplierJson;

        public PostSupplier(Supplier supplier) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(supplier);
            try {
                supplierJson = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String url = urlStart + "postsupplier";
            JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.POST, url, supplierJson,
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

    private class DeleteSupplier implements Runnable {
        private int suppId;
        public DeleteSupplier(int suppId) {
            this.suppId = suppId;
        }

        @Override
        public void run() {
            String url = urlStart + "deletesupplier/" + suppId;
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