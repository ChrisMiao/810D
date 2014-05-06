package vo;

import java.util.LinkedList;
import java.util.List;

public class Genre {
	private int id;
	private List<Artist> artists;
	private List<Album> albums;
	private List<Integer> tracks = new LinkedList<Integer>();
	
	public int hashCode(){    
        return id;    
	}    
   
	public boolean equals(Object o){    
        return (o instanceof Genre) && (id==((Genre)o).id);    
    }  
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<Artist> getArtists() {
		return artists;
	}
	public void setArtists(List<Artist> artists) {
		this.artists = artists;
	}
	public List<Album> getAlbums() {
		return albums;
	}
	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}
	public List<Integer> getTracks() {
		return tracks;
	}
	public void setTracks(List<Integer> tracks) {
		this.tracks = tracks;
	}
	
	
	
	
}
