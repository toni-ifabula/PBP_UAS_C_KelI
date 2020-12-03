package com.antoniuswicaksana.project_pbp;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.antoniuswicaksana.project_pbp.adapter.AdapterJadwal;
import com.antoniuswicaksana.project_pbp.api.JadwalApi;
import com.antoniuswicaksana.project_pbp.model.Jadwal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class JadwalFragment extends Fragment {

    private Context context;
    private RecyclerView recyclerView;
    private AdapterJadwal adapter;
    private Button btnTambah;
    private List<Jadwal> listJadwal;
    private View view;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public JadwalFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_jadwal, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        btnTambah = view.findViewById(R.id.btnTambah);

        setAdapter();
        getJadwal();

        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser == null) {
                    Bundle data = new Bundle();
                    data.putString("status", "tambah");
                    TambahEdit tambahEdit = new TambahEdit();
                    tambahEdit.setArguments(data);

                    loadFragment(tambahEdit);
                } else {
                    Toast.makeText(getContext(), "Hanya admin yang dapat menambah jadwal", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void setAdapter(){
        getActivity().setTitle("Data Jadwal");
        listJadwal = new ArrayList<Jadwal>();
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new AdapterJadwal(view.getContext(), listJadwal);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_jadwal,fragment)
                .commit();
    }

    public void getJadwal() {
        //deklarasi queue
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        //meminta tanggapan string daari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data jadwal");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, JadwalApi.URL_SELECT,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //bagian jika response berhasil
                progressDialog.dismiss();
                try {
                    //mengambil data response json object yang berupa data mahasiswa
                    JSONArray jsonArray = response.getJSONArray("data");

                    if (!listJadwal.isEmpty())
                        listJadwal.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //mengubah data jsonArray tertentu menjadi object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        String id           = jsonObject.optString("id");
                        String tanggal       = jsonObject.optString("tanggal");
                        String waktu          = jsonObject.optString("waktu");
                        String keterangan    = jsonObject.optString("keterangan");

                        //membuat objek user
                        Jadwal jadwal =
                                new Jadwal(id, tanggal, waktu, keterangan);

                        //menambahkan obejk user tadi ke list user
                        listJadwal.add(jadwal);
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