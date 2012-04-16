package br.unb.unbiquitous.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.unb.unbiquitous.hydra.HydraConnection;
import br.unb.unbiquitous.marker.decoder.DecoderObject;
import br.unb.unbiquitous.marker.detection.MultiMarkerSetup;

public class TesteDroidARActivity extends Activity  {

	private DecoderObject decoderObject;
	private MultiMarkerSetup markerSetup;
	private Intent intent;
	
	private HydraConnection hydraConnection;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.main);

		decoderObject = new DecoderObject(this);

		
		markerSetup = new MultiMarkerSetup();
		
		markerSetup.setActivity(this);
		markerSetup.setDecoderObject(decoderObject);

//		Button b = new Button(this);
//		b.setText("Load Camera");
//		b.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				ArActivity.startWithSetup(TesteDroidARActivity.this,
//						markerSetup);
//			}
//		});

		 intent = new Intent(this, ListViewActivity.class);
		
		Button b = new Button(this);
		b.setText("Load Activity");
		b.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				
				ArrayList<String> drivers = new ArrayList<String>();
				drivers.add("France");
				drivers.add("United Kingdom");
				drivers.add("Ireland");
				drivers.add("Germany");
				drivers.add("Belgium");
				drivers.add("Luxembourg");
				drivers.add("Netherlands");
				drivers.add("Italy");
				drivers.add("Denmark");
				drivers.add("Spain");
				
				intent.putStringArrayListExtra("drivers", drivers);
				
				
				startActivity(intent);
				
				
			}
		});
		setContentView(b);
	}

	public HydraConnection getHydraConnection() {
		return hydraConnection;
	}

	public void setHydraConnection(HydraConnection hydraConnection) {
		this.hydraConnection = hydraConnection;
	}
	
	
	
}