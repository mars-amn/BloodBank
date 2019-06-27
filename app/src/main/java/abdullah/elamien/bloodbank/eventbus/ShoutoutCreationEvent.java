package abdullah.elamien.bloodbank.eventbus;

/**
 * Created by AbdullahAtta on 6/19/2019.
 */
public class ShoutoutCreationEvent {

    private final String shoutoutCreationMessage;

    public ShoutoutCreationEvent(String shoutoutCreationMessage) {
        this.shoutoutCreationMessage = shoutoutCreationMessage;
    }

    public String getShoutoutCreationMessage() {
        return shoutoutCreationMessage;
    }
}
