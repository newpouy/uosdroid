package br.unb.unbiquitous.hydra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;
import br.unb.unbiquitous.activity.MainUOSActivity;
import br.unb.unbiquitous.application.UOSDroidApp;
import br.unb.unbiquitous.json.JSONArray;
import br.unb.unbiquitous.json.JSONObject;
import br.unb.unbiquitous.ubiquitos.uos.adaptabitilyEngine.Gateway;
import br.unb.unbiquitous.ubiquitos.uos.adaptabitilyEngine.SmartSpaceGateway;
import br.unb.unbiquitous.ubiquitos.uos.driverManager.DriverData;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.dataType.UpDevice;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.dataType.json.JSONDevice;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.dataType.json.JSONDriver;
import br.unb.unbiquitous.ubiquitos.uos.messageEngine.messages.ServiceResponse;
import br.unb.unbiquitous.util.DriverType;

/**
 * Class responsible by connect connect the Android to the Hydra 
 * application and improve the redirection of the resources.
 * 
 * @author Ricardo Andrade
 */
public class HydraConnection {

	/************************************************************
	 * CONSTANTS
	 ************************************************************/
	
	private static final String TAG = "HydraConnection";
	private static final String DRIVER_INSTANCE_ID_PARAMETER = "driverInstanceID";
	private static final String HYDRA_REDIRECT_SERVICE = "redirectResource";
	private static final String HYDRA_RELEASE_SERVICE = "releaseResource";
	private static final String HYDRA_DRIVER_IN_USE_SERVICE = "isDriverInUse";
	private static final String HYDRA_REGISTER_LIST_DRIVERS = "listDrivers";
	private static final String HYDRA_NOT_FOUND = "Hydra not found!";
	private static final String HYDRA_DRIVERS = "getListDrivers";
	private static final String HYDRA_DEVICE_NAME = "HydraApp";
	

	/************************************************************
	 * VARIABLES
	 ************************************************************/

	private UOSDroidApp app;
	private Gateway gateway;
	private MainUOSActivity activity;
	
	private Set<DriverData> driversList;
	
	/************************************************************
	 * CONSTRUCTOR
	 ************************************************************/
	
	public HydraConnection() {
		gateway = app.getApplicationContext().getGateway();
		this.driversList = new HashSet<DriverData>();
	}

	public HydraConnection(Gateway gateway) {
		this.gateway = gateway;
		this.driversList = new HashSet<DriverData>();
	}
	
	/************************************************************
	 * PUBLIC METHODS
	 ************************************************************/
	
	public void getListDriversInHydra(){
		
			try{
				Map<String, String> parameterMap = new HashMap<String, String>();
				
				// Retira a busca de drivers da Hydra.
				parameterMap.put("device", (new JSONDevice(getHydraDevice())).toString());
				
				ServiceResponse response = gateway.callService(this.getHydraDevice(), 
						HYDRA_REGISTER_LIST_DRIVERS,
						DriverType.REGISTER.getPath(),
						null, //instanceID
						null, //security
						parameterMap);

				JSONArray jsonList = new JSONArray(response.getResponseData().get("driverList"));
				
				// TODO [RICARDO] Verificar qual o motivo de estar deixando gravar duplicado, equals não eh chamado, só o hash..
				
				driversList = new HashSet<DriverData>();
				
				for(int i=0 ; i < jsonList.length(); i++){
					
					JSONObject object = jsonList.getJSONObject(i);
					
					JSONDriver jsonDriver = new JSONDriver(object.getString("driver"));
					JSONDevice jsonDevice = new JSONDevice(object.getString("device"));
					String instanceID = object.getString("instanceID");
					
					driversList.add(new DriverData(jsonDriver.getAsObject(), jsonDevice.getAsObject(), instanceID));
					
				}
				
				Log.i(TAG, "Drivers recebidos.");
				
			}catch (Exception e) {
				Log.i(TAG, e.getMessage());
			}
	}
	
	public void redirectResource(DriverData driverData){
		
		if ( driverData != null){
			try{
					
				Map<String, String> parameterMap = new HashMap<String, String>();

				parameterMap.put(DRIVER_INSTANCE_ID_PARAMETER, driverData.getInstanceID());
				
				gateway.callService(this.getHydraDevice(), 
						HYDRA_REDIRECT_SERVICE,
						DriverType.HYDRA.getPath(),
						null,
						null, //security
						parameterMap);
				
				Log.i(TAG, "Recurso redirecionado.");
				
			}catch (Exception e) {
				Log.i(TAG, e.getMessage());
			}
		}
	}
	
