package vo;

import java.util.List;

public class Track {
	private int id;
	private int albumId;
	private int artistId;
	private List<Integer> genreId;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAlbumId() {
		return albumId;
	}
	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}
	public int getArtistId() {
		return artistId;
	}
	public void setArtistId(int artistId) {
		this.artistId = artistId;
	}
	public List<Integer> getGenreId() {
		return genreId;
	}
	public void setGenreId(List<Integer> genreId) {
		this.genreId = genreId;
	}
	
	
	
}
