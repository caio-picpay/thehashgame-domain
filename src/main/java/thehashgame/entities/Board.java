package thehashgame.entities;

import thehashgame.exceptions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static thehashgame.config.Config.VALID_POSITIONS;
import static thehashgame.config.Config.WIN_POSITIONS;

/**
 * The Hash Game
 *
 * Positions:
 * 0 | 1 | 2
 * ---------
 * 3 | 4 | 5
 * ---------
 * 6 | 7 | 8
 */
public class Board {

    private final Mark turn;
    private final Map<Integer, Mark> marks;

    public Board(final Map<Integer, Mark> marks, final Mark turn) {
        this.marks = marks;
        this.turn = turn;
    }

    public Board(Mark firstTurn) {
        marks = new HashMap<>();
        turn = firstTurn;
    }

    public HashMap<Integer, Mark> getMarks() {
        return (HashMap<Integer, Mark>) marks
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public boolean isCircleWinner() {
        return isWinner(Mark.CIRCLE);
    }

    public boolean isSquareWinner() {
        return isWinner(Mark.SQUARE);
    }

    private boolean isWinner(final Mark mark) {
        return WIN_POSITIONS.stream().anyMatch(positions -> positions.stream().allMatch(position -> marks.get(position) == mark));
    }

    private boolean gameIsOver() {
        if (isCircleWinner() || isSquareWinner()) return true;
        return VALID_POSITIONS.stream().allMatch(p -> marks.get(p) != null);
    }

    public Board put(final Mark mark, int position) {
        if (isNull(mark) || !List.of(Mark.SQUARE, Mark.CIRCLE).contains(mark)) throw new InvalidMarkException();
        if (!VALID_POSITIONS.contains(position)) throw new InvalidPositionException();
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
