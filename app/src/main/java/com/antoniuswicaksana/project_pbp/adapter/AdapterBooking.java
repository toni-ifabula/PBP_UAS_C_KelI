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
import com.antoniuswicaksana.project_pbp.BookingFragment;
import com.antoniuswicaksana.project_pbp.R;
import com.antoniuswicaksana.project_pbp.TambahEditBooking;
import com.antoniuswicaksana.project_pbp.api.BookingApi;
import com.antoniuswicaksana.project_pbp.model.Booking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.android.volley.Request.Method.POST;

public class AdapterBooking extends RecyclerView.Adapter<AdapterBooking.adapterBookingViewHolder> {

    private List<Booking> bookingList;
    private Context context;
    private View view;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public AdapterBooking(Context context, List<Booking> bookingList) {
        this.context=context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public AdapterBooking.adapterBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.adapter_booking, parent, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        return new AdapterBooking.adapterBookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBooking.adapterBookingViewHolder holder, int position) {
        final Booking booking = bookingList.get(position);

        holder.tvPaket.setText(booking.getPaket());
        holder.tvAlamat.setText(booking.getAlamat());
        holder.tvTanggal.setText(booking.getTanggal());
        holder.tvWaktu.setText(booking.getWaktu());

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Bundle data = new Bundle();
                data.putSerializable("booking", booking);
                data.putString("status", "edit");
                TambahEditBooking tambahEditBooking = new TambahEditBooking();
                tambahEditBooking.setArguments(data);
                loadFragment(tambahEditBooking);
            }
        });

        holder.ivHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Anda yakin ingin menghapus booking ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteBooking(booking.getId());

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
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class adapterBookingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPaket, tvTanggal, tvWaktu, tvAlamat, ivEdit, ivHapus;

        public adapterBookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPaket         = (TextView) itemView.findViewById(R.id.tvPaket);
            tvTanggal         = (TextView) itemView.findViewById(R.id.tvTanggal);
            tvWaktu          = (TextView) itemView.findViewById(R.id.tvWaktu);
            tvAlamat = (TextView) itemView.findViewById(R.id.tvAlamat);
            ivEdit          = (TextView) itemView.findViewById(R.id.ivEdit);
            ivHapus         = (TextView) itemView.findViewById(R.id.ivHapus);
        }
    }

    public void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_booking,fragment)
                .commit();
    }

    //Fungsi menghapus data mahasiswa
    public void deleteBooking(String id){
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menghapus data booking");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //memulai membuat permintaan request menghapus data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, BookingApi.URL_DELETE + id,
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
                            loadFragment(new BookingFragment());
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
