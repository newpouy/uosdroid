package br.unb.unbiquitous.activity;

import java.util.PropertyResourceBundle;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import br.unb.unbiquitous.application.UOSDroidApp;
import br.unb.unbiquitous.configuration.ConfigLog4j;
import br.unb.unbiquitous.hydra.HydraConnection;
import br.unb.unbiquitous.marker.decoder.DecoderObject;
import br.unb.unbiquitous.marker.decoder.QRCodeDecoder;
import br.unb.unbiquitous.marker.detection.MultiMarkerSetup;

import com.google.droidar.system.ArActivity;
import com.google.zxing.client.android.camera.CameraManager;

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
	private static final String REDIRECIONAR_RECURSO = "Redirecionar";
	private static final boolean DEBUG = true;

	/************************************************************
	 * VARIABLES
	 ************************************************************/

	private UOSDroidApp droidobiquitousApp;
	private HydraConnection hydraConnection;
	
	private DecoderObject decoderObject;
	private MultiMarkerSetup markerSetup;

	private ListView listView;
	
	private CameraManager cameraManager;
	private QRCodeDecoder qrCodeDecoder;
	
	private Button button;
	
	private String flagContextMenu = REDIRECIONAR_RECURSO;

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
		startMiddleware();

//		setContentView(R.layout.main);

		// Creating the list view of the drivers
//		initListView();
		
		// Start the augmented reality
		startAR();
		
		setContentView(button);
	}


	


	/**
	 * Method invoked when a option menu is selected.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (DEBUG)
			Log.e(TAG, "++ ON OPTIONS ITEM SELECTED ++");

		/* chamando uma activity */
		// Launch the DeviceListActivity to see devices and do scan
		// serverIntent = new Intent(this, DeviceListActivity.class);
		// startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE
		/* coloca uma mensagem que some ap�s alguns segundos */
		// Toast.makeText(this, "You pressed the exit!",
		// Toast.LENGTH_LONG).show();

//		switch (item.getItemId()) {
//		
//		case R.id.camera_driver:
//			this.createListView(DriverType.CAMERA, hydraConnection.getCameraDriversList());
//			break;
//
//		case R.id.keyboard_driver:
//			this.createListView(DriverType.KEYBOARD, hydraConnection.getKeyboardDriversList());
//			break;
//		
//		case R.id.mouse_driver:
//			this.createListView(DriverType.MOUSE, hydraConnection.getMouseDriversList());
//			break;
//
//		case R.id.screen_driver:
//			this.createListView(DriverType.SCREEN, hydraConnection.getScreenDriversList());
//			break;
//		
//		case R.id.hydra_app:
//			this.createListView(DriverType.HYDRA, hydraConnection.getHydraDriversList());
//			break;
		
//		case R.id.all:
//			this.createListView(DriverType.ALL, hydraConnection.getDriversList());
//			break;
			
//		case R.id.camera:
//			MultiMarkerSetup markerSetup = new MultiMarkerSetup();
//			ArActivity.startWithSetup(this, markerSetup);
//		}
		return true;

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
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, view, menuInfo);  
	    menu.setHeaderTitle("Ação");
	    menu.add(0, view.getId(), 0, flagContextMenu);  
	}

//	@Override
//	public boolean onContextItemSelected(MenuItem menuItem) {  
//		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuItem.getMenuInfo();
//		ItemListView itemListView = this.adapterListView.getItem(info.position);
//
//		Toast.makeText(this, "You pressed the: " + itemListView.getTexto(),Toast.LENGTH_LONG);
//		
//		return true;
//	}	      
	
	
	
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
	
	private void configureARParameters() {
		decoderObject = new DecoderObject(this);

		markerSetup = new MultiMarkerSetup();
		
		markerSetup.setActivity(this);
		markerSetup.setDecoderObject(decoderObject);
		
	}
	
	/**
	 * Start the middleware with the configs of the bundle and 
	 * start the hydra connection.
	 */
	private void startMiddleware() {
		try {
			PropertyResourceBundle bundle;

			droidobiquitousApp = new UOSDroidApp();

			bundle = new PropertyResourceBundle(getResources().openRawResource(
					R.raw.uosdroid));

			droidobiquitousApp.start(bundle);

			hydraConnection = new HydraConnection(droidobiquitousApp.getApplicationContext().getGateway());
			hydraConnection.setActivity(this);
			
			
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}





	
	
	
//	/**
//	 * Inicializa o List View.
//	 */
//	private void initListView() {
//		listView = (ListView) findViewById(R.id.tela_consulta_listView);
//		//listView.setOnItemClickListener(this);
//		registerForContextMenu(listView);
//		
//		//Cria a lista com todos os drivers encontrado por default.
//		createListView(DriverType.ALL, hydraConnection.getDriversList());
//	}

	
//	/**
//	 * Cria o List View a partir dos drivers passados.
//	 */
//	private void createListView(DriverType driverType, List<DriverData> driversData) {
//		
//		if(driversData == null){
//			Toast.makeText(this, "Não foi encontrado drivers para "+ driverType.toString(),Toast.LENGTH_LONG).show();
//			return;
//		}
//		
//		// Inicializando a lista que preenchera o ListView
//		itens = new ArrayList<ItemListView>();
//		
//		// Populando a lista com os drivers
//		for (DriverData driverData : driversData) {
//			ItemListView item = new ItemListView(driverData.getInstanceID(), driverData, driverType);
//			itens.add(item);
//		}
//
//		// Cria o adapter
//		adapterListView = new AdapterListView(this, itens);
//
//		// Define o Adapter
//		listView.setAdapter(adapterListView);
//		
//		// Cor quando a lista é selecionada para rolagem.
//		listView.setCacheColorHint(Color.GRAY);
//
//		TextView text = (TextView) findViewById(R.id.titulo_driver);
//		text.setText(driverType.toString());
//		
//	}
	
	public HydraConnection getHydraConnection() {
		return hydraConnection;
	}

	public void setHydraConnection(HydraConnection hydraConnection) {
		this.hydraConnection = hydraConnection;
	}
	
	
}