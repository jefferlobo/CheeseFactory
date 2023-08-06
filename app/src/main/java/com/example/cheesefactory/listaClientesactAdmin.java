package com.example.cheesefactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cheesefactory.Adapter.clientesAdapterAdmin;
import com.example.cheesefactory.Modelo.Clientes;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class listaClientesactAdmin extends AppCompatActivity {

    private Button btnAgregarClientes;

    private View root;
    SearchView srvClientes;
    RecyclerView mRecyclerView;
    clientesAdapterAdmin mClientesAbdapter;
    FirebaseFirestore mFirestore;
    Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientesact_admin);

        iniciarControles();
        mFirestore=FirebaseFirestore.getInstance();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        query=mFirestore.collection("Clientes");
        FirestoreRecyclerOptions<Clientes> firestoreRecyclerOptions=
                new FirestoreRecyclerOptions.Builder<Clientes>()
                        .setQuery(query,Clientes.class).build();
        mClientesAbdapter=new clientesAdapterAdmin(firestoreRecyclerOptions, this,getSupportFragmentManager());
        mClientesAbdapter.notifyDataSetChanged();
        mRecyclerView.setItemAnimator(null);
        mRecyclerView.setAdapter(mClientesAbdapter);
        searchView();
        btnAgregarClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),Content.class);

                i.putExtra("toShow","nuevoCliente");
                startActivity(i);

            }
        });



    }
    private void searchView(){
        srvClientes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                textSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                textSearch(s);
                return false;
            }
        });
    }




    private void textSearch(String s) {

        FirestoreRecyclerOptions<Clientes>firestoreRecyclerOptions=
                new FirestoreRecyclerOptions.Builder<Clientes>().setQuery(query.orderBy("nombreCliente")
                        .startAt(s).endAt(s+"~"),Clientes.class).build();
        mClientesAbdapter=new clientesAdapterAdmin(firestoreRecyclerOptions,this,getSupportFragmentManager());
        mClientesAbdapter.startListening();
        mRecyclerView.setAdapter(mClientesAbdapter);
    }

    private void iniciarControles(){
        btnAgregarClientes=findViewById(R.id.btnAgregarClientesActAdmin);
        srvClientes=findViewById(R.id.srvClientesActAdmin);
        mRecyclerView=findViewById(R.id.rvlistaClientesActAdmin);
    }
    @Override
    public void onStart() {
        super.onStart();
        mClientesAbdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClientesAbdapter.stopListening();
    }

}