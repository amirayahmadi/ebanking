package com.example.ebanking.exceptions;


/*extends Exception : est une exception surveillle : c a dire il faut ajouter throws dans la  tet de la methode*/
/* extends runtimeException : est une Exception non surveillle **/
public class BalanceNotSuffisantException extends Exception{
    public BalanceNotSuffisantException(String message){
        super(message);
    }
}
