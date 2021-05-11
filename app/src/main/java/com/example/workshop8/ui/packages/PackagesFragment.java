package com.example.workshop8.ui.packages;

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
import com.example.workshop8.ui.packages.Package;
import com.example.workshop8.ui.packages.PackagesFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class PackagesFragment extends Fragment {

    private String urlStart = "http://10.0.0.165:8080/workshop7_war_exploded/packages/";
    //private String urlStart = "http://10.0.2.2:8081/workshop7_war_exploded/packages/";
    RequestQueue requestQueue;
    ListView lvPackages;
    FloatingActionButton btnAdd_packages, btnSave_packages, btnDelete_packages;
    EditText etPackageId, etPkgName, etPkgStartDate, etPkgEndDate,
            etPkgDesc, etPkgBasePrice, etPkgAgencyCommission;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        package = new ViewModelProvider(this).get(Package.class);
        View root = inflater.inflate(R.layout.fragment_packages, container, false);

        lvPackages = root.findViewById(R.id.lvPackages);
        btnAdd_packages = root.findViewById(R.id.btnAdd_packages);
        btnSave_packages = root.findViewById(R.id.btnSave_packages);
        btnDelete_packages = root.findViewById(R.id.btnDelete_packages);

        etPackageId = root.findViewById(R.id.etPackageId);
        etPkgName = root.findViewById(R.id.etPkgName);
        etPkgStartDate = root.findViewById(R.id.etPkgStartDate);
        etPkgEndDate = root.findViewById(R.id.etPkgEndDate);
        etPkgDesc = root.findViewById(R.id.etPkgDesc);
        etPkgBasePrice = root.findViewById(R.id.etPkgBasePrice);
        etPkgAgencyCommission = root.findViewById(R.id.etPkgAgencyCommission);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        Executors.newSingleThreadExecutor().execute(new PackagesFragment.GetPackages());

        lvPackages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Package p = (Package) lvPackages.getAdapter().getItem(position);
                etPackageId.setText(p.getPackageId()+"");
                etPkgName.setText(p.getPkgName());
                etPkgStartDate.setText(p.getPkgStartDate().toString());
                etPkgEndDate.setText(p.getPkgEndDate().toString());
                etPkgDesc.setText(p.getPkgDesc());
                etPkgBasePrice.setText(p.getPkgBasePrice()+"");
                etPkgAgencyCommission.setText(p.getPkgAgencyCommission()+"");
            }
        });

        return root;
    }

    private class GetPackages implements Runnable {
        @Override
        public void run() {

            listPackages();

        }
    }

    private void listPackages() {
        String url = "http://10.0.0.165:8080/workshop7_war_exploded/packages/getallpackages";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayAdapter<Package> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++){
                        Package p = new Gson().fromJson(jsonArray.getString(i), Package.class);
                        adapter.add(p);
                    }
                    lvPackages.setAdapter(adapter);

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

    private class PutPackage implements Runnable {
        JSONObject pkgJson;

        public PutPackage(Package pkg) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(pkg);
            try {
                pkgJson = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String url = urlStart + "putpackage";
            JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, pkgJson,
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

    private class PostPackage implements Runnable {
        JSONObject pkgJson;

        public PostPackage(Package pkg) {
            Gson gson = new Gson();
            String jsonString = gson.toJson(pkg);
            try {
                pkgJson = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            String url = urlStart + "postpackage";
            JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.POST, url, pkgJson,
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

    private class DeletePackage implements Runnable {
        private int pkgId;
        public DeletePackage(int pkgId) {
            this.pkgId = pkgId;
        }

        @Override
        public void run() {
            String url = urlStart + "deletepackage/" + pkgId;
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