package me.gostev.opms.opapi;

public class Student {

	private final String username;
	private final String email;
	private long OPID = 0;
	private boolean added = false;

	public Student(String username, String email) {
		this.username = username;
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public long getOPID() {
		return OPID;
	}

	public void setOPID(long oPID) {
		OPID = oPID;
	}

	public boolean getAdded() {
		return added;
	}

	public void setAdded(boolean added) {
		this.added = added;
	}

	@Override
	public boolean equals(Object s) {

		if (!(s instanceof Student))
			return false;
		else
			return username.equals(((Student) s).getUsername());
	}

	@Override
	public int hashCode() {

		return username.hashCode();
	}
}
