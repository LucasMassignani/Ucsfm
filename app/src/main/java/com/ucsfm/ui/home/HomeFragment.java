package com.ucsfm.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ucsfm.MainActivity;
import com.ucsfm.Player;
import com.ucsfm.R;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;

    private LinearLayout btnRadioCaxias;
    private LinearLayout btnRadioBento;
    private LinearLayout btnRadioVacaria;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        btnRadioCaxias = (LinearLayout) root.findViewById(R.id.radio_caxias);

        btnRadioCaxias.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player player = Player.getInstance();
                player.setSelecionadoPeloUsuario(true);
                root.findViewById(R.id.radio_bento).setBackgroundResource(R.drawable.shape_radio);
                root.findViewById(R.id.radio_vacaria).setBackgroundResource(R.drawable.shape_radio);
                v.setBackgroundResource(R.drawable.shape_radio_selecionada);

                player.setRadio(Player.CAXIAS);
                TextView textView = MainActivity.getTextRadio();
                textView.setText("106.5 - Caxias do Sul");
            }
        });

        btnRadioBento = (LinearLayout) root.findViewById(R.id.radio_bento);
        btnRadioBento.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player player = Player.getInstance();
                player.setSelecionadoPeloUsuario(true);
                root.findViewById(R.id.radio_caxias).setBackgroundResource(R.drawable.shape_radio);
                root.findViewById(R.id.radio_vacaria).setBackgroundResource(R.drawable.shape_radio);
                v.setBackgroundResource(R.drawable.shape_radio_selecionada);
                player.setRadio(Player.BENTO);
                TextView textView = MainActivity.getTextRadio();
                textView.setText("89.9 - Bento Gonçalves");
            }
        });

        btnRadioVacaria = (LinearLayout) root.findViewById(R.id.radio_vacaria);
        btnRadioVacaria.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player player = Player.getInstance();
                player.setSelecionadoPeloUsuario(true);
                root.findViewById(R.id.radio_bento).setBackgroundResource(R.drawable.shape_radio);
                root.findViewById(R.id.radio_caxias).setBackgroundResource(R.drawable.shape_radio);
                v.setBackgroundResource(R.drawable.shape_radio_selecionada);
                player.setRadio(Player.VACARIA);
                TextView textView = MainActivity.getTextRadio();
                textView.setText("106.1 - Vacaria");
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        Player player = Player.getInstance();

        player.getSelectedRadioLive().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer value) {
                TextView textView = MainActivity.getTextRadio();
                switch (value) {
                    case Player.BENTO:
                        btnRadioBento.setBackgroundResource(R.drawable.shape_radio_selecionada);
                        btnRadioVacaria.setBackgroundResource(R.drawable.shape_radio);
                        btnRadioCaxias.setBackgroundResource(R.drawable.shape_radio);
                        textView.setText("89.9 - Bento Gonçalves");
                        break;
                    case Player.VACARIA:
                        btnRadioBento.setBackgroundResource(R.drawable.shape_radio);
                        btnRadioVacaria.setBackgroundResource(R.drawable.shape_radio_selecionada);
                        btnRadioCaxias.setBackgroundResource(R.drawable.shape_radio);
                        textView.setText("106.1 - Vacaria");
                        break;
                    default:
                        btnRadioBento.setBackgroundResource(R.drawable.shape_radio);
                        btnRadioVacaria.setBackgroundResource(R.drawable.shape_radio);
                        btnRadioCaxias.setBackgroundResource(R.drawable.shape_radio_selecionada);
                        textView.setText("106.5 - Caxias do Sul");
                        break;
                }
            }
        });
    }
}