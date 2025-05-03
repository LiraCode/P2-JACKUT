package br.ufal.ic.p2.jackut.exceptions;

public class SystemOperationException extends RuntimeException {

    public SystemOperationException(Throwable cause) {
        super("Erro na operação do sistema: " + cause.getMessage(), cause);
    }

    public SystemOperationException(String message) {
        super("Erro na operação do sistema: " + message);
    }
}