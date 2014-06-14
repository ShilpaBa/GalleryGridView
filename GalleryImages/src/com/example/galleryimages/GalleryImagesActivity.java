package com.example.galleryimages;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

/**
 * This is main activity class which loads all the images 
 * from device gallery.
 *
 * @author Shilpa Bambore
 */	
public class GalleryImagesActivity extends Activity {

	private static int RESULT_LOAD_IMAGE = 1;
	private GridView gridView;
	private GridViewAdapter customGridAdapter;
	private String[] picturePaths = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery_images);

		// Set external storage in intent
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		
		Log.d("onCreate-URI", 
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());

		// Start result activity which loads images
		startActivityForResult(i, RESULT_LOAD_IMAGE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.d("onActivityResult", requestCode + "  "+ resultCode +" "+ RESULT_OK);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			//Get image path
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			
			Log.d("selectedImage", selectedImage.getPath());

			Log.d("onActivityResult", "open cursor");	
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = 0;
			String picturePath = null;
			picturePaths = new String[filePathColumn.length];

			for (int i = 0; i < filePathColumn.length; i++) {
				Log.d("onActivityResult", filePathColumn[i]);
				columnIndex = cursor.getColumnIndex(filePathColumn[i]);
				picturePath = cursor.getString(columnIndex);
				picturePaths[i] = picturePath;
			}
			cursor.close();

			gridView = (GridView) findViewById(R.id.gridView);
			customGridAdapter = new GridViewAdapter(this, R.layout.images,
					getData());
			gridView.setAdapter(customGridAdapter);
			
			// Get position of file
			String pos = "0";
            try{
            	pos = selectedImage.getPath().substring(
            			selectedImage.getPath().lastIndexOf("/")+1);
            }catch(Exception e){
            	// Nothing needs to be done
            }
            Intent i = new Intent(getApplicationContext(), ImageActivity.class);
            // passing array index
            i.putExtra("path", picturePath);
            i.putExtra("id", pos);
            startActivity(i);
            
			
			gridView.setOnItemClickListener(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Toast.makeText(GalleryImagesActivity.this, position + "#Selected",
		                    Toast.LENGTH_SHORT).show();
					
	            	Log.d("setOnItemClickListener", "clicked image - "+ position + "  id - " + id);
	                // Sending image id to FullScreenActivity
	                Intent i = new Intent(getApplicationContext(), ImageActivity.class);
	                // passing array index
	                i.putExtra("id", picturePaths[position]);
	                startActivity(i);
				}
			});
		}
	}

	private ArrayList<ImageItem> getData() {
		
		Bitmap bitmap = null;
		final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
		// retrieve String drawable array
		// TypedArray imgs = getResources().obtainTypedArray(R.array.image);
		for (int i = 0; i < picturePaths.length; i++) {
			bitmap = BitmapFactory.decodeFile(picturePaths[i]);
			imageItems.add(new ImageItem(bitmap, "Image#" + i));
		}
		return imageItems;
	}
}
