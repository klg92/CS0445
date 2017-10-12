package cs445.a4;

/**
 * This abstract data type is a predictive engine for video ratings in a streaming video system. It
 * stores a set of users, a set of videos, and a set of ratings that users have assigned to videos.
 *
 * NOTE: There is no capacity limit for the number of Videos, the client will be responsible for not running out of memory. 
 * NOTE: this data structure cannot contain duplicate elements. 
 */
public interface VideoEngine {

    /**
     * The abstract methods below are declared as void methods with no parameters. You need to
     * expand each declaration to specify a return type and parameters, as necessary. You also need
     * to include a detailed comment for each abstract method describing its effect, its return
     * value, any corner cases that the client may need to consider, any exceptions the method may
     * throw (including a description of the circumstances under which this will happen), and so on.
     * You should include enough details that a client could use this data structure without ever
     * being surprised or not knowing what will happen, even though they haven't read the
     * implementation.
     */

    /**
     * Adds a new video to the system, returning a boolean to signify whether the video given in parameters was successfully added or not. 
	 * Returns false if video was already in the system, true if successfully added. Throws NullPointerException if video given in parameters is null. 
	 * @param theVideo which is the video to add to the system.
	 * @throws NullPointerException if theVideo is null.
	 * @return boolean that signifies whether the video was successfully added or not. 
	 * 
     */
    boolean addVideo(Video theVideo) throws NullPointerException;
	
    /**
     * Removes an existing video from the system, specified by the parameter theVideo. Returns a boolean to signify whether 
	 * the video was successfully removed or not. Returns false if theVideo was not in the system. NullPointerException if theVideo is null.
	 * @param theVideo is the video to remove.
	 * @throws NullPointerException if theVideo is null. 
	 * @return boolean to represent whether the video was removed or not. 
     */
    boolean removeVideo(Video theVideo) throws NullPointerException;
	
    /**
     * Adds an existing television episode to an existing television series. returns a boolean to signify whether if was successfully added or not. 
	 * returns false if episode was already in the specified series as well as if the episode or series are not in the system. 
	 * @param episode denotes the episode to add to the series. 
	 * @param series denotes the series that episode will be added to. 
	 * @throws NullPointerException if either episode or series is null. 
	 * @return returns a boolean to note whether if was added successfully.
     */
    boolean addToSeries(TvEpisode episode, TvSeries series) throws NullPointerException;
	
    /**
     * Removes a television episode from a television series. returns true if the video was successfully removed, false if the episode or series is not in the system as well as
	 * if the episode is not in the series. 
	 * @param episode is the episode to be removed
	 * @param series is the series the episode is being removed from. 
	 * @throws NullPointerException if episode or series is null. 
	 * @return boolean to note whether episode was successfully removed or not. 
     */
    boolean removeFromSeries(TvEpisode episode, TvSeries series) throws NullPointerException;
	
    /**
     * Sets a user's rating for a video, as a number of stars from 1 to 5. user is theUser, video is theVideo, rating is the rating the user wishes to give. 
	 * NOTE: The client should make sure that the int rating is between 1 to 5.  Throws NullPointerException if theVideo or theUser is null. Returns false if video is not in system. 
	 * @param theUser is an object of type User that represents the user making the rating. 
	 * @param theVideo is an object of type Video that represents the video the user is rating. 
	 * @param rating is the rating, int stars, that the user wishes to give the video. 
	 * @throws NullPointerException if theVideo is null.
	 * 
	 * @return boolean to signify whether video has successfully been rated or not. returns false if video is not in system. 
     */
    boolean rateVideo(User theUser, Video theVideo, int rating) throws NullPointerException;
	
    /**
     * Clears a user's rating on a video. If this user has rated this video and the rating has not
     * already been cleared, then the rating is cleared and the state will appear as if the rating
     * was never made. If this user has not rated this video, or if the rating has already been
     * cleared, then this method will throw an IllegalArgumentException.
     *
     * @param theUser user whose rating should be cleared
     * @param theVideo video from which the user's rating should be cleared
     * @throws IllegalArgumentException if the user does not currently have a rating on record for
     * the video
     * @throws NullPointerException if either the user or the video is null
     */
    public void clearRating(User theUser, Video theVideo);

    /**
     * Predicts the rating a user will assign to a video that they have not yet rated, as a number
     * of stars from 1 to 5. Throws NullPointerException if theVideo is null and IllegalArgumentException if theVideo is not in the system and not null. 
	 * @param theVideo is theVideo to predict a rating for.
	 * @throws NullPointerException if theVideo is null. 
	 * @throws IllegalArgumentException if theVideo is not in the system. 
	 * @return an int from 1 to 5 representing the rating predicted. 
     */
    int predictRating(Video theVideo) throws NullPointerException, IllegalArgumentException;
	
    /**
     * Suggests a video for a user (e.g.,based on their predicted ratings). Returns null if all videos in the system have been rated. 
	 * @return the video that was suggested. 
     */
    Video suggestVideo();
	

}

