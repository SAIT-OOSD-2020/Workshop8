package com.example.workshop8.ui.packages;

import android.app.DatePickerDialog;

import android.app.Dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class PackagesFragment extends Fragment {

    private final String urlStart = "http://10.0.0.165:8080/workshop7_war_exploded/packages/";
//    private String urlStart = "http://10.0.2.2:8081/workshop7_war_exploded/packages/";

    private String saveState = "";

    RequestQueue requestQueue;
    ListView lvPackages;
    FloatingActionButton btnAdd_packages, btnSave_packages, btnDelete_packages;
    EditText etPackageId, etPkgName, etPkgStartDate, etPkgEndDate,
            etPkgDesc, etPkgBasePrice, etPkgAgencyCommission;
    ImageButton btnStartDate, btnEndDate;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy, hh:mm:ss aaa");

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

        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

//                String myFormat = "MM/dd/yyyy"; //In which you need put here
                String myFormat = "yyyy-MM-dd";

                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

                etPkgStartDate.setText(sdf.format(myCalendar.getTime()));
            }

        };





        etPkgEndDate = root.findViewById(R.id.etPkgEndDate);
        etPkgDesc = root.findViewById(R.id.etPkgDesc);
        etPkgBasePrice = root.findViewById(R.id.etPkgBasePrice);
        etPkgAgencyCommission = root.findViewById(R.id.etPkgAgencyCommission);

        btnStartDate = root.findViewById(R.id.btnStartDate);
        btnEndDate = root.findViewById(R.id.btnEndDate);

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        Executors.newSingleThreadExecutor().execute(new PackagesFragment.GetPackages());

        lvPackages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Package p = (Package) lvPackages.getAdapter().getItem(position);
                etPackageId.setText(String.valueOf(p.getPackageId()));
                etPkgName.setText(p.getPkgName());
                etPkgStartDate.setText(p.getPkgStartDate().toString());
                etPkgEndDate.setText(p.getPkgEndDate().toString());
                etPkgDesc.setText(p.getPkgDesc());
                etPkgBasePrice.setText(String.valueOf(p.getPkgBasePrice()));
                etPkgAgencyCommission.setText(String.valueOf(p.getPkgAgencyCommission()));
                saveState = "update";

                btnAdd_packages.setEnabled(true);

                etPkgName.setEnabled(true);
                etPkgStartDate.setEnabled(true);
                etPkgEndDate.setEnabled(true);
                etPkgDesc.setEnabled(true);
                etPkgBasePrice.setEnabled(true);
                etPkgAgencyCommission.setEnabled(true);

            }
        });

        btnAdd_packages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etPackageId.setText("");
                etPkgName.setText("Sample Name");
                etPkgStartDate.setText("2020-01-30");
                etPkgEndDate.setText("2020-02-30");
                etPkgDesc.setText("Sample");
                etPkgBasePrice.setText("111");
                etPkgAgencyCommission.setText("111");
                btnAdd_packages.setEnabled(false);
                btnSave_packages.setEnabled(true);
                saveState = "create";

                etPkgName.setEnabled(true);
                etPkgStartDate.setEnabled(true);
                etPkgEndDate.setEnabled(true);
                etPkgDesc.setEnabled(true);
                etPkgBasePrice.setEnabled(true);
                etPkgAgencyCommission.setEnabled(true);

            }
        });

        btnSave_packages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String test = etPackageId.getText().toString();
                if (saveState.equals("create")){
                    Package p = new Package(0,
                            etPkgName.getText().toString(),
                            Date.valueOf((etPkgStartDate.getText().toString())),
                            Date.valueOf((etPkgEndDate.getText().toString())),
                            etPkgDesc.getText().toString(),
                            BigInteger.valueOf(Integer.parseInt(etPkgBasePrice.getText().toString())),
                            BigInteger.valueOf(Integer.parseInt(etPkgAgencyCommission.getText().toString())));
                    Executors.newSingleThreadExecutor().execute(new PostPackage(p));
                } else if (saveState.equals("update")) {
                    Package p = new Package(
                            Integer.parseInt(etPackageId.getText().toString()),
                            etPkgName.getText().toString(),
                            Date.valueOf((etPkgStartDate.getText().toString())),
                            Date.valueOf((etPkgEndDate.getText().toString())),
                            etPkgDesc.getText().toString(),
                            BigInteger.valueOf(Integer.parseInt(etPkgBasePrice.getText().toString())),
                            BigInteger.valueOf(Integer.parseInt(etPkgAgencyCommission.getText().toString())));
                    Executors.newSingleThreadExecutor().execute(new PutPackage(p));
                }

