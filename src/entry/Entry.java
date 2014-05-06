package entry;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import vo.*;


public class Entry {

    private static void process(int trackId, int rating, Map<Integer, Integer> predictUserTracksMap, List<Integer> allTracksInAlbum, User trainUser) {
        int totalRating = 0;
        int totalTrackNum = 0;
        for (Integer trackIdInAlbum : allTracksInAlbum) {
            // All tracks in this album
            Map<Integer, Integer> allTracksOfUser = trainUser.getTracks();
            if (allTracksOfUser.containsKey(trackIdInAlbum)) {
                // Get track's rating by this user
                int track_rating = trainUser.getTracks().get(trackIdInAlbum);
                totalRating += track_rating;
                totalTrackNum++;
            }
        }
        if (totalTrackNum != 0) {
            int avgRating = totalRating / totalTrackNum;

            predictUserTracksMap.put(trackId, avgRating);
            System.out.println("This is the average rating - " + avgRating);
        } else {

            System.out.println("Sorry, there is no other track listened by the user in this album, please search by Artist!");
            System.out.println("And the only thing I have is the rating of Album by user - " + rating);
            predictUserTracksMap.put(trackId, rating);
        }
    }

    public static void main(String[] args) {
        DataProcessing processEngine = new DataProcessing();

        // Test data set
        List<User> predictUserList = processEngine.getTestUsers();

        for (User predictUser : predictUserList) {
            Map<Integer, User> allUsersHistory = processEngine.getTrainUsers();
            User trainUser = allUsersHistory.get(predictUser.getId());

            if (trainUser == null) continue;

            // Get all tracks listened by this user
            // id, rating
            Map<Integer, Integer> trainUserTracksMap = trainUser.getTracks();
            Map<Integer, Integer> predictUserTracksMap = predictUser.getTracks();

            for (Integer trackId : predictUserTracksMap.keySet()) {
                // Find this track in Training set, then let the test track be this rating
                if (trainUserTracksMap.containsKey(trackId)) {
                    int rating = trainUserTracksMap.get(trackId);
                    predictUserTracksMap.put(trackId, rating);
                    continue;
                }

                Map<Integer, Track> allTracks = processEngine.getAllTracks();
                Track track = allTracks.get(trackId);

                if (track == null) continue;

                // Go to find Album
                // TODO: By this id to find album

                Map<Integer, Integer> trainUserAlbums = trainUser.getAlbums();
                int trackAlbumId = track.getAlbumId();
                Integer albumRating = trainUserAlbums.get(trackAlbumId);

                Map<Integer, Integer> trainUserArtists = trainUser.getArtists();
                int trackArtistId = track.getArtistId();
                Integer artistRating = trainUserArtists.get(trackArtistId);

                if (albumRating != null) {
                    // Rating for this album
                    List<Integer> allTracksInAlbum = processEngine.getAllAlbums().get(trackAlbumId).getTracks();
                    process(trackId, albumRating, predictUserTracksMap, allTracksInAlbum, trainUser);
                }

                else if (artistRating != null) {
                    System.out.println("Sorry, the user never hear about this album, please search by Artist!");
                    List<Integer> allTracksOfArtist = processEngine.getALLArtists().get(track.getArtistId()).getTracks();
                    process(trackId, artistRating, predictUserTracksMap, allTracksOfArtist, trainUser);
                } else {
                    System.out.println("Sorry, the user never hear about this artist, please search by Genre!");
                    predictUserTracksMap.put(trackId, -1); // -1 -> go to search genre

                    // TODO: GO TO find Genre by this track ID
                    track.getGenreId();
                }


            }


        }

        /**
		 * Write the result to file
		 */
		try {
			FileWriter fw=new FileWriter("result.txt");
			List<User> usersInTest = processEngine.getTestUsers();
			for(Iterator<User> iter = usersInTest.iterator(); iter.hasNext();) { 
				User user = iter.next();
				//int id = user.getId();
				//fw.append(id+"\n");
				TreeMap<Integer, Integer> tracksOfUser = user.getTracks();
				List<Integer> tracksInTest = user.getTracksInTest();
				int numRate[] = new int[6];
				int k=0;
				for(Iterator<Integer> it = tracksInTest.iterator(); it.hasNext();) { 
					int trackIdInList = it.next();
					numRate[k++] = tracksOfUser.get(trackIdInList);
				}
				Arrays.sort(numRate);
				for(Iterator<Integer> it = tracksInTest.iterator(); it.hasNext();) { 
					int trackIdInList = it.next();
					 if(tracksOfUser.get(trackIdInList)>numRate[2]){
						 fw.append(1 + "\n");
					 }else{
						 fw.append(0 + "\n");
					 }
				}
				
				
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}		

}
