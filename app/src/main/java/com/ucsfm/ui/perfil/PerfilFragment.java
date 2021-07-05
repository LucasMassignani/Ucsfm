package com.ucsfm.ui.perfil;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.MutableLiveData;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ucsfm.MainActivity;
import com.ucsfm.R;
import com.ucsfm.database.model.User;

import java.io.File;

public class PerfilFragment extends Fragment {
    private PerfilViewModel mViewModel;
    private String urlPicture;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.perfil_fragment, container, false);

        Button btnSalvar = (Button) root.findViewById(R.id.btn_save);

        btnSalvar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextName = root.findViewById(R.id.name);
                EditText editTextEmail = root.findViewById(R.id.email);

                String name = editTextName.getText().toString();
                String email= editTextEmail.getText().toString();

                MutableLiveData loggedUser = MainActivity.getLoggedUser();
                loggedUser.setValue(new User(name, email, urlPicture));
            }
        });

        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        ActivityResultLauncher<Intent> changePicture = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Activity activity = getActivity();
                            Uri selectedImage = data.getData();
                            String[] filePath = {MediaStore.Images.Media.DATA};
                            Cursor c = activity.getContentResolver().query(selectedImage, filePath, null,
                                    null, null);
                            c.moveToFirst();
                            int columnIndex = c.getColumnIndex(filePath[0]);
                            String picturePath = c.getString(columnIndex);
                            c.close();
                            File arquivoFoto = new File(picturePath);
                            urlPicture = arquivoFoto.getAbsolutePath();
                            ImageButton foto = activity.findViewById(R.id.picture);
                            Bitmap bitmap =
                                    BitmapFactory.decodeFile(urlPicture);
                            foto.setImageBitmap(bitmap);
                        }
                    }
                }
            }
        );

        ImageButton foto = root.findViewById(R.id.picture);

        foto.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                changePicture.launch(intent);
            }
        });

        return root;
    }
}