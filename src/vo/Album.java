package vo;

import java.util.LinkedList;
import java.util.List;

public class Album {
	
	private int id;	
	private Artist artist;
	private List<Genre> genres;
	private List<Integer> tracks = new LinkedList<Integer>();
	
	
	public int hashCode(){    
        return id;    
	}    
   
	public boolean equals(Object o){    
        return (o instanceof Album) && (id==((Album)o).id);    
    }   
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Artist getArtist() {
		return artist;
	}
	public void setArtist(Artist artist) {
		this.artist = artist;
	}
	public List<Genre> getGenres() {
		return genres;
	}
	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}
	public List<Integer> getTracks() {
		return tracks;
	}
	public void setTracks(List<Integer> tracks) {
		this.tracks = tracks;
	}
	
	
}
