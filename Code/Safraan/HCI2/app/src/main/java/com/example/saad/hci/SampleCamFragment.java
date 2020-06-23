package com.example.saad.hci;

import android.hardware.SensorManager;
import android.location.LocationListener;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.wikitude.architect.ArchitectView.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView.SensorAccuracyChangeListener;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Objects;


public class SampleCamFragment extends AbstractArchitectCamFragmentV4{

	/**
	 * last time the calibration toast was shown, this avoids too many toast shown when compass needs calibration
	 */
	private long lastCalibrationToastShownTimeMillis = System.currentTimeMillis();
	
	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	@Override
	public String getARchitectWorldPath() {
		try {
			return URLDecoder.decode(Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getExtras()).getString(ARchitectUrlLauncherCamActivity.ARCHITECT_ACTIVITY_EXTRA_KEY_URL), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(this.getActivity(), "Unexpected Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getContentViewId() {
		return R.layout.sample_cam;
	}

	@Override
	public int getArchitectViewId() {
		return R.id.architectView;
	}
	
	@Override
	public String getWikitudeSDKLicenseKey() {
		return WikitudeSDKConstants.WIKITUDE_SDK_KEY;
	}
	

	@Override
	public SensorAccuracyChangeListener getSensorAccuracyListener() {
		return new SensorAccuracyChangeListener() {
			@Override
			public void onCompassAccuracyChanged( int accuracy ) {
				/* UNRELIABLE = 0, LOW = 1, MEDIUM = 2, HIGH = 3 */
				if ( accuracy < SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM && getActivity() != null && !getActivity().isFinishing()  && System.currentTimeMillis() - SampleCamFragment.this.lastCalibrationToastShownTimeMillis > 5 * 1000) {
					Toast.makeText( getActivity(), R.string.compass_accuracy_low, Toast.LENGTH_LONG ).show();
				}
			}
		};
	}

	@Override
	public ArchitectUrlListener getUrlListener() {
		return new ArchitectUrlListener() {

			@Override
			public boolean urlWasInvoked(String uriString) {
				// by default: no action applied when url was invoked
				return false;
			}
		};
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	@Override
	public ILocationProvider getLocationProvider(final LocationListener locationListener) {
		return new LocationProvider(Objects.requireNonNull(this.getActivity()), locationListener);
	}

}