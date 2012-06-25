package pl.mu;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class LocationSelectionActivity extends MapActivity {
	private MapView mapView;
	private MapController mapController;
	private LocationManager locationManager;
	private LocationListener locationListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		mapController = mapView.getController();
		mapController.setZoom(10);   

		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

//		GeoPoint initGeoPoint = new GeoPoint((int) (locationManager
//				.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//				.getLatitude() * 1000000), (int) (locationManager
//				.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//				.getLongitude() * 1000000));
//
//		mapController.animateTo(initGeoPoint);

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

	public void sendResult(double lat, double lon) {
		Intent intent = new Intent();
		intent.putExtra("lat", String.valueOf(lat));
		intent.putExtra("lon", String.valueOf(lon));

		if (getParent() == null) {
			setResult(007, intent);
		} else {
			getParent().setResult(007, intent);
		}
		finish();
	}

	private class MyLocationListener implements LocationListener {
		public void onLocationChanged(Location argLocation) {
			// TODO Auto-generated method stub
			GeoPoint myGeoPoint = new GeoPoint(
					(int) (argLocation.getLatitude() * 1000000),
					(int) (argLocation.getLongitude() * 1000000));
			mapController.animateTo(myGeoPoint);
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	}

	public class LocationMapOverlay extends Overlay {

		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			// TODO Auto-generated method stub
			return super.draw(canvas, mapView, shadow, when);
		}

		@Override
		public boolean onTap(GeoPoint point, MapView mapView) {
			Toast.makeText(
					mapView.getContext(),
					"Location: " + point.getLatitudeE6() / 1E6 + ","
							+ point.getLongitudeE6() / 1E6, Toast.LENGTH_SHORT)
					.show();

			double lat = point.getLatitudeE6() / 1E6;
			double lon = point.getLongitudeE6() / 1E6;

			sendResult(lat, lon);
			return false;
		}

	}
}