	public void releaseResource(DriverData driverData){
		
		if(driverData != null){
			try{
					
				Map<String, String> parameterMap = new HashMap<String, String>();

				parameterMap.put(DRIVER_INSTANCE_ID_PARAMETER, driverData.getInstanceID());
				
				gateway.callService(this.getHydraDevice(), 
						HYDRA_RELEASE_SERVICE,
						DriverType.HYDRA.getPath(),
						null,
						null, //security
						parameterMap);
				
				Log.i(TAG, "Recurso liberado.");
				
			}catch (Exception e) {
				Log.i(TAG, e.getMessage());
			}
		}
	}
	
	public boolean isDriverInUse(DriverData driverData){
		
		if(driverData != null){
			try{
					
				Map<String, String> parameterMap = new HashMap<String, String>();

				parameterMap.put(DRIVER_INSTANCE_ID_PARAMETER, driverData.getInstanceID());
				
				ServiceResponse response = gateway.callService(this.getHydraDevice(), 
						HYDRA_DRIVER_IN_USE_SERVICE,
						DriverType.HYDRA.getPath(),
						null,
						null, //security
						parameterMap);
			
				return Boolean.parseBoolean(response.getResponseData().get("valor"));

			}catch (Exception e) {
				Log.i(TAG, e.getMessage());
			}
		}
		
		
		
		return true;
	}
	
	public UpDevice getHydraDevice(){
		return ((SmartSpaceGateway) gateway).getDeviceManager().retrieveDevice(HYDRA_DEVICE_NAME);
	}
	
	
	/**
	 * Returns a list with devices implementing any drivers.
	 * 
	 * @return A list with all the devices and instances implementing any drivers.
	 * @since 2011.0930
	 */
	public List<DriverData> getDriversList() {
		List<DriverData> lista = new ArrayList<DriverData>();
		lista.addAll(driversList);
		return lista;
	}

	/**
	 * Returns the driver data by the instanceID.
	 * @param instanceID
	 * @return
	 */
	public DriverData getDriverData(String instanceID){
		
		for (DriverData driverData : this.driversList) {
			
			if(driverData.getInstanceID().equals(instanceID) || driverData.getInstanceID().endsWith(instanceID)){
				return driverData;
			}
		}
		return null;
	}
	
	/**
	 * Returns a list with the drivers of the device. This list will contain only 
	 * mouse, keyboard, screen and camera drivers.
	 * 
	 * @param deviceName
	 * @return
	 */
	public List<DriverData> getDriversByDevice(String deviceName){
		

		List<DriverData> drivers = new ArrayList<DriverData>();
		
		for (DriverData driverData : this.getDriversList()) {
			if(	 driverData.getDevice().getName().equalsIgnoreCase(deviceName) &&	
				( driverData.getDriver().getName().equals(DriverType.CAMERA.getPath()) ||
				  driverData.getDriver().getName().equals(DriverType.KEYBOARD.getPath()) ||
				  driverData.getDriver().getName().equals(DriverType.MOUSE.getPath()) ||
				  driverData.getDriver().getName().equals(DriverType.SCREEN.getPath())
			    )){

				drivers.add(driverData);
			}
		}
		
		return drivers;
	}
	
	public boolean isDeviceValid(String deviceName){
		for (DriverData driverData : this.getDriversList()) {
			if(driverData.getDevice().getName().equals(deviceName)){
				return true;
			}
		}
		return false;
	}
	
	public void agendarBuscaDriverHydra(){
		
		Timer myTimer = new Timer();
		myTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				Log.i(TAG, "Atualizando a lista de drivers com a hydra");
				getListDriversInHydra();
			}

		}, 2000, 60000); // 1 minuto
		
	}
	
	/************************************************************
	 * PRIVATE METHODS
	 ************************************************************/
	

	/************************************************************
	 * GETTERS AND SETTERS
	 ************************************************************/
	
	public MainUOSActivity getActivity() {
		return activity;
	}
	
	public void setActivity(MainUOSActivity activity) {
		this.activity = activity;
	}
	
}
