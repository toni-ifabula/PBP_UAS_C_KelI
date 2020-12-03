package com.antoniuswicaksana.project_pbp.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.antoniuswicaksana.project_pbp.JadwalFragment;
import com.antoniuswicaksana.project_pbp.R;
import com.antoniuswicaksana.project_pbp.TambahEdit;
import com.antoniuswicaksana.project_pbp.api.JadwalApi;
import com.antoniuswicaksana.project_pbp.model.Jadwal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.android.volley.Request.Method.POST;

public class AdapterJadwal extends RecyclerView.Adapter<AdapterJadwal.adapterJadwalViewHolder>{

    private List<Jadwal> jadwalList;
    private Context context;
    private View view;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public AdapterJadwal(Context context, List<Jadwal> jadwalList) {
        this.context=context;
        this.jadwalList = jadwalList;
    }

    @NonNull
    @Override
    public AdapterJadwal.adapterJadwalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.adapter_jadwal, parent, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        return new adapterJadwalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterJadwal.adapterJadwalViewHolder holder, int position) {
        final Jadwal jadwal = jadwalList.get(position);

        holder.tanggal.setText(jadwal.getTanggal());
        holder.waktu.setText(jadwal.getWaktu());
        holder.keterangan.setText(jadwal.getKeterangan());

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firebaseUser == null){
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Bundle data = new Bundle();
                    data.putSerializable("jadwal", jadwal);
                    data.putString("status", "edit");
                    TambahEdit tambahEdit = new TambahEdit();
                    tambahEdit.setArguments(data);
                    loadFragment(tambahEdit);
                } else {
                    Toast.makeText(context, "Hanya admin yang dapat mengganti jadwal", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.ivHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseUser == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Anda yakin ingin menghapus mahasiswa ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteJadwal(jadwal.getId());

                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            notifyDataSetChanged();
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    Toast.makeText(context, "Hanya admin yang dapat menghapus jadwal", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return jadwalList.size();
    }

    public void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_jadwal,fragment)
                .commit();
    }

    public class adapterJadwalViewHolder extends RecyclerView.ViewHolder {
        private TextView tanggal, waktu, keterangan, ivEdit, ivHapus;

        public adapterJadwalViewHolder(@NonNull View itemView) {
            super(itemView);
            tanggal         = (TextView) itemView.findViewById(R.id.tvTanggal);
            waktu          = (TextView) itemView.findViewById(R.id.tvWaktu);
            keterangan = (TextView) itemView.findViewById(R.id.tvKeterangan);
            ivEdit          = (TextView) itemView.findViewById(R.id.ivEdit);
            ivHapus         = (TextView) itemView.findViewById(R.id.ivHapus);
        }
    }

    //Fungsi menghapus data mahasiswa
    public void deleteJadwal(String id){
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menghapus data jadwal");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, JadwalApi.URL_DELETE + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            //mengubah response string menjadi objek
                            JSONObject obj = new JSONObject(response);

                            //obj.geetString("message") digunakan untuk mengambil pesan dari response
                            Toast.makeText(context, obj.getString("message"), Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                            loadFragment(new JadwalFragment());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //proses penambahan request yang sudah kita buat ke request queue
        //yang sudah dideklarasi
        queue.add(stringRequest);
    }
}
