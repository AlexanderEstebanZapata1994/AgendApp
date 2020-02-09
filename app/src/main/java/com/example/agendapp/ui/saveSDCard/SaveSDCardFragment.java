package com.example.agendapp.ui.saveSDCard;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.agendapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class SaveSDCardFragment extends Fragment {

    private SaveSDCardViewModel saveSDCardViewModel;
    private EditText txtFileName;
    private EditText txtContent;

    private Button btnSave;
    private Button btnSearch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        saveSDCardViewModel =
                ViewModelProviders.of(this).get(SaveSDCardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_save_sd_card, container, false);

        txtFileName = (EditText)root.findViewById(R.id.txtFileName);
        txtContent = (EditText)root.findViewById(R.id.txtContent);

        btnSave = (Button) root.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            SaveFile(v);
            }
        });

        btnSearch = (Button) root.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                SearchFile(v);
            }
        });


        return root;
    }

    public void SaveFile(View view){
        String name = txtFileName.getText().toString();
        String content = txtContent.getText().toString();

        try {
            File sdCard = Environment.getExternalStorageDirectory();
            Snackbar.make(getActivity().findViewById(R.id.fragment_saveSdCard), "Se guardará en la siguiente ruta: "+ sdCard.getPath(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            OutputStreamWriter createFile = new OutputStreamWriter(this.getActivity().openFileOutput(name, Activity.MODE_PRIVATE));

            createFile.write(content);
            createFile.flush();//Limpiamos el contenido del objeto
            createFile.close();

            Toast.makeText(this.getActivity(), "Guardado correctamente", Toast.LENGTH_SHORT).show();
            //Limpiamos los campos de entrada
            txtContent.setText("");
            txtFileName.setText("");
        }
        catch (IOException ex){
            Toast.makeText(this.getActivity(), "No se pudo guardar", Toast.LENGTH_LONG).show();
        }
    }

    public void SearchFile(View view) {
        String name = txtFileName.getText().toString();

        try {
            File sdCard = Environment.getExternalStorageDirectory();
            InputStreamReader loadFile = new InputStreamReader(this.getActivity().openFileInput(name));

            if (loadFile.ready()){
                BufferedReader readFile = new BufferedReader(loadFile);
                String line = readFile.readLine();
                String contentComplet = "";

                while (line != null){
                    contentComplet += line +"\n";
                    line = readFile.readLine();
                }

                readFile.close();
                loadFile.close();

                txtContent.setText(contentComplet);
            }
        }catch (IOException ex){

        }
    }
}