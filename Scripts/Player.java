class Player {
    String playerName;
    Tile[] playerTiles;
    int numberOfTiles;

    public Player(String name) {
        setName(name);
        playerTiles = new Tile[15]; // there are at most 15 tiles a player owns at any time
        numberOfTiles = 0; // currently this player owns 0 tiles, will pick tiles at the beggining of the game
    }

    /*
     * @return discardedTile
     * Burak
     */
    public Tile getAndRemoveTile(int index) {

        Tile discardedTile = playerTiles[index];
        playerTiles[index] = null;
        numberOfTiles--;

        return discardedTile;
    }

    /*
     * should also update numberOfTiles accordingly.
     * make sure playerTiles are not more than 15 at any time
     * Burak
     */
    public void addTile(Tile t) {

        if( numberOfTiles == 14 ) {

            playerTiles[playerTiles.length-1] = t;

        }
        else{

            for (int i = 0; i < playerTiles.length ; i++) {

                if( playerTiles[i] == null ){
                    playerTiles[i] = t;
                    break;
                }

            }

        }

        sortTiles();
        numberOfTiles++;
    }

    /**
     * Tiles are sorted according to their priorities.
     * Every tile has its own unique priority value.
     * Priority value of a tile = ( 5 * value of tile ) + color of tile
     */

    public void sortTiles( ) {
        
        Tile[] sortedHand = new Tile[ playerTiles.length ];
        int[] priorities = new int[ playerTiles.length ];
        int priority = 0;
        int colorCode = 0 ;

        for( int i = 0 ; i < playerTiles.length ; i++ ) {

            if( playerTiles[i] == null){
                continue;
            }
            if( playerTiles[ i ].getColor() == 'Y' ) {
                colorCode = 0;
            }
            else if( playerTiles[ i ].getColor() == 'B' ) {
                colorCode = 1;
            }
            else if( playerTiles[ i ].getColor() == 'R' ) {
                colorCode = 2;
            }
            else{
                colorCode = 3;
            }

            priority = 5 * playerTiles[ i ].getValue() + colorCode;
            priorities[ i ] = priority;

        }

        int currentPriority;
        int minPriorityIndex = 0;

        for( int p = 0 ; p < playerTiles.length ; p++ ) {

            int minPriority = Integer.MAX_VALUE;

            for( int k = 0 ; k < playerTiles.length ; k++ ) {
                currentPriority = priorities[ k ];
                
                if( currentPriority < minPriority ) {
                    minPriority = currentPriority;
                    minPriorityIndex = k;
                }
            }

            sortedHand[ p ] = playerTiles[ minPriorityIndex ];
            priorities[ minPriorityIndex ] = Integer.MAX_VALUE;
        }

        playerTiles = sortedHand;
    }

    /*
     * to win this player should have 3 chains of length 4, extra tiles
     * does not disturb the winning condition
     * Semih
     * @return
     */
    public boolean isWinningHand() {
        int count=0;
        int count_=0;
        for(int i=0;i<playerTiles.length-2;i++){
            
            if(playerTiles[i] == null){
                continue;
            }
            if(playerTiles[i].compareTo(playerTiles[i+1])==0){
                continue;
            }
            if(playerTiles[i].canFormChainWith(playerTiles[i+1])){
                count_+=1;
            }else{
                if(count_==4){
                    count+=1;
                }
                count_=1;
            }
        }
        return count==3;
    }

    public int findPositionOfTile(Tile t) {
        int tilePosition = -1;
        for (int i = 0; i < numberOfTiles; i++) {
            if(playerTiles[i] == null){
                continue;
            }
            if(playerTiles[i].compareTo(t) == 0) {
                tilePosition = i;
            }
        }
        return tilePosition;
    }

    public void displayTiles() {
        System.out.println(playerName + "'s Tiles:");
        sortTiles();
        for (int i = 0; i < playerTiles.length; i++) {
            if(playerTiles[i] == null){
                continue;
            }

            System.out.print(playerTiles[i].toString() + " ");
        }
        System.out.println();
    }

    public Tile[] getTiles() {
        Tile[] tilesWithNull = new Tile[15];
        int tilesCount = 0;

        for (int i = 0; i < playerTiles.length; i++) {
            if(playerTiles[i] == null)
                continue;
            tilesWithNull[tilesCount] = playerTiles[i];
            tilesCount ++;
        }

        return playerTiles;
    }

    public void setName(String name) {
        playerName = name;
    }

    public String getName() {
        return playerName;
    }
}
