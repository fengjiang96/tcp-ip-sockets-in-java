import java.util.HashMap;
import java.util.Map;

public class VoteService {
	private Map<Integer, Long> results = new HashMap<Integer, Long>();

	public VoteMsg handleRequest(VoteMsg msg) {
		if (msg.isResponse()) {
			return msg;
		}

		msg.setResponse(true);
		int candidate = msg.getCandidateID();
		Long count = results.get(candidate);
		if (count == null) {
			count = 0L;
		}
		if (!msg.isInquiry()) {
			results.put(candidate, ++count);
		}
		msg.setVoteCount(count);
		return msg;
	}
}