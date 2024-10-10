public class Player {
    String playerName;
    Tile[] playerTiles;
    int numberOfTiles;

    public Player(String name) {
        setName(name);
        playerTiles = new Tile[15]; // there are at most 15 tiles a player owns at any time
        numberOfTiles = 0; // currently this player owns 0 tiles, will pick tiles at the beggining of the game
    }

        /*
     * TODO: removes and returns the tile in given index
     * @return discardedTile
     * Burak
     */
    public Tile getAndRemoveTile(int index) {

        Tile discardedTile = new Tile( playerTiles[index].getValue() , playerTiles[index].getColor() );
        Tile[] discardedTileList = new Tile[ numberOfTiles - 1 ];
        
        for( int i = 0 ; i < discardedTileList.length ; i++ ) {

            if( i < index ) {
                discardedTileList[ i ] = playerTiles[ i ];
            }

            else {
                discardedTileList[ i ] = playerTiles[ i + 1 ];
            }

        }

        playerTiles = discardedTileList;
        numberOfTiles--;
        return discardedTile;
    }

    /*
     * TODO: adds the given tile to the playerTiles in order
     * should also update numberOfTiles accordingly.
     * make sure playerTiles are not more than 15 at any time
     * Burak
     */
    public void addTile(Tile t) {

        if( numberOfTiles < 15 ) {

            Tile[] addedTileList = new Tile[ numberOfTiles + 1 ];
            int colorCode = 0;  //Used to determine the priority of the tile

            if( t.getColor() == 'Y' ) {
                colorCode = 0;
            }
            else if( t.getColor() == 'B' ) {
                colorCode = 1;
            }
            else if( t.getColor() == 'R' ) {
                colorCode = 2;
            }
            else{
                colorCode = 3;
            }

            int tilePriority = 5 * t.getValue() + colorCode;
            sortTiles();

            int currentTilePriority = 0;
            int prevTilePriority = 0;
            int currentColorCode = 0;
            for( int i = 0 ; i < playerTiles.length ; i++ ) {

                if( playerTiles[ i ].getColor() == 'Y' ) {
                    currentColorCode = 0;
                }
                else if( playerTiles[ i ].getColor() == 'B' ) {
                    currentColorCode = 1;
                }
                else if( playerTiles[ i ].getColor() == 'R' ) {
                    currentColorCode = 2;
                }
                else{
                    currentColorCode = 3;
                }

                currentTilePriority = 5 * playerTiles[ i ].getValue() + currentColorCode; //Creates an unique priority value for each element of the list

                if( prevTilePriority <= tilePriority && tilePriority <= currentTilePriority ) {

                    int b = i;
                    for( int a = 0 ; a < addedTileList.length ; a++ ) {
                        if( a < i ) {
                            addedTileList[ a ] = playerTiles[ a ];
                        }

                        else if( a == i ){
                            addedTileList[ a ] = t;
                        }

                        else{
                            addedTileList[ a ] = playerTiles[ b ];
                            b++;
                        }

                    }
                    break;
                }

                prevTilePriority = currentTilePriority;
            }

            numberOfTiles++;
            playerTiles = addedTileList;
        }

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

        int minPriority = Integer.MAX_VALUE;
        int currentPriority;
        int minPriorityIndex = 0;

        for( int p = 0 ; p < playerTiles.length ; p++ ) {

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
     * TODO: checks if this player's hand satisfies the winning condition
     * to win this player should have 3 chains of length 4, extra tiles
     * does not disturb the winning condition
     * Semih
     * @return
     */
    public boolean isWinningHand() {
        int count=0;
        int count_=0;
        for(int i=0;i<numberOfTiles-1;i++){
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
            if(playerTiles[i].compareTo(t) == 0) {
                tilePosition = i;
            }
        }
        return tilePosition;
    }

    public void displayTiles() {
        System.out.println(playerName + "'s Tiles:");
        for (int i = 0; i < numberOfTiles; i++) {
            System.out.print(playerTiles[i].toString() + " ");
        }
        System.out.println();
    }

    public Tile[] getTiles() {
        return playerTiles;
    }

    public void setName(String name) {
        playerName = name;
    }

    public String getName() {
        return playerName;
    }
}
