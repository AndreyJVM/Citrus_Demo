package citrus.tests;

import citrus.pojo.Data;
import citrus.pojo.Support;
import citrus.pojo.User;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import static com.consol.citrus.actions.EchoAction.Builder.echo;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class GetUser_Test extends TestNGCitrusSpringSupport {

    private TestContext context;

    @Test(description = "Получение информации о пользователе")
    @CitrusTest
    public void getActions() {
        this.context = citrus.getCitrusContext().createTestContext();

        context.setVariable("value", "superValue");
        $(echo("Property \"value\" = " + context.getVariable("value")));

        $(echo("We have userId = " + context.getVariable("userId")));
        $(echo("Property \"userId\" = " + "${userId}"));

        variable("now", "citrus:currentDate()");
        $(echo("Today is: ${now}"));
        run(http()
                .client("restClientReqres")
                .send()
                .get("users/" + context.getVariable("userId")));

        run(http()
                .client("restClientReqres")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .type(MessageType.JSON)
                .body(new ObjectMappingPayloadBuilder(getJsonData(), "objectMapper"))
        );
    }

    private User getJsonData() {
        User user = new User();

        Data data = new Data();
        data.setId(Integer.valueOf(context.getVariable("userId")));
        data.setEmail("janet.weaver@reqres.in");
        data.setFirst_name("Janet");
        data.setLast_name("Weaver");
        data.setAvatar("https://reqres.in/img/faces/2-image.jpg");
        user.setData(data);

        Support support = new Support();
        support.setUrl("https://reqres.in/#support-heading");
        support.setText("To keep ReqRes free, contributions towards server costs are appreciated!");
        user.setSupport(support);

        return user;
    }
}