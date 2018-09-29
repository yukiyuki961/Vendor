package com.valjapan.vendor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String spinnerItems[] = {"コカコーラ", "DyDo", "アサヒ", "キリン", "サントリー", "ポッカサッポロ", "明治", "災害支援型", "その他"};
    private double locateX, locateY;
    EditText contentEdiText;
    String context;
    private GoogleMap previewMap;
    private LatLng latlng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Log.d("AddActivity", "onCreate()");

        Intent intent = getIntent();
        locateX = intent.getDoubleExtra("LocateX", 0);
        locateY = intent.getDoubleExtra("LocateY", 0);
        Log.d("AddActivity", "渡された座標は\n緯度" + String.valueOf(locateX) + "\n経度" + String.valueOf(locateY) + "\nです。");


        contentEdiText = (EditText) findViewById(R.id.contentEditText);

        Spinner spinner = findViewById(R.id.spinner);

        // ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // spinner に adapter をセット
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //　アイテムが選択された時
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
                Log.d("AddActivity", item);
            }

            //　アイテムが選択されなかった
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_add);
        mapFragment.getMapAsync(this);
    }

    public void saveData(View v) {
        context = contentEdiText.getText().toString();
        if (TextUtils.isEmpty(context)) {
            context = "特になし";
        }
        Log.d("AddActivity", "保存ボタンを押しました");
        Log.d("AddActivity", "EditTextの内容は -> " + context);
        finish();
    }

    /*
    GoogleMapApiを利用したクラス
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        previewMap = googleMap;

        // Add a marker in Sydney and move the camera
        latlng = new LatLng(locateX, locateY);
        previewMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));

        // marker 追加
        previewMap.addMarker(new MarkerOptions().position(latlng).title("この場所を追加します"));
        // camera 移動
        previewMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_location) {
            previewMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18));
        }
        return true;
    }
}
