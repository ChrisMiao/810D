package entry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import vo.*;

public class DataProcessing {

    private List<User> testUsers;
    private Map<Integer, User> trainUsers;
    private Map<Integer, Track> allTracks;
    private Map<Integer, Album> allAlbums;
    private Map<Integer, Artist> allArtists;
    private Map<Integer, Genre> allGenres;

    public DataProcessing(String path) {

        long start, end;

        System.out.print("Loading data genreData2.txt...");
        start = System.currentTimeMillis();
        loadDataGenre(path + "genreData2.txt");
        end = System.currentTimeMillis();
        System.out.println((end - start) + " millisec");

        System.out.print("Loading data artistData2.txt...");
        start = System.currentTimeMillis();
        loadDataArtist(path + "artistData2.txt");
        end = System.currentTimeMillis();
        System.out.println((end - start) + " millisec");

        System.out.print("Loading data albumData2.txt...");
        start = System.currentTimeMillis();
        loadDataAlbum(path + "albumData2.txt");
        end = System.currentTimeMillis();
        System.out.println((end - start) + " millisec");

        System.out.print("Loading data trackData2.txt...");
        start = System.currentTimeMillis();
        loadDataTrack(path + "trackData2.txt");
        end = System.currentTimeMillis();
        System.out.println((end - start) + " millisec");

        System.out.print("Loading data trainIdx2.txt...");
        start = System.currentTimeMillis();
        loadDataTrain(path + "trainIdx2.txt");
        end = System.currentTimeMillis();
        System.out.println((end - start) + " millisec");

        System.out.print("Loading data testIdx2.txt...");
        start = System.currentTimeMillis();
        loadDataTest(path + "testIdx2.txt");
        end = System.currentTimeMillis();
        System.out.println((end - start) + " millisec");

    }

    public List<User> getTestUsers() {
        return testUsers;
    }

    public Map<Integer, User> getTrainUsers() {
        return trainUsers;
    }

    public Map<Integer, Track> getAllTracks() {
        return allTracks;
    }

    public Map<Integer, Album> getAllAlbums() {
        return allAlbums;
    }

    public Map<Integer, Artist> getALLArtists() {
        return allArtists;
    }

    public Map<Integer, Genre> getAllGenres() {
        return allGenres;
    }

