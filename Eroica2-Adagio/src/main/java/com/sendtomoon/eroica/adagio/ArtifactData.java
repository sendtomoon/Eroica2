package com.sendtomoon.eroica.adagio;

public class ArtifactData implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sha1;
	
	private byte[] datas;
	
	public ArtifactData() {}
	
	public ArtifactData(byte[] datas) {
		this.datas=datas;
	}
	
	public ArtifactData(byte[] datas,String sha1) {
		this.datas=datas;
		this.sha1=sha1;
	}

	public String getSha1() {
		return sha1;
	}

	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}

	public byte[] getDatas() {
		return datas;
	}

	public void setDatas(byte[] datas) {
		this.datas = datas;
	}
	
	public int getDataSize() {
		return (datas==null?-1:datas.length);
	}

	@Override
	public String toString() {
		return "{dataSize="+this.getDataSize()+",sha1="+this.sha1+"}";
	}
	
	
}
