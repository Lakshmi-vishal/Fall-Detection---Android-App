package com.example.falldetect;


import java.util.ArrayList;


import android.graphics.drawable.Drawable;

import android.util.Log;


import com.google.android.maps.ItemizedOverlay;

import com.google.android.maps.OverlayItem;


public class CurrentLocationOverlay extends ItemizedOverlay<OverlayItem> 
{
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	
                public CurrentLocationOverlay(Drawable defaultMarker) 
{
		super(boundCenterBottom(defaultMarker));		
	}

	
             // Executed, when populate() method is called
	
                @Override

	protected OverlayItem createItem(int arg0)
 {
		return mOverlays.get(arg0);		
	}

	
              @Override

	     public int size()
 {		
		
             return mOverlays.size();
	}
	

       	public void addOverlay(OverlayItem overlay)
{
		mOverlays.add(overlay);
	
	populate(); // Calls the method createItem()
	}
	
	
        @Override
 
	protected boolean onTap(int arg0)
 {
		Log.d("Tapped", mOverlays.get(arg0).getSnippet());

		return true;
	
}
}
