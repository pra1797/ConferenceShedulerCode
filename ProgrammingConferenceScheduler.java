import java.util.ArrayList;
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

class Session {
    List<Talk> talks = new ArrayList<>();
    int startTime;

    public Session(int startTime) {
        this.startTime = startTime;
    }
}

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

public class ProgrammingConferenceScheduler {
    public static void main(String[] args) {
        List<Talk> talks = getTalksFromUser();
        scheduleConference(talks);
    }
    
    private static List<Talk> getTalksFromUser() {
        List<Talk> talks = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
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
        return talks;
    }

    public static void scheduleConference(List<Talk> talks) {
        int startTime = 9 * 60;
        int totalTracks = (int) Math.ceil((double) talks.size() / 12);
    
        for (int i = 0; i < totalTracks; i++) {
            TrackTheTime track = new TrackTheTime(480);

            while (!talks.isEmpty() && track.sessions.size() < 4) {
                Session morningSession = createSession(startTime, talks, 180);
                Session afternoonSession = createSession(startTime + 240, talks, 240);

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
