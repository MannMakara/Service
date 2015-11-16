package com.hammersmith.john.service.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.IdentityProviders;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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
import java.util.Iterator;
import java.util.Map;

import utils.Constant;

import static com.facebook.FacebookSdk.*;

/**
 * Created by John on 8/27/2015.
 */
public class Tab4 extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

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
    String emailGoogle,personID,personName;
    Uri personPhotoUri;
//    Person currentPerson;
    /*faceBook*/
    String userName, link, email;
    int btnFacebook,btnGoogle;

    /*Google +*/
    SignInButton button;
    Button signOut;
    // Google client to interact with Google API
    public static final int RC_SIGN_IN = 0;
    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    GoogleApiClient googleApiClient;
    /*Google +*/

    TextView txtTitle,mEmail,mSocialLink;
    ImageView googlePro;

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            if(AccessToken.getCurrentAccessToken() != null ) {
                btnFacebook = 2;
                profilePictureView.setVisibility(View.VISIBLE); // Open ImageView Facebook
                button.setVisibility(View.GONE); // Disable Google+ logIn
                //[START Request DATA]
                RequestData();
                //[END Request DATA]
            }
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    private Map<String, String> checkParams(Map<String, String> map){
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
            if(pairs.getValue()==null){
                map.put(pairs.getKey(), "");
            }
        }
        return map;
    }

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_4,container,false);

        /*Text View */
        googlePro = (ImageView) v.findViewById(R.id.googleProfile);
        mEmail = (TextView) v.findViewById(R.id.email);
        mSocialLink = (TextView) v.findViewById(R.id.social_link);
        txtTitle = (TextView) v.findViewById(R.id.title_name);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Gasalt-Regular.ttf");
        txtTitle.setTypeface(font);
        mEmail.setTypeface(font);
        /*Set TypeFace*/
        userNameView = (TextView) v.findViewById(R.id.profileUserName);
        profilePictureView = (ProfilePictureView) v.findViewById(R.id.profilePic);
        if (pendingUpdateForUser != null) {
            setProfile(pendingUpdateForUser);
            pendingUpdateForUser = null;
        }
        // Build GoogleApiClient, don't set account name
        buildGoogleApiClient(null);
        // Sign in button Google Plus
        button = (SignInButton) v.findViewById(R.id.google);
        signOut = (Button) v.findViewById(R.id.googleSignOut);
        Typeface newfont = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Roboto-Medium.ttf");
        signOut.setTypeface(newfont);
        signOut.setText("Sign Out");

        button.setOnClickListener(this);

        signOut.setOnClickListener(this);

        return v;
    }

    private void buildGoogleApiClient(String accountName) {
        GoogleSignInOptions.Builder gsoBuilder = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile();

        if (accountName != null) {
            gsoBuilder.setAccountName(accountName);
        }

        if (googleApiClient != null) {
            googleApiClient.stopAutoManage(getActivity());
        }

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.CREDENTIALS_API)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gsoBuilder.build());

        googleApiClient = builder.build();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginButton = (LoginButton) view.findViewById(R.id.facebook);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));
        if(Profile.getCurrentProfile() != null){
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

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("Tab4", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // ID to Button Google
            btnGoogle = 1;
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            personName = acct.getDisplayName();
            emailGoogle = acct.getEmail();
            personID = acct.getId();
            personPhotoUri = acct.getPhotoUrl();
            // Set Google Plus information to Layout

            userNameView.setText(personName);
            mEmail.setText(emailGoogle);
            mSocialLink.setText(personID);
            Picasso.with(getActivity()).load(personPhotoUri).into(googlePro);
            Toast.makeText(getActivity(), "Hi: " + personName+ " Email: " + emailGoogle + " ID: " + personID + " Photo " + personPhotoUri, Toast.LENGTH_LONG)
                    .show();
            // [UPLOAD data to Server Via Json]
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
                    Map<String, String> params = new HashMap<>();
                    params.put("link", personID);
                    params.put("username", personName);
                    params.put("email", emailGoogle);
                    params.put("social_type", String.valueOf(btnGoogle));
                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(gooleRequest);
            // [END Upload]
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        userNameView.setText("");
                        mEmail.setText("");
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.startTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        setProfile(profile);
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
            button.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.google:
                signIn();
                break;
            case R.id.googleSignOut:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you want to sign out from Google Plus?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        signOut();
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
                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("Tab4", "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onStart() {
        super.onStart();

//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
//        if (opr.isDone()) {
//            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//            // and the GoogleSignInResult will be available instantly.
//            Log.d("Tab4", "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        } else {
//            // If the user has not previously signed in on this device or the sign-in has expired,
//            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//            // single sign-on will occur in this branch.
//            showProgress();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgress();
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
    }

    /* facebook get all user information */
    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object,GraphResponse response) {

                JSONObject json = response.getJSONObject();
                try {
                    if(json != null){
                        userName = json.getString("name");
                        email = json.getString("email");
                        link = json.getString("id");
//                        link = json.getString("link");
                        userNameView.setText(json.getString("name"));
                        mEmail.setText(json.getString("email"));
                        mSocialLink.setText(link);
                        profilePictureView.setProfileId(json.getString("id"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                /*JSON Request*/
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_POST,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Toast.makeText(getApplicationContext(), "Data Inserted Successful", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(getApplicationContext(), volleyError + "", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
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
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * Updating the UI, showing/hiding buttons and profile layout
     * */

    public void updateUI(boolean isSignIN){
        if (isSignIN){
            button.setVisibility(View.GONE);
            signOut.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
            googlePro.setVisibility(View.VISIBLE);
        }
        else {
            button.setVisibility(View.VISIBLE);
            signOut.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
            googlePro.setVisibility(View.GONE);
        }
    }
    
    private void showProgress() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
        }

        progressDialog.show();
    }

    private void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
