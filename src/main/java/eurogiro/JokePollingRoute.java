package eurogiro;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by Vladan.Vulovic on 4/13/2017.
 */

/**
 *  Route that each 20s polls an Chuck Norris joke, parse it and forward it to another endpoint
 */
public class JokePollingRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("timer://foo?fixedRate=true&delay=1000&period=20000")
            .streamCaching()
            .setHeader(Exchange.HTTP_METHOD, constant("GET"))
            .to("http://api.icndb.com/jokes/random")
            .log(LoggingLevel.INFO, "Exchange received: ${body}")
            .process(new Processor(){
                @Override
                public void process(Exchange exchange)throws Exception{
                    //this is a dirty way
                    JSONObject json = new JSONObject(exchange.getIn().getBody(String.class));
                    JSONObject value = (JSONObject)json.get("value");
                    exchange.getOut().setBody(URLEncoder.encode(value.get("joke").toString()));
                }
            })
            .to("direct:jokesToSend");
    }
}
