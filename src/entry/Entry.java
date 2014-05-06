package entry;

import vo.Track;
import vo.User;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


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

    private static String generateRate(DataProcessing processEngine) {
        StringBuffer buf = new StringBuffer();

        for (User testUser : processEngine.getTestUsers()) {
            // K - trackId, V - rate
            Map<Integer, Integer> allTracks = testUser.getTracks();
            // trackId
            List<Integer> testTracks = testUser.getTracksInTest();

            int numRate[] = new int[testTracks.size()];
            int i = 0;
            for (Integer trackId : testTracks) {
                numRate[i++] = allTracks.get(trackId);
            }
            Arrays.sort(numRate);

            int threshold = numRate[2];

            for (Integer trackId : testTracks) {
                int rating = allTracks.get(trackId);
                rating = rating > threshold ? 1 : 0;
                buf.append(rating + "\n");
            }
        }

        return buf.toString();
    }

    public static void main(String[] args) {
        DataProcessing processEngine = new DataProcessing();

        // Test data set
        List<User> predictUserList = processEngine.getTestUsers();
        Map<Integer, User> allUsersHistory = processEngine.getTrainUsers();

        for (User predictUser : predictUserList) {
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

                Track track = processEngine.getAllTracks().get(trackId);
                if (track == null) continue;

                // Find in Album
                Map<Integer, Integer> trainUserAlbums = trainUser.getAlbums();
                int trackAlbumId = track.getAlbumId();
                Integer albumRating = trainUserAlbums.get(trackAlbumId);

                Map<Integer, Integer> trainUserArtists = trainUser.getArtists();
                int trackArtistId = track.getArtistId();
                Integer artistRating = trainUserArtists.get(trackArtistId);

                if (albumRating != null) {
                    List<Integer> allTracksInAlbum = processEngine.getAllAlbums().get(trackAlbumId).getTracks();
                    process(trackId, albumRating, predictUserTracksMap, allTracksInAlbum, trainUser);
                } else if (artistRating != null) {
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

        String result = generateRate(processEngine);

        try {
            FileWriter fw = new FileWriter("result.txt");
            fw.write(result);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
