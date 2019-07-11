package svn.error;

public interface ErrorListener {

	/**
	 * Handle error output 
	 * @param output - output string
	 * @return {@code true} if error should be output
	 */
	public boolean handleErrorOutput(String output);
	
	/**
	 * @return {@code true} if it is willing to handle errors.
	 * If not, {@link #handleErrorOutput(String)} will not be called.
	 */
	public boolean isHandling();
	
}
