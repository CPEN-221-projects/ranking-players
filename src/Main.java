public class Main {
    public static void main(String[] args) {
        int[][] scorecard = {{1, 2, 2},
                {2, 5, 2},
                {3, 2, 2},
                {4, 8, 2},
                {5, 5, 2},
                {6, 3, 2},
                {8, 4, 2},
                {8, 4, 2},
                {9, 2, 2},
                {10, 4, 2},
                {11, 4, 2}};
        int sets = 3;
        int games = 1;
        int[] playerRankings = tournament.Tournament.rankPlayers(scorecard, sets, games);
        for (int i = 0; i < scorecard.length; i++) {
            System.out.println(playerRankings[i]);
        }
    }
}