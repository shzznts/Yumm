package com.yumm;

import java.util.List;

import android.app.ListActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class FavoriteRecipesActivity extends ListActivity {
	FavoriteRecipesDbHelper dbHelper;
	private List<Recipe> recipes;
	private RecipeAdapter recipeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipes);
		dbHelper = new FavoriteRecipesDbHelper(this);

		// Load recipes from db
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		recipes = dbHelper.getFavorites(db);
		db.close();
		// Bind to adapter, then load images asyncly
		recipeAdapter = new RecipeAdapter(this, recipes);
		setListAdapter(recipeAdapter);
		for (Recipe recipe : recipes) {
			new ImageLoadTask(recipe, recipeAdapter).execute(recipe.getThumbUrl());
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case (R.id.menu_new_recipes):
				finish();
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.favorite_recipes, menu);
		return true;
	}

}
