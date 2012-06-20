package pl.mu;

import java.util.List;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class LocationSelectionActivity extends MapActivity{
	private MapView mapView;
//	private MapController mapController;
//	private LocationManager locationManager;
//	private LocationListener locationListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	    LocationMapOverlay mapOverlay = new LocationMapOverlay();
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay); 
        mapView.invalidate();
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	public void sendResult(double lat, double lon){
		Intent intent = new Intent();
		intent.putExtra("lat", lat);
		intent.putExtra("lon", lon); 
		setResult(007,intent);
	}
	
	public class LocationMapOverlay extends Overlay{
		
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			// TODO Auto-generated method stub
			return super.draw(canvas, mapView, shadow, when);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapView) {
			if(event.getAction() == 1) {
				GeoPoint point = mapView.getProjection().fromPixels((int) event.getX(),(int) event.getY());
				
				Toast.makeText(mapView.getContext(), "Location: " 
						+ point.getLatitudeE6() / 1E6 + "," 
						+ point.getLongitudeE6() /1E6 ,
						Toast.LENGTH_SHORT).show();
				
				double lat = point.getLatitudeE6() / 1E6;
				double lon = point.getLongitudeE6() / 1E6;
				
				sendResult(lat, lon);
			} 
			return false;
		}
	}
}
