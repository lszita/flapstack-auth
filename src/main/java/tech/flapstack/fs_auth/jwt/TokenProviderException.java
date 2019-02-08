/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tech.flapstack.fs_auth.jwt;

/**
 *
 * @author osboxes
 */
public class TokenProviderException extends Exception {

    /**
     * Creates a new instance of <code>TokenProviderException</code> without
     * detail message.
     */
    public TokenProviderException() {
    }

    /**
     * Constructs an instance of <code>TokenProviderException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public TokenProviderException(String msg) {
        super(msg);
    }
}
