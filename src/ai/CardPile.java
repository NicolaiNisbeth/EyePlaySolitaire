package ai;

public class CardPile {
    private static final class CardNode {

        CardNode next;
        CardNode prev;
        Card card;
        public CardNode(Card card) {
            this.card = card;
        }

        @Override
        public String toString() {
            String string = card == null ? "null" : card.toString();
            return " <- " + string + " -> ";
        }
    }
    private CardNode left;

    private CardNode right;
    private CardNode firstVisible;
    private int invisibleCount = -1;
    public CardPile(Card... cards) {
        for (Card card : cards)
            addLast(card);
    }

    public void addFirst(Card card) {
        CardNode node = new CardNode(card);
        if (left == null) {
            right = node;
        } else {
            left.prev = node;
            node.next = left;
        }
        left = node;
    }

    public void addLast(Card card) {
        CardNode node = new CardNode(card);
        if (right == null) {
            left = node;
        } else {
            right.next = node;
            node.prev = right;
        }
        right = node;
    }

    public boolean hasInvisible(){
        return left != null && left.card == null;
    }

    public int getInvisibleCount() {
        if (invisibleCount == -1)
            invisibleCount = findInvisibleCount();

        return invisibleCount;
    }

    private int findInvisibleCount() {
        CardNode node = left;
        int count = 0;
        while (node.card == null){
            count++;
            node = node.next;
        }
        return count;
    }

    public CardNode getFirstVisible() {
        if (firstVisible == null)
            firstVisible = findFirstVisible();

        return firstVisible;
    }

    public void setFirstVisible(CardNode firstVisible) {
        this.firstVisible = firstVisible;
    }

    private CardNode findFirstVisible() {
        CardNode index = left;
        while (index.card == null){
            index = index.next;
        }
        return index;
    }

    public CardNode getLeft() {
        return left;
    }

    public void setLeft(CardNode left) {
        this.left = left;
    }

    public CardNode getRight() {
        return right;
    }

    public void setRight(CardNode right) {
        this.right = right;
    }

    public CardPile deepCopy() {
        CardPile copy = new CardPile();
        CardNode index = left;
        while(index != null){
            copy.addLast(index.card.copy());
            index = index.next;
        }
        return copy;
    }

    @Override
    public String toString() {
        getFirstVisible();
        getInvisibleCount();

        return "CardPile{" +
                "left=" + left +
                ", right=" + right +
                ", firstVisible=" + firstVisible +
                ", invisibleCount=" + invisibleCount +
                '}' + "\n";
    }
}
