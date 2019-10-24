package org.bees.optimizer.server;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataProvider {
    private static ClassLoader loader = DataProvider.class.getClassLoader();

    static String getTeamSum() throws IOException {
        InputStream resourceAsStream = loader.getResourceAsStream("json/teamsum.json");
        return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
    }


     static String getCarsum() throws IOException {
        InputStream resourceAsStream = loader.getResourceAsStream("json/carsum.json");
        return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
    }

     static String getRoutes() throws IOException {
        InputStream resourceAsStream = loader.getResourceAsStream("json/routes.json");
        return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
    }

     static String getTraffic() throws IOException {
        InputStream resourceAsStream = loader.getResourceAsStream("json/traffic.json");
        return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
    }

     static String getPoints() throws IOException {
        InputStream resourceAsStream = loader.getResourceAsStream("json/points.json");
        return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
    }
}
