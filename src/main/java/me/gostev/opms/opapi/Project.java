package me.gostev.opms.opapi;

import java.util.ArrayList;
import java.util.List;

public class Project {

	private final String name;
	private final String identifier;
	private List<Student> assignees = new ArrayList<>();

	public Project(String name) {
		this.name = name;
		this.identifier = name.toLowerCase().replaceAll("[^a-zA-Z0-9 ]", "").replaceAll("[ ]", "-");
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getName() {
		return name;
	}

	public void addAssignee(Student n) {

		assignees.add(n);
	}

	public List<Student> getAssignees() {
		return assignees;
	}

	@Override
	public boolean equals(Object obj) {

		if (!(obj instanceof Project))
			return false;
		else
			return identifier.equals(((Project) obj).getIdentifier());
	}

	@Override
	public int hashCode() {
		return identifier.hashCode();
	}

}
