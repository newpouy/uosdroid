package br.unb;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.unb.unbiquitous.marker.decoder.DecoderObject;
import br.unb.unbiquitous.marker.detection.MultiMarkerSetup;

import com.google.droidar.system.ArActivity;

public class TesteDroidARActivity extends Activity  {

	private DecoderObject decoderObject;
	private MultiMarkerSetup markerSetup;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.main);

		decoderObject = new DecoderObject(this);

		
		markerSetup = new MultiMarkerSetup();
		
		markerSetup.setActivity(this);
		markerSetup.setDecoderObject(decoderObject);

		Button b = new Button(this);
		b.setText("Load " + markerSetup.getClass().getName());
		b.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ArActivity.startWithSetup(TesteDroidARActivity.this,
						markerSetup);
			}
		});
		setContentView(b);
	}
}