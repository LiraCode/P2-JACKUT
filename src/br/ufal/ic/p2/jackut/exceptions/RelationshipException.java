package br.ufal.ic.p2.jackut.exceptions;

/**
 * General exception for relationshipFeatures-related errors.
 */
public class RelationshipException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RelationshipException(String message) {
        super(message);
    }
}