package objectBuilder;

import models.request.PostUpdateEventRequest;
import models.request.SignUpLoginRequest;

public class PostUpdateEventObjectBuilder {


    public static PostUpdateEventRequest createBodyForPostEvent() {
        return PostUpdateEventRequest.builder()
                .title("default title")
                .image("https://www.istockphoto.com/vector/upcoming-events-paper-word-sign-with-colorful-spectrum-paint-brush-strokes-over-gm1758686294-544622150")
                .date("2026-01-08")
                .location("default location")
                .description("default description")
                .build();

    }
}