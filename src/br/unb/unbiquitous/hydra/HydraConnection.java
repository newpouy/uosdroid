package br.unb.unbiquitous.hydra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import com.google.droidar.util.Vec;

import android.util.Log;
import android.widget.Toast;
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
//	private DriverData hydraDriver;
	private MainUOSActivity activity;
	
	private Set<DriverData> driversList;
	
	/************************************************************
	 * CONSTRUCTOR
	 ************************************************************/
	
	public HydraConnection() {
		gateway = app.getApplicationContext().getGateway();
		driversList = new TreeSet<DriverData>();
	}

	public HydraConnection(Gateway gateway) {
		this.gateway = gateway;
		driversList = new TreeSet<DriverData>();
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
		
//		hydraDriver = getHydraApplication();
		
//		if(hydraDriver == null){
//			Log.e(TAG, HYDRA_NOT_FOUND);
//			Toast.makeText(activity,HYDRA_NOT_FOUND, Toast.LENGTH_LONG).show();
//		}
		
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
		
//		hydraDriver = getHydraApplication();
//		
//		if(hydraDriver == null){
//			Log.e(TAG, HYDRA_NOT_FOUND);
//			Toast.makeText(activity,HYDRA_NOT_FOUND, Toast.LENGTH_LONG).show();
//		}
		
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
		
//		hydraDriver = getHydraApplication();
//		
//		if(hydraDriver == null){
//			Log.e(TAG, HYDRA_NOT_FOUND);
//			Toast.makeText(activity,HYDRA_NOT_FOUND, Toast.LENGTH_LONG).show();
//		}
		
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
		
//		if(this.driversList == null){
//			
//		}else{
//			driversList = gateway.listDrivers(null);
//		}
//		
		List<DriverData> drivers = new ArrayList<DriverData>();
		drivers.addAll(driversList);
		return drivers;
	}

	/**
	 * Returns the driver data by the instanceID.
	 * @param instanceID
	 * @return
	 */
	public DriverData getDriverData(String instanceID){
		
		for (DriverData driverData : this.getDriversList()) {
			
			if(driverData.getInstanceID().equals(instanceID)){
				return driverData;
			}
		}
		return null;
	}
	
	/**
	 * Returns a list with the drivers of the device.
	 * @param deviceName
	 * @return
	 */
	public List<DriverData> getDriversByDevice(String deviceName){
		
		List<DriverData> drivers = new ArrayList<DriverData>();
		
		for (DriverData driverData : this.getDriversList()) {
			if(driverData.getDevice().getName().equalsIgnoreCase(deviceName)){
				drivers.add(driverData);
			}
		}
		
		return drivers;
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
