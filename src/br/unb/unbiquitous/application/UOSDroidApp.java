package br.unb.unbiquitous.application;

import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import br.unb.unbiquitous.ubiquitos.uos.adaptabitilyEngine.Gateway;
import br.unb.unbiquitous.ubiquitos.uos.application.UosApplication;
import br.unb.unbiquitous.ubiquitos.uos.context.ContextException;
import br.unb.unbiquitous.ubiquitos.uos.context.UOSApplicationContext;
import br.unb.unbiquitous.ubiquitos.uos.ontology.OntologyDeploy;
import br.unb.unbiquitous.ubiquitos.uos.ontology.OntologyStart;
import br.unb.unbiquitous.ubiquitos.uos.ontology.OntologyUndeploy;

/**
 * 
 * @author ricardoandrade
 *
 */
public class UOSDroidApp implements UosApplication{
	
	/************************************************************
	 * CONSTANTS
	 ************************************************************/
	
	private static final Logger logger = Logger.getLogger(UOSDroidApp.class);
	
	/************************************************************
	 * VARIABLES
	 ************************************************************/
	
	private UOSApplicationContext applicationContext;
	
	/************************************************************
	 * PUBLIC METHODS
	 ************************************************************/
	
	public void start(ResourceBundle bundle){
		applicationContext = new UOSApplicationContext();
		try {
			applicationContext.init(bundle);
		} catch (ContextException e) {
			logger.error("Não foi possível inicializar o middleware: " + e.getMessage());
		}
	}
	
	@Override
	public void start(Gateway gateway, OntologyStart ontology) {}

	@Override
	public void stop() throws Exception {}

	@Override
	public void init(OntologyDeploy ontology) {}

	@Override
	public void tearDown(OntologyUndeploy ontology) throws Exception {}
	
	/************************************************************
	 * GETTERS AND SETTERS
	 ************************************************************/
	public UOSApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(UOSApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	


}
