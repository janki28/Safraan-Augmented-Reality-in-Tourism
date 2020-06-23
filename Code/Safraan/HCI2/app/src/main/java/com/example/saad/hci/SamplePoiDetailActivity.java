package com.example.saad.hci;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class SamplePoiDetailActivity extends Activity {

	public static final String EXTRAS_KEY_POI_ID = "id";
	public static final String EXTRAS_KEY_POI_TITILE = "title";
	public static final String EXTRAS_KEY_POI_DESCR = "description";

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.sample_poidetail);
		
		((TextView)findViewById(R.id.poi_id)).setText(  Objects.requireNonNull(getIntent().getExtras()).getString(EXTRAS_KEY_POI_ID) );
		((TextView)findViewById(R.id.poi_title)).setText( getIntent().getExtras().getString(EXTRAS_KEY_POI_TITILE) );
		((TextView)findViewById(R.id.poi_description)).setText(  getIntent().getExtras().getString(EXTRAS_KEY_POI_DESCR) );
	}

}
