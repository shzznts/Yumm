package com.yumm;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Gets new recipe data from the url passed in. Parses the JSON response and
 * sets the data for a Recipe. Notifies the RecipeAdapter when the Recipe's data
 * is ready and when the Recipe's Bitmap image has loaded.
 * 
 */
public class NewRecipeTask extends AsyncTask<String, Integer, String> {
	private Recipe recipe;
	private RecipeAdapter adapter;

	public NewRecipeTask(Recipe recipe, RecipeAdapter adapter) {
		this.recipe = recipe;
		this.adapter = adapter;
	}

	@Override
	protected String doInBackground(String... urls) {
		String url = urls[0];
		try {
			HttpUriRequest request = new HttpGet(new URI(url));
			HttpClient client = new DefaultHttpClient();
			HttpResponse serverResponse = client.execute(request);
			BasicResponseHandler handler = new BasicResponseHandler();
			String response = handler.handleResponse(serverResponse);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(String result) {
		if (result != null && !result.equals("")) {
			RecipeHandler.setRecipeData(recipe, result);
			Log.i("DataLoadTask", "Retrieved data for: " + recipe.getName());
			adapter.notifyDataSetChanged();
			new ImageLoadTask(recipe, adapter).execute(recipe.getThumbUrl());
		}
	}
}
