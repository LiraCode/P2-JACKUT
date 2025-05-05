package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando ocorre uma operação inválida no sistema.
 */

public class SystemOperationException extends RuntimeException {

    public SystemOperationException(Throwable cause) {
        super("Erro na operação do sistema: " + cause.getMessage(), cause);
    }

    public SystemOperationException(String message) {
        super("Erro na operação do sistema: " + message);
    }
}