package in.santiniketan.pdfreader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv_pdf;
    public static ArrayList<File> fileList = new ArrayList<>();
    PDFAdapter obj_adapter;
    public static int REQUEST_PERMISSION = 1;
    boolean bolean_permission;
    File dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_pdf = (ListView)findViewById(R.id.listView_pdf);
        dir = new File(Environment.getExternalStorageDirectory().toString());
        permission_fn();

        lv_pdf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(getApplicationContext(),ViewPDFFiles.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });


    }

    private void permission_fn() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)){

            }
            else {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION);
            }
        } else {
            bolean_permission = true;
            getfile(dir);
            obj_adapter = new PDFAdapter(getApplicationContext(),fileList);
            lv_pdf.setAdapter(obj_adapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                bolean_permission = true;
                getfile(dir);
                obj_adapter = new PDFAdapter(getApplicationContext(),fileList);
                lv_pdf.setAdapter(obj_adapter);
            }
            else {
                Toast.makeText(this,"Please Allow the Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ArrayList<File> getfile(File dir){

        File listFile[] = dir.listFiles();
        if (listFile!=null && listFile.length>0){

            for (int i=0; i<listFile.length;i++){

                if (listFile[i].isDirectory()){
                    getfile(listFile[i]);
                }
                else {

                    boolean booleanpdf = false;
                    if (listFile[i].getName().endsWith(".pdf")){
                        for (int j=0;j<fileList.size();j++){
                            if (fileList.get(j).getName().equals(listFile[i].getName())){
                                booleanpdf =true;
                            }
                            else {

                            }
                        }
                        if(booleanpdf){
                            booleanpdf =false;
                        }
                        else {
                            fileList.add(listFile[i]);
                        }
                    }
                }
            }
        }
        return fileList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.about:
                Toast.makeText(this, "Tinku Mahato", Toast.LENGTH_SHORT).show();
                break;

            case R.id.exit:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Do you want to exit")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                MainActivity.super.onBackPressed();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                return  true;

        }
        return true;
    }

}
