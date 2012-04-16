package br.unb.unbiquitous.handler;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import br.unb.unbiquitous.activity.R;
import br.unb.unbiquitous.manager.ARManager;
import br.unb.unbiquitous.marker.decoder.DecodeDTO;

/**
 * 
 * @author ricardoandrade
 *
 */
final public class RepositionMarkerHandler extends Handler {
	
	/************************************************
	 * CONSTANS
	 ************************************************/
	private static final String TAG = RepositionMarkerHandler.class.getSimpleName();

	/************************************************
	 * VARIABLES
	 ************************************************/
	
	private ARManager arManager;
	
	/************************************************
	 * CONSTRUCTOR
	 ************************************************/
	
	public RepositionMarkerHandler(ARManager arManager) {
		this.arManager = arManager;
	}

	/************************************************
	 * PUBLIC METHODS
	 ************************************************/
	@Override
	public void handleMessage(Message message) {

		switch (message.what) {
		case R.id.reposition:
			Log.i(TAG, "Mensagem recebida: Reposicionar.");
			reposicionar((DecodeDTO) message.obj);
			break;
		case R.id.create:
			Log.i(TAG, "Mensagem recebida: Criar.");
			criar((DecodeDTO) message.obj);
			break;
		}
	}
	
	/************************************************
	 * PRIVATE METHODS
	 ************************************************/

	/**
	 * 
	 */
	private void reposicionar(DecodeDTO dto) {
		arManager.reposicionarObjetoVirtual(dto.getAppName(), dto.getRotacao());
	}
	
	/**
	 * 
	 * @param dto
	 */
	private void criar(DecodeDTO dto){
		arManager.inserirNovoObjetoVirtual(dto.getAppName(), dto.getRotacao());
	}

}
