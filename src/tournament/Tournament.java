package tournament;

public class Tournament {

    /**
     * @param scorecard the scores in the tournament and is not null; {@code scorecard[i][j]} represents the score of Player i in Game j. Each row in this array has {@code sets * games} entries, and each entry has a value between 0 and 5 (limits inclusive).
     * @param sets the number of sets played in the tournament, {@code sets > 0}
     * @param games the number of games per set, {@code games > 0}
     * @return a list of players in order of their performance (best to worst)
     */
    public static int[] rankPlayers(int[][] scorecard, int sets, int games) {
        int[] playerRankings = new int[scorecard.length];

        for(int i = 0; i < playerRankings.length; i++){
            playerRankings[i] = i;
        }

        sortByLastSet(scorecard, playerRankings, games, sets);

        //find ties in current rankings
        int playerRankIndex = 0;
        while (playerRankIndex < playerRankings.length - 1){
            if (lastSetScore(scorecard, playerRankings[playerRankIndex], games, sets) ==
                    lastSetScore(scorecard, playerRankings[playerRankIndex + 1], games, sets)) {
                int[] player_range = tiedPlayerRange(scorecard, playerRankings, playerRankIndex, games, sets);
                tieBreak(scorecard, playerRankings, player_range, games, sets, sets * games - 1);

                //skip over the players that have already been tie-sorted
                playerRankIndex = player_range[1];
            }
            playerRankIndex++;
        }

        return playerRankings;
    }

    /**
     * @param scorecard the scores in the tournament and is not null; {@code scorecard[i][j]} represents the score of Player i in Game j. Each row in this array has {@code sets * games} entries, and each entry has a value between 0 and 5 (limits inclusive).
     * @param playerRankings playerRankings[i] is person in ith place in tournament
     * @param games the number of games per set, {@code games > 0}
     * @param sets the number of sets played in the tournament, {@code sets > 0}
     * @return a list of players in order of their performance in the last set, via bubble sort
     */
    private static void sortByLastSet(int[][] scorecard, int[] playerRankings, int games, int sets){
        boolean swap = true;
        int numPlayers = scorecard.length;
        while (swap) {
            swap = false;
            for (int i = 0; i < numPlayers - 1; i++) {
                if (lastSetScore(scorecard, playerRankings[i], games, sets)
                        < lastSetScore(scorecard, playerRankings[i+1], games, sets)) {

                    int temp = playerRankings[i];
                    playerRankings[i] = playerRankings[i+1];
                    playerRankings[i+1] = temp;

                    swap = true;
                }
            }
        }
    }

    /**
     * @param scorecard the scores in the tournament and is not null; {@code scorecard[i][j]} represents the score of Player i in Game j. Each row in this array has {@code sets * games} entries, and each entry has a value between 0 and 5 (limits inclusive).
     * @param player the player's index in the scorecard
     * @param games the number of games per set, {@code games > 0}
     * @param sets the number of sets played in the tournament, {@code sets > 0}
     * @return their total score in the last set
     */
    private static int lastSetScore(int[][] scorecard, int player, int games, int sets){
        int playerSetScore = 0;
        for (int i = 0; i < games; i++) {
            playerSetScore += scorecard[player][sets * games - 1 - i];
        }
        return playerSetScore;
    }

    /**
     * @param scorecard the scores in the tournament and is not null; {@code scorecard[i][j]} represents the score of Player i in Game j. Each row in this array has {@code sets * games} entries, and each entry has a value between 0 and 5 (limits inclusive).
     * @param playerRankings playerRankings[i] is person in ith place in tournament
     * @param playerRankIndex the index of the player in the playerRankings
     * @param game the index of the game played in the scorecard
     * @return their score in the game specified
     */
    private static int gameScore(int[][] scorecard, int[] playerRankings, int playerRankIndex, int game){
        return scorecard[playerRankings[playerRankIndex]][game];
    }

    /**
     * @param scorecard the scores in the tournament and is not null; {@code scorecard[i][j]} represents the score of Player i in Game j. Each row in this array has {@code sets * games} entries, and each entry has a value between 0 and 5 (limits inclusive).
     * @param playerRankings playerRankings[i] is person in ith place in tournament, sorted by score in last set
     * @param playerRankIndex the index of the player in the playerRankings
     * @param games the number of games per set, {@code games > 0}
     * @param sets the number of sets played in the tournament, {@code sets > 0}
     * @return the range of tied players
     */
    private static int[] tiedPlayerRange(int[][] scorecard, int[] playerRankings, int playerRankIndex, int games, int sets){

        int[] playerIndices = new int[2]; //first index is first tied player in range, 2nd index is last player in range

        playerIndices[0] = playerRankIndex;
        playerIndices[1] = playerRankIndex + 1;

        while (playerIndices[1] + 1 < playerRankings.length &&
                lastSetScore(scorecard, playerRankings[playerIndices[0]], games, sets) ==
                lastSetScore(scorecard, playerRankings[playerIndices[1] + 1], games, sets)) {
            playerIndices[1]++;
        }
        return playerIndices;
    }

    /**
     * @param scorecard the scores in the tournament and is not null; {@code scorecard[i][j]} represents the score of Player i in Game j. Each row in this array has {@code sets * games} entries, and each entry has a value between 0 and 5 (limits inclusive).
     * @param playerRankings playerRankings[i] is person in ith place in tournament, sorted by score in last set
     * @param player_range player_range is the range of players that are in a tie in playerRankings, first and last indices are inclusive
     * @param games the number of games per set, {@code games > 0}
     * @param sets the number of sets played in the tournament, {@code sets > 0}
     * @param gameComparison the game being used to compare player performance and break tie
     * @return boolean value, indicating if the original array has been modified or not (for bubble sort);
     * breaks ties in playerRankings using bubble sort
     */
    private static boolean tieBreak(int[][] scorecard, int[] playerRankings,
                                int[] player_range, int games, int sets, int gameComparison){

        boolean swap = true;
        int[] old_rankings = new int[playerRankings.length];

        for (int i = 0; i < old_rankings.length; i++) {
            old_rankings[i] = playerRankings[i];
        }

        while (swap) {
            swap = false;
            for (int i = player_range[0]; i < player_range[1]; i++) {
                if (gameScore(scorecard, playerRankings, i, gameComparison) <
                        gameScore(scorecard, playerRankings, i+1, gameComparison)) {

                    int temp = playerRankings[i];
                    playerRankings[i] = playerRankings[i+1];
                    playerRankings[i+1] = temp;

                    swap = true;
                } else if (gameScore(scorecard, playerRankings, i, gameComparison) ==
                        gameScore(scorecard, playerRankings, i+1, gameComparison)){
                    if (gameComparison - 1 >= 0) {
                        //compare a pair of games and recursively tiebreak them
                        int[] sub_range = {i, i + 1};
                        if (tieBreak(scorecard, playerRankings, sub_range, games, sets,
                                gameComparison - 1)) {
                            swap = true;
                        }
                    }
                }
            }
        }

        //for recursive tieBreak, used to tell if a swap was performed
        for (int i = 0; i < old_rankings.length; i++) {
            if (old_rankings[i] != playerRankings[i]) {
                return true;
            }
        }

        return false;
    }
}
