package br.unb.unbiquitous.marker.command;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import br.unb.CheckViewActivity;
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
		
		Intent intent = new Intent(objetoVirtual.getActivity(), CheckViewActivity.class);
		
		ArrayList<String> drivers = new ArrayList<String>();
		
		for (String driver : objetoVirtual.getNomeDrivers()) {
			drivers.add(driver);
			
		}
		intent.putStringArrayListExtra("drivers", drivers);
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