    private List<String> loadData(String path) {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path));) {
            String tempstr;
            while ((tempstr = br.readLine()) != null) {
                tempstr.trim();
                list.add(tempstr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void loadDataTrack(String path) {

        allTracks = new HashMap<Integer, Track>();
        List<String> data = loadData(path);
        for (String str : data) {
            Track track = new Track();
            String[] strArray = str.split("\\|");
            int trackId = Integer.parseInt(strArray[0]);
            int trackAlbum = -1;  //-1 means none
            int trackArtist = -1;
            List<Integer> trackGenres = new LinkedList<Integer>();
            if (!strArray[1].equals("None")) {
                trackAlbum = Integer.parseInt(strArray[1]);
                if (allAlbums.containsKey(trackAlbum)) {
                    Album album = allAlbums.get(trackAlbum);
                    List<Integer> tracksInList = album.getTracks();
                    tracksInList.add(trackId);
                    album.setTracks(tracksInList);
                    allAlbums.put(trackAlbum, album);
                }
            }
            if (!strArray[2].equals("None")) {
                trackArtist = Integer.parseInt(strArray[2]);
                if (allArtists.containsKey(trackArtist)) {
                    Artist artist = allArtists.get(trackArtist);
                    List<Integer> tracksInList = artist.getTracks();
                    tracksInList.add(trackId);
                    artist.setTracks(tracksInList);
                    allArtists.put(trackArtist, artist);
                }
            }
            if (strArray.length > 3) {
                for (int i = 0; i < strArray.length - 3; i++) {
                    int trackGenre = Integer.parseInt(strArray[i + 3]);
                    trackGenres.add(trackGenre);
                    if (allGenres.containsKey(trackGenre)) {
                        Genre genre = allGenres.get(trackGenre);
                        List<Integer> tracksInList = genre.getTracks();
                        tracksInList.add(trackId);
                        genre.setTracks(tracksInList);
                        allGenres.put(trackGenre, genre);
                    }
                }
            } else {
                trackGenres.add(-1);
            }
            track.setId(trackId);
            track.setAlbumId(trackAlbum);
            track.setArtistId(trackArtist);
            track.setGenreId(trackGenres);
            allTracks.put(trackId, track);
        }
    }

    public void loadDataAlbum(String path) {
        allAlbums = new HashMap<Integer, Album>();
        List<String> data = loadData(path);
        for (String str : data) {
            String[] strArray = str.split("\\|");
            int albumId = Integer.parseInt(strArray[0]);
            Album album = new Album();
            allAlbums.put(albumId, album);
        }
    }

    public void loadDataArtist(String path) {
        allArtists = new HashMap<Integer, Artist>();
        List<String> data = loadData(path);
        for (String str : data) {
            int artistId = Integer.parseInt(str);
            Artist artist = new Artist();
            artist.setId(artistId);
            allArtists.put(artistId, artist);
        }
    }

    public void loadDataGenre(String path) {
        allGenres = new HashMap<Integer, Genre>();
        List<String> data = loadData(path);
        for (String str : data) {
            int genreId = Integer.parseInt(str);
            Genre genre = new Genre();
            genre.setId(genreId);
            allGenres.put(genreId, genre);
        }
    }

    public void loadDataTrain(String path) {

        trainUsers = new HashMap<Integer, User>();

        User user = null;
        TreeMap<Integer, Integer> track_rating = null;
        Map<Integer, Integer> album_rating = null;
        Map<Integer, Integer> artist_rating = null;
        Map<Integer, Integer> genre_rating = null;

        List<String> data = loadData(path);
        for (String str : data) {
            if (str.contains("|")) {
                if (track_rating != null) {
                    user.setTracks(track_rating);
                    user.setAlbums(album_rating);
                    user.setArtists(artist_rating);
                    user.setGeners(genre_rating);
                    trainUsers.put(user.getId(), user);
                }
                user = new User();
                track_rating = new TreeMap<Integer, Integer>();
                album_rating = new HashMap<Integer, Integer>();
                artist_rating = new HashMap<Integer, Integer>();
                genre_rating = new HashMap<Integer, Integer>();
                String[] strArray = str.split("\\|");
                user.setId(Integer.parseInt(strArray[0]));
            } else {
                String[] trackRating = str.split("\t");
                int id = Integer.parseInt(trackRating[0]);
                int rating = Integer.parseInt(trackRating[1]);
                if (allAlbums.containsKey(id)) {
                    album_rating.put(id, rating);
                } else if (allArtists.containsKey(id)) {
                    artist_rating.put(id, rating);
                } else if (allGenres.containsKey(id)) {
                    genre_rating.put(id, rating);
                } else {
                    track_rating.put(Integer.parseInt(trackRating[0]), rating);
                }
            }
        }
    }

    public void loadDataTest(String path) {

        testUsers = new LinkedList<User>();

        User user = null;
        TreeMap<Integer, Integer> track_rating = null;
        List<Integer> tracksInTest = null;

        List<String> data = loadData(path);
        for (String str : data) {
            if (str.contains("|")) {
                if (track_rating != null) {
                    user.setTracks(track_rating);
                    user.setTracksInTest(tracksInTest);
                    testUsers.add(user);
                }
                user = new User();
                track_rating = new TreeMap<Integer, Integer>();
                tracksInTest = new LinkedList<Integer>();
                String[] strArray = str.split("\\|");
                user.setId(Integer.parseInt(strArray[0]));
            } else {
                track_rating.put(Integer.parseInt(str), 0);  //Initialize the rating 0
                tracksInTest.add(Integer.parseInt(str));
            }
        }

        user.setTracks(track_rating);
        user.setTracksInTest(tracksInTest);
        testUsers.add(user);
    }


}
