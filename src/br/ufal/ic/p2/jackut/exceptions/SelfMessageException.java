package br.ufal.ic.p2.jackut.exceptions;

public class SelfMessageException extends Exception {
    public SelfMessageException() {
        super("Usu�rio n�o pode enviar recado para si mesmo.");
    }
}
