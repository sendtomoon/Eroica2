package com.sendtomoon.eroica2.allergo.impl;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;

import com.sendtomoon.eroica2.allergo.AllergoPathListener;

public class AllergoZkPathListener implements IZkChildListener {

	private AllergoPathListener pathListener;

	public AllergoZkPathListener(AllergoPathListener pathListener) {
		this.pathListener = pathListener;
	}

	@Override
	public void handleChildChange(String parentPath, List<String> childrenPaths) throws Exception {
		pathListener.allergoPathChanged(parentPath, childrenPaths);
	}

}
