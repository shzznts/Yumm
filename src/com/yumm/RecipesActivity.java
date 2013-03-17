package com.yumm;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class RecipesActivity extends ListActivity {
	private List<Recipe> recipes;
	private RecipeAdapter adapter;
	private FavoriteRecipesDbHelper favoritesHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipes);
		// Register context menu
		registerForContextMenu(getListView());
		// Instantiate the db helper
		favoritesHelper = new FavoriteRecipesDbHelper(this);
		// Load recipes to view
		recipes = RecipeHandler.generateRecipes(RecipeHandler.DEFAULT_RECIPES_TO_LOAD);
		adapter = new RecipeAdapter(this, recipes);
		setListAdapter(adapter);
		for (Recipe recipe : recipes) {
			RecipeHandler.getNewData(recipe, adapter);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
			case R.id.add_favorites:
				// Add recipe to favorites
				SQLiteDatabase db = favoritesHelper.getWritableDatabase();
				Recipe recipe = recipes.get((int) info.id);
				favoritesHelper.addToFavorites(db, recipe);
				db.close();
		}
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.recipe_longclick, menu);
		menu.setHeaderTitle("Choose an Option");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recipes, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_get_new_recipes:
				List<Recipe> newRecipes = new ArrayList<Recipe>();
				newRecipes = RecipeHandler.generateRecipes(RecipeHandler.DEFAULT_RECIPES_TO_LOAD);
				recipes.addAll(newRecipes);
				for (Recipe recipe : newRecipes) {
					RecipeHandler.getNewData(recipe, adapter);
				}
				break;
			case R.id.menu_show_favorites:
				Intent i = new Intent();
				i.setClass(this, FavoriteRecipesActivity.class);
				startActivity(i);
				break;
		}
		return true;
	}

}
