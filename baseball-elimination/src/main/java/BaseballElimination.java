import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BaseballElimination {

    private Map<String, BaseballTeam> namesToTeams = new HashMap<>();
    private BaseballTeam [] teams;
    private int [][] remainingMatches;

    /**
     * create a baseball division from given filename in format specified below
     */
    public BaseballElimination(String filename) {
        readInput(filename);
    }

    /**
     * number of teams
     */
    public int numberOfTeams() {
        return teams.length;
    }

    /**
     * all teams
     */
    public Iterable<String> teams() {
        return Collections.unmodifiableSet(namesToTeams.keySet());
    }

    /**
     * number of wins for given team
     * */
    public int wins(String team) {
        checkIfTeamValid(team);
        return namesToTeams.get(team).wins;
    }

    /**
     * number of losses for given team
     */
    public int losses(String team) {
        checkIfTeamValid(team);
        return namesToTeams.get(team).losses;
    }

    /**
     * number of remaining games for given team
     * */
    public int remaining(String team) {
        checkIfTeamValid(team);
        return namesToTeams.get(team).remaining;
    }

    /**
     * number of remaining games between team1 and team2
     * */
    public int against(String team1, String team2) {
        checkIfTeamValid(team1);
        checkIfTeamValid(team2);
        BaseballTeam firstTeam = namesToTeams.get(team1);
        BaseballTeam secondTeam = namesToTeams.get(team2);
        return remainingMatches[firstTeam.index][secondTeam.index];
    }

    /**
     * is given team eliminated?
     * */
    public boolean isEliminated(String team) {
        checkIfTeamValid(team);
        return certificateOfElimination(team).iterator().hasNext();
    }

    /**
     * subset R of teams that eliminates given team; null if not eliminated
     * */
    public Iterable<String> certificateOfElimination(String team) {
        checkIfTeamValid(team);

        Iterable<String> trivialElimination = computeTrivialElimination(team);
        boolean isTriviallyEliminated = trivialElimination.iterator().hasNext();
        if (isTriviallyEliminated) {
            return trivialElimination;
        } else {
            return computeNonTrivialElimination(team);
        }
    }

    private Iterable<String> computeNonTrivialElimination(String team) {
        Set<String> certificateOfElimination = new HashSet<>();

        FlowNetwork flowNetwork = constructFlowNetwork(team);
        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, flowNetwork.V() - 2, flowNetwork.V() - 1);

        for (int teamVertex = 0; teamVertex < teams.length; teamVertex++) {
            if (fordFulkerson.inCut(teamVertex)) {
                certificateOfElimination.add(teams[teamVertex].name);
            }
        }

        return certificateOfElimination;
    }

    /**
     * Constructs the flow network for baseball elimination problem. The network consists of following layers:
     *   start->matches->teams->sink
     *
     * where start and sink are artificial vertices, matches are remaining matches (number of teams over 2).
     * For the sake of easier indexing to arrays containing data (fields of this class), vertices of the team layer
     * start from index 0 (index of the team vertex == index of team to data arrays).
     *
     * */
    private FlowNetwork constructFlowNetwork(String team) {
        int teamsCount = teams.length;

        List<Match> remainingMatches = getRemainingMatches(teamsCount);
        int games = remainingMatches.size();
        int vertices = 1 + games + teamsCount + 1;

        int start = vertices - 2;
        int sink = vertices - 1;

        FlowNetwork flowNetwork = new FlowNetwork(vertices);

        int gameVertex = teamsCount;
        for (Match match : remainingMatches) {
            // start -> matches layer
            flowNetwork.addEdge(new FlowEdge(start, gameVertex, match.numberOfGames));

            // matches -> teams layer
            flowNetwork.addEdge(new FlowEdge(gameVertex, match.firstTeamIndex, Double.POSITIVE_INFINITY));
            flowNetwork.addEdge(new FlowEdge(gameVertex, match.secondTeamIndex, Double.POSITIVE_INFINITY));
            gameVertex++;
        }

        // teams -> sink
        for (int teamVertex = 0; teamVertex < teamsCount; teamVertex++) {
            flowNetwork.addEdge(new FlowEdge(teamVertex, sink, getCapacityToSink(team, teamVertex)));
        }

        return flowNetwork;
    }

    /**
     * Generates possible combinations of remaining games between teams.
     * */
    private List<Match> getRemainingMatches(int teamsCount) {
        List<Match> remainingGames = new ArrayList<>();

        for (int i = 0; i < teamsCount; i++) {
            for (int j = i + 1; j < teamsCount; j++) {
                remainingGames.add(new Match(i, j, remainingMatches[i][j]));
            }
        }

        return remainingGames;
    }

    private double getCapacityToSink(String thisTeam, int OtherTeamIndex) {
        BaseballTeam thisBaseballTeam = namesToTeams.get(thisTeam);
        return thisBaseballTeam.wins + thisBaseballTeam.remaining - teams[OtherTeamIndex].wins;
    }

    private Iterable<String> computeTrivialElimination(String team) {
        BaseballTeam baseballTeam = namesToTeams.get(team);

        Set<String> certificateOfElimination = new HashSet<>();
        int requiredWins = baseballTeam.wins + baseballTeam.remaining;
        for (Map.Entry<String, BaseballTeam> otherTeamEntry : namesToTeams.entrySet()) {
            if (otherTeamEntry.getValue().wins > requiredWins) {
                certificateOfElimination.add(otherTeamEntry.getKey());
            }
        }

        return certificateOfElimination;
    }

    private void readInput(String filename) {
        In in = new In(filename);
        try {
            int numberOfTeams = in.readInt();
            remainingMatches = new int[numberOfTeams][numberOfTeams];
            teams = new BaseballTeam [numberOfTeams];

            int teamCounter = 0;
            while (!in.isEmpty()) {
                String teamName = in.readString();
                int wins = in.readInt();
                int losses = in.readInt();
                int left = in.readInt();

                for (int i = 0; i < numberOfTeams; i++) {
                    int matches = in.readInt();
                    remainingMatches[teamCounter][i] = matches;
                }

                BaseballTeam baseballTeam = new BaseballTeam(teamCounter, teamName, wins, losses, left);
                namesToTeams.put(teamName, baseballTeam);
                teams[teamCounter++] = baseballTeam;
            }

        } finally {
            in.close();
        }
    }

    private void checkIfTeamValid(String team) {
        if (team == null || namesToTeams.get(team) == null) {
            throw new IllegalArgumentException("Invalid team name.");
        }
    }

    private class BaseballTeam {
        private final int index;
        private final String name;
        private final int wins;
        private final int losses;
        private final int remaining;

        BaseballTeam(int index, String name, int wins, int losses, int remaining) {
            this.index = index;
            this.name = name;
            this.wins = wins;
            this.losses = losses;
            this.remaining = remaining;
        }
    }

    private class Match {
        private final int firstTeamIndex;
        private final int secondTeamIndex;
        private final int numberOfGames;

        public Match(final int firstTeamIndex, final int secondTeamIndex, final int numberOfGames) {
            this.firstTeamIndex = firstTeamIndex;
            this.secondTeamIndex = secondTeamIndex;
            this.numberOfGames = numberOfGames;
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

}
