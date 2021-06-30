package com.ucsfm;

import android.media.AudioManager;
import android.media.MediaPlayer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;

public class Player {
    public static final int CAXIAS = 0;
    public static final int BENTO = 1;
    public static final int VACARIA = 2;

    public static String STREAM_CAXIAS="https://play.radio.br:8089/UCS/FMCaxias/icecast.audio";
    public static String STREAM_BENTO="https://play.radio.br:8089/ucsfmbento/ucsfmbento/icecast.audio";
    public static String STREAM_VACARIA="https://stream1.play.radio.br:8089/UCS/FMVacaria/icecast.audio";
    private static Player _instance;

    MediaPlayer mediaPlayer;
    boolean isPlaying = false;
    private boolean selecionadoPeloUsuario = false;
    MutableLiveData<Integer> selectedRadio;

    private Player() {
        selectedRadio = new MutableLiveData<>();
        selectedRadio.setValue(0);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(STREAM_CAXIAS);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRadio(int radio) {
        String stream;
        switch (radio) {
            case BENTO:
                stream = STREAM_BENTO;
                selectedRadio.setValue(BENTO);
                break;
            case VACARIA:
                stream = STREAM_VACARIA;
                selectedRadio.setValue(VACARIA);
                break;
            default:
                stream = STREAM_CAXIAS;
                selectedRadio.setValue(CAXIAS);
                break;
        }

        if(mediaPlayer!=null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(stream);
            mediaPlayer.prepare();
            play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        mediaPlayer.start();
        MainActivity.getBtnPlayStop().setImageResource(R.drawable.pause);
        isPlaying = true;
    }

    public void pause() {
        mediaPlayer.pause();
        MainActivity.getBtnPlayStop().setImageResource(R.drawable.play);
        isPlaying = false;
    }

    public void togglePlayPause() {
        if(isPlaying) {
            pause();
        } else {
            play();
        }
    }

    public boolean getSelecionadoPeloUsuario() {
        return selecionadoPeloUsuario;
    }

    public void setSelecionadoPeloUsuario(boolean valor) {
        selecionadoPeloUsuario = valor;
    }

    public Boolean getIsPlaying() {
        return isPlaying;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public int getSelectedRadio() {
        return selectedRadio.getValue();
    }

    public LiveData<Integer> getSelectedRadioLive() {
        return selectedRadio;
    }

    public static Player getInstance()
    {
        if (_instance == null)
        {
            _instance = new Player();
        }
        return _instance;
    }
}
