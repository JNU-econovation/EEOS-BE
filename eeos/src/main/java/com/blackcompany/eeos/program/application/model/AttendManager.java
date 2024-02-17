package com.blackcompany.eeos.program.application.model;

import com.blackcompany.eeos.target.application.model.AttendModel;
import java.util.ArrayList;
import java.util.List;

public class AttendManager {

	private final List<AttendModel> nonRelated = new ArrayList<>();
	private final List<AttendModel> related = new ArrayList<>();

	public void addRelated(AttendModel model) {
		related.add(model);
	}

	public void addNonRelated(AttendModel model) {
		nonRelated.add(model);
	}

	public List<AttendModel> getNonRelated() {
		return List.copyOf(nonRelated);
	}

	public List<AttendModel> getRelated() {
		return List.copyOf(related);
	}
}
