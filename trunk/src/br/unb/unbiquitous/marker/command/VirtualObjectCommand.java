package br.unb.unbiquitous.marker.command;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import br.unb.unbiquitous.activity.ListViewActivity;
import br.unb.unbiquitous.marker.virtual.object.MeuObjetoVirtual;

import com.google.droidar.commands.Command;
import com.google.droidar.util.Log;

/**
 * 
 * @author ricardoandrade
 *
 */
public class VirtualObjectCommand extends Command {
	
	/************************************************
	 * VARIABLES
	 ************************************************/
	
	private MeuObjetoVirtual objetoVirtual;
	
	
	/************************************************
	 * PUBLIC METHODS
	 ************************************************/

	/**
	 * Método chamado toda vez que o objeto virtual for clicado.
	 */
	@Override
	public boolean execute() {
		Log.i("VirtualObjectCommand", "objeto clicado.");
		
		Intent intent = new Intent(objetoVirtual.getActivity(), ListViewActivity.class);
		
		ListViewActivity.hydraConnection = objetoVirtual.getHydraConnection();
		
		ArrayList<String> driversName = new ArrayList<String>();
		
		for (String driver : objetoVirtual.getNomeDrivers()) {
			driversName.add(driver);
			
		}
		intent.putStringArrayListExtra("driversName", driversName);
		objetoVirtual.getActivity().startActivity(intent);
		
		
		return false;
	}


	/************************************************
	 * PRIVATE METHODS
	 ************************************************/
	
	
	/************************************************
	 * GETTERS AND SETTERS
	 ************************************************/
	
	public MeuObjetoVirtual getObjetoVirtual() {
		return objetoVirtual;
	}


	public void setObjetoVirtual(MeuObjetoVirtual objetoVirtual) {
		this.objetoVirtual = objetoVirtual;
	}
	
	
	
}
