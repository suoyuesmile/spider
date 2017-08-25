package priv.suo.vo;

public class WebKey {
	private String key;
	private String url;
	private int rank;
	public WebKey(String key, String url, int rank) {
		super();
		this.url = url;
		this.key = key;
		this.rank = rank;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	
}
