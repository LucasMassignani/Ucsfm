package com.ucsfm;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private static ImageButton btnPlayStop;
    private static TextView textRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        btnPlayStop = findViewById(R.id.playStop);
        textRadio = findViewById(R.id.radio_tocando);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Player.getInstance();
        pedirPermissoes();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void btnPlayPause(View view){
        Player player = Player.getInstance();
        player.togglePlayPause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Player player = Player.getInstance();
        player.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MediaPlayer mediaPlayer = Player.getInstance().getMediaPlayer();
        mediaPlayer.release();
    }

    public static ImageButton getBtnPlayStop() {
        return btnPlayStop;
    }

    public static TextView getTextRadio() {
        return textRadio;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configurarServico();
                }
                return;
            }
        }
    }


    private void pedirPermissoes() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]
                    {
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 1);
        } else {
            configurarServico();
        }
    }

    public void configurarServico(){
        try {
            MainActivity context = this;
            final LocationManager[] locationManager = {(LocationManager) getSystemService(this.LOCATION_SERVICE)};
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    try {
                        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

                        List<Address> addresses = null;
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        String cityName = addresses.get(0).getAddressLine(0);
                        Player player = Player.getInstance();
                        if(!player.getSelecionadoPeloUsuario()) {
                            if(cityName.contains("Caxias do Sul")) {
                                player.setRadio(Player.CAXIAS);
                            } else if(cityName.contains("Bento Gonçalves")) {
                                player.setRadio(Player.BENTO);

                            } else if(cityName.contains("Vacaria")) {
                                player.setRadio(Player.VACARIA);
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                public void onStatusChanged(String provider, int status, Bundle extras) { }
                public void onProviderEnabled(String provider) { }
                public void onProviderDisabled(String provider) { }
            };

            locationManager[0].requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }catch(SecurityException ex){
            ex.printStackTrace();
        }
    }
}