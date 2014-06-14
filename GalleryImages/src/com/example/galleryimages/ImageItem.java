package com.example.galleryimages;


import android.graphics.Bitmap;

/**
 * This is a bean class to store image details
 *
 * @author Shilpa Bambore
 */
public class ImageItem {
	private Bitmap image;
    private String title;
 
    public ImageItem(Bitmap image, String title) {
        super();
        this.image = image;
        this.title = title;
    }
 
    public Bitmap getImage() {
        return image;
    }
 
    public void setImage(Bitmap image) {
        this.image = image;
    }
 
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
}


