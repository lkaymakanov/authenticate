package authenticate.token;

public  class UserNavigationPage {
	String navPage;
	Object user;
	boolean logged = false;
	public String getNavPage() {
		return navPage;
	}
	public Object getUser() {
		return user;
	}
	public boolean isLogged() {
		return logged;
	}
	public void setLogged(boolean looged) {
		this.logged = looged;
	}
}