package net.apepestudio.gametwocars;

//This interface is used to connect Core code with Android code

public interface ActionResolver {
	public void showOrLoadInterstital();

	public void signIn();

	public void signOut();

	public void submitScore(long score);

	public void showScores();

	public boolean isSignedIn();

	public void share(Integer points);
	
	public void rate();

}
