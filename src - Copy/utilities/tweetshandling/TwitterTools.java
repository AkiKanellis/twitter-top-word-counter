/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import utilities.Helper;

/**
 *
 * @author Dimitrios
 */
public class TwitterTools {

    public static List<Status> search(Query query) {
        QueryResult result;
        List<Status> tweets = new ArrayList<>();
        try {
            Twitter twitter = new TwitterFactory().getInstance();
            Helper.println("Downloading...");
            do {
                result = twitter.search(query);
                tweets.addAll(result.getTweets());
            } while ((query = result.nextQuery()) != null);
        } catch (TwitterException te) {
            Helper.printErrln("Failed to search tweets: " + te.getMessage());
            if (te.getStatusCode() == 503) {
                Helper.printErrln("Please retry after: "
                        + te.getRetryAfter() + " seconds");
            }
        } catch (Exception e) {
            Helper.printErrln(e.getMessage());
        } finally {
            return tweets;
        }
    }

    public static void filterTweetsBasedOnCity(List<Status> tweets, final String city) {
        if (city.isEmpty()) {
            return;
        }

        // Using iterators to be able to modify the list
        _it = tweets.iterator();
        try {
            while (_it.hasNext()) {
                _status = (Status) _it.next();
                if ((_status.getPlace() == null || !_status.getPlace().getName().equalsIgnoreCase(city))
                        && (_status.getUser().getLocation() == null || !_status.getUser().getLocation().equalsIgnoreCase(city))) {
                    _it.remove();
                }
            }
        } catch (NullPointerException ne) {
            System.err.println(ne.getMessage());
        }

    }

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
