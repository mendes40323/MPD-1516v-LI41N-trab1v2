package weathergw.dal.external;

import weathergw.dal.DatesInterval;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

/**
 * Created by tonym on 29/03/2016.
 */
public class WeatherInfoFileProvider extends WeatherInfoExternalProvider {

    public WeatherInfoFileProvider(String location, String fileExtension, int max_interval) {
        super(location, fileExtension, max_interval);
    }

    @Override
    protected List<String> readFile() {
        try {

            Path p = Paths.get(ClassLoader.getSystemResource(location + "." + fileExtension).toURI());

            return Files.readAllLines(p);

        } catch (Exception  e) {
            // TODO Should log to some log mechanism
            e.printStackTrace();
            return null;
        }

    }

}
