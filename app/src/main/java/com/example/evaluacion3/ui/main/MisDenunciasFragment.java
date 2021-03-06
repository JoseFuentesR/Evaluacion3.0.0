package com.example.evaluacion3.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.evaluacion3.R;
import com.example.evaluacion3.adapter.AdapterDenuncia;
import com.example.evaluacion3.ui.main.model.Denuncia;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MisDenunciasFragment extends Fragment {


    FirebaseAuth auth;
    List<Denuncia> list;
    FirebaseDatabase database;
    DatabaseReference myRef;
    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mis_denuncias, container, false);


        recyclerView = view.findViewById(R.id.recicler_denuncia);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        String uid = auth.getCurrentUser().getUid();
        myRef = database.getReference("denuncias").child(uid);
        list = new ArrayList<>();


        loadTask();
        return view;
    }

    public void loadTask(){


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    list.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        Denuncia denuncia = ds.getValue(Denuncia.class);
                        denuncia.setId(ds.getKey());
                        list.add(denuncia);
                    }

                    LinearLayoutManager lm = new LinearLayoutManager(getActivity());
                    lm.setOrientation(RecyclerView.VERTICAL);
                    AdapterDenuncia ad = new AdapterDenuncia(getActivity(),R.layout.item_denuncia,list);
                    recyclerView.setLayoutManager(lm);
                    recyclerView.setAdapter(ad);

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }
}