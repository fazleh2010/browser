/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.core.api;

import java.io.IOException;

/**
 *
 * @author elahi
 */
public interface JavaScriptCode {

    public void generateScript() throws IOException, Exception;

    public void generateScript(String category) throws IOException, Exception;
}
