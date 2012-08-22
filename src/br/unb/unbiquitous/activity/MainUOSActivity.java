package br.unb.unbiquitous.activity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.PropertyResourceBundle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import br.unb.unbiquitous.application.UOSDroidApp;
import br.unb.unbiquitous.configuration.ConfigLog4j;
import br.unb.unbiquitous.hydra.HydraConnection;
import br.unb.unbiquitous.marker.decoder.DecoderObject;
import br.unb.unbiquitous.marker.setup.SingleMarkerSetup;
import br.unb.unbiquitous.util.CalculoMedicao;

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
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.item_relatorio:
			CalculoMedicao.getInstance().calcular();
			exibirRelatorio();
			break;
		case R.id.resetar_relatorio:
			confirmarExclusaoRelatorio();
			break;
		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	public void confirmarExclusaoRelatorio(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Confirma exclusão do relatório?")
		       .setCancelable(false)
		       .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                CalculoMedicao.getInstance().resetarMedicoes();
		           }
		       })
		       .setNegativeButton("Não", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void exibirRelatorio(){
		final Dialog dialog = new Dialog(this);

		dialog.setContentView(R.layout.relatorio);
		dialog.setTitle("Relatório dos testes");

		NumberFormat format = new DecimalFormat("#.00");
		TextView primeiro = (TextView) dialog.findViewById(R.id.relatorio_primeira);
		primeiro.setText(	
							"Total de primeira aparições= " + CalculoMedicao.getInstance().getTotalPrimeiraAparicao()  +
							"\nTempo médio da primeira aparição = "+ format.format(CalculoMedicao.getInstance().getTempoMedioPrimeiraAparicao()) + "s" +
							"\n\nTotal de recorrências = " + CalculoMedicao.getInstance().getTotalPrimeiraAparicao() +
							"\nTempo médio de recorrência = " + format.format(CalculoMedicao.getInstance().getTempoMedioRecorrencia()) + "s" +
							"\n\nTaxa de erro = " + format.format(CalculoMedicao.getInstance().getTaxaErro()) + "%" +
							"\nTaxa que não conseguiu decodificar = " + format.format(CalculoMedicao.getInstance().getTaxaNaoDecodificacao()) + "%"
						);
		
		Button button = (Button) dialog.findViewById(R.id.relatorio_botao);
		 
		button.setOnClickListener(new OnClickListener() {
        @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
		dialog.show();
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
		button.setText("Iniciar");
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