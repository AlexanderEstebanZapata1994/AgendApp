package com.example.agendapp.ui.SqlLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.agendapp.R;
import com.example.agendapp.ui.SqlLite.Database.AdminSQLLiteOpenHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class SqlLiteFragment extends Fragment {

    private EditText txtCode, txtDescription, txtPrice;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sqllite, container, false);
        txtCode = (EditText) root.findViewById(R.id.txtCode);
        txtDescription = (EditText) root.findViewById(R.id.txtDescription);
        txtPrice = (EditText) root.findViewById(R.id.txtPrice);

        addEventListeners(root);
        return root;
    }

    private void addEventListeners(View v) {
        FloatingActionButton fabAdd    = (FloatingActionButton) v.findViewById(R.id.fabAdd);
        FloatingActionButton fabSearch = (FloatingActionButton) v.findViewById(R.id.fabSearch);
        FloatingActionButton fabUpdate = (FloatingActionButton) v.findViewById(R.id.fabUpdate);
        FloatingActionButton fabDelete = (FloatingActionButton) v.findViewById(R.id.fabDelete);

        fabAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addRecord(v);
            }
        });

        fabSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                searchRecord(v);
            }
        });

        fabUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateRecord(v);
            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                deleteRecord(v);
            }
        });
    }

    public void addRecord(View v){
        AdminSQLLiteOpenHelper admin = new AdminSQLLiteOpenHelper(this.getActivity(), "administracion", null, 1);

        SQLiteDatabase db = admin.getWritableDatabase();

        String code = txtCode.getText().toString();
        String description = txtDescription.getText().toString();
        String price = txtPrice.getText().toString();

        if (!code.isEmpty() && !description.isEmpty() && !price.isEmpty()){
            ContentValues contentValues = new ContentValues();
            contentValues.put("code", code);
            contentValues.put("description", description);
            contentValues.put("price", price);

            db.insert("Articulos", null, contentValues);

            db.close();

            txtCode.setText("");
            txtDescription.setText("");
            txtPrice.setText("");

            Toast.makeText(this.getActivity(), "Se guardó correctamente", Toast.LENGTH_SHORT).show();

        } else{
            Snackbar.make(getActivity().findViewById(R.id.fragment_sqllite), "Debes llenar todos los datos", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    }

    public void searchRecord(View v){
        AdminSQLLiteOpenHelper admin = new AdminSQLLiteOpenHelper(this.getActivity(), "administracion", null, 1);

        SQLiteDatabase db = admin.getWritableDatabase();
        String code = txtCode.getText().toString();

        if (! code.isEmpty()){
            try {
                Cursor row = db.rawQuery("SELECT description, price FROM articulos WHERE code = "+code, null);

                if (row.moveToFirst()){
                    txtDescription.setText(row.getString(row.getColumnIndex("description")));
                    txtPrice.setText(row.getString(row.getColumnIndex("price")));
                }else{
                    Toast.makeText(this.getActivity(), "No existe el articulo buscado", Toast.LENGTH_LONG).show();
                }
            }catch (SQLException ex){
                Snackbar.make(this.getActivity().findViewById(R.id.fragment_sqllite), "Ocurrió un error en la consulta", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
            finally {
                db.close();
            }
        }else{
            Snackbar.make(this.getActivity().findViewById(R.id.fragment_sqllite), "Debes introducir el código del producto", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    public void updateRecord(View v){
        AdminSQLLiteOpenHelper admin = new AdminSQLLiteOpenHelper(this.getActivity(), "administracion", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        String code = txtCode.getText().toString();
        String description = txtDescription.getText().toString();
        String price = txtPrice.getText().toString();

        if (!code.isEmpty() && !description.isEmpty() && !price.isEmpty()){
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put("description", description);
                contentValues.put("price", price);

                int quantity = db.update("articulos", contentValues, "code = "+code, null);
                if (quantity ==1){
                   Snackbar.make(this.getActivity().findViewById(R.id.fragment_sqllite), "Se actualizó el articulo correctamente", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }else{
                    Toast.makeText(this.getActivity(), "No existe el articulo que desea actualizar", Toast.LENGTH_SHORT).show();
                }
            }catch (SQLException ex){
                Toast.makeText(this.getActivity(), "Ocurrió un error en la base de datos", Toast.LENGTH_LONG).show();
            }
            finally {
                db.close();
            }
        }else {
            Toast.makeText(this.getActivity(), "Debes llenar todos los campos", Toast.LENGTH_LONG).show();
        }

    }

    public void deleteRecord(View v){
        AdminSQLLiteOpenHelper admin = new AdminSQLLiteOpenHelper(this.getActivity(), "administracion",null, 1);

        SQLiteDatabase db = admin.getWritableDatabase();

        String code = txtCode.getText().toString();
        if (!code.isEmpty()){
            try {
                int quantity = db.delete("articulos", "code=" + code, null);
                txtDescription.setText("");
                txtCode.setText("");
                txtPrice.setText("");
                if (quantity == 1) {
                    Snackbar.make(this.getActivity().findViewById(R.id.fragment_sqllite), "Articulo eliminado correctamente", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Action", null).show();
                }else{
                    Toast.makeText(this.getActivity(), "Articulo no existe",Toast.LENGTH_SHORT).show();
                }
            }catch (SQLException ex){

            }finally {
                db.close();
            }
        }else{
            Toast.makeText(this.getActivity(), "Debes de introducir un código valido", Toast.LENGTH_SHORT).show();
        }
    }
}