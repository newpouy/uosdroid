package com.google.droidar.de.rwth;

import br.unb.unbiquitous.marker.detection.MultiMarkerSetup;

import com.google.droidar.system.ArActivity;
import com.google.droidar.system.Setup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Button b = new Button(this);
		final MultiMarkerSetup markerSetup = new MultiMarkerSetup();
		b.setText("Load " + markerSetup.getClass().getName());
		b.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ArActivity.startWithSetup(Main.this, markerSetup);
			}
		});
		setContentView(b);
	}

	private Button generateStartButton(final Setup setup) {
		Button b = new Button(this);
		b.setText("Load " + setup.getClass().getName());
		b.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ArActivity.startWithSetup(Main.this, setup);
			}
		});
		return b;
	}
}