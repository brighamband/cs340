package edu.byu.cs.tweeter.client.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Implements the login screen.
 */
public class LoginFragment extends Fragment implements LoginPresenter.View {

    private EditText alias;
    private EditText password;
    private TextView errorView;

    private LoginPresenter loginPresenter;

    /**
     * Creates an instance of the fragment and places the user and auth token in an arguments
     * bundle assigned to the fragment.
     *
     * @return the fragment.
     */
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        alias = view.findViewById(R.id.loginUsername);
        password = view.findViewById(R.id.loginPassword);
        errorView = view.findViewById(R.id.loginError);
        Button loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            // Login and move to MainActivity.
            loginPresenter.onLoginButtonClick(alias.getText().toString(), password.getText().toString());
        });

        loginPresenter = new LoginPresenter(this);

        // FIXME -- PRE-FILLED LOGIN INFO FOR SIMPLE TESTING -- REMOVE LATER
        alias.setText("@user");
        password.setText("password");
        // FIXME -- PRE-FILLED LOGIN INFO FOR SIMPLE TESTING -- REMOVE LATER

        return view;
    }

    @Override
    public void setErrorViewText(String text) {
        errorView.setText(text);
    }

    // FIXME - DUPLICATED
    @Override
    public void displayToastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void bypassLoginScreen(User loggedInUser, String loggedInAlias) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(MainActivity.CURRENT_USER_KEY, loggedInUser);

        Toast.makeText(getContext(), "Hello " + loggedInAlias, Toast.LENGTH_LONG).show();
        startActivity(intent);
    }
}
