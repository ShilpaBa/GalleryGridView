package com.example.galleryimages;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * This class expands the selected image and display image information
 * 
 * @author Shilpa Bambore
 */
public class ImageActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);

		// get intent data
		Intent i = getIntent();

		// Selected image id
		String picturePath = i.getExtras().getString("path");
		String position = i.getExtras().getString("id");
		
		TextView view = (TextView) findViewById(R.id.textView1);
		view.setText("Photo: " + position);
		
		ImageView image = (ImageView) findViewById(R.id.full_image_view);
		image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
		String date = "Not Found";
		String location = "Not Found";
		try {
			ExifInterface exif = new ExifInterface(picturePath);
			date = exif.getAttribute(ExifInterface.TAG_DATETIME);
			
			if(null != exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE) && 
					null != exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)){

				Geocoder geocoder;
				List<Address> addresses;
				geocoder = new Geocoder(this, Locale.getDefault());
				addresses = geocoder.getFromLocation(
					Double.parseDouble(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)), 
					Double.parseDouble(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)),
					1);

				location = addresses.get(0).getAddressLine(0) + " " 
						+ addresses.get(0).getAddressLine(1) +" "
						+ addresses.get(0).getAddressLine(2);
			}
		} catch (IOException e) {
			// Not able to find image information. We can ignore this exception
			e.printStackTrace();
		}
		view = (TextView) findViewById(R.id.textView2);
		view.setText("Location:  " + location);

		view = (TextView) findViewById(R.id.textView3);
		view.setText("Date:  " + date);
	}

	public void backButton(View v) {
		Intent intent = new Intent(this, GalleryImagesActivity.class);
		startActivity(intent);
	}
}
