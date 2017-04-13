package eurogiro;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Created by Vladan.Vulovic on 4/13/2017.
 */
public class JokePollingRouteTest extends CamelTestSupport{

    @EndpointInject(uri = "mock:result")
    protected MockEndpoint resultEndpoint;

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @Test
    public void test() throws Exception {
        resultEndpoint.expectedMessageCount(1);
        template.sendBodyAndHeader("test", "test", "test");
        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("direct:start")
                        .streamCaching()
                        .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                        .to("http://api.icndb.com/jokes/random")
                        .log(LoggingLevel.INFO, "Exchange received ${body}")
                        .setHeader(Exchange.FILE_NAME, constant("message.html"))
                        .to("file:result")
                        .to("mock:result");
            }
        };
    }
}
