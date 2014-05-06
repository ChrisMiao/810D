package entry;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import vo.*;


public class Entry {

	public static void main(String[] args) {
		DataProcessing dataPro = new DataProcessing();

		/*
		 * Map<Integer, Track> allTracks = dataPro.getAllTracks(); Track track1
		 * = allTracks.get(2); Track track2 = allTracks.get(9); Track track3 =
		 * allTracks.get(38);
		 * 
		 * System.out.println(track1.getId() + "-" + track1.getArtistId() + "-"
		 * + track1.getAlbumId() + "-" + track1.getGenreId().get(0));
		 * System.out.println(track2.getId() + "-" + track2.getArtistId() + "-"
		 * + track2.getAlbumId() + "-" + track2.getGenreId().get(0));
		 * System.out.println(track3.getId() + "-" + track3.getArtistId() + "-"
		 * + track3.getAlbumId() + "-" + track3.getGenreId().get(0));
		 */
		// dataPro.loadDataTest();

		// Test data set

		List<User> testUsers = dataPro.getTestUsers();
		//refactor
		for (Iterator<User> iter = testUsers.iterator(); iter.hasNext();) {
			
			
			User testUser = iter.next();
			int testUserId = testUser.getId();
			// System.out.println("Test user_ID:"+ testUserId);
			Map<Integer, User> allUsers = dataPro.getTrainUsers();
			if (allUsers.containsKey(testUserId)) {

				User trainUser = allUsers.get(testUserId);
				// Get all tracks listened by this user
				Map<Integer, Integer> allTracksInTrainOfUser = trainUser
						.getTracks();

				TreeMap<Integer, Integer> allTracksInTestOfUser = testUser
						.getTracks();
				Iterator it = allTracksInTestOfUser.keySet().iterator();

				while (it.hasNext()) {
					Integer key = (Integer) it.next();
					Integer tracksIdInTest = key;
					// Find this track in Training set, then let the test track
					// be this rating
					if (allTracksInTrainOfUser.containsKey(tracksIdInTest)) {
						int rating = allTracksInTrainOfUser.get(tracksIdInTest);
						allTracksInTestOfUser.put(tracksIdInTest, rating);
					} else { // Go to find Album
						// TODO: By this id to find album
						Map<Integer, Track> allTracks = dataPro.getAllTracks();

						if (allTracks.containsKey(tracksIdInTest)) {
							Track track = allTracks.get(tracksIdInTest);
							int albumIdOfTrack = track.getAlbumId();
							Map<Integer, Integer> allAlbumsInTrainOfUser = trainUser
									.getAlbums();
							if (allAlbumsInTrainOfUser
									.containsKey(albumIdOfTrack)) {

								// Rating for this album
								int album_rating = allAlbumsInTrainOfUser
										.get(albumIdOfTrack);
								List<Integer> allTracksInAlbum = dataPro
										.getAllAlbums().get(albumIdOfTrack)
										.getTracks();
								// System.out.println("All tracks in Album "+
								// albumIdOfTrack);

								int totalRating = 0;
								int totalTrackNum = 0;
								for (Iterator<Integer> iter1 = allTracksInAlbum
										.iterator(); iter1.hasNext();) {
									// All tracks in this album
									int trackIdInAlbum = iter1.next();
									Map<Integer, Integer> allTracksOfUser = trainUser
											.getTracks();
									if (allTracksOfUser
											.containsKey(trackIdInAlbum)) {
										// Get track's rating by this user
										int track_rating = trainUser
												.getTracks()
												.get(trackIdInAlbum);
										totalRating += track_rating;
										totalTrackNum++;
									}
								}
								if (totalTrackNum != 0) {
									int avgRating = totalRating / totalTrackNum;

									allTracksInTestOfUser.put(tracksIdInTest,
											avgRating);
									System.out
											.println("This is the average rating - "
													+ avgRating);
								} else {

									System.out
											.println("Sorry, there is no other track listened by the user in this album, please search by Artist!");
									System.out
											.println("And the only thing I have is the rating of Album by user - "
													+ album_rating);
									allTracksInTestOfUser.put(tracksIdInTest,
											album_rating);
								}
							} else {
								System.out
										.println("Sorry, the user never hear about this album, please search by Artist!");
								int artistIdOfTrack = track.getArtistId();//
								Map<Integer, Integer> allArtistInTrainOfUser = trainUser
										.getArtists();
								if (allArtistInTrainOfUser
										.containsKey(artistIdOfTrack)) {
									int artist_rating = allArtistInTrainOfUser
											.get(artistIdOfTrack);
									List<Integer> allTracksOfArtist = dataPro
											.getALLArtists()
											.get(artistIdOfTrack).getTracks();

									int totalRating = 0;
									int totalTrackNum = 0;
									for (Iterator<Integer> iter1 = allTracksOfArtist
											.iterator(); iter1.hasNext();) {
										// All tracks Of this artist
										int trackIdOfArtist = iter1.next();
										Map<Integer, Integer> allTracksOfUser = trainUser
												.getTracks();
										if (allTracksOfUser
												.containsKey(trackIdOfArtist)) {
											// Get track's rating by this user
											int track_rating = trainUser
													.getTracks().get(
															trackIdOfArtist);
											totalRating += track_rating;
											totalTrackNum++;
										}
									}
									if (totalTrackNum != 0) {
										int avgRating = totalRating
												/ totalTrackNum;
										allTracksInTestOfUser.put(
												tracksIdInTest, avgRating);
										System.out
												.println("This is the average rating - "
														+ avgRating);

									} else {

										System.out
												.println("Sorry, there is no other track listened by the user of this artist, please search by Genre!");
										System.out
												.println("And the only thing I have is the rating of Artist by user - "
														+ artist_rating);
										allTracksInTestOfUser.put(
												tracksIdInTest, artist_rating);
									}
								} else {
									System.out
											.println("Sorry, the user never hear about this artist, please search by Genre!");
									allTracksInTestOfUser.put(tracksIdInTest,
											-1); // -1 means need to go to
													// search genre

									// TODO: GO TO find Genre by this track ID
									track.getGenreId();
								}

							}
						}

					}

				}

			}

		}

		// Train data set
		/*
		 * Map<Integer, User> trainUsers = dataPro.getTrainUsers(); Iterator
		 * iter = trainUsers.entrySet().iterator(); while(iter.hasNext()){
		 * Map.Entry entry0 = (Map.Entry) iter.next(); User user = (User)
		 * entry0.getValue(); System.out.println("User_ID:"+user.getId());
		 * Map<Integer, Integer> tracks = user.getTracks(); Iterator it =
		 * tracks.entrySet().iterator(); while (it.hasNext()) { Map.Entry entry
		 * = (Map.Entry) it.next(); Integer key = (Integer) entry.getKey();
		 * Integer val = (Integer) entry.getValue();
		 * System.out.println("Track_ID: "+key+",Rating:"+val); }
		 * 
		 * Map<Integer, Integer> album = user.getAlbums(); Iterator it2 =
		 * album.entrySet().iterator(); while (it2.hasNext()) { Map.Entry entry
		 * = (Map.Entry) it2.next(); Integer key = (Integer) entry.getKey();
		 * Integer val = (Integer) entry.getValue();
		 * System.out.println("Album_ID: "+key+",Rating:"+val); }
		 * 
		 * Map<Integer, Integer> artist = user.getArtists(); Iterator it3 =
		 * artist.entrySet().iterator(); while (it3.hasNext()) { Map.Entry entry
		 * = (Map.Entry) it3.next(); Integer key = (Integer) entry.getKey();
		 * Integer val = (Integer) entry.getValue();
		 * System.out.println("Artist_ID: "+key+",Rating:"+val); }
		 * 
		 * Map<Integer, Integer> genre = user.getGeners(); Iterator it4 =
		 * genre.entrySet().iterator(); while (it4.hasNext()) { Map.Entry entry
		 * = (Map.Entry) it4.next(); Integer key = (Integer) entry.getKey();
		 * Integer val = (Integer) entry.getValue();
		 * System.out.println("Genre_ID: "+key+",Rating:"+val); } }
		 */

		/**
		 * Write the result to file
		 */
		try {
			FileWriter fw = new FileWriter("result1.txt");
			List<User> usersInTest = dataPro.getTestUsers();
			for (Iterator<User> iter = usersInTest.iterator(); iter.hasNext();) {
				User user = iter.next();
				// int id = user.getId();
				// fw.append(id+"\n");
				TreeMap<Integer, Integer> tracksOfUser = user.getTracks();
				List<Integer> tracksInTest = user.getTracksInTest();
				int numRate[] = new int[6];
				// ��ֵ�� ��rateing��ֵ����һ�����飻
				for (int i = 0; i < 6; i++) {
					Iterator<Integer> it = tracksInTest.iterator();
					int trackId = it.next();
					int rating = tracksOfUser.get(trackId);
					numRate[i] = rating;
				}
				// �Ը�ֵ���������з��࣬process the rating numbers;
				/**
				 * for(int i = 0; i < 6; i++){ int count = 1; for(int j = 0; j <
				 * 6; j++){
				 * 
				 * if(numRate[j] > numRate[i]){ count++; } } if(count >= 4){
				 * numRate[i] = 0; } else{ numRate[i] = 1; }
				 * 
				 * }
				 */
				for (int j = 0; j < 6; j++) {
					fw.append(numRate[j] + "\n");
				}

			}

			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
