package com.hammersmith.john.service.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.hammersmith.john.service.R;
import com.hammersmith.john.service.controller.AppController;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import utils.Constant;

import static com.facebook.FacebookSdk.*;

/**
 * Created by John on 8/27/2015.
 */
public class Tab4 extends Fragment implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    ProgressDialog progressDialog;

    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    ProfileTracker profileTracker;
    LoginButton loginButton;

    TextView userNameView;
    ProfilePictureView profilePictureView;
    Profile pendingUpdateForUser;
    /*Google*/
    String emailGoogle,personGooglePlusProfile,personPhotoUrl,personName;
    Person currentPerson;
    /*faceBook*/
    String userName, link, email;
    int btnFacebook,btnGoogle;

    /*Google +*/
    SignInButton button;
    Button signOut;
    // Google client to interact with Google API
    GoogleApiClient apiClient;
    public static final int RC_SIGN_IN = 0;
    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;
    private boolean mSignInClicked;
    private ConnectionResult mConnectionResult;
    GoogleApiClient googleApiClient;
    /*Google +*/

    TextView txtTitle,mEmail,mSocialLink;
    ImageView googlePro;

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
//            AccessToken accessToken = loginResult.getAccessToken();
//            Profile profile = Profile.getCurrentProfile();
            if(AccessToken.getCurrentAccessToken() != null){
                btnFacebook = 2;
                profilePictureView.setVisibility(View.VISIBLE); // Open ImageView Facebook
//                button.setVisibility(View.GONE); // Disable Google+ logIn
                Toast.makeText(getApplicationContext(),btnFacebook+"",Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),AccessToken.getCurrentAccessToken()+"",Toast.LENGTH_SHORT).show();
                RequestData();
                /*JSON Request*/
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_POST,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Toast.makeText(getApplicationContext(),"Data Inserted Successful",Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(getApplicationContext(), volleyError+"", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("link", link);
                        params.put("username", userName);
                        params.put("email", email);
                        params.put("social_type", String.valueOf(btnFacebook));
                        return params;
                    }
                };

                AppController.getInstance().addToRequestQueue(stringRequest);
            /*Finish JsonRequest*/
            }

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sdkInitialize(getActivity());

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken1) {
                Profile.fetchProfileForCurrentAccessToken();
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile1) {
                setProfile(profile1);
            }
        };

        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();
        Profile.fetchProfileForCurrentAccessToken();
        setProfile(Profile.getCurrentProfile());
//        accessTokenTracker.stopTracking();
//        profileTracker.stopTracking();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_4,container,false);
        /*Google Plus*/
        /**
         * API to return GoogleApiClient Make sure to create new after revoking
         * access or for first time sign in
         *
         * @return
         */

        /*Text View */
        googlePro = (ImageView) v.findViewById(R.id.googleProfile);
        txtTitle = (TextView) v.findViewById(R.id.title_name);
        mEmail = (TextView) v.findViewById(R.id.email);
        mSocialLink = (TextView) v.findViewById(R.id.social_link);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Gasalt-Regular.ttf");
        txtTitle.setTypeface(font);
        mEmail.setTypeface(font);
        /*Set TypeFace*/


        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();
        /*Google Plus*/

        userNameView = (TextView) v.findViewById(R.id.profileUserName);
        profilePictureView = (ProfilePictureView) v.findViewById(R.id.profilePic);
        if (pendingUpdateForUser != null) {
            setProfile(pendingUpdateForUser);
            pendingUpdateForUser = null;
        }

        /*Google*/

        button = (SignInButton) v.findViewById(R.id.google);
        signOut = (Button) v.findViewById(R.id.googleSignOut);
        signOut.setText("Sign Out");
        // Initializing google plus api client

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!googleApiClient.isConnecting()) {
                    mSignInClicked = true;
                    resolveSignInError();
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Authenticating...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                }
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you want to sign out from Google Plus?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signOutGoogle();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginButton = (LoginButton) view.findViewById(R.id.facebook);
        loginButton.setReadPermissions(Arrays.asList("public_profile,email"));
        if(AccessToken.getCurrentAccessToken() != null){
            RequestData();
        }
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

