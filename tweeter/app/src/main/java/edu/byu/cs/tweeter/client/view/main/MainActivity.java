package edu.byu.cs.tweeter.client.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import edu.byu.cs.client.R;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.view.login.LoginActivity;
import edu.byu.cs.tweeter.client.view.login.StatusDialogFragment;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * The main activity for the application. Contains tabs for feed, story,
 * following, and followers.
 */
public class MainActivity extends AppCompatActivity implements StatusDialogFragment.Observer, MainPresenter.View {

  public static final String CURRENT_USER_KEY = "CurrentUser";

  private Toast logoutToast;
  private User selectedUser;
  private TextView followeeCount;
  private TextView followerCount;
  private Button followButton;

  private MainPresenter mainPresenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mainPresenter = new MainPresenter(this);

    selectedUser = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
    if (selectedUser == null) {
      throw new RuntimeException("User not passed to activity");
    }

    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(),
        selectedUser);
    ViewPager viewPager = findViewById(R.id.view_pager);
    viewPager.setOffscreenPageLimit(1);
    viewPager.setAdapter(sectionsPagerAdapter);
    TabLayout tabs = findViewById(R.id.tabs);
    tabs.setupWithViewPager(viewPager);

    FloatingActionButton fab = findViewById(R.id.fab);

    fab.setOnClickListener(v -> {
      StatusDialogFragment statusDialogFragment = new StatusDialogFragment();
      statusDialogFragment.show(getSupportFragmentManager(), "post-status-dialog");
    });

    fetchFollowingAndFollowersCounts();

    TextView userName = findViewById(R.id.userName);
    userName.setText(selectedUser.getName());

    TextView userAlias = findViewById(R.id.userAlias);
    userAlias.setText(selectedUser.getAlias());

    ImageView userImageView = findViewById(R.id.userImage);
    Picasso.get().load(selectedUser.getImageUrl()).into(userImageView);

    followeeCount = findViewById(R.id.followeeCount);
    followeeCount.setText(getString(R.string.followeeCount, "..."));

    followerCount = findViewById(R.id.followerCount);
    followerCount.setText(getString(R.string.followerCount, "..."));

    followButton = findViewById(R.id.followButton);

    if (selectedUser.compareTo(Cache.getInstance().getCurrUser()) == 0) {
      followButton.setVisibility(View.GONE);
    } else {
      followButton.setVisibility(View.VISIBLE);
      mainPresenter.isFollower(selectedUser);
    }

    followButton.setOnClickListener(v -> {
      mainPresenter.onFollowButtonClick(
          followButton.getText().toString().equals(v.getContext().getString(R.string.following)),
          selectedUser);
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == R.id.logoutMenu) {
      logoutToast = Toast.makeText(this, "Logging Out...", Toast.LENGTH_LONG);
      logoutToast.show();
      mainPresenter.logout();
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onStatusPosted(String post) {
    mainPresenter.postStatus(post);
  }

  @Override
  public void returnToLoginScreen() {
    // Revert to login screen
    Intent intent = new Intent(this, LoginActivity.class);
    // Clear everything so that the main activity is recreated with the login page
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

    logoutToast.cancel();

    startActivity(intent);
  }

  @Override
  public void displayToastMessage(String message) {
    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
  }

  @Override
  public void displayFolloweeCount(int count) {
    followeeCount.setText(getString(R.string.followeeCount, String.valueOf(count)));
  }

  @Override
  public void displayFollowersCount(int count) {
    followerCount.setText(getString(R.string.followerCount, String.valueOf(count)));
  }

  /**
   * Get count of most recently selected user's followers and followees (who they
   * are following).
   */
  @Override
  public void fetchFollowingAndFollowersCounts() {
    mainPresenter.getFollowingAndFollowersCounts(selectedUser);
  }

  @Override
  public void updateFollowButton(boolean currentlyFollowing) {
    // If follow relationship was removed.
    if (currentlyFollowing) {
      followButton.setText(R.string.following);
      followButton.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.white));
      followButton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.lightGray));
    } else {
      followButton.setText(R.string.follow);
      followButton.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
      followButton.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.white));
    }
  }

  @Override
  public void setEnabledFollowButton(boolean makeEnabled) {
    followButton.setEnabled(makeEnabled);
  }
}
