package hale.bc.server.to;

import java.util.ArrayList;
import java.util.List;

public class MockHits {
	final public int LATEST_COUNT = 20;
	
	private List<MockHit> latestHits = new ArrayList<>(LATEST_COUNT);
	private String hitsLogName;
	
	public List<MockHit> getLatestHits() {
		return latestHits;
	}
	public void setLatestHits(List<MockHit> latestHits) {
		this.latestHits = latestHits;
	}
	public String getHitsLogName() {
		return hitsLogName;
	}
	public void setHitsLogName(String hitsLogName) {
		this.hitsLogName = hitsLogName;
	}
	
}
