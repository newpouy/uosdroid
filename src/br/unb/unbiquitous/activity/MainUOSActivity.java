package br.unb.unbiquitous.activity;

import java.util.PropertyResourceBundle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.unb.unbiquitous.application.UOSDroidApp;
import br.unb.unbiquitous.configuration.ConfigLog4j;
import br.unb.unbiquitous.hydra.HydraConnection;
import br.unb.unbiquitous.marker.decoder.DecoderObject;
import br.unb.unbiquitous.marker.setup.SingleMarkerSetup;
import br.unb.unbiquitous.util.Medicao;

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
	private static final String TAG_MEDICAO = "Medicao Testes";
	private static final boolean DEBUG = true;

	/************************************************************
	 * VARIABLES
	 ************************************************************/

	private UOSDroidApp droidobiquitousApp;
	private HydraConnection hydraConnection;

	private DecoderObject decoderObject;
	private SingleMarkerSetup markerSetup;

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
		
		verificarWifi();
			
		new StartMiddlewareTask().execute();
		
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
	
	
	@Override
	public void onStart() {
		super.onStart();
		
		if (decoderObject.getMedicoes() != null && !decoderObject.getMedicoes().isEmpty()){
			
			boolean first = true;
			Log.d(TAG_MEDICAO, "+++++++++  INICIO DO RELATORIO DOS TESTES +++++++++ ");
			
			for (Medicao medicao : decoderObject.getMedicoes()) {
				
				// Medicao quando a aplicacao eh iniciada
				if(first){
					Log.d(TAG_MEDICAO, "+++++++++ [TESTE] PRIMEIRA APARICAO +++++++++ ");
					first = false;
				}else{
					Log.d(TAG_MEDICAO, "+++++++++ [TESTE]  NOVA APARICAO +++++++++ ");
				}
				
				Log.d(TAG_MEDICAO, "[TESTE] Tempo de reconhecimento =  " + medicao.getTempoPrimeiraAparicao());
				
				if(medicao.getTemposSemPerderAlvo().size() > 0){
					Log.d(TAG_MEDICAO, "Recorrências: ");
				}
				
				for(int i = 0; i < medicao.getTemposSemPerderAlvo().size(); i++){
					Log.d(TAG_MEDICAO, "\t" + i + ". Tempo =  " + medicao.getTemposSemPerderAlvo().get(i));
				}
				
				Log.d(TAG_MEDICAO, "++++++++++++++++++++++++++++++++++++++ ");
			}
			
		}
	}
	
	@Override
	protected void onDestroy() {
		stopMiddleware();
		hydraConnection.getScheduler().cancel();
		super.onDestroy();
	}

	/************************************************************
	 * PRIVATE METHODS
	 ************************************************************/

	/**
	 * 
	 */
	private void startAR() {
		decoderObject = new DecoderObject(this);

		markerSetup = new SingleMarkerSetup();
		markerSetup.setActivity(this);
		markerSetup.setDecoderObject(decoderObject);

		button = new Button(this);
		button.setText("Load Camera");
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				ArActivity.startWithSetup(MainUOSActivity.this, markerSetup);
			}
		});

	}

	/**
	 * Start the middleware with the configs of the bundle and start the hydra
	 * connection.
	 */
	private void startMiddleware() {
		try {

			PropertyResourceBundle bundle;

			droidobiquitousApp = new UOSDroidApp();

			bundle = new PropertyResourceBundle(getResources().openRawResource(R.raw.arhydra));

			droidobiquitousApp.start(bundle);

			hydraConnection = new HydraConnection(droidobiquitousApp.getApplicationContext().getGateway());
			hydraConnection.setActivity(this);

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}	
	
	private void stopMiddleware(){
		try {
			droidobiquitousApp.tearDown(null);
		} catch (Exception e) {
			Log.e(TAG,"Erro ao parar o middleware:" +  e.getMessage());
		}
	}
	
	/**
	 * 
	 */
	private void waitHydraHandshake(){
		while(hydraConnection.getHydraDevice() == null){}
	}
	
	//TODO arrumar isso aqui
	private void verificarWifi(){
		boolean firstTime = true;
		
		ConnectivityManager manager = (ConnectivityManager)getSystemService(MainUOSActivity.CONNECTIVITY_SERVICE);
       	Boolean isWifiEnable = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();

       	while(!isWifiEnable){
       		if(firstTime){
       			startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
       			firstTime = false;
       		}
       		isWifiEnable = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
        }
	}

	/************************************************************
	 * INNER CLASSES
	 ************************************************************/
	
	/**
	 * 
	 */
	private class StartMiddlewareTask extends AsyncTask<Void, Void, Void> {

		private ProgressDialog progressDialog;

		protected void onPreExecute() {
			progressDialog = new ProgressDialog(MainUOSActivity.this);
			progressDialog.setMessage("Aguardando o handshake com a Hydra...");
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			Log.i(TAG, "++ Inicializando o middleware... ++");
			startMiddleware();
			Log.i(TAG, "++ Middleware inicializado. ++");
			
			Log.i(TAG, "++ Esperando pelo handshake com a Hydra... ++");
			waitHydraHandshake();
			Log.i(TAG, "++ Handshake efetuado com sucesso ... ++");
			
			Log.i(TAG, "++ Agendando busca de drivers na Hydra ... ++");
			hydraConnection.agendarBuscaDriverHydra();
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
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