package thehashgame.entities;

import thehashgame.exceptions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

/**
 * 0 | 1 | 2
 * ---------
 * 3 | 4 | 5
 * ---------
 * 6 | 7 | 8
 */
public class Board {
    static final List<Integer> VALID_POSITIONS = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8);
    static final List<List<Integer>> WIN_POSITIONS = List.of(
            // horizontal
            List.of(0, 1, 2),
            List.of(3, 4, 5),
            List.of(6, 7, 8),

            // vertical
            List.of(0, 3, 6),
            List.of(1, 4, 7),
            List.of(2, 5, 8),

            // diagonal
            List.of(0, 4, 8),
            List.of(2, 4, 6)
    );

    final Mark turn;
    private final Map<Integer, Mark> marks;

    public Board(final Map<Integer, Mark> marks, final Mark turn) {
        this.marks = marks;
        this.turn = turn;
    }

    public Board(Mark firstTurn) {
        marks = new HashMap<>();
        turn = firstTurn;
    }

    HashMap<Integer, Mark> getMarks() {
        return (HashMap<Integer, Mark>) marks
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    boolean isCircleWinner() {
        return isWinner(Mark.CIRCLE);
    }

    boolean isSquareWinner() {
        return isWinner(Mark.SQUARE);
    }

    private boolean isWinner(final Mark mark) {
        return WIN_POSITIONS.stream().anyMatch(positions -> positions.stream().allMatch(position -> marks.get(position) == mark));
    }

    private boolean gameIsOver(){
        if(isCircleWinner() || isSquareWinner()) return true;
        return VALID_POSITIONS.stream().allMatch(p -> marks.get(p) != null);
    }

    public Board put(final Mark mark, int position) {
        if (isNull(mark) || !List.of(Mark.SQUARE, Mark.CIRCLE).contains(mark)) throw new InvalidMarkException();
        if (position < 0 || position > 8) throw new InvalidPositionException();
        if (gameIsOver()) throw new TheGameIsAlreadyOverException();
        if (marks.get(position) != null) throw new PositionAlreadyMarkedException();
        if (mark != turn && mark == Mark.CIRCLE) throw new NotTurnOfCirclesException();
        if (mark != turn && mark == Mark.SQUARE) throw new NotTurnOfSquaresException();
        final HashMap<Integer, Mark> nextMarks = getMarks();
        nextMarks.put(position, mark);
        final Mark nextTurn = mark == Mark.CIRCLE ? Mark.SQUARE : Mark.CIRCLE;
        return new Board(nextMarks, nextTurn);
    }
}
