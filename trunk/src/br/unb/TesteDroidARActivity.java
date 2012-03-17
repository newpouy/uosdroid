package br.unb;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.droidar.de.rwth.MultiMarkerSetup;
import com.google.droidar.system.ArActivity;
import com.google.zxing.client.android.camera.CameraManager;


public class TesteDroidARActivity extends Activity {
	
	private CameraManager cameraManager;
	private QRCodeDecoder qrCodeDecoder;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//       setContentView(R.layout.main);
        
        cameraManager = new CameraManager(getApplication());
    	qrCodeDecoder = new QRCodeDecoder(cameraManager);
        
        Button b = new Button(this);
		final MultiMarkerSetup markerSetup = new MultiMarkerSetup();
		b.setText("Load " + markerSetup.getClass().getName());
		b.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ArActivity.startWithSetup(TesteDroidARActivity.this, markerSetup);
			}
		});
		
		markerSetup.setQrCodeDecoder(qrCodeDecoder);
		
		setContentView(b);
		
    }
    
}