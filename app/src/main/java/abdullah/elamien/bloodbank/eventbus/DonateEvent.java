package abdullah.elamien.bloodbank.eventbus;

/**
 * Created by AbdullahAtta on 6/19/2019.
 */
public class DonateEvent {
    private final String donateEventMessage;

    public DonateEvent(String donateEventMessage) {
        this.donateEventMessage = donateEventMessage;
    }

    public String getDonateEventMessage() {
        return donateEventMessage;
    }
}
