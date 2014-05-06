package vo;

import java.util.LinkedList;
import java.util.List;

public class Artist {
	private int id;
	private List<Album> albums;
	private List<Integer> tracks = new LinkedList<Integer>();
	
	public int hashCode(){    
        return id;    
	}    
   
	public boolean equals(Object o){    
        return (o instanceof Artist) && (id==((Artist)o).id);    
    }  
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
