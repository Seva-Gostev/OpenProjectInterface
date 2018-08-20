package me.gostev.opms.opapi;

public class NewUserData {

	private String login;
	private String email;
	private String firstName;
	private String lastName;
	private boolean admin;
	private String language;
	private String password;

	public static class Builder {

		private String login;
		private String email = "";
		private String firstName = "";
		private String lastName = "";
		private boolean admin = false;
		private String language = "en";
		private String password = "";

		public Builder(String login) {
			this.login = login;
		}

		public Builder setEmail(String email) {
			this.email = email;
			return this;
		}

		public Builder setFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public Builder setLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public Builder setIsAdmin(boolean isAdmin) {
			this.admin = isAdmin;
			return this;
		}

		public Builder setLanguage(String language) {
			this.language = language;
			return this;
		}

		public Builder setPassword(String password) {
			this.password = password;
			return this;
		}

		public NewUserData build() {
			NewUserData n = new NewUserData();

			n.login = this.login;
			n.email = this.email;
			n.firstName = this.firstName;
			n.lastName = this.lastName;
			n.admin = this.admin;
			n.language = this.language;
			n.password = this.password;

			return n;
		}
	}

	private NewUserData() {

	}

	public String getLogin() {
		return login;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public boolean getAdmin() {
		return admin;
	}

	public String getLanguage() {
		return language;
	}

	public String getPassword() {
		return password;
	}
}