//                if (etPackageId.getText().toString().isEmpty()){
//                    Package p = new Package(0,
//                                etPkgName.getText().toString(),
//                                Date.valueOf((etPkgStartDate.getText().toString())),
//                                Date.valueOf((etPkgEndDate.getText().toString())),
//                                etPkgDesc.getText().toString(),
//                                BigInteger.valueOf(Integer.parseInt(etPkgBasePrice.getText().toString())),
//                                BigInteger.valueOf(Integer.parseInt(etPkgAgencyCommission.getText().toString())));
//                    Executors.newSingleThreadExecutor().execute(new PostPackage(p));
//                } else {
//                    Package p = new Package(
//                            Integer.parseInt(etPackageId.getText().toString()),
//                            etPkgName.getText().toString(),
//                            Date.valueOf((etPkgStartDate.getText().toString())),
//                            Date.valueOf((etPkgEndDate.getText().toString())),
//                            etPkgDesc.getText().toString(),
//                            BigInteger.valueOf(Integer.parseInt(etPkgBasePrice.getText().toString())),
//                            BigInteger.valueOf(Integer.parseInt(etPkgAgencyCommission.getText().toString())));
//                    Executors.newSingleThreadExecutor().execute(new PutPackage(p));
//                }

                saveState = "";
                btnAdd_packages.setEnabled(true);

                etPkgName.setEnabled(false);
                etPkgStartDate.setEnabled(false);
                etPkgEndDate.setEnabled(false);
                etPkgDesc.setEnabled(false);
                etPkgBasePrice.setEnabled(false);
                etPkgAgencyCommission.setEnabled(false);


                // TODO: Refresh the listview. ↓ This sometimes work... Just call three times!!!
//                Executors.newSingleThreadExecutor().execute(new GetCustomers());
                listPackages();
                listPackages();
                listPackages();


            }
        });

        btnDelete_packages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPackageId.getText().toString().isEmpty()){
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Please select a package to delete!", Toast.LENGTH_LONG);
                    toast.show();
                } else{
                    // means one customer is clicked.
                    int pkgId = Integer.parseInt(etPackageId.getText().toString());
                    Executors.newSingleThreadExecutor().execute(new PackagesFragment.DeletePackage(pkgId));

                }

                btnAdd_packages.setEnabled(true);

                etPackageId.setText("");
                etPkgName.setText("Sample Name");
                etPkgStartDate.setText("2020-01-30");
                etPkgEndDate.setText("2020-02-30");
                etPkgDesc.setText("Sample");
                etPkgBasePrice.setText("111");
                etPkgAgencyCommission.setText("111");
                etPkgName.setEnabled(false);
                etPkgStartDate.setEnabled(false);
                etPkgEndDate.setEnabled(false);
                etPkgDesc.setEnabled(false);
                etPkgBasePrice.setEnabled(false);
                etPkgAgencyCommission.setEnabled(false);

                // TODO: Refresh the listview. ↓ This sometimes work... Just call twice!!!
//                Executors.newSingleThreadExecutor().execute(new GetCustomers());
                listPackages();
                listPackages();
                listPackages();


            }
        });

//        etPkgStartDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                new DatePickerDialog(v.getContext(), date,
//                        myCalendar.get(Calendar.YEAR),
//                        myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });
//
//        etPkgEndDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                new DatePickerDialog(v.getContext(), date,
//                        myCalendar.get(Calendar.YEAR),
//                        myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });

        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setETView(etPkgStartDate);
                newFragment.show(getActivity().getSupportFragmentManager(), "startDate");

                // If using code above, after selecting a date, the showing month value will decrease 1, will cause error.
//
//                new DatePickerDialog(v.getContext(), date,
//                        myCalendar.get(Calendar.YEAR),
//                        myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = new DatePickerFragment();
                newFragment.setETView(etPkgEndDate);
                newFragment.show(getActivity().getSupportFragmentManager(), "endDate");

                // If using code above, after selecting a date, the showing month value will decrease 1, will cause error.

//                new DatePickerDialog(v.getContext(), date,
//                        myCalendar.get(Calendar.YEAR),
//                        myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
        String url = urlStart + "getallpackages";
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
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
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

                            try {
                                String state = response.getString("state");
                                if (state.equals("fail")){
                                    String detail = response.getString("detail");
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), detail, Toast.LENGTH_LONG);
                                    toast.show();
                                } else if (state.equals("success")){
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Updated!", Toast.LENGTH_LONG);
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

    private class PostPackage implements Runnable {
        JSONObject pkgJson;

        public PostPackage(Package pkg) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
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
                            try {
                                String state = response.getString("state");
                                if (state.equals("fail")){
                                    String detail = response.getString("detail");
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                            detail, Toast.LENGTH_LONG);
                                    toast.show();
                                } else if (state.equals("success")){
                                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                            "Package Added", Toast.LENGTH_LONG);
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
                                            "Package Deleted", Toast.LENGTH_LONG);
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

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        Calendar c;
        EditText et;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            int year, month, day;
            if (et.getText().toString().isEmpty()) {
                c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            } else {
                c = Calendar.getInstance();
                c.setTime(Date.valueOf(et.getText().toString()));

                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
            }

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void setETView(EditText et) {
            this.et = et;
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            et.setText(year+"-"+(month+1)+"-"+day);
        }
    }

}