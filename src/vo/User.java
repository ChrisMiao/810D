package vo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class User {
	
	private int id;
	private Map<Integer, Integer> albums;
	private Map<Integer, Integer> artists;
	private Map<Integer, Integer> geners;
	private TreeMap<Integer, Integer> tracks;
	private List<Integer> tracksInTest;  //Used to get sequence, because HashMap can not output in order
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	public TreeMap<Integer, Integer> getTracks() {
		return tracks;
	}
	public void setTracks(TreeMap<Integer, Integer> tracks) {
		this.tracks = tracks;
	}
	public Map<Integer, Integer> getAlbums() {
		return albums;
	}
	public void setAlbums(Map<Integer, Integer> albums) {
		this.albums = albums;
	}
	public Map<Integer, Integer> getArtists() {
		return artists;
	}
	public void setArtists(Map<Integer, Integer> artists) {
		this.artists = artists;
	}
	public Map<Integer, Integer> getGeners() {
		return geners;
	}
	public void setGeners(Map<Integer, Integer> geners) {
		this.geners = geners;
	}
	public List<Integer> getTracksInTest() {
		return tracksInTest;
	}
	public void setTracksInTest(List<Integer> tracksInTest) {
		this.tracksInTest = tracksInTest;
	}
	

}
