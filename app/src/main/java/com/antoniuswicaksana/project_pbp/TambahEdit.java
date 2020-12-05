package com.antoniuswicaksana.project_pbp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.antoniuswicaksana.project_pbp.api.JadwalApi;
import com.antoniuswicaksana.project_pbp.model.Jadwal;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;

public class TambahEdit extends Fragment {

    private TextInputEditText etTanggal, etWaktu, etKeterangan;
    private Button btnSimpan, btnBatal;
    private String status, tanggal, waktu, keterangan;
    private Jadwal jadwal;
    private View view;

    public TambahEdit() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tambah_edit, container, false);

        setAtribut(view);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etTanggal.getText().toString().equals("")) {
                    etTanggal.setError("Isikan dengan benar");
                    etTanggal.requestFocus();
                } else if(etWaktu.getText().toString().equals("")) {
                    etWaktu.setError("Isikan dengan benar");
                    etWaktu.requestFocus();
                } else if (etKeterangan.getText().toString().equals("")) {
                    etKeterangan.setError("Isikan dengan benar");
                    etKeterangan.requestFocus();
                } else {
                    tanggal      = etTanggal.getText().toString().trim();
                    waktu     = etWaktu.getText().toString().trim();
                    keterangan = etKeterangan.getText().toString().trim();

                    if(status.equals("tambah"))
                        tambahJadwal(tanggal, waktu, keterangan);
                    else
                        editJadwal(jadwal.getId(), tanggal, waktu, keterangan);
                }
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new JadwalFragment());
            }
        });

        return view;
    }

    public void setAtribut(View view) {
        jadwal = (Jadwal) getArguments().getSerializable("jadwal");
        etTanggal = view.findViewById(R.id.etTanggal);
        etWaktu = view.findViewById(R.id.etWaktu);
        etKeterangan = view.findViewById(R.id.etKeterangan);
        btnSimpan = view.findViewById(R.id.btnSimpan);
        btnBatal = view.findViewById(R.id.btnBatal);

        status = getArguments().getString("status");

        if (status.equals("edit")) {
            etTanggal.setText(jadwal.getTanggal());
            etWaktu.setText(jadwal.getWaktu());
            etKeterangan.setText(jadwal.getKeterangan());
        }
    }

    public void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_jadwal,fragment)
                .commit();
    }

    //fungsi ini digunakan untuk menambahkan data mahasiswa dengan butuh 4 parameter key
    //yang diperlukan (npm, nama, jenis_kelamin, dan prodi) untuk parameter ini harus sama
    //namanya dan hal ini dapat dilihat pada fungsi getParams
    public void tambahJadwal(final String tanggal, final String waktu, final String keterangan){
        //deklarasi queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menambahkan data jadwal");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, JadwalApi.URL_ADD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //bagian jika response berhasil
                        progressDialog.dismiss();
                        try {
                            //mengubah response string menjadi objek
                            JSONObject obj = new JSONObject(response);
                            //obj.getString("status") digunakan untuk mengambil pesan status dari response
//                            if (obj.getString("status").equals("Success")) {
//                                loadFragment(new JadwalFragment());
//                            }
                            loadFragment(new JadwalFragment());

                            //obj.getString("message") digunakan untuk mengambil pesan message dari response
                            Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //bagian jika response jaringan terdapat gangguan/error
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                //proses memasukkan / mengirimkan parameter key dengan data
                //value dan nama key nya harus sesuai dengan parameter key ang diminta
                //oleh jaringan API
                Map<String, String> params = new HashMap<String, String>();
                params.put("tanggal", tanggal);
                params.put("waktu", waktu);
                params.put("keterangan", keterangan);

                return params;
            }
        };

        //proses penambahan request yang sudah kita buat ke request queue
        //yang sudah dideklarasi
        queue.add(stringRequest);
    }

    //fungsi ini digunakan untuk mengubah data mahasiswa dengan butuh 3 parameter key yang
    //diperlukan (nama, jenis_kelamin dan prodi) untuk parameter in iharus sama namanua
    //dan hal ini dapat dilihat pada fungsi getParams
    public void editJadwal(final String id, final String tanggal, final String waktu, final String keterangan){
        //deklarasi queue
        RequestQueue queue = Volley.newRequestQueue(getContext());

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Mengubah data jadwal");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, JadwalApi.URL_UPDATE + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            //mengubah response string menjadi objek
                            JSONObject obj = new JSONObject(response);

                            //obj.geetString("message") digunakan untuk mengambil pesan dari response
                            Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                            loadFragment(new JadwalFragment());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //proses memasukkan / mengirimkan parameter key dengan data
                //value dan nama key nya harus sesuai dengan parameter key ang diminta
                //oleh jaringan API
                Map<String, String> params = new HashMap<String, String>();
                params.put("tanggal", tanggal);
                params.put("waktu", waktu);
                params.put("keterangan", keterangan);

                return params;
            }
        };

        //proses penambahan request yang sudah kita buat ke request queue
        //yang sudah dideklarasi
        queue.add(stringRequest);
    }
}