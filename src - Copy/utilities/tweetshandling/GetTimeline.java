/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities.tweetshandling;

import java.util.List;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 *
 * @author Dimitrios
 */
public class GetTimeline {

    public static void main(String[] args) {
        Twitter twitter = new TwitterFactory().getInstance();
        try {
            List<Status> statuses = twitter.getHomeTimeline();
            for (int i = 0; i < 10; i++) {
                System.out.println();
            }
            System.out.println("Showing home timeline.");
            for (Status status : statuses) {
                System.out.println(status.getUser().getName() + ":"
                        + status.getText());
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }

    }
}
