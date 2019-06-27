package abdullah.elamien.bloodbank.eventbus;

/**
 * Created by AbdullahAtta on 6/20/2019.
 */
public class SearchEvent {
    private final String searchEventMessage;

    public SearchEvent(String searchEventMessage) {
        this.searchEventMessage = searchEventMessage;
    }

    public String getSearchEventMessage() {
        return searchEventMessage;
    }
}
