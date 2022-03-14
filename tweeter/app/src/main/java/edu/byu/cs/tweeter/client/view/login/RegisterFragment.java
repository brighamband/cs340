package edu.byu.cs.tweeter.client.view.login;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.presenter.RegisterPresenter;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Implements the register screen.
 */
public class RegisterFragment extends Fragment implements RegisterPresenter.View {
    private static final int RESULT_IMAGE = 10;

    private EditText firstName;
    private EditText lastName;
    private EditText alias;
    private EditText password;
    private Button imageUploaderButton;
    private Button registerButton;
    private ImageView imageToUpload;
    private TextView errorView;
    private Toast registeringToast;

    private RegisterPresenter registerPresenter;

    /**
     * Creates an instance of the fragment and places the user and auth token in an arguments
     * bundle assigned to the fragment.
     *
     * @return the fragment.
     */
    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        firstName = view.findViewById(R.id.registerFirstName);
        lastName = view.findViewById(R.id.registerLastName);
        alias = view.findViewById(R.id.registerUsername);
        password = view.findViewById(R.id.registerPassword);
        imageUploaderButton = view.findViewById(R.id.imageButton);
        imageToUpload = view.findViewById(R.id.registerImage);
        registerButton = view.findViewById(R.id.registerButton);
        errorView = view.findViewById(R.id.registerError);

        imageUploaderButton.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(gallery, RESULT_IMAGE);
        });

        registerButton.setOnClickListener(v -> {
            // Register and move to MainActivity.
            registerPresenter.onRegisterButtonClick(firstName.getText().toString(), lastName.getText().toString(),
                    alias.getText().toString(), password.getText().toString(), imageToUpload);
        });

        registerPresenter = new RegisterPresenter(this);

        return view;
    }

    @Override
    public void setErrorViewText(String text) {
        errorView.setText(text);
    }

    // Get image if uploaded from gallery.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageToUpload.setImageURI(selectedImage);
            imageUploaderButton.setText(R.string.afterUploadPicture);
        }
    }

    @Override
    public void displayToastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void bypassRegisterScreen(User registeredUser, String registeredAlias) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER_KEY, registeredUser);

        Toast.makeText(getContext(), "Hello " + registeredAlias, Toast.LENGTH_LONG).show();
        startActivity(intent);
    }
}
