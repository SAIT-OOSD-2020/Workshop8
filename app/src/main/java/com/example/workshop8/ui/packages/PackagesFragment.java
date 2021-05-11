package com.example.workshop8.ui.packages;

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
import com.example.workshop8.ui.packages.Package;
import com.example.workshop8.ui.packages.PackagesFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.Executors;

public class PackagesFragment extends Fragment {

    RequestQueue requestQueue;
    ListView lvPackages;
    FloatingActionButton btnAdd_packages, btnSave_packages, btnDelete_packages;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        customer = new ViewModelProvider(this).get(Customer.class);
        View root = inflater.inflate(R.layout.fragment_packages, container, false);
        lvPackages = root.findViewById(R.id.lvPackages);
        btnAdd_packages = root.findViewById(R.id.btnAdd_packages);
        btnSave_packages = root.findViewById(R.id.btnSave_packages);
        btnDelete_packages = root.findViewById(R.id.btnDelete_packages);

        

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        Executors.newSingleThreadExecutor().execute(new PackagesFragment.GetPackages());

        lvPackages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Package p = (Package) lvPackages.getAdapter().getItem(position);
                //apply setText to fields;
            }
        });

        return root;
    }

    private class GetPackages implements Runnable {
        @Override
        public void run() {
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
    }
}