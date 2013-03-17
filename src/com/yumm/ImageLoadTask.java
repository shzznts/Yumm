package com.yumm;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class ImageLoadTask extends AsyncTask<String, String, Bitmap> {
	private Recipe recipe;
	private RecipeAdapter adapter;

	public ImageLoadTask(Recipe recipe, RecipeAdapter adapter) {
		this.recipe = recipe;
		this.adapter = adapter;
	}

	@Override
	protected Bitmap doInBackground(String... urls) {
		try {
			Bitmap b = getBitmapFromURL(urls[0]);
			return b;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(Bitmap response) {
		if (response != null) {
			recipe.setImage(response);
			adapter.notifyDataSetChanged();
		}
	}

	private static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
