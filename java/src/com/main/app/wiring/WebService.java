//package com.main.app.wiring;
//
//import com.main.app.accounts.AccountBase;
//import com.main.app.accounts.AccountFactory;
//import io.javalin.Javalin;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static com.main.app.accounts.AccountType.ADULT;
//
//public class WebService {
//
//    public static WebService instance;
//    private WebService() {}
//
//    public static WebService getInstance() {
//        if (instance == null) {
//            instance = new WebService();
//        } return  instance;
//    }
//
//    public static Javalin getJavalinConnection() {
//        return Javalin.create(config -> {
//            config.enableCorsForAllOrigins();
//            config.enforceSsl = true;
//            config.asyncRequestTimeout = 10_000L;
//        }).start(7070);
//    }
//
//    public static void main(String[] args) {
//        Javalin app = getJavalinConnection();
//        AccountBase account = AccountFactory.createAccount(
//                ADULT, "JavalinTestUser", "Password123!", 100f, "Javalin", "Test");
//
//        app.post("/account", ctx -> {
//            .put(ctx.formParam("day"), ctx.formParam("time"));
//            ctx.html("Your reservation has been saved");
//        });
//
//        app.get("/check-reservation", ctx -> ctx.html(reservations.get(ctx.queryParam("day"))));
//
//    }
//
//}
