package com.yumm;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeHandler {
	public static final String API_URL = "http://api.punchfork.com/random_recipe?key=INSERT_KEY_HERE";
	public static final int DEFAULT_RECIPES_TO_LOAD = 5;

	/**
	 * Returns a list of blank Recipes.
	 * 
	 * @param numRecipes
	 *            The number of recipes to be in the returned list.
	 */
	public static List<Recipe> generateRecipes(int numRecipes) {
		List<Recipe> result = new ArrayList<Recipe>();
		try {
			for (int i = 0; i < numRecipes; i++) {
				result.add(new Recipe());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!result.isEmpty()) {
			return result;
		} else {
			return null;
		}
	}

	/**
	 * Loads new recipe data and sets it on a Recipe. Also, notifies the adapter
	 * whenever the data is ready and the UI may refresh.
	 * 
	 * @param recipe
	 * @param adapter
	 */
	public static void getNewData(Recipe recipe, RecipeAdapter adapter) {
		new NewRecipeTask(recipe, adapter).execute(API_URL);
	}

	/**
	 * Sets the data fields for a Recipe r.
	 * 
	 * @param r
	 *            Recipe to set data of.
	 * @param jsonResponse
	 *            JSON String to parse and use the data to set fields for a
	 *            Recipe.
	 */
	public static void setRecipeData(Recipe r, String jsonResponse) {
		JSONObject json = null;
		try {
			json = new JSONObject(jsonResponse);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		if (json != null) {
			try {
				String recipeJson = json.getString("recipe");
				JSONObject data = new JSONObject(recipeJson);
				r.setName(data.getString("title"));
				r.setThumbUrl(data.getString("thumb"));
				r.setPublisher(data.getString("source_name"));
				r.setUrl(data.getString("source_url"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
