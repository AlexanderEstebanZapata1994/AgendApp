package com.example.agendapp.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.agendapp.R;
import com.google.android.material.snackbar.Snackbar;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private EditText editTextNombre;
    private EditText editTextDatos;

    private Button btnGuardar;
    private Button btnBuscar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        editTextNombre = (EditText) root.findViewById(R.id.txt_Nombre);
        editTextDatos = (EditText) root.findViewById(R.id.txt_Datos);

        btnBuscar = (Button)root.findViewById(R.id.btnBuscar);

        btnBuscar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Buscar(v);
            }
        });
        btnGuardar = (Button) root.findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Guardar(v);
            }
        });
        return root;
    }

    //Método para guardar
    public void Guardar(View view){
        String nombre = editTextNombre.getText().toString();
        String datos = editTextDatos.getText().toString();

        SharedPreferences preferences = this.getActivity().getSharedPreferences("agenda", Context.MODE_PRIVATE);
        SharedPreferences.Editor objEditor = preferences.edit();
        objEditor.putString(nombre, datos);
        objEditor.commit();

        Snackbar.make(getActivity().findViewById(R.id.fragment_home), "La información ha sido guardada correctamente.", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        //Toast.makeText(this.getActivity(), "La información ha sido guardada correctamente.", Toast.LENGTH_LONG).show();
    }

    //Metodo para buscar
    public void Buscar(View view){
        String nombre = editTextNombre.getText().toString();

        SharedPreferences preferences = this.getActivity().getSharedPreferences("agenda", Context.MODE_PRIVATE);
        String datos = preferences.getString(nombre, "");

        if (datos.length() == 0){
            Toast.makeText(this.getActivity(), "No se encontró ningún registro.", Toast.LENGTH_LONG).show();
        }else {
            editTextDatos.setText(datos);
        }
    }
}