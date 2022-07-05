package scraper;

import lombok.experimental.UtilityClass;
import scraper.cookie.CookieHashSet;
import scraper.cookie.DefaultCookieJar;
import scraper.interceptor.ErrorInterceptor;
import scraper.interceptor.FakeBrowserInterceptor;
import okhttp3.OkHttpClient;

import java.io.IOException;

@UtilityClass
public class InstagramFactory {

    public static Instagram getAuthenticatedInstagramClient(String login, String password, String userAgent)
            throws IOException{

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new FakeBrowserInterceptor(userAgent))
                .addInterceptor(new ErrorInterceptor())
                .cookieJar(new DefaultCookieJar(new CookieHashSet()))
                .build();
        Instagram client = new Instagram(httpClient);
        client.basePage();
        client.login(login, password);
        client.basePage();
        return client;
    }
}
