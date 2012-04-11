package br.unb.unbiquitous.marker.decoder;

public class DecodeDTO {
	
	private byte[] frame;
	private float[] rotacao;
	private String appName;
	
	
	public byte[] getFrame() {
		return frame;
	}
	public void setFrame(byte[] frame) {
		this.frame = frame;
	}
	public float[] getRotacao() {
		return rotacao;
	}
	public void setRotacao(float[] rotacao) {
		this.rotacao = rotacao;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	
	

}
