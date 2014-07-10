package utilities.tweetshandling;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import utilities.generalutils.Printer;

/**
 * Static class which contains tools regarding search on the Twitter API.
 *
 * @author Kanellis Dimitris
 */
public class TwitterTools {

    /**
     * Gets the maximum number of tweets with the query given from the API.
     *
     * @param query the query that was given
     * @return a list of Status that were downloaded
     */
    public static List<Status> search(Query query) {
        QueryResult result;
        List<Status> tweets = new ArrayList<>();
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            Printer.println("Downloading...");
            do {
                result = twitter.search(query);
                tweets.addAll(result.getTweets());
            } while ((query = result.nextQuery()) != null);
            return tweets;
        } catch (TwitterException te) {
            Printer.printErrln("Failed to search tweets: " + te.getMessage());
            if (te.getStatusCode() == 503) {
                Printer.printErrln("Please retry after: "
                        + te.getRetryAfter() + " seconds");
            }
            return tweets;
        }
    }

    /**
     * For every status in the list, if the statu's place or the user's place
     * does not equal the city given then that status is removed from the list.
     *
     * @param tweets the list of tweets
     * @param city the city to filter the list by
     */
    public static void filterTweetsBasedOnCity(final List<Status> tweets,
            final String city) {

        if (city.isEmpty()) {
            return;
        }

        _it = tweets.iterator();
        while (_it.hasNext()) {
            _status = (Status) _it.next();

            if ((_status.getPlace() == null
                    || !_status.getPlace().getName().equalsIgnoreCase(city))
                    && (_status.getUser().getLocation() == null
                    || !_status.getUser().getLocation().equalsIgnoreCase(city))) {
                _it.remove();
            }
        }
    }

    /**
     * Creates a query based on the filters that were given.
     *
     * If no keywords were given then the letter 'a' is used as a keyword since
     * the Twitter API does not support empty queries.
     *
     * @param keywords keywords to search in the API
     * @param since starting date to find tweets from
     * @param until ending date to find tweets from
     * @param count maximum number of tweets to return per page (max 100)
     * @return the query with the filters set
     */
    public static Query queryMaker(final String keywords, final Date since,
            final Date until, final int count) {

        Query query;
        if (keywords.isEmpty()) {
            query = new Query("a");
        } else {
            query = new Query(keywords);
        }

        query.setCount(count);
        query.setLang("en");
        query.setResultType(Query.ResultType.recent);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (since != null) {
            query.setSince(df.format(since));
        }
        if (until != null) {
            query.setUntil(df.format(until));
        }
        return query;
    }

    private static Status _status;
    private static Iterator _it;
}
