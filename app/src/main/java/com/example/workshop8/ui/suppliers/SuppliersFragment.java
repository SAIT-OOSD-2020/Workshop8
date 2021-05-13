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
import android.widget.Toast;

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
import com.example.workshop8.ui.packages.Package;
import com.example.workshop8.ui.packages.PackagesFragment;
import com.example.workshop8.ui.suppliers.Supplier;
import com.example.workshop8.ui.suppliers.SuppliersFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class SuppliersFragment extends Fragment {

//    private String urlStart = "http://10.0.0.165:8080/workshop7_war_exploded/suppliers/";
    private String urlStart = "http://10.0.2.2:8081/workshop7_war_exploded/suppliers/";

    RequestQueue requestQueue;
    ListView lvSuppliers;
    FloatingActionButton btnAdd_suppliers, btnSave_suppliers, btnDelete_suppliers;
    EditText etSupplierId, etSupName;

    String saveState = "";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        supplier = new ViewModelProvider(this).get(Supplier.class);
        View root = inflater.inflate(R.layout.fragment_suppliers, container, false);
        lvSuppliers = root.findViewById(R.id.lvSuppliers);
        btnAdd_suppliers = root.findViewById(R.id.btnAdd_suppliers);
        btnSave_suppliers = root.findViewById(R.id.btnSave_suppliers);
        btnDelete_suppliers = root.findViewById(R.id.btnDelete_suppliers);
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
                saveState = "update";

                etSupName.setEnabled(true);
                btnAdd_suppliers.setEnabled(true);


            }
        });

        btnAdd_suppliers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSupplierId.setText("");
                etSupName.setText("New Supplier");

                saveState = "create";

                etSupName.setEnabled(true);
                btnAdd_suppliers.setEnabled(false);
            }
        });

        btnSave_suppliers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveState.equals("create")){
                    Supplier s = new Supplier(
                            0,
                            etSupName.getText().toString());
                    Executors.newSingleThreadExecutor().execute(new PostSupplier(s));
                } else if (saveState.equals("update")) {
                    Supplier s = new Supplier(
                            Integer.parseInt(etSupplierId.getText().toString()),
                            etSupName.getText().toString());
                    Executors.newSingleThreadExecutor().execute(new PutSupplier(s));
                }

//                if (etSupplierId.getText().toString().isEmpty()){
//                    Supplier s = new Supplier(
//                            0,
//                            etSupName.getText().toString());
//                    Executors.newSingleThreadExecutor().execute(new PostSupplier(s));
//                } else {
//                    Supplier s = new Supplier(
//                            Integer.parseInt(etSupplierId.getText().toString()),
//                            etSupName.getText().toString());
//                    Executors.newSingleThreadExecutor().execute(new PutSupplier(s));
//                }

                saveState = "";

                etSupName.setEnabled(false);
                btnAdd_suppliers.setEnabled(true);


                // TODO: Refresh the listview. ↓ This sometimes work... Just call twice!!!
//                Executors.newSingleThreadExecutor().execute(new GetSuppliers());
                listSuppliers();
                listSuppliers();

            }
        });

        btnDelete_suppliers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSupplierId.getText().toString().isEmpty()){
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "Please select a supplier to delete!", Toast.LENGTH_LONG);
                    toast.show();
                } else{
                    // means one supplier is clicked.
                    int suppId = Integer.parseInt(etSupplierId.getText().toString());
                    Executors.newSingleThreadExecutor().execute(new DeleteSupplier(suppId));

                    etSupplierId.setText("");
                    etSupName.setText("");
                }

                etSupName.setEnabled(false);
                btnAdd_suppliers.setEnabled(true);


                // TODO: Refresh the listview. ↓ This sometimes work... Just call twice!!!
//                Executors.newSingleThreadExecutor().execute(new GetSuppliers());
                listSuppliers();
                listSuppliers();
                listSuppliers();
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
        String url = urlStart + "getallsuppliers";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayAdapter<Supplier> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                        android.R.layout.simple_list_item_1);
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

                            try {
                                String state = response.getString("state");
                                if (state.equals("fail")){
                                    String detail = response.getString("detail");
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                            detail, Toast.LENGTH_LONG);
                                    toast.show();
                                } else if (state.equals("success")){
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                            "Supplier Updated", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                            try {
                                String state = response.getString("state");
                                if (state.equals("fail")){
                                    String detail = response.getString("detail");
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                            detail, Toast.LENGTH_LONG);
                                    toast.show();
                                } else if (state.equals("success")){
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                            "Supplier Added", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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

                            JsonObject json = new Gson().fromJson(response, JsonObject.class);
                            try {
                                String state = json.get("state").getAsString();
                                if (state.equals("fail")){
                                    String detail = json.get("detail").toString();
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                            detail, Toast.LENGTH_LONG);
                                    toast.show();
                                } else if (state.equals("success")){
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                            "Supplier Deleted", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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