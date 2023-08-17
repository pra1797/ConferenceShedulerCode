import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Shedule the conferance as per the topics and ts duration
 */
class Talk {
    String topic;
    int duration;

    public Talk(String topic, int duration) {
        this.topic = topic;
        this.duration = duration;
    }
}

/**
 * Represents a session of talks in track.
 */
class Session {
    List<Talk> talks = new ArrayList<>();
    int startTime;

    public Session(int startTime) {
        this.startTime = startTime;
    }
}

/**
 * Manages the scheduling of sessions in tracks.
 */
class TrackTheTime {
    List<Session> sessions = new ArrayList<>();
    int remainingTime;

    public TrackTheTime(int totalMinutes) {
        this.remainingTime = totalMinutes;
    }

    public boolean addSession(Session session) {
        int totalDuration = session.talks.stream().mapToInt(t -> t.duration).sum();
        if (remainingTime >= totalDuration) {
            sessions.add(session);
            remainingTime -= totalDuration;
            return true;
        }
        return false;
    }
}

/**
 * Main class to execute the conference scheduling.
 */
public class ProgrammingConferenceScheduler {
    private static final int MORNING_SESSION_DURATION = 180;
    private static final int AFTERNOON_SESSION_DURATION = 240;
    private static final int MAX_TALKS_PER_SESSION = 4;
    private static final int TOTAL_MINUTES = 480;

    public static void main(String[] args) {
        List<Talk> talks = getTalksFromUser();
        scheduleConference(talks);
    }

    private static List<Talk> getTalksFromUser() {
        List<Talk> talks = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Enter the number of talks:");
            int numberOfTalks = scanner.nextInt();
            for (int i = 0; i < numberOfTalks; i++) {
                System.out.println("Enter the topic of talk " + (i + 1) + ":");
                String topic = scanner.nextLine();
                scanner.nextLine();
                System.out.println("Enter the duration of talk " + (i + 1) + " in minutes:");
                int duration = scanner.nextInt();
                talks.add(new Talk(topic, duration));
            }
        } catch (InputMismatchException e) {
            System.out.println("Please enter valid input.");
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        } finally {
            scanner.close();
        }

        return talks;
    }

    public static void scheduleConference(List<Talk> talks) {
        int startTime = 9 * 60;
        int totalTracks = (int) Math.ceil((double) talks.size() / 12);

        for (int i = 0; i < totalTracks; i++) {
            TrackTheTime track = new TrackTheTime(TOTAL_MINUTES);

            while (!talks.isEmpty() && track.sessions.size() < MAX_TALKS_PER_SESSION) {
                Session morningSession = createSession(startTime, talks, MORNING_SESSION_DURATION);
                Session afternoonSession = createSession(startTime + AFTERNOON_SESSION_DURATION, talks,
                        AFTERNOON_SESSION_DURATION);

                if (!morningSession.talks.isEmpty()) {
                    track.addSession(morningSession);
                }
                if (!afternoonSession.talks.isEmpty()) {
                    track.addSession(afternoonSession);
                }
            }

            printTrackSchedule(track, i + 1);
            startTime += 480;
        }
    }

    public static Session createSession(int startTime, List<Talk> talks, int availableTime) {
        Session session = new Session(startTime);

        Iterator<Talk> iterator = talks.iterator();
        while (iterator.hasNext()) {
            Talk talk = iterator.next();
            if (talk.duration <= availableTime) {
                session.talks.add(talk);
                availableTime -= talk.duration;
                iterator.remove();
            }
        }

        return session;
    }

    public static void printTrackSchedule(TrackTheTime trackTime, int trackNumber) {
        System.out.println("Track " + trackNumber + ":");

        for (Session session : trackTime.sessions) {
            int currentTime = session.startTime;

            for (Talk talk : session.talks) {
                int hours = currentTime / 60;
                int minutes = currentTime % 60;

                System.out.printf("%02d:%02d %s %s %dmin%n", hours, minutes, (hours >= 12) ? "PM" : "AM",
                        talk.topic, talk.duration);

                currentTime += talk.duration;
            }
        }
    }
}
