package com.antoniuswicaksana.project_pbp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.antoniuswicaksana.project_pbp.adapter.AdapterBooking;
import com.antoniuswicaksana.project_pbp.api.BookingApi;
import com.antoniuswicaksana.project_pbp.model.Booking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class BookingFragment extends Fragment {

    private RecyclerView recyclerView;
    private AdapterBooking adapter;
    private Button btnTambah;
    private List<Booking> listBooking;
    private View view;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userID;

    public BookingFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_booking, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userID = firebaseUser.getUid();

        btnTambah = view.findViewById(R.id.btnTambah);

        setAdapter();
        getBooking();

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putString("status", "tambah");
                TambahEditBooking tambahEditBooking = new TambahEditBooking();
                tambahEditBooking.setArguments(data);

                loadFragment(tambahEditBooking);
            }
        });

        return view;
    }

    public void setAdapter(){
        getActivity().setTitle("Data Booking");
        listBooking = new ArrayList<Booking>();
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new AdapterBooking(view.getContext(), listBooking);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_booking,fragment)
                .commit();
    }

    public void getBooking() {
        //deklarasi queue
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        //meminta tanggapan string daari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data booking");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, BookingApi.URL_SELECT,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //bagian jika response berhasil
                progressDialog.dismiss();
                try {
                    //mengambil data response json object yang berupa data mahasiswa
                    JSONArray jsonArray = response.getJSONArray("data");

                    if (!listBooking.isEmpty())
                        listBooking.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //mengubah data jsonArray tertentu menjadi object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        String clientID     = jsonObject.optString("clientID");

                        if (userID.equals(clientID)) {
                            String id           = jsonObject.optString("id");
                            String paket        = jsonObject.optString("paket");
                            String alamat    = jsonObject.optString("alamat");
                            String tanggal       = jsonObject.optString("tanggal");
                            String waktu          = jsonObject.optString("waktu");

                            //membuat objek user
                            Booking booking =
                                    new Booking(id, clientID, paket, alamat, tanggal, waktu);

                            //menambahkan obejk user tadi ke list user
                            listBooking.add(booking);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(view.getContext(), response.optString("message"),
                        Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //bagian jika response jaringan terdapat gangguan/error
                progressDialog.dismiss();
                Toast.makeText(view.getContext(), error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        //proses penambahan request yang sudah kita buat ke request queue
        //yang sudah dideklarasi
        queue.add(stringRequest);
    }
}