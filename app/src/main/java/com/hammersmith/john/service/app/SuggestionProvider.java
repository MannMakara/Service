package com.hammersmith.john.service.app;

import android.app.SearchManager;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.hammersmith.john.service.controller.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import utils.Constant;

public class SuggestionProvider extends SearchRecentSuggestionsProvider {

  public final static String AUTHORITY = SuggestionProvider.class.getName();
  public final static int MODE = SearchRecentSuggestionsProvider.DATABASE_MODE_2LINES
      | SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES;

  public SuggestionProvider() {
    super();
    setupSuggestions(AUTHORITY, MODE);
  }

//  private static Random RANDOM = new Random(System.currentTimeMillis());
//
//  public static String generateRandomSuggestion() {
//    return sCheeseStrings[RANDOM.nextInt(sCheeseStrings.length)];
//  }
}