//        Fragment fragment = (Fragment) getChildFragmentManager().findFragmentByTag(null);
//        if (fragment != null)
//            fragment.onActivityResult(requestCode, resultCode, data);

        /*Google Plus*/
        if (requestCode == RC_SIGN_IN){
            if (resultCode != Activity.RESULT_OK){
                mSignInClicked = false;
            }
            mIntentInProgress = false;
            if (!googleApiClient.isConnecting()){
                googleApiClient.connect();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.startTracking();

        /*Google Plus*/
        if (googleApiClient.isConnected())
            googleApiClient.disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
        accessTokenTracker.stopTracking();
    }

    private void setProfile(Profile profile) {

        if (userNameView == null || profilePictureView == null || !isAdded()) {
            // Fragment not yet added to the view. So let's store which user was intended
            // for display.
            pendingUpdateForUser = profile;
            return;
        }

        if (profile == null) {
            profilePictureView.setProfileId(null);
            userNameView.setText("");
            mEmail.setText("");
            profilePictureView.setVisibility(View.GONE);
        } else {
//            profilePictureView.setProfileId(profile.getId());
//            userNameView.setText("Welcome " + profile.getName() + "!!!!!!!"+ email);
        }
    }

    /**
     * Callback for GoogleApiClient connection success
     */
    @Override
    public void onConnected(Bundle bundle) {
        if (progressDialog.isShowing() == true) {
            progressDialog.dismiss();
        }
        mSignInClicked = false;
        Toast.makeText(getApplicationContext(), "Sign In successfully", Toast.LENGTH_LONG).show();
        // Get user's information
        getProfileInformation();

        btnGoogle = 1;

        Toast.makeText(getApplicationContext(),btnGoogle+"",Toast.LENGTH_SHORT).show();

        /*JSON String Request*/
        /*JSON Request*/
        StringRequest gooleRequest = new StringRequest(Request.Method.POST, Constant.URL_POST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Toast.makeText(getApplicationContext(),"Data from Google Inserted Successful",Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), volleyError+"", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("link", personGooglePlusProfile);
                params.put("username", personName);
                params.put("email", emailGoogle);
                params.put("social_type", String.valueOf(btnGoogle));
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(gooleRequest);
        /*Finish JsonRequest*/

        // Update the UI after signin
        updateUI(true);

    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()){
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(),getActivity(),0).show();
            return;
        }
        if (!mIntentInProgress){
            // Store the ConnectionResult for later usage
            mConnectionResult = connectionResult;
            if (mSignInClicked){
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()){
            mIntentInProgress = true;
            try {
                mConnectionResult.startResolutionForResult(getActivity(), RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                googleApiClient.connect();
                e.printStackTrace();
            }
        }
    }


    /**
     * Fetching user's information name, email, profile pic
     * */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(googleApiClient) != null){

                currentPerson = Plus.PeopleApi.getCurrentPerson(googleApiClient);
//                String personName;
                personName = currentPerson.getDisplayName();
//                String personPhotoUrl;
                personPhotoUrl = currentPerson.getImage().getUrl();
                personPhotoUrl = personPhotoUrl.substring(0,personPhotoUrl.indexOf("sz=")+3)+"155";
//                String personGooglePlusProfile;
                personGooglePlusProfile = currentPerson.getUrl();

                emailGoogle = Plus.AccountApi.getAccountName(googleApiClient);
                Toast.makeText(getActivity(), "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + emailGoogle
                        + ", Image: " + personPhotoUrl,Toast.LENGTH_SHORT).show();

                userNameView.setText(personName);
                mEmail.setText(emailGoogle);
                mSocialLink.setText(personGooglePlusProfile);
                Picasso.with(getActivity()).load(personPhotoUrl).into(googlePro);
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /* facebook get all user information */
    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object,GraphResponse response) {

                JSONObject json = response.getJSONObject();
                try {
                    if(json != null){
                        String text;
//                        text = "<b>Name :</b> "+json.getString("name")+"<br><br><b>Email :</b> "+json.getString("email")+"<br><br><b>Profile link :</b> "+json.getString("link");
                        userName = json.getString("name");
                        email = json.getString("email");
                        link = json.getString("link");
//                        userNameView.setText(Html.fromHtml(text));
                        userNameView.setText(json.getString("name"));
                        mEmail.setText(json.getString("email"));
                        mSocialLink.setText(json.getString("link"));
                        profilePictureView.setProfileId(json.getString("id"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * Sign-out from google
     * */

    public void signOutGoogle(){
        if (googleApiClient.isConnected()){
            Plus.AccountApi.clearDefaultAccount(googleApiClient);
            googleApiClient.disconnect();
            googleApiClient.connect();
            updateUI(false);
            userNameView.setText("");
            mEmail.setText("");
            googlePro.setVisibility(View.GONE);
        }
    }

    /**
     * Updating the UI, showing/hiding buttons and profile layout
     * */

    public void updateUI(boolean isSignIN){
        if (isSignIN){
            button.setVisibility(View.GONE);
            signOut.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            profilePictureView.setVisibility(View.GONE);
        }
        else {
            button.setVisibility(View.VISIBLE);
            signOut.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
            profilePictureView.setVisibility(View.GONE);
            googlePro.setVisibility(View.GONE);
        }
    }
}
