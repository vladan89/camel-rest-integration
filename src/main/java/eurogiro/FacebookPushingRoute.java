package eurogiro;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * Created by Vladan.Vulovic on 4/13/2017.
 */
public class FacebookPushingRoute extends RouteBuilder {

    final static private String ACCESS_TOKEN = "EAACEdEose0cBAAbFLDguZBZCpNg8jGhBs4HaBrT1x2kPbE5S8ZBAGhXcDUOZBQME4fpndMz47KbcUnk0CBs7MQZCKznFfz6PrdYmGtfzcVLXvvSDWAEJgUULwM8vp23OtFyOxC4SPqVeemP4ylLQI1DEie86XeIC6ZBgqPVPkLPKqCPKmOdh2Fj0ZApukHh9lb0GdP3l7u67gZDZD";
    final static private String PROFILE_ID = "106194546602466";

    @Override
    public void configure() throws Exception {
        from("direct:jokesToSend")
            .streamCaching()
            .log(LoggingLevel.INFO, "Exchange received: ${body}")
            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
            .setHeader(Exchange.HTTP_QUERY, constant("message=").append(simple("${body}")).append(constant("&access_token=")+ ACCESS_TOKEN))
            .to("https://graph.facebook.com/v2.8/"+PROFILE_ID+"/"+"feed");

    }
}
