package weathergw.dal.external;

import weathergw.dal.DatesInterval;
import weathergw.domain.WeatherInfo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by tonym on 29/03/2016.
 */
public class ExternalProviderManager {

    private final Collection<WeatherInfoExternalProvider> EXTERNAL_PROVIDERS;

    private Iterator<WeatherInfoExternalProvider> iterator;

    private WeatherInfoExternalProvider currentProvider = null;

    public ExternalProviderManager(Collection<WeatherInfoExternalProvider> external_providers) {
        EXTERNAL_PROVIDERS = external_providers;
        iterator = EXTERNAL_PROVIDERS.iterator();
    }

    public Collection<WeatherInfo> getWeatherInfos(String location, DatesInterval datesInterval) {
        Collection<WeatherInfo> result = null;

        if (!iterator.hasNext())
            iterator = EXTERNAL_PROVIDERS.iterator();

        while (iterator.hasNext()){

            WeatherInfoExternalProvider externalProvider = iterator.next();

            if (externalProvider.getLocation().compareToIgnoreCase(location) == 0) {

                if(!externalProvider.isLimited(datesInterval))
                    result = externalProvider.get(datesInterval);

                if (result != null && !result.isEmpty() )
                    break;

                else  {
                    Collection<DatesInterval> dates =  createCollectionOfDates(datesInterval.getSTART(), datesInterval.getEND(), externalProvider.MAX_INTERVAL);
                    for (DatesInterval d : dates) {

                        if (result == null)
                            result = externalProvider.get(d);
                        else
                            result.addAll(externalProvider.get(d));
                    }

                }

            }
        }
        return result;

    }

    private long calculateDifferenceBetweenDates(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start,end);
    }

    private Collection<DatesInterval> createCollectionOfDates(LocalDate start, LocalDate end, int maxIntervals) {
        Collection<DatesInterval> datesIntervals = new ArrayList<>();
        LocalDate aux_start = start, aux_end = start;

        while((aux_start.isAfter(end) && !aux_start.equals(end)) && aux_end.isBefore(end)) {
            datesIntervals.add(new DatesInterval(aux_start, aux_end.plusDays(maxIntervals)));
            aux_start = aux_start.plusDays(maxIntervals+1);
            aux_end = aux_start.plusDays(maxIntervals);
        }
        datesIntervals.add(new DatesInterval(aux_start, end));
        return datesIntervals;
    }



}
