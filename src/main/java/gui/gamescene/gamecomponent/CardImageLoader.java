package gui.gamescene.gamecomponent;

import static gui.gamescene.gamestate.UICard.Suit;

import gui.gamescene.gamestate.UICard;
import javafx.scene.image.Image;

import java.util.HashMap;

/**
 * Class to load card images (including card back).
 *
 * It only loads the cards once, and then stores them
 * for successive uses.
 */
class CardImageLoader {

    private static final String FILE_SUFFIX = "png";
    private static final String IMAGE_PATH = "images/cards/";

    private HashMap<Suit, Image[]> suitImageMap = new HashMap<>();
    private Image cardBack = null;

    /**
     * Returns the image of the given card
     */
    Image getCard(UICard UICard){
        if( UICard.isUnknown() ) return getCardBack();
        return getSuit(UICard.getSuit())[UICard.getValue()-1];
    }

    /**
     * Returns the image of the back of the cards.
     */
    Image getCardBack(){
        if( cardBack == null )
            cardBack = loadCardImage("back");
        return cardBack;
    }

    HashMap<Suit, Image[]> getAllSuits(){
        // Make sure all are loaded
        getSuit(Suit.CLUBS);
        getSuit(Suit.HEARTS);
        getSuit(Suit.SPADES);
        getSuit(Suit.DIAMONDS);
        return suitImageMap;
    }

    Image[] getSuit(Suit suit){
        Image[] suitImages = suitImageMap.get(suit);

        // Suit exists, so we just return image
        if( suitImages != null )
            return suitImages;

        // Suit doesn't exist so we load it
        suitImages = new Image[13];
        suitImageMap.put(suit, suitImages);

        // Load images
        for(int i=1; i<=13; i++ ){
            suitImages[i-1] = loadCardImage(String.format("%s_%d",suit.toString(), i));
        }
        return suitImages;
    }

    private Image loadCardImage(String name){
        String url = String.format("%s%s.%s", IMAGE_PATH, name, FILE_SUFFIX);
        return new Image(url, false);
    }

}
