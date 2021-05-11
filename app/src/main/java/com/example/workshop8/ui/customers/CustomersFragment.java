package com.example.workshop8.ui.customers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class CustomersFragment extends Fragment {


    private String urlStart = "http://10.0.2.2:8081/workshop7_war_exploded/customers/";

    private Customer customer;
    RequestQueue requestQueue;
    ListView lvCustomers;
    Button btnEditCust;
    Button btnSaveCust;

    EditText etCustomerId;
    EditText etCustFirstName;
    EditText etCustLastName;
    EditText etCustAddress;

    EditText etCustCity;
    EditText etCustProv;
    EditText etCustPostal;
    EditText etCustCountry;

    EditText etCustHomePhone;
    EditText etCustBusPhone;
    EditText etCustEmail;
    EditText etAgentId;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        customer = new ViewModelProvider(this).get(Customer.class);
        View root = inflater.inflate(R.layout.fragment_customers, container, false);
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.start();


        lvCustomers = root.findViewById(R.id.lvCustomers);
        btnEditCust = root.findViewById(R.id.btnEditCust);
        btnSaveCust = root.findViewById(R.id.btnSaveCust);


        etCustomerId = root.findViewById(R.id.etCustomerId);
        etCustFirstName = root.findViewById(R.id.etCustFirstName);
        etCustLastName = root.findViewById(R.id.etCustLastName);
        etCustAddress = root.findViewById(R.id.etCustAddress);

        etCustCity = root.findViewById(R.id.etCustCity);
        etCustProv = root.findViewById(R.id.etCustProv);
        etCustPostal = root.findViewById(R.id.etCustPostal);
        etCustCountry = root.findViewById(R.id.etCustCountry);

        etCustHomePhone = root.findViewById(R.id.etCustHomePhone);
        etCustBusPhone = root.findViewById(R.id.etCustBusPhone);
        etCustEmail = root.findViewById(R.id.etCustEmail);
        etAgentId = root.findViewById(R.id.etAgentId);



        // Load customers data into listview.
        Executors.newSingleThreadExecutor().execute(new GetCustomers());

        btnEditCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCustFirstName.setEnabled(true);
                etCustLastName.setEnabled(true);
                btnSaveCust.setEnabled(true);
            }
        });

        btnSaveCust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Customer c = new Customer(
                        Integer.parseInt(etCustomerId.getText().toString()), etCustFirstName.getText().toString(),
                        etCustLastName.getText().toString(), etCustAddress.getText().toString(), etCustCity.getText().toString(),
                        etCustProv.getText().toString(), etCustPostal.getText().toString(), etCustCountry.getText().toString(),
                        etCustHomePhone.getText().toString(), etCustBusPhone.getText().toString(), etCustEmail.getText().toString(),
                        Integer.parseInt(etAgentId.getText().toString()));
                Executors.newSingleThreadExecutor().execute(new PutCustomer(c));

                btnSaveCust.setEnabled(false);
                etCustFirstName.setEnabled(false);
                etCustLastName.setEnabled(false);

                // Load customers data into listview.
                Executors.newSingleThreadExecutor().execute(new GetCustomers());

            }

        });


        lvCustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Customer c = (Customer) lvCustomers.getAdapter().getItem(position);
                etCustomerId.setText(c.getCustomerId() + "");
                etCustFirstName.setText(c.getCustFirstName());
                etCustLastName.setText(c.getCustLastName());
                etCustAddress.setText(c.getCustAddress());

                etCustCity.setText(c.getCustCity());
                etCustProv.setText(c.getCustProv());
                etCustPostal.setText(c.getCustPostal());
                etCustCountry.setText(c.getCustCountry());

                etCustHomePhone.setText(c.getCustHomePhone());
                etCustBusPhone.setText(c.getCustBusPhone());
                etCustEmail.setText(c.getCustEmail());
                etAgentId.setText(c.getAgentId()+"");

            }
        });

        return root;
    }

    private class GetCustomers implements Runnable {
        @Override
        public void run() {

            String url = urlStart + "getallcustomers";
          
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    ArrayAdapter<Customer> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++){
                            Customer c = new Gson().fromJson(jsonArray.getString(i), Customer.class);
                            adapter.add(c);
                        }
                        lvCustomers.setAdapter(adapter);

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

    private class PutCustomer implements Runnable {
        JSONObject customerJson;

        public PutCustomer(Customer customer) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(customer);
            try {
                customerJson = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String url = urlStart + "putcustomer";
            JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, customerJson,
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
}