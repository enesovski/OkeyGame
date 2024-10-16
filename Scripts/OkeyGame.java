import java.util.Arrays;
import java.util.Random;
public class OkeyGame {

    Player[] players;
    final int maxTileNumber = 15;
    Tile[] tiles;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    public OkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[112];
        int currentTile = 0;

        // two copies of each color-value combination, no jokers
        for (int i = 1; i <= 7; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[currentTile++] = new Tile(i,'Y');
                tiles[currentTile++] = new Tile(i,'B');
                tiles[currentTile++] = new Tile(i,'R');
                tiles[currentTile++] = new Tile(i,'K');
            }
        }
    }

    /*
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles
     * this method assumes the tiles are already shuffled
     * Deniz
     */
    public void distributeTilesToPlayers() {
        int playerIndex,
            i;

        playerIndex = 0;
        i = 0;

        while( playerIndex < 4 ){
            int j = 0;

            for(; j < maxTileNumber  ; i++ , j++){ //adds 14 for each player
                players[playerIndex].addTile(tiles[i]);
            }

            j = 0;
            playerIndex++;
        }

        players[0].addTile(tiles[i]);    //adds an additional tile for the starting player
    }

    /*
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we picked
     * Deniz
     */
    public String getLastDiscardedTile() {
        players[currentPlayerIndex].addTile(lastDiscardedTile);

        return lastDiscardedTile.toString();
    }

    /*
     * that tile is no longer in the tiles array (this simulates picking up the top tile)
     * it should return the toString method of the tile so that we can print what we picked
     * Deniz
     */
    public String getTopTile() {
        Tile newTile = tiles[tiles.length - 1];
        players[currentPlayerIndex].addTile(newTile);

        tiles = Arrays.copyOf(tiles, tiles.length - 1);    //ommits the last element of the tiles array, which is the top tile

        return newTile.toString();
    }

    /*
     * TODO: should randomly shuffle the tiles array before game starts
     * Semih
     */
    public void shuffleTiles() {
        Random rand=new Random();
        for(int i=0;i<tiles.length;i++){
            int random_number_index=rand.nextInt(tiles.length-i)+i;
            Tile temp_tile=tiles[random_number_index];
            tiles[random_number_index]=tiles[i];
            tiles[i]=temp_tile;
            
        }

    }

    /*
     * Decides if current player finished the game or not
     */
    public boolean didGameFinish() {
        boolean didFinish = false;
        if(players[currentPlayerIndex].isWinningHand())
        {
            didFinish = true;
        }

        return didFinish;
    }

    /*
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * You should consider if the discarded tile is useful for the computer in
     * the current status. Print whether computer picks from tiles or discarded ones.
     * Yavuz
     */
    public void pickTileForComputer() {
        int priorityOfDiscardedTile = calculateTilePriority(lastDiscardedTile, players[currentPlayerIndex]);
        //int priorityOfTileInTheStack = calculateTilePriority(tiles[tiles.length - 1], players[currentPlayerIndex]);

        Random rand = new Random();
        int randPriority = rand.nextInt( -1 , 4 );
        
        if(priorityOfDiscardedTile < randPriority){
            getTopTile();
        }
        else{
            getLastDiscardedTile();
        }
    }


    //#region ComputerDiscardTile
    /*
     * This method selects least useful tile for computer and discards it 
     * prints what tile is discarded 
     * Priorities tiles that are duplicate. 
     * If there is not duplicate, discardes least useful tile
     * Enes.
     */
    public void discardTileForComputer() {
        Player currentPlayer = players[currentPlayerIndex];
        int leastImportantTileIndex = 0;
        int leastPriority = Integer.MAX_VALUE;


        for (int i = 0; i < currentPlayer.getTiles().length; i++) {

            if(currentPlayer.getTiles()[i] == null) {
                continue;
            }
            
            int priority = calculateTilePriority(currentPlayer.getTiles()[i], currentPlayer);

            if(priority < leastPriority)
            {
                leastImportantTileIndex = i;
                leastPriority = priority;
            }
        }

        Tile discardedTile = currentPlayer.getAndRemoveTile(leastImportantTileIndex);
        lastDiscardedTile = discardedTile;

        System.out.println(lastDiscardedTile.toString() + " discarded.");
    }

    /**
     * Calculates priority by considering duplication and chain count
     * @param tile
     * @param player
     * @return
     */
    private int calculateTilePriority(Tile tile, Player player)
    {
        int priority = 0;

        if(isDuplicate(tile, player))
        {
            priority = -1;

            for( int i = 0 ; i < player.getTiles().length ; i++ ) {
                if( tile.equals(player.getTiles()[ i ])) {
                    priority--;
                }
            }
        }
        else
        {
            for (int i = 0; i < player.getTiles().length; i++) {

                if(!(player.getTiles()[i] == null) && tile.canFormChainWith(player.getTiles()[i]))
                {
                    for (int j = 0; j < player.getTiles().length; j++) {
                        if( tile.canFormChainWith(player.getTiles()[i]) ) {
                            priority++;
                        }
                    }
                }
            }    
        }
        
        return priority;
    }

    /**
     * Decides a tile is duplicate or not
     * @param tile
     * @param player
     * @return true if duplicate
     */
    private boolean isDuplicate(Tile tile, Player player)
    {
        if(tile == null){
            return false;
        }

        boolean isExists = false;
        for (int i = 0; i < player.getTiles().length; i++) {
            if(player.getTiles()[i] == null ){
                continue;
            }
            if(player.getTiles()[i].compareTo(tile) == 0)
            {
                isExists = true;
                break;
            }
        }
        return isExists;
    }

    //#endregion

    /*
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     * Yavuz
     */
    public void discardTile(int tileIndex) {
        Player currentPlayer = players[currentPlayerIndex];

        lastDiscardedTile = currentPlayer.getAndRemoveTile(tileIndex);


    }

    public void displayDiscardInformation() {
        if(lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }
//#region DÜZELT
    public void displayCurrentPlayersTiles() {

        players[currentPlayerIndex].displayTiles();
    }
//#endregion
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

      public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if(index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }

}
