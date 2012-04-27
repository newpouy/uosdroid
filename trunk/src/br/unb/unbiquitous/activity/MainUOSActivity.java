package br.unb.unbiquitous.activity;

import java.util.PropertyResourceBundle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.unb.unbiquitous.application.UOSDroidApp;
import br.unb.unbiquitous.configuration.ConfigLog4j;
import br.unb.unbiquitous.hydra.HydraConnection;
import br.unb.unbiquitous.marker.decoder.DecoderObject;
import br.unb.unbiquitous.marker.detection.MultiMarkerSetup;

import com.google.droidar.system.ArActivity;

/**
 * 
 * @author Ricardo Andrade
 * 
 */
public class MainUOSActivity extends Activity { 

	/************************************************************
	 * CONSTANTS
	 ************************************************************/
	private static final String TAG = MainUOSActivity.class.getSimpleName();
	private static final boolean DEBUG = true;

	/************************************************************
	 * VARIABLES
	 ************************************************************/

	private UOSDroidApp droidobiquitousApp;
	private HydraConnection hydraConnection;
	
	private DecoderObject decoderObject;
	private MultiMarkerSetup markerSetup;

	private Button button;
	
	/************************************************************
	 * PUBLIC METHODS
	 ************************************************************/

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (DEBUG)
			Log.e(TAG, "++ ON CREATE ++");

		super.onCreate(savedInstanceState);

		// Configuring the Log4J
		ConfigLog4j.configure();

		// Starting the middleware
//		startMiddleware();

		// Start the augmented reality
		startAR();
		
		setContentView(button);
	}

	/**
	 * Creating the options menu.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (DEBUG)
			Log.e(TAG, "++ ON CREATE OPTIONS MENU ++");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	/************************************************************
	 * PRIVATE METHODS
	 ************************************************************/
	
	private void startAR(){
		decoderObject = new DecoderObject(this);

		markerSetup = new MultiMarkerSetup();
		markerSetup.setActivity(this);
		markerSetup.setDecoderObject(decoderObject);
		
		
		button = new Button(this);
		button.setText("Load Camera");
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ArActivity.startWithSetup(MainUOSActivity.this,
						markerSetup);
			}
		});
		
	}
	
	/**
	 * Start the middleware with the configs of the bundle and 
	 * start the hydra connection.
	 */
	private void startMiddleware() {
		try {
			PropertyResourceBundle bundle;

			droidobiquitousApp = new UOSDroidApp();

			bundle = new PropertyResourceBundle(getResources().openRawResource(R.raw.uosdroid));

			droidobiquitousApp.start(bundle);

			hydraConnection = new HydraConnection(droidobiquitousApp.getApplicationContext().getGateway());
			hydraConnection.setActivity(this);
			
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	/************************************************************
	 * GETTERS AND SETTERS
	 ************************************************************/
	
	public HydraConnection getHydraConnection() {
		return hydraConnection;
	}

	public void setHydraConnection(HydraConnection hydraConnection) {
		this.hydraConnection = hydraConnection;
	}
	
	
}