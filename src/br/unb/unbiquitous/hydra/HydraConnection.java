package br.unb.unbiquitous.hydra;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import android.widget.Toast;
import br.unb.unbiquitous.activity.MainUOSActivity;
import br.unb.unbiquitous.application.UOSDroidApp;
import br.unb.unbiquitous.ubiquitos.uos.adaptabitilyEngine.Gateway;
import br.unb.unbiquitous.ubiquitos.uos.driverManager.DriverData;
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
	private static final String HYDRA_NOT_FOUND = "Hydra not found!";

	/************************************************************
	 * VARIABLES
	 ************************************************************/

	private UOSDroidApp app;
	private Gateway gateway;
	private DriverData hydraDriver;
	private MainUOSActivity activity;
	
	/************************************************************
	 * CONSTRUCTOR
	 ************************************************************/
	
	public HydraConnection() {
		gateway = app.getApplicationContext().getGateway();
	}

	public HydraConnection(Gateway gateway) {
		this.gateway = gateway;
	}
	
	/************************************************************
	 * PUBLIC METHODS
	 ************************************************************/
	
	public void redirectResource(DriverData driverData){
		
		hydraDriver = getHydraApplication();
		
		if(hydraDriver == null){
			Log.e(TAG, HYDRA_NOT_FOUND);
			Toast.makeText(activity,HYDRA_NOT_FOUND, Toast.LENGTH_LONG).show();
		}
		
		if(hydraDriver != null && driverData != null){
			try{
					
				Map<String, String> parameterMap = new HashMap<String, String>();

				parameterMap.put(DRIVER_INSTANCE_ID_PARAMETER, driverData.getInstanceID());
				
				gateway.callService(hydraDriver.getDevice(), 
						HYDRA_REDIRECT_SERVICE,
						DriverType.HYDRA.getPath(),
						hydraDriver.getInstanceID(),
						null, //security
						parameterMap);
				
				Log.i(TAG, "Recurso redirecionado.");
				
			}catch (Exception e) {
				Log.i(TAG, e.getMessage());
			}
		}
	}
	
	public void releaseResource(DriverData driverData){
		
		hydraDriver = getHydraApplication();
		
		if(hydraDriver == null){
			Log.e(TAG, HYDRA_NOT_FOUND);
			Toast.makeText(activity,HYDRA_NOT_FOUND, Toast.LENGTH_LONG).show();
		}
		
		if(hydraDriver != null && driverData != null){
			try{
					
				Map<String, String> parameterMap = new HashMap<String, String>();

				parameterMap.put(DRIVER_INSTANCE_ID_PARAMETER, driverData.getInstanceID());
				
				gateway.callService(hydraDriver.getDevice(), 
						HYDRA_RELEASE_SERVICE,
						DriverType.HYDRA.getPath(),
						hydraDriver.getInstanceID(),
						null, //security
						parameterMap);
				
				Log.i(TAG, "Recurso liberado.");
				
			}catch (Exception e) {
				Log.i(TAG, e.getMessage());
			}
		}
	}
	
	public boolean isDriverInUse(DriverData driverData){
		
		hydraDriver = getHydraApplication();
		
		if(hydraDriver == null){
			Log.e(TAG, HYDRA_NOT_FOUND);
			Toast.makeText(activity,HYDRA_NOT_FOUND, Toast.LENGTH_LONG).show();
		}
		
		if(hydraDriver != null && driverData != null){
			try{
					
				Map<String, String> parameterMap = new HashMap<String, String>();

				parameterMap.put(DRIVER_INSTANCE_ID_PARAMETER, driverData.getInstanceID());
				
				ServiceResponse response = gateway.callService(hydraDriver.getDevice(), 
						HYDRA_DRIVER_IN_USE_SERVICE,
						DriverType.HYDRA.getPath(),
						hydraDriver.getInstanceID(),
						null, //security
						parameterMap);
			
				return Boolean.parseBoolean(response.getResponseData().get("valor"));

			}catch (Exception e) {
				Log.i(TAG, e.getMessage());
			}
		}
		
		
		
		return true;
	}
	
	/**
	 * Por enquanto estou considerando que no smart space s� ter� uma aplica��o da hydra rodando. 
	 * @return
	 */
	public DriverData getHydraApplication(){
		List<DriverData> drivers = this.getHydraDriversList();
		if(drivers!=null && !drivers.isEmpty()){
			return drivers.get(0);
		}
		return null;
	}
	
	/**
	 * Returns a list with devices implementing any drivers.
	 * 
	 * @return A list with all the devices and instances implementing any drivers.
	 * @since 2011.0930
	 */
	public List<DriverData> getDriversList() {
		return gateway.listDrivers(null);
	}
	
	/**
	 * Returns a list with devices implementing the mouse driver.
	 * 
	 * @return A list with all the devices and instances implementing the mouse driver.
	 * @since 2011.0709
	 */
	public List<DriverData> getMouseDriversList() {
		return gateway.listDrivers(DriverType.MOUSE.getPath());
	}

	/**
	 * Returns a list with devices implementing the keyboard driver.
	 * 
	 * @return A list with all the devices and instances implementing the keyboard driver.
	 * @since 2011.0709
	 */
	public List<DriverData> getKeyboardDriversList() {
		return gateway.listDrivers(DriverType.KEYBOARD.getPath());
	}

	/**
	 * Returns a list with devices implementing the camera driver.
	 * 
	 * @return A list with all the devices and instances implementing the camera driver.
	 * @since 2011.0709
	 */
	public List<DriverData> getCameraDriversList() {
		return gateway.listDrivers(DriverType.CAMERA.getPath());
	}

	/**
	 * Returns a list with devices implementing the screen driver.
	 * 
	 * @return A list with all the devices and instances implementing the screen driver.
	 * @since 2011.0709
	 */
	public List<DriverData> getScreenDriversList() {
		return gateway.listDrivers(DriverType.SCREEN.getPath());
	}
	
	/**
	 * Returns a list with devices implementing the hydra driver.
	 * 
	 * @return A list with all the devices and instances implementing the hydra driver.
	 * @since 2011.0709
	 */
	public List<DriverData> getHydraDriversList() {
		return gateway.listDrivers(DriverType.HYDRA.getPath());
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
