package abdullah.elamien.bloodbank.eventbus;

/**
 * Created by AbdullahAtta on 6/24/2019.
 */
public class AuthenticationEvent {
    private final String AuthenticationEventMessage;

    public AuthenticationEvent(String authenticationEventMessage) {
        AuthenticationEventMessage = authenticationEventMessage;
    }

    public String getAuthenticationEventMessage() {
        return AuthenticationEventMessage;
    }
}
