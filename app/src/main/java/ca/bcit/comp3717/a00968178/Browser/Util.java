package ca.bcit.comp3717.a00968178.Browser;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Brayden on 3/26/2017.
 */

public abstract class Util {

    public static String resolveURL(String givenUrl) throws URISyntaxException {

        URI u = new URI(givenUrl);
        if (!u.isAbsolute()) {

            URI orig = new URI(MainActivity.JSON_URL);
            String port = orig.getPort() == -1 ? "" : ":" + orig.getPort();
            URI base = new URI(orig.getScheme() + "://" + orig.getHost() + port);
            return base.toString() + "/" + u.toString();

        } else {
            return givenUrl;
        }
    }

}
