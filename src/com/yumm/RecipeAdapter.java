package com.yumm;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipeAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<Recipe> items;

	public RecipeAdapter(Context context, List<Recipe> items) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		Recipe recipe = items.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.activity_recipes_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.image = (ImageView) convertView.findViewById(R.id.thumbnail);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(recipe.getName());
		if (recipe.getImage() != null) {
			holder.image.setImageBitmap(recipe.getImage());
		} else {
			// Set to a default pic
			holder.image.setImageResource(R.drawable.ic_launcher);
		}

		// Bind recipe to its view
		convertView.setTag(Recipe.TAG_KEY, recipe);

		// Click listener for view
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Load the (web)view for the recipe
			}
		});

		// Long click listener
		convertView.setLongClickable(true);
		convertView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				// Opens a context Menu
				((Activity) context).openContextMenu(v);
				return true;
			}
		});

		return convertView;
	}

	// was static
	private class ViewHolder {
		TextView title;
		ImageView image;
	}
}
