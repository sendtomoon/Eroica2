package com.sendtomoon.eroica2.allergo;

import java.util.List;

public interface AllergoPathListener {

	void allergoPathChanged(String parentPath, List<String> childrenPaths);

}